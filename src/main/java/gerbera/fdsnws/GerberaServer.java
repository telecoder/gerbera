package gerbera.fdsnws;

import gerbera.config.Config;
import gerbera.fdsnws.common.Query;
import gerbera.fdsnws.common.QueryFactory;
import gerbera.fdsnws.dataselect.DataselectService;
import gerbera.fdsnws.station.StationService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Julian Peña.
 */
public class GerberaServer extends AbstractVerticle {

	/**
	 * FDSN Dataselect service instance.
	 */
	private DataselectService dataselect;

	/**
	 * FDSN Station service instance.
	 */
	private StationService station;

	/**
	 * Our logger.
	 */
	private final Logger LOG = LoggerFactory.getLogger("gerbera");

	public GerberaServer() {
	}

	@Override
	public void start(Promise<Void> promise) {

		int fdsnPort = Config.port();

		LOG.info("Starting gerbera FDSN server on port {}", fdsnPort);

		station = new StationService(vertx);
		dataselect = new DataselectService(vertx);

		station
				.start()
				.compose(c -> dataselect.start())
				.compose(c -> startHttpServer(fdsnPort))
				.onSuccess(s -> {
					LOG.info("started");
					promise.complete();
				})
				.onFailure(f -> {
					LOG.error("failed to start");
					LOG.error("exiting");
					System.exit(1);
				});
	}

	/**
	 * Starts the HTTP server and sets all its handlers.
	 *
	 * @return A future that will succeed if the server starts successfully.
	 */
	private Future startHttpServer(int port) {

		LOG.info("Starting http server at port {}", port);

		Promise promise = Promise.promise();

		Router router = Router.router(vertx);

		// fdsn station service route handlers
		router
				.get("/fdsnws/:service/:version/:method")
				.handler(this::onQuery);

		// invalid request
		router.route().last().handler(this::invalidURL);

		vertx
				.createHttpServer()
				.requestHandler(router)
				.listen(port)
				.onSuccess(s -> {
					LOG.info("http server started");
					promise.complete();
				})
				.onFailure(f -> {
					LOG.error("Failed to start http server");
					LOG.error(f.getMessage());
					promise.fail("Failed start http server");
				});

		return promise.future();
	}

	/**
	 * Handles all request to this server. It validates the request URL is valid
	 * and that a valid Query object could be parsed. It formwards the query to
	 * the appropriate service handler.
	 *
	 * @param context
	 */
	private void onQuery(RoutingContext context) {
		
		QueryFactory
				.make(context)
				.onSuccess(query -> {
					switch (query.getType()) {
						case STATION -> onStation(query, context);
						case DATASELECT -> onDataselect(query, context);
						case INVALID -> invalidURL(context);
					}
				})
				.onFailure(f -> {
					LOG.error("Invalid request: {}", f.getMessage());
					String error = makeErrorMessage("400", "Bad request",
							f.getMessage(), context.request().absoluteURI());
					sendTextPlain(context, 400, error);
				});
	}

	/**
	 * Entry point for FDSN Station service requests.
	 *
	 * @param context
	 */
	private void onStation(Query query, RoutingContext context) {

		// should we reject requests for service versions diffent to ours?
		LOG.debug("Station request: {}", context.request().absoluteURI());

		switch (query.getMethod()) {
			case VERSION -> onStationVersion(context);
			case WADL -> onStationWadl(context);
			case QUERY -> onStationQuery(query, context);
		}
	}

	/**
	 * Handles FDSN Station service requests.
	 *
	 * @param context The vertx RoutingContext.
	 */
	private void onStationQuery(Query query, RoutingContext context) {

		HttpServerResponse response = context.request().response();
		response
				.setStatusCode(200)
				.putHeader("Content-Type", query.getFormat().mime())
				.setChunked(true);
		
		// this promise gets completed onece the first packet is ready to be
		// send to the client (it implies there is content to be send)
		Promise start = Promise.promise();
		start
				.future()
				.onSuccess(s -> {
					response
							.setStatusCode(200)
							.putHeader("Content-Type", query.getFormat().mime())
							.setChunked(true);
				});

		AtomicInteger packets = new AtomicInteger(0);

		Handler<Buffer> handler = buffer -> {
			if(packets.addAndGet(1) == 1){
				start.complete();
			}
			response.write(buffer);
		};

		station
				.query(query, handler)
				.onSuccess(s -> {
					if (packets.get() == 0) {
						response.setStatusCode(204);
					}
					response.end();
				})
				.onFailure(f -> {
					LOG.error("Failed while querying for data");
					response.setStatusCode(500).end();
				});
	}

