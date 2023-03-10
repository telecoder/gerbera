package gerbera.fdsnws.common;

import gerbera.fdsnws.station.StationQueryFormat;
import gerbera.fdsnws.station.StationQueryLevel;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Julian Peña.
 */
public class Query {

	protected QueryType type = QueryType.INVALID;
	protected QueryMethod method = QueryMethod.INVALID;

	protected String starttime;
	protected String endtime;

	protected final List<String> stations = new ArrayList<>();
	protected final List<String> locations = new ArrayList<>();
	protected final List<String> networks = new ArrayList<>();
	protected final List<String> channels = new ArrayList<>();

	/**
	 * If this Query object is invalid for some reason, this variable will
	 * contain an error description.
	 */
	protected String error;

	/**
	 * Level of detail for the query.
	 */
	protected StationQueryLevel level = StationQueryLevel.NETWORK;

	/**
	 * Output format.
	 */
	protected StationQueryFormat format;

	protected Query() {
	}

	public QueryMethod getMethod() {
		return method;
	}

	/**
	 *
	 * @return The start time as specified in the FDSN query.
	 */
	public String getStarttimeFdsn() {
		return starttime;
	}

	/**
	 *
	 * @return The start time formatted for DataLink.
	 */
	public String getStarttime() {
		return starttime
				.replace("-", "/")
				.replace("T", " ");
	}

	/**
	 *
	 * @return The start time as microseconds from epoch. However, the precision
	 * is limited to milliseconds.
	 */
	public long getStartTime() {
		return millisFromEpoch(starttime) * 1000;
	}

	/**
	 *
	 * @return The end time as specified in the FDSN query.
	 */
	public String getEndtimeFdsn() {
		return endtime;
	}

	/**
	 *
	 * @return The end time formatted for DataLink.
	 */
	public String getEndtime() {
		return endtime
				.replace("-", "/")
				.replace("T", "")
				.replace("T", "");
	}

	/**
	 *
	 * @return The start time as microseconds from epoch. However, the precision
	 * is limited to milliseconds.
	 */
	public long getEndTime() {
		return millisFromEpoch(endtime) * 1000;
	}

	public List<String> getNetworks() {
		return networks;
	}

	public List<String> getStations() {
		return stations;
	}

	public List<String> getChannels() {
		return channels;
	}

	public List<String> getLocations() {
		return locations;
	}

	public String getError() {
		return error;
	}

	public StationQueryLevel getLevel() {
		return level;
	}

	public StationQueryFormat getFormat() {
		return format;
	}

	public QueryType getType() {
		return type;
	}

	public boolean isStationQuery() {
		return type == QueryType.STATION;
	}

	public boolean isDataselectQuery() {
		return type == QueryType.DATASELECT;
	}

	public boolean isValid() {

		if (type == QueryType.INVALID || method == QueryMethod.INVALID) {
			error = "query type or method is(are) invalid " + type + " " + method;
			return false;
		}

		if (type == QueryType.STATION) {
			if (method == QueryMethod.QUERY_AUTH) {
				error = "method query_auth is not a valid Station method";
				return false;
			}
			if (level == StationQueryLevel.INVALID
					|| format == StationQueryFormat.INVALID) {
				error = "query level or format is(are) invalid";
				return false;
			}
			return true;
		}

		if (type == QueryType.DATASELECT) {

			if (starttime == null || starttime.isBlank()
					|| endtime == null || endtime.isBlank()) {
				error = "starttime or endtime is(are) invalid: "
						+ starttime + " " + endtime;
				return false;
			}

			Long start = millisFromEpoch(starttime);
			if (start == null && start < 0) {
				error = "starttime is invalid: " + starttime;
				return false;
			}

			Long end = millisFromEpoch(endtime);
			if (end == null) {
				error = "endtime is invalid: " + starttime;
				return false;
			}

			if (start > end) {
				error = "starttime can't be after endtime";
				return false;
			}

			return true;
		}

		return false;
	}

	/**
	 * Returns milliseconds from epoch given that the timestamp uses one of the
	 * following formats:
	 * <p>
	 * yyyy-MM-ddTHH:mm:ss.SSSSSS or yyyy-MM-ddTHH:mm:ss yyyy-MM-dd
	 *
	 * @param timestamp
	 * @return milliseconds from epoch if the timestamp is well formatted, null
	 * otherwise.
	 */
	private Long millisFromEpoch(String timestamp) {

		try {
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
					.appendPattern("yyyy-MM-dd'T'HH:mm:ss")
					.appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
					.toFormatter();
			return LocalDateTime
					.parse(timestamp, formatter)
					.toInstant(ZoneOffset.UTC)
					.toEpochMilli();
		} catch (DateTimeParseException ex) {
			// timestamp is not correctly formatted
		} catch (DateTimeException ex) {
			// timestamp couldn't be converted to Instant
		}

		try {
			return LocalDate
					.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
					.atStartOfDay()
					.toInstant(ZoneOffset.UTC)
					.toEpochMilli();
		} catch (DateTimeParseException ex) {
			// timestamp is not correctly formatted
		} catch (DateTimeException ex) {
			// timestamp couldn't be converted to Instant
		}

		return null;
	}

	@Override
	public String toString() {
		return type + " "
				+ method + " "
				+ networks + " "
				+ stations + " "
				+ channels + " "
				+ locations + " "
				+ starttime + " "
				+ endtime;
	}

}
