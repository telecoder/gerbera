package gerbera.datalink;

import java.util.Optional;

/**
 *
 * @author Julian Peña.
 */
public class DataLinkPacket {

	/**
	 * DataLinkPacket backing byte array.
	 */
	final byte[] bytes;

	DataLinkPacket(byte[] bytes) {
		this.bytes = bytes;
	}

	public DataLinkPacket(byte[] header, byte[] data) {
		bytes = new byte[header.length + data.length];
		System.arraycopy(header, 0, bytes, 0, header.length);
		System.arraycopy(data, 0, bytes, header.length, data.length);
	}

	/**
	 * Tries to parse a DataLink message from the given byte array.
	 *
	 * @param bytes
	 * @return An Optional that could contain a DataLink message, or be empty if
	 * the bytes couldn't be parsed.
	 */
	public static Optional<DataLinkPacket> of(byte[] bytes) {

		if (bytes == null || bytes.length < 3) {
			return Optional.empty();
		}

		// 'D' = 44 'L' = 76
		if (bytes[0] != 44 && bytes[1] != 76) {
			return Optional.empty();
		}

		if (bytes.length < 3 + (bytes[2] & 0xFF)) {
			return Optional.empty();
		}

		return Optional.of(new DataLinkPacket(bytes));
	}

	/**
	 * 
	 * @return True if this packet already has a preheader, false otherwise.
	 */
	public boolean hasPreheader() {
		return (bytes != null && bytes.length > 2);
	}

	/**
	 * 
	 * @return True if this packet already has a header, false otherwise.
	 */
	public boolean hasHeader() {
		return (hasPreheader() && bytes.length >= getHeaderSize());
	}

	/**
	 * 
	 * @return A copy of the bytes backing this DataLinkPacket.
	 */
	public byte[] getBytes() {
		byte[] copy = new byte[bytes.length];
		System.arraycopy(bytes, 0, copy, 0, bytes.length);
		return copy;
	}

	/**
	 * @return The header size as specified in the preheader.
	 */
	public int getHeaderSize() {
		return bytes[2] & 0xFF;
	}

	/**
	 *
	 * @return A copy of this DataLink message's header.
	 */
	public byte[] getHeader() {
		byte[] header = new byte[getHeaderSize()];
		System.arraycopy(bytes, 0, header, 0, getHeaderSize());
		return header;
	}

	/**
	 *
	 * @return A copy if this DataLink message's data.
	 */
	public byte[] getData() {
		int dataSize = bytes.length - 3 - getHeaderSize();
		byte[] data = new byte[dataSize];
		System.arraycopy(bytes, 3 + getHeaderSize(), data, 0, dataSize);
		return data;
	}

	/**
	 * Make a header.
	 *
	 * @param command The DataLink command including its parameters.
	 * @return
	 */
	static byte[] header(String command) {
		byte[] preheader = preheader(command.getBytes());
		byte[] header = new byte[3 + command.length()];
		System.arraycopy(preheader, 0, header, 0, 3);
		System.arraycopy(command.getBytes(), 0, header, 3, command.length());
		return header;
	}

	/**
	 * Make a preheader for the given header.
	 *
	 * @param header
	 * @return
	 */
	private static byte[] preheader(byte[] header) {
		return new byte[]{'D', 'L', (byte) header.length};
	}

}
