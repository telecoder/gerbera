package gerbera.datalink;

import gerbera.config.Config;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a partial DataLink client. REJECT, POSITION SET, READ
 * and WRITE commands are not yet implemented.
 *
 * @author Julian Peña.
 */
public class DataLinkClient {

	private final Vertx vertx;
	private NetSocket socket;

	private Handler<DataLinkResponse> clientHandler;

	private Promise<DataLinkResponse> promise = Promise.promise();

	private long timerId;

	private final DataLinkPacketFactory factory = new DataLinkPacketFactory();

	public enum State {
		DOWN, WAITING_ID, IDLE, WAITING_RESULT, WAITING_INFO, STREAMING, PAUSED;
	}

	private State state = State.DOWN;
	private State oldState = State.DOWN;

	// timeout for packet during streaming
	private int timeout = 500;

	/**
	 * Our logger.
	 */
	private final Logger LOG = LoggerFactory.getLogger("DataLink");

	public DataLinkClient(Vertx vertx) {
		this.vertx = vertx;
		this.timeout = Config.timeout();
	}

	/**
	 *
	 * @return This client's current state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Tries to connect to the DataLink server.
	 *
	 * @param server datalink server address.
	 * @param port datalink server port.
	 * @return A Future that will succeed if the connection was successfully
	 * stablish.
	 */
	public Future<?> connect(String server, int port) {

		if (state != State.DOWN) {
			return Future.failedFuture("connection is already up");
		}

		LOG.info("Connecting to {}:{}", server, port);

		Promise connectPromise = Promise.promise();

		NetClientOptions options = new NetClientOptions();
		options
				.setConnectTimeout(1000) // unit is milliseconds
				.setTcpKeepAlive(true);

		vertx
				.createNetClient(options)
				.connect(port, server)
				.onSuccess(netSocket -> {
					socket = netSocket;
					socket
							.handler(buffer -> receive(buffer.getBytes()))
							.endHandler(e -> promise.tryComplete())
							.closeHandler(c -> state = State.DOWN);
					state = State.IDLE;
					connectPromise.complete();
				})
				.onFailure(f -> {
					LOG.error("Failed to connect");
					LOG.error(f.getMessage());
					connectPromise.fail("failed to connect");
				});

		return connectPromise.future();
	}

	private void receive(byte[] bytes) {

		factory.addBytes(bytes);

		if (factory.isEmpty()) {
			return;
		}

		// do we have too many packets already parsed?, lets pause for a moment
		if (state == State.STREAMING) {
			if (factory.packets() > 1000) {
				pause();
			} else if (state == State.PAUSED && factory.packets() < 100) {
				resume();
			}
		}

		switch (state) {
			case DOWN, STREAMING -> onPacket();
			case WAITING_ID -> onServerId();
			case WAITING_RESULT, WAITING_INFO -> onResult();
		}
	}

	/**
	 * Tries to disconnect from the DataLink server. If the client is not
	 * connected, then this method does nothing.
	 *
	 * @return A Future object that will eventually succeed if the socket was
	 * closed.
	 */
	public Future disconnect() {

		if (socket == null) {
			return Future.failedFuture("not connected");
		}

		if (state == State.DOWN) {
			return Future.succeededFuture("already disconnected");
		}

		return socket
				.close()
				.onSuccess(s -> state = State.DOWN)
				.onFailure(f -> {
					LOG.error("failed to disconnect!");
					LOG.error(f.getMessage());
				});
	}

	/**
	 * Tries to pauses the underlying socket. You can call this method if this
	 * client is overwhelming you with packets.
	 */
	public void pause() {
		socket.pause();
		oldState = state;
		state = State.PAUSED;
		LOG.info("Paused");
	}

	/**
	 * Resumed the reading from the underlying socket.
	 */
	public void resume() {
		LOG.info("resuming");
		state = oldState;
		socket.resume();
	}

	/**
	 * Sends an ID command to the server.
	 *
	 * @param clientid This client's ID.
	 * @return A Future that will eventually succeed with the server response,
	 * it will fail in case of error or timeout.
	 */
	public Future<DataLinkResponse> doID(String clientid) {

		if (state != State.IDLE) {
			return Future.failedFuture("connection is not idle");
		}

		return send(DataLinkRequest.ID(clientid))
				.compose(c -> {
					state = State.WAITING_ID;
					timeout(timeout, "ID timeout");
					promise = Promise.promise();
					return promise.future();
				});
	}

	/**
	 * Sends a POSITION AFTER command to the server.
	 *
	 * @param hptime high precision time (microseconds from unix epoch).
	 * @return A Future that will eventually succeed with the server response,
	 * it will fail in case of error or timeout.
	 */
	public Future<DataLinkResponse> doPOSITION_AFTER(long hptime) {

		if (state != State.IDLE) {
			return Future.failedFuture("connection is not idle");
		}

		LOG.debug("POSITION AFTER {}", hptime);

		return send(DataLinkRequest.POSITION_AFTER(hptime + ""))
				.compose(c -> {
					state = State.WAITING_RESULT;
					timeout(timeout, "POSITION_AFTER timeout");
					promise = Promise.promise();
					return promise.future();
				});
	}

