package gerbera.fdsnws;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author Julian Peña.
 */
@ExtendWith(VertxExtension.class)
public class GerberaServerTest {

	static final String SERVER = "localhost";
	static final int PORT = 8080;

	static final String VERSION = "1.1";
	static final String PREFIX = "/fdsnws";
	static final String STATION = PREFIX + "/station/" + VERSION;
	static final String DATASELECT = PREFIX + "/dataselect/" + VERSION;

	static HttpClient client;

	public GerberaServerTest() {
	}

	@BeforeAll
	static void prepare(Vertx vertx, VertxTestContext context) {
		client = vertx.createHttpClient();
		vertx.deployVerticle(new GerberaServer(), context.succeedingThenComplete());
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void stationVersion(Vertx vertx, VertxTestContext context) {
		checkVersion(STATION + "/version", "1.1.1", context);
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void dataselectVersion(Vertx vertx, VertxTestContext context) {
		checkVersion(DATASELECT + "/version", "1.1.1", context);
	}

	private void checkVersion(String uri, String expectedVersion,
			VertxTestContext context) {
		client
				.request(HttpMethod.GET, PORT, SERVER, uri)
				.compose(request -> request.send())
				.compose(response -> {
					if (response.statusCode() != 200) {
						context.failNow("non 200 http response");
					}
					return response.body();
				})
				.map(buffer -> buffer.toString())
				.onSuccess(body -> {
					if (body.equals(expectedVersion)) {
						context.completeNow();
					} else {
						context.failNow("wrong version");
					}
				})
				.onFailure(f -> context.failNow("failed to check version"));
	}
	
	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void wrongUrl(Vertx vertx, VertxTestContext context) {
		client
				.request(HttpMethod.GET, PORT, SERVER, "/chicuca3000")
				.compose(request -> request.send())
				.onSuccess(response -> {
					if (response.statusCode() == 404) {
						context.completeNow();
					} else {
						context.failNow("non 404 http response");
					}
				})
				.onFailure(f -> context.failNow("failed to check version"));
	}
	
}
