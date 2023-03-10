package gerbera.fdsnws.station;

/**
 *
 * @author Julian Peña.
 */
public abstract class PatternMatcher {

	// prevents instantiation
	private PatternMatcher() {
	}

	/**
	 * Test if the given input matches the given FDSN match pattern.
	 *
	 * @param pattern
	 * @param input
	 * @return
	 */
	public static boolean test(String pattern, String input) {
		if (pattern == null || pattern.isEmpty() || input == null) {
			return true;
		}
		return input.matches(toRegex(pattern));
	}

	/**
	 * Converts a FDSN match pattern into a standard regex expression.
	 *
	 * @param fdsnPattern
	 * @return
	 */
	private static String toRegex(String fdsnPattern) {
		return fdsnPattern
				.replace("*", ".*")
				.replace("?", ".");
	}

}
