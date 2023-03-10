package gerbera.fdsnws.station;

/**
 *
 * @author Julian Peña.
 */
public enum StationQueryFormat {

	XML("xml", "application/xml"),
	TEXT("text", "text/plain"),
	JSON("json", "application/json"),
	INVALID("invalid", "invalid");

	private final String value;
	private final String mimeType;

	private StationQueryFormat(String value, String mimeType) {
		this.value = value;
		this.mimeType = mimeType;
	}

	/**
	 *
	 * @return The format actual value.
	 */
	public String value() {
		return value;
	}

	/**
	 *
	 * @return The mime type for this format.
	 */
	public String mime() {
		return mimeType;
	}

	/**
	 * Tries to parses the given value to a valid Format. If the value does not
	 * correspond to a valid Format then INVALID is returned.
	 *
	 * @param value
	 * @return a Format enumeration value.
	 */
	public static StationQueryFormat lookup(String value) {

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
