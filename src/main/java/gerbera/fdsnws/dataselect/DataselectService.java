package gerbera.fdsnws.dataselect;

import gerbera.config.Config;
import gerbera.datalink.DataLinkClient;
import gerbera.fdsnws.common.Query;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Julian Peña.
 */
public class DataselectService {

	/**
	 * Dataselect web service version.
	 */
	public static final String VERSION = "1.1.1";

	/**
	 * DataLink client reference.
	 */
	private final DataLinkClient dataLinkClient;

	/**
	 * Our logger.
	 */
	private final Logger LOG = LoggerFactory.getLogger("Dataselect");

	public DataselectService(Vertx vertx) {
		dataLinkClient = new DataLinkClient(vertx);
	}

	/**
	 * Starts the service.
	 *
	 * @return A future that will succeed if the service starts successfully.
	 */
	public Future start() {
		LOG.info("Starting");
		LOG.info("Connecting to datalink server");
		return dataLinkClient
				.connect(Config.ringserverAddress(), Config.ringserverPort())
				.compose(c -> dataLinkClient.doID("gerbera:dataselect"))
				.onFailure(f -> LOG.error("Dataselect service failed"));
	}

	/**
	 * FDSN Version method request handler.
	 *
	 * @return This web service's version.
	 */
	public String version() {
		return VERSION;
	}

	/**
	 * FDSN WADL method request handler.
	 *
	 * @return The WADL document for this web service.
	 */
	public String wadl() {
		return "TODO";
	}

	/**
	 * FDSN Query method request handler.This method uses backpressure.
	 *
	 * @param query The Dataselect query.
	 * @param writeStream A vertx WriteStream where write the received DataLink
	 * packet's payload(we trust,with our soul, that the server is sending valid
	 * miniSEED packets).
	 * @param start Promise that will be completed once streaming has started.
	 * @return A future that will succeed once the query has been fulfilled and
	 * there is not more data to write on the given handler.
	 */
	public Future<Long> query(Query query, WriteStream writeStream,
			Promise start) {

		if (!query.isValid()) {
			return Future.failedFuture(query.getError());
		}

		Promise promise = Promise.promise();

		AtomicLong packets = new AtomicLong(0);

		Future<?> future;
		if (dataLinkClient.getState() == DataLinkClient.State.STREAMING) {
			// a streaming could be going on, if this is the case, we must end
			// it first
			future = dataLinkClient.doENDSTREAM();
		} else {
			// otherwise do nothing, this is a no op
			future = Future.succeededFuture();
		}

		future
				.compose(c -> dataLinkClient.doMATCH(matchPattern(query)))
				.compose(c -> dataLinkClient.doPOSITION_AFTER(query.getStartTime()))
				.compose(c -> {
					return dataLinkClient.doSTREAM(packet -> {
						if (packet.getHpdatastart() < query.getEndTime()) {
							// signals the caller there are matching packets
							if (packets.addAndGet(1) == 1) {
								start.complete();
							}
							writeStream.write(Buffer.buffer(packet.getData()));
							if (packet.getHpdataend() >= query.getEndTime()) {
								dataLinkClient.doENDSTREAM();
							}
						}
					});
				})
				.onSuccess(s -> promise.complete(packets.get()))
				.onFailure(f -> {
					LOG.error("failed to perform query");
					LOG.error(f.getMessage());
					promise.fail("query failed");
				});

		return promise.future();
	}

	/**
	 * Craft a match pattern for the given dataselect query. IMPORTANT: when
	 * multiples selectors are provided then only the first one is used.
	 *
	 * @param query
	 * @return
	 */
	private String matchPattern(Query query) {

		StringBuilder stringBuilder = new StringBuilder();

		if (query.getNetworks().isEmpty()) {
			stringBuilder.append("*");
		} else {
			stringBuilder.append(query.getNetworks().get(0));
		}
		stringBuilder.append("_");

		if (query.getStations().isEmpty()) {
			stringBuilder.append("*");
		} else {
			stringBuilder.append(query.getStations().get(0));
		}
		stringBuilder.append("_");

		if (query.getLocations().isEmpty() || query.getLocations().get(0).isBlank()) {
			stringBuilder.append("?");
		} else {
			stringBuilder.append(query.getLocations().get(0));
		}
		stringBuilder.append("_");

		if (query.getChannels().isEmpty()) {
			stringBuilder.append("*");
		} else {
			stringBuilder.append(query.getChannels().get(0));
		}

		return stringBuilder.append("/MSEED").toString();
	}

}
