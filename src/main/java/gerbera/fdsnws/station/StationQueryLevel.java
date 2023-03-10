package gerbera.fdsnws.station;

/**
 *
 * @author Julian Peña.
 */
public enum StationQueryLevel {

	NETWORK("network"),
	STATION("station"),
	CHANNEL("channel"),
	RESPONSE("response"),
	INVALID("invalid");

	private final String value;

	private StationQueryLevel(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	/**
	 * Tries to parses the given value to a valid Level. If the value does not
	 * correspond to a valid Level then INVALID is returned.
	 *
	 * @param value
	 * @return a Level enumeration value.
	 */
	public static StationQueryLevel lookup(String value) {

		if (value == null || value.length() == 0) {
			return INVALID;
		}

		try {
			return valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			return INVALID;
		}
	}

}