	/**
	 * Sends a MATCH command to the server.
	 *
	 * @param pattern DataLink match pattern.
	 * @return A Future that will eventually succeed with the server response,
	 * it will fail in case of error or timeout.
	 */
	public Future<DataLinkResponse> doMATCH(String pattern) {

		if (state != State.IDLE) {
			return Future.failedFuture("connection is not idle");
		}

		LOG.debug("MATCH {}", pattern);

		if (pattern == null || pattern.isBlank()) {
			return Future.failedFuture("pattern can not be empty");
		}

		return send(DataLinkRequest.MATCH(pattern))
				.compose(c -> {
					state = State.WAITING_RESULT;
					timeout(1000, "MATCH timeout");
					promise = Promise.promise();
					return promise.future();
				});
	}

	/**
	 * Sends a STREAM command to the DataLink server.
	 *
	 * @param handler A handler where to report the DataLink packets received.
	 * @return A Future that will eventually succeed when the stream ends, or
	 * fail if the command couldn't be send, timeout, or the server responds
	 * with an error.
	 */
	public Future<?> doSTREAM(Handler<DataLinkResponse> handler) {

		// if currently streaming stop it first, then proceed with the new one
		if (state == State.STREAMING) {
			return Future.failedFuture("client is already streaming");
		}

		if (state != State.IDLE) {
			return Future.failedFuture("connection is not idle: " + state);
		}

		if (handler == null) {
			return Future.failedFuture("STREAM handler can't be null");
		}

		LOG.debug("STREAM");

		this.clientHandler = handler;

		return send(DataLinkRequest.STREAM())
				.compose(c -> {
					state = State.STREAMING;
					promise = Promise.promise();
					timerId = vertx.setTimer(timeout, t -> {
						state = State.IDLE;
						promise.complete();
					});
					return promise.future();
				});
	}

	/**
	 * Sends an ENDSTREAM command to the DataLink server.
	 *
	 * @return A Future that will eventually succeed if the server acknowledges
	 * the ENDSTREAM command, it will if the command couldn't be send, timeout
	 * or the server responds with an error.
	 */
	public Future<?> doENDSTREAM() {

		if (state != State.STREAMING) {
			return Future.succeededFuture();
		}

		LOG.debug("ENDSTREAM");

		// complete the promise set by STREAM.
		promise.tryComplete();

		return send(DataLinkRequest.ENDSTREAM())
				.compose(c -> {
					state = State.WAITING_RESULT;
					factory.clear();
					promise = Promise.promise();
					return promise.future();
				});
	}

	/**
	 * Sends a INFO command to the DataLink server.
	 *
	 * @param type Could be 'streams', 'connections' or 'status'.
	 * @param match A pattern for filtering results (filtering done by server).
	 * @return A Future that will eventually contain the DataLinkResponse.
	 */
	public Future<DataLinkResponse> doINFO(String type, String match) {

		if (state != State.IDLE) {
			return Future.failedFuture("connection is not idle");
		}

		LOG.debug("INFO {} {}", type, match);

		return send(DataLinkRequest.INFO(type, match))
				.compose(c -> {
					state = State.WAITING_INFO;
					promise = Promise.promise();
					return promise.future();
				});
	}

	/**
	 * Sends a client command.
	 *
	 * @param request
	 * @return A Future that will succeed if the command was successfully send.
	 */
	private Future send(DataLinkRequest request) {
		return socket.write(Buffer.buffer(request.bytes));
	}

	/**
	 * Sets a timeout for a server result. When this client sends a a command
	 * that expects a result, if no result has been received in time, then this
	 * timeout will inform the caller.
	 *
	 * @param timeout in milliseconds.
	 */
	private void timeout(int timeout, String message) {
		timerId = vertx.setTimer(timeout, t -> {
			state = State.IDLE;
			if (message != null) {
				promise.tryFail(message);
			} else {
				promise.tryFail("timeout");
			}
		});
	}

	/**
	 * Handles packets received during streaming.
	 */
	private void onPacket() {

		// cancel timeout
		vertx.cancelTimer(timerId);

		DataLinkResponse packet;

		while (state == State.STREAMING && factory.hasPackets()) {
			packet = factory.getPacket();
			if (packet.getType() == DataLinkResponseType.PACKET) {
				clientHandler.handle(packet);
			} else {
				LOG.error("bad packet: {}", new String(packet.getBytes()));
			}
		}

		if (state == State.PAUSED) {
			resume();
		}

		timerId = vertx.setTimer(timeout, s -> {
			promise.complete();
			state = State.IDLE;
		});
	}

	/**
	 * Handles the response to a server ID request.
	 */
	private void onServerId() {

		// cancel timeout
		vertx.cancelTimer(timerId);

		state = State.IDLE;

		DataLinkResponse packet = factory.getLastPacket();
		if (packet.getType() == DataLinkResponseType.ID) {
			LOG.info("connected: {}", packet.getHeaderAsString());
			promise.complete(packet);
		} else {
			LOG.error("expected and ID but got: {}", packet.getType());
			LOG.error(new String(packet.getBytes()));
			promise.fail(new String(packet.getData()));
		}
	}

	/**
	 * Handles results for POSITION_AFTER, MATCH, INFO and ENDSTREAM commands.
	 */
	private void onResult() {

		// cancel timeout
		vertx.cancelTimer(timerId);

		// This returns the most recent packet
		DataLinkResponse response = factory.getLastPacket();

		switch (response.getType()) {
			case OK, INFO -> {
				state = State.IDLE;
				promise.complete(response);
			}
			case ERROR -> {
				state = State.IDLE;
				promise.fail(new String(response.getData()));
			}
			case ENDSTREAM -> {
				state = State.IDLE;
				promise.tryComplete(response);
			}
			case PACKET -> {
				// probably a residual packet from aprevious streaming state
				// nop
			}
		}
	}

}
