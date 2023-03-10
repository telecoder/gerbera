package gerbera.fdsnws.common;

/**
 *
 * @author Julian Peña.
 */
public enum QueryType {
	
	STATION, DATASELECT, INVALID;

	/**
	 * Tries to parses the given value to a valid query type.
	 *
	 * @param value
	 * @return a QueryType enumeration value.
	 */
	public static QueryType lookup(String value) {

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
