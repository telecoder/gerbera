package gerbera.datalink;

/**
 * This class extends a standard DataLink packet and adds methods useful for
 * creating client commands.
 *
 * @author Julian Peña.
 */
public class DataLinkRequest extends DataLinkPacket {

	/**
	 * Convenience constant for referring to the earliest packet in the server.
	 */
	public static final long POSITION_EARLIEST = -2;

	/**
	 * Convenience constant for referring to the latest packet in the server.
	 */
	public static final long POSITION_LATEST = -3;

	/**
	 * INFO type STATUS.
	 */
	public static final String INFO_STATUS = "STATUS";

	/**
	 * INFO type STREAMS.
	 */
	public static final String INFO_STREAMS = "STREAMS";

	/**
	 * INFO type CONNECTIONS.
	 */
	public static final String INFO_CONNECTIONS = "CONNECTIONS";

	private DataLinkRequest(byte[] bytes) {
		super(bytes);
	}

	private DataLinkRequest(byte[] header, byte[] data) {
		super(header, data);
	}

	/**
	 * Makes DataLink ID message.
	 *
	 * @param clientid
	 * @return
	 */
	public static DataLinkRequest ID(String clientid) {
		if (clientid == null || clientid.isBlank()) {
			clientid = "me:dump:client:hi";
		}
		if (clientid.length() > 252) {
			clientid = clientid.substring(0, 252);
		}
		clientid = "ID " + clientid;
		return new DataLinkRequest(header(clientid));
	}

	/**
	 * Makes a DataLink POSITION SET message.
	 *
	 * @param pktid
	 * @param hppkttime
	 * @return
	 */
	public static DataLinkRequest POSITION_SET(long pktid, long hppkttime) {
		String command = "POSITION SET " + pktid + " " + hppkttime;
		return new DataLinkRequest(header(command));
	}

	/**
	 * Makes a DataLink POSITION AFTER message.
	 *
	 * @param hptime
	 * @return
	 */
	public static DataLinkRequest POSITION_AFTER(String hptime) {
		String command = "POSITION AFTER " + hptime;
		return new DataLinkRequest(header(command));
	}

	/**
	 * Makes a DataLink READ message.
	 *
	 * @param pattern The pattern.
	 * @return
	 */
	public static DataLinkRequest MATCH(String pattern) {
		byte[] header = header("MATCH " + pattern.length());
		byte[] data = pattern.getBytes();
		return new DataLinkRequest(header, data);
	}

	/**
	 * Makes a DataLink READ message.
	 *
	 * @param pktid
	 * @return
	 */
	public static DataLinkRequest READ(long pktid) {
		String command = "READ " + pktid;
		return new DataLinkRequest(header(command));
	}

	/**
	 * Makes a DataLink STREAM message.
	 *
	 * @return
	 */
	public static DataLinkRequest STREAM() {
		return new DataLinkRequest(header("STREAM"));
	}

	/**
	 * Makes a DataLink ENDSTREAM message.
	 *
	 * @return
	 */
	public static DataLinkRequest ENDSTREAM() {
		return new DataLinkRequest(header("ENDSTREAM"));
	}

	/**
	 * Makes a DataLink INFO message.
	 *
	 * @param type
	 * @param match
	 * @return
	 */
	public static DataLinkRequest INFO(String type, String match) {

		String command = "INFO";

		if (!type.equals(INFO_STATUS)
				& type.equals(INFO_STREAMS)
				& type.equals(INFO_CONNECTIONS)) {
			type = INFO_STATUS;
		}

		command += " " + type;

		if (type.equals(INFO_STREAMS) && match != null) {
			command += " " + match;
		}

		return new DataLinkRequest(header(command));
	}

}
