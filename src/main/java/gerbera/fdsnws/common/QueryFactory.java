package gerbera.fdsnws.common;

import gerbera.fdsnws.station.StationQueryLevel;
import gerbera.fdsnws.station.StationQueryFormat;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Julian Peña.
 */
public class QueryFactory {

	/**
	 * Our logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger("Query Factory");

	// prevents instantiation
	private QueryFactory() {
	}

	/**
	 * Makes a FDSN Query object from the given HTTP request (GET or POST).
	 *
	 * @param context vertx RoutingContext
	 * @return A Future that will eventually contain a FDSN Query object, it can
	 * fail if no query could be parsed.
	 */
	public static Future<Query> make(RoutingContext context) {

		Query query = new Query();

		// the query type correspond to the fdsn service (station or dataselect)
		// method correspond to acceptable method for each service
		// both, the service and method are parameters found in the request URL
		setQueryTypeAndMethod(query, context);

		if (query.type == QueryType.INVALID) {
			return Future.failedFuture("invalid fdsn service");
		}

		if (query.method == QueryMethod.INVALID) {
			return Future.failedFuture("invalid fdsn service method");
		}

		return switch (context.request().method().name()) {
			case "GET" ->
				fromGet(query, context);
			case "POST" ->
				fromPost(query, context);
			default ->
				Future.failedFuture("invalid http method");
		};
	}

	/**
	 * Extracts query's type and service method from the requests URL.
	 *
	 * @param query The query whose type and method is to be set.
	 * @param context The request's routing context.
	 */
	private static void setQueryTypeAndMethod(Query query,
			RoutingContext context) {
		query.type = QueryType.lookup(context.pathParam("service"));
		query.method = QueryMethod.lookup(context.pathParam("method"));
	}

	/**
	 * Tries to make a FDSN query from a HTTP GET request.
	 *
	 * @return A valid FDSN Query object. A failure to parse a Query from the
	 * given HTTP request will result in a query that will match ALL streams for
	 * the last 5 minutes.
	 */
	private static Future<Query> fromGet(Query query, RoutingContext context) {

		HttpServerRequest request = context.request();

		MultiMap params = request.params();
		if (params.isEmpty()) {
			return Future.failedFuture("no parameters found in http request");
		}

		if (query.type == QueryType.STATION) {

			String level = getParam("level", null, "station", params);
			query.level = StationQueryLevel.lookup(level);
			if (query.level == StationQueryLevel.INVALID) {
				return Future.failedFuture("invalid query level: " + level);
			}

			String format = getParam("format", null, "xml", params);
			query.format = StationQueryFormat.lookup(format);
			if (query.format == StationQueryFormat.INVALID) {
				return Future.failedFuture("invalid query format: " + format);
			}
		}

		query.starttime = getParam("starttime", "start", null, params);
		query.endtime = getParam("endtime", "end", null, params);

		// if not provided, we set query time range to last 5 minutes
		if (query.starttime == null || query.endtime == null) {
			LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
			var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			query.endtime = now.format(formatter);
			query.starttime = now.plusMinutes(-5).format(formatter);
		}

		query.networks.addAll(split(getParam("network", "net", "*", params)));
		query.stations.addAll(split(getParam("station", "sta", "*", params)));
		query.locations.addAll(split(getParam("location", "loc", "*", params)));
		for (int i = 0; i < query.locations.size(); i++) {
			if (query.locations.get(i).equals("--")) {
				query.locations.set(i, "");
			}
		}
		query.channels.addAll(split(getParam("channel", "cha", "*", params)));

		if (query.isValid()) {
			return Future.succeededFuture(query);
		}

		LOG.error("Query is invalid: " + query.error);
		return Future.failedFuture("invalid request");
	}

	/**
	 * Return the value of the given fdsn query parameter from within the HTTP
	 * GET request parameters.
	 *
	 * @param name Parameter name.
	 * @param alias Parameter name alias.
	 * @param defaultValue Default value to be returned in case the parameter
	 * was not found in the map.
	 * @param map HTTP request parameters map.
	 * @return
	 */
	private static String getParam(String name, String alias,
			String defaultValue, MultiMap map) {

		String value = defaultValue;

		if (alias != null && map.get(alias) != null) {
			value = map.get(alias);
		}

		if (map.get(name) != null) {
			value = map.get(name);
		}

		return value;
	}

	/**
	 * If multiple parameter values are separated by commas, then this method
	 * split them a return them as a list.
	 *
	 * @param params
	 * @return
	 */
	private static List<String> split(String params) {
		if (params == null || params.isBlank()) {
			return List.of();
		}
		return List.of(params.split(","));
	}

	/**
	 * Tries to make a FDSN query from a HTTP POST request.
	 *
	 * @param request
	 * @return A Future that will eventually contain an FDSN Query when
	 * successfully parse, or fail otherwise.
	 */
	private static Future<Query> fromPost(Query query, RoutingContext context) {

		HttpServerRequest request = context.request();

		Promise<Query> promise = Promise.promise();

		request
				.body()
				.map(buffer -> buffer.toString().lines())
				.map(lines -> lines.toList())
				.onSuccess(lines -> {

					Map<String, String> params = new HashMap<>();
					for (String line : lines) {

						// does this line contain a param = value pair?
						if (line.contains("=")) {
							String[] split = line.split("=");
							if (split.length == 2) {
								params.put(split[0], split[1]);
							}
							continue;
						}

						// line should contain a 'N S L C starttime endtime'
						String[] split = line.split(" ");
						if (split.length >= 6) {
							query.networks.add(split[0]);
							query.stations.add(split[1]);
							query.locations.add(split[2]);
							query.channels.add(split[3]);
							query.starttime = split[4];
							query.endtime = split[5];
						}
					}

					if (query.type == QueryType.STATION) {
						String level = params.get("level");
						query.level = StationQueryLevel.lookup(level);
						String format = params.get("format");
						query.format = StationQueryFormat.lookup(format);
					}

					if (query.isValid()) {
						promise.complete(query);
					} else {
						promise.fail(query.error);
					}
				})
				.onFailure(f -> promise.fail("failed to parse request body"));

		return promise.future();
	}

}
