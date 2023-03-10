package gerbera.fdsnws.common;

/**
 *
 * @author Julian Peña.
 */
public enum QueryMethod {
	
	QUERY, VERSION, WADL, QUERY_AUTH, INVALID;

	/**
	 * Tries to parses the given value to a valid query method.
	 *
	 * @param value
	 * @return a QueryMethod enumeration value.
	 */
	public static QueryMethod lookup(String value) {

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
