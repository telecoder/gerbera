package gerbera.datalink;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * This tests require a ringserver instance to be running on localhost and have
 * the DataLink interface open on TCP port 16000.
 *
 * @author Julian Peña.
 */
@ExtendWith(VertxExtension.class)
public class DataLinkClientTest {

	private DataLinkClient client;

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void tcpConnection(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.connect("localhost", 16000)
				.compose(c -> client.disconnect())
				.onSuccess(s -> context.completeNow())
				.onFailure(f -> context.failNow("tcp socket problem"));
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void id(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.connect("localhost", 16000)
				.compose(c -> client.doID("fake:datalink:id"))
				.onSuccess(s -> context.completeNow())
				.onFailure(f -> context.failNow(f.getMessage()))
				.onComplete(c -> client.disconnect());
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void match(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.connect("localhost", 16000)
				.compose(c -> client.doMATCH("random_patern"))
				.onSuccess(c -> context.completeNow())
				.onFailure(f -> context.failNow(f.getMessage()))
				.onComplete(c -> client.disconnect());
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void info(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.connect("localhost", 16000)
				.compose(c -> client.doINFO(DataLinkRequest.INFO_STREAMS, null))
				.compose(response -> {
					if (response.getType() != DataLinkResponseType.INFO) {
						context.failNow("server returned " + response.getType());
					}
					return client.disconnect();
				})
				.onFailure(f -> context.failNow("INFO STATUS failed"))
				.onSuccess(s -> context.completeNow())
				.onComplete(c -> client.disconnect());
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void position(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.connect("localhost", 16000)
				.compose(c -> client.doPOSITION_AFTER(0))
				.onSuccess(c -> context.completeNow())
				.onFailure(error -> context.failNow(error.getMessage()))
				.onComplete(c -> client.disconnect());
	}

	@Test
	@Timeout(value = 1000, timeUnit = TimeUnit.MILLISECONDS)
	void streamEndstream(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.connect("localhost", 16000)
				.compose(c -> client.doSTREAM(h -> {
		}))
				.compose(c -> client.doENDSTREAM())
				.onSuccess(s -> context.completeNow())
				.onFailure(f -> context.failNow("STREAM/ENDSTREAM failed"));
	}

	@Test
	@Timeout(value = 500, timeUnit = TimeUnit.MILLISECONDS)
	void commandWhileDownMustFail(Vertx vertx, VertxTestContext context) {
		client = new DataLinkClient(vertx);
		client
				.doID("hi")
				.onFailure(f -> context.completeNow())
				.onSuccess(s -> context.failNow("command accepted while down"));

	}

}
