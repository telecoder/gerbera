package gerbera.fdsnws.station;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

import java.io.OutputStream;

/**
 *
 * @author Julian Peña.
 */
public class StationOutputStream extends OutputStream {

	private final Handler<Buffer> handler;

	public StationOutputStream(Handler<Buffer> handler) {
		this.handler = handler;
	}

	@Override
	public void write(int b) {
		handler.handle(Buffer.buffer().appendByte((byte) b));
	}

}
