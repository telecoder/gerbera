package gerbera.datalink;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * An instance of this factory should be use to process and unbounded stream of
 * bytes containing DataLink packets. Packets are reported back to the caller
 * when they are complete and are valid.
 *
 * @author Julian Peña.
 */
public class DataLinkPacketFactory {

	private static final int BUFFER_SIZE = 100 * 1024;

	/**
	 * Internal buffer for storing incoming bytes.
	 */
	private ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

	/**
	 * byte array for holding header bytes as they are received.
	 */
	private byte[] header;
	private int headerSize;

	/**
	 * The size of the data portion of the current DataLink packet.
	 */
	private int dataSize;

	/**
	 * Complete size for the current DataLink packet.
	 */
	private int packetSize;

	// flags
	private boolean gotPreheader;
	private boolean gotHeader;

	/**
	 * Queues for holding DataLink packets already parsed and ready to be
	 * consumed by the caller.
	 */
	private final Deque<DataLinkResponse> deque = new ArrayDeque<>();

	/**
	 * Our logger.
	 */
	private final Logger LOG = LoggerFactory.getLogger("Factory");

	/**
	 *
	 * @return The number packets available.
	 */
	public int packets() {
		return deque.size();
	}

	/**
	 *
	 * @return Whether or not this factory has DataLink packets in queue.
	 */
	public boolean hasPackets() {
		return !deque.isEmpty();
	}

	/**
	 *
	 * @return Whether or not this factory is empty.
	 */
	public boolean isEmpty() {
		return deque.isEmpty();
	}

	/**
	 *
	 * @return The oldest packet in the queue. The packet is removed from the
	 * internal queue.
	 */
	public DataLinkResponse getPacket() {
		return deque.pollFirst();
	}

	/**
	 *
	 * @return The newest packet in the queue. The packet is removed from the
	 * internal queue.
	 */
	public DataLinkResponse getLastPacket() {
		return deque.pollLast();
	}

	/**
	 * Add bytes to this factory. The bytes being added could contain a portion
	 * of a DataLink packet, a complete packet or multiple packets.
	 *
	 * @param bytes
	 */
	public void addBytes(byte[] bytes) {

		if (inputBuffer.remaining() < bytes.length) {
			resizeInputBuffer(inputBuffer.capacity() + bytes.length);
		}

		inputBuffer.put(bytes);

		processInput();
	}

	/**
	 * Process bytes already received parses DataLink packets from them. After a
	 * packet is parsed it is added to the internal queue.
	 */
	private void processInput() {

		// do we have the preheader?
		if (!gotPreheader) {
			if (inputBuffer.position() >= 3) {
				headerSize = inputBuffer.get(2) & 0xFF;
				gotPreheader = true;
			} else {
				return;
			}
		}

		// do we have the header?, preheader is always 3 bytes long
		if (!gotHeader) {
			if (inputBuffer.position() >= 3 + headerSize) {
				gotHeader = true;
				makeHeader();
				dataSize = getDataSize();
				packetSize = 3 + headerSize + dataSize;
				// it may be possible that an huge incoming packet wont fit
				// in our buffer, better check now that later
				if (inputBuffer.capacity() < packetSize) {
					resizeInputBuffer(packetSize);
				}
			} else {
				return;
			}
		}

		// do we have the complete packet?
		if (inputBuffer.position() < packetSize) {
			return;
		}

		if (inputBuffer.position() >= packetSize) {
			parsePacket();
			if (inputBuffer.position() > 0) {
				processInput();
			}
		}
	}

	/**
	 * Creates a DataLink packet header for the packet being parsed.
	 */
	private void makeHeader() {
		header = new byte[headerSize];
		int oldPos = inputBuffer.position();
		inputBuffer.position(3);
		inputBuffer.get(header, 0, headerSize);
		inputBuffer.position(oldPos);
	}

	/**
	 *
	 * @return The data portion size for the packet being parsed.
	 */
	private int getDataSize() {
		String[] parts = new String(header).split(" ");
		return switch (parts[0]) {
			case "PACKET" -> Integer.parseInt(parts[6]);
			case "OK", "ERROR", "INFO" -> Integer.parseInt(parts[2]);
			default -> 0;
		};
	}

	/**
	 * Resizes our internal input buffer.
	 *
	 * @param newSize
	 */
	private void resizeInputBuffer(int newSize) {

		int position = inputBuffer.position();

		ByteBuffer temp = ByteBuffer.allocate(inputBuffer.capacity());
		temp.put(inputBuffer.position(0));

		inputBuffer = ByteBuffer.allocate(newSize);
		inputBuffer
				.put(temp.position(0))
				.position(position);
	}

	/**
	 * Tries to parse a DataLink packet from the bytes in the internal buffer.
	 * This method MUST only be called once there are enough bytes for a
	 * DataLink packet to be form. After a parsing attempt, the corresponding
	 * bytes are removed from the input buffer.
	 *
	 * @return True is a packet was parsed, false otherwise.
	 */
	private boolean parsePacket() {

		int oldPos = inputBuffer.position();

		byte[] packetBytes = new byte[packetSize];
		inputBuffer
				.get(0, packetBytes)
				.position(packetSize)
				.compact()
				.position(oldPos - packetSize);

		// clean flags for next packet
		cleanFlags();

		var optional = DataLinkPacket.of(packetBytes);

		if (optional.isPresent()) {
			deque.add(DataLinkResponse.of(optional.get()));
			return true;
		} else {
			LOG.error("parsing problem: {}", new String(packetBytes));
			return false;
		}
	}

	private void cleanFlags() {
		header = null;
		gotPreheader = false;
		gotHeader = false;
	}

	/**
	 * Clear all cached packets.
	 */
	public void clear() {
		deque.clear();
	}

}
