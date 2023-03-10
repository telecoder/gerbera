package gerbera.datalink;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class extends a standard DataLink packet and adds methods useful for
 * dealing with server responses.
 *
 * @author Julian Peña.
 */
public class DataLinkResponse extends DataLinkPacket {

	private final DataLinkResponseType type;

	private DataLinkResponse(byte[] bytes) {

		super(bytes);

		if (bytes[3] == 'I' & bytes[4] == 'D') {
			type = DataLinkResponseType.ID;
		} else if (bytes[3] == 'O' & bytes[4] == 'K') {
			type = DataLinkResponseType.OK;
		} else if (bytes[3] == 'E' & bytes[4] == 'R' & bytes[5] == 'R'
				& bytes[6] == 'O' & bytes[7] == 'R') {
			type = DataLinkResponseType.ERROR;
		} else if (bytes[3] == 'I' & bytes[4] == 'N' & bytes[5] == 'F'
				& bytes[6] == 'O') {
			type = DataLinkResponseType.INFO;
		} else if (bytes[3] == 'P' & bytes[4] == 'A' & bytes[5] == 'C'
				& bytes[6] == 'K' & bytes[7] == 'E' & bytes[8] == 'T') {
			type = DataLinkResponseType.PACKET;
		} else if (bytes[3] == 'E' & bytes[4] == 'N' & bytes[5] == 'D'
				& bytes[6] == 'S' & bytes[7] == 'T' & bytes[8] == 'R'
				& bytes[9] == 'E' & bytes[10] == 'A' & bytes[11] == 'M') {
			type = DataLinkResponseType.ENDSTREAM;
		} else {
			type = DataLinkResponseType.INVALID;
		}
	}

	public static DataLinkResponse of(DataLinkPacket packet) {
		return new DataLinkResponse(packet.bytes);
	}

	/**
	 *
	 * @return This DataLink server packet type (ID, OK, ERROR, INFO or PACKET).
	 */
	public DataLinkResponseType getType() {
		return type;
	}

	/**
	 * Some DataLink server responses include a data portion, some are header
	 * only, so, given a header, this method returns the size of the data
	 * portion or 0 is there isn't any.
	 *
	 * @param header
	 * @return
	 */
	public static int dataSize(byte[] header) {

		String[] parts = new String(header).split(" ");
		parts[0] = parts[0].substring(3);

		return switch (parts[0]) {
			case "PACKET" ->
				Integer.parseInt(parts[6]);
			case "OK", "ERROR", "INFO" ->
				Integer.parseInt(parts[2]);
			default ->
				0;
		};
	}

	/**
	 * @return A List containing the 'payload' of this DataLink message header.
	 * The contents of the payload is message dependent, check the DataLink
	 * protocol documentation. The caller is responsible to cast the strings to
	 * integer when necessary.
	 */
	public List<String> getHeaderPayload() {
		String header = new String(getHeader());
		// OK value size
		// 0123456
		header = header.substring(3);
		return Arrays
				.stream(header.split(" "))
				.collect(Collectors.toList());
	}

	/**
	 * @return The 'payload' of this DataLink message header. The contents of
	 * the payload is message dependent, check the DataLink protocol
	 * documentation.
	 */
	public String getHeaderAsString() {
		return new String(getHeader()).substring(3);
	}

	/**
	 * Applicable to responses to ID commands.
	 *
	 * @return The DataLink server identification.
	 */
	public String getServerId() {
		String header = new String(getHeader());
		// DLXID serverID
		// 0123456
		return header.substring(6);
	}

	/**
	 * Applicable when the server response type is OK or ERROR.
	 *
	 * @return Integer value is command dependent.
	 */
	public int getValue() {
		return Integer.parseInt(getHeaderPayload().get(0));
	}

	/**
	 * Applicable when the server response type is OK or ERROR.
	 *
	 * @return Size in the header is the length of the message in bytes.
	 */
	public int getSize() {
		return Integer.parseInt(getHeaderPayload().get(1));
	}

	/**
	 * Applicable only when the server response type is INFO.
	 *
	 * @return "STATUS", "STREAMS" or "CONNECTIONS".
	 */
	public String getInfoType() {
		return getHeaderPayload().get(0);
	}

	/**
	 * Applicable only when the server response type is PACKET.
	 *
	 * @return A string identifying a data stream in W_X_Y_Z/TYPE format.
	 */
	public String getStreamId() {
		return getHeaderPayload().get(0);
	}

	/**
	 * Applicable only when the server response type is PACKET.
	 *
	 * @return An integer ID, unique within a DataLink server at any given time.
	 */
	public long getPktId() {
		return Long.parseLong(getHeaderPayload().get(1));
	}

	/**
	 * Applicable only when the server response type is PACKET.
	 *
	 * @return Time that a packet was accepted by the DataLink server.
	 */
	public long getHppkttime() {
		return Long.parseLong(getHeaderPayload().get(2));
	}

	/**
	 * Applicable only when the server response type is PACKET.
	 *
	 * @return Start time associated with the data in the packet.
	 */
	public long getHpdatastart() {
		return Long.parseLong(getHeaderPayload().get(3));
	}

	/**
	 * Applicable only when the server response type is PACKET.
	 *
	 * @return End time associated with the data in the packet.
	 */
	public long getHpdataend() {
		return Long.parseLong(getHeaderPayload().get(4));
	}

	/**
	 * Applicable only when the server response type is PACKET.
	 *
	 * @return Length of the data section of the packet in bytes.
	 */
	public int getPacketSize() {
		return Integer.parseInt(getHeaderPayload().get(5));
	}

}