	/**
	 * Handles the 'version' method for the Dataselect service
	 *
	 * @param context
	 */
	private void onStationVersion(RoutingContext context) {
		sendTextPlain(context, 200, station.version());
	}

	/**
	 * Handles the 'aplication.wadl' method for the Station service.
	 *
	 * @param context
	 */
	private void onStationWadl(RoutingContext context) {
		sendTextPlain(context, 200, station.wadl());
	}

	/**
	 * Entry point for FDSN Dataselect service requests.
	 *
	 * @param context
	 */
	private void onDataselect(Query query, RoutingContext context) {

		// should we reject requests for service versions diffent to ours?		
		LOG.debug("Dataselect request: {}", context.request().absoluteURI());
		switch (query.getMethod()) {
			case VERSION -> onDataselectVersion(context);
			case WADL -> onDataselectWadl(context);
			case QUERY -> onDataselectQuery(query, context);
			case QUERY_AUTH -> {
				String error = makeErrorMessage("400",
						"invalid method", "queryauth is not available",
						context.request().absoluteURI());
				sendTextPlain(context, 400, error);
			}
			default -> invalidURL(context);
		}
	}

	/**
	 * Handles the 'query' method for the Dataselect service.
	 *
	 * @param context The vertx RoutingContext.
	 */
	private void onDataselectQuery(Query query, RoutingContext context) {

		HttpServerRequest request = context.request();

		HttpServerResponse response = request.response();

		Promise streamStart = Promise.promise();
		streamStart
				.future()
				.onSuccess(c -> {
					response
							.setStatusCode(200)
							.putHeader("Content-Type", "application/vnd.fdsn.mseed")
							.putHeader("Content-Disposition", "filename=gerbera.mseed")
							.setChunked(true);
				});

		dataselect
				.query(query, response, streamStart)
				.onSuccess(s -> {
					if (response.bytesWritten() == 0) {
						response.setStatusCode(204);
					}
					response.end();
				})
				.onFailure(f -> LOG.error("Dataselect query failed"));
	}

	/**
	 * Handles the 'version' method for the Dataselect service.
	 *
	 * @param context
	 */
	private void onDataselectVersion(RoutingContext context) {
		sendTextPlain(context, 200, dataselect.version());
	}

	/**
	 * Handles the 'aplication.wadl' method for the Dataselect service.
	 *
	 * @param context
	 */
	private void onDataselectWadl(RoutingContext context) {
		sendTextPlain(context, 200, dataselect.wadl());
	}

	/**
	 * Handler for invalid requests.
	 *
	 * @param context The RoutingContext.
	 */
	private void invalidURL(RoutingContext context) {
		String message = makeErrorMessage("404", "Resource not found",
				"The requested URL didn't match any service",
				context.request().absoluteURI());
		sendTextPlain(context, 404, message);
	}

	/**
	 * Returns the text plain FSDN error message.
	 *
	 * @param error Error code
	 * @param shortDescription Error's simple description.
	 * @param description Error's more detailed description.
	 * @param requestUrl The request URL that generated the error.
	 * @return
	 */
	private String makeErrorMessage(String error, String shortDescription,
			String description, String requestUrl) {

		String timestamp = new Timestamp(System.currentTimeMillis()).toString();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("Error ").append(error).append(": ")
				.append(shortDescription).append("\n\n")
				.append(description).append("\n\n")
				.append("Request:\n")
				.append(requestUrl).append("\n\n")
				.append("Request Submited:\n")
				.append(timestamp).append("\n\n")
				.append("Service version:\n")
				.append(DataselectService.VERSION);

		return stringBuilder.toString();
	}

	/**
	 * Sends an text plain message to the client.
	 *
	 * @param context The RountingContext
	 * @param statusCode
	 * @param message
	 */
	private void sendTextPlain(RoutingContext context, int statusCode,
			String message) {
		context
				.response()
				.putHeader("Content-Type", "text/plain")
				.setStatusCode(statusCode)
				.end(message);
	}

}
