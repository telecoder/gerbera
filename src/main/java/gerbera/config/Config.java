package gerbera.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class used to get values from gerbera.properties file.
 *
 * @author Julian Peña.
 */
public abstract class Config {

	/**
	 * Our logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger("Config");

	private static final Properties PROPERTIES;

	static {
		PROPERTIES = new Properties();
		try {
			PROPERTIES.load(new FileInputStream("gerbera.properties"));
		} catch (FileNotFoundException ex) {
			LOG.error(ex.getMessage());
			LOG.error("Missing gerbera.properties file?");
			LOG.warn("Using default values");
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			LOG.error("Couldn't open gerbera.properties");
			LOG.warn("Using default values");
		}
	}

	// prevents instantiation
	private Config() {
	}

	/**
	 *
	 * @return The ringserver address. If not configured then 'localhost' is
	 * returned.
	 */
	public static String ringserverAddress() {
		// TODO validate proper address
		return getString("ringserver", "localhost");
	}

	/**
	 *
	 * @return The ringserver DataLink port. If not configured then 16000 is
	 * returned.
	 */
	public static int ringserverPort() {
		int port = getInt("ringserverPort", 16000);
		if (port < 1 || port > 65535) {
			port = 8080;
		}
		return port;
	}

	/**
	 *
	 * @return The timeout for ringserver responses in milliseconds. If not
	 * configured, 500 is returned.
	 */
	public static int timeout() {
		int timeout = getInt("timeout", 500);
		if (timeout < 0) {
			return 500;
		}
		return timeout;
	}

	/**
	 *
	 * @return The listening port for FDSN Station and Dataselect services. If
	 * not configured, 8080 is returned.
	 */
	public static int port() {
		int value = getInt("port", 8080);
		if (value < 1 || value > 65535) {
			value = 8080;
		}
		return value;
	}

	private static int getInt(String property, int defaultValue) {

		String stringProperty = Config.PROPERTIES.getProperty(property);

		if (stringProperty == null || stringProperty.length() < 1) {
			LOG.warn("Empty or invalid {}, using {}", property, defaultValue);
			return defaultValue;
		}

		try {
			return Integer.parseInt(stringProperty);
		} catch (NumberFormatException ex) {
			LOG.error("Non numeric {}, using {}", property, defaultValue);
		}
		return defaultValue;
	}

	private static String getString(String property, String defaultValue) {
		String stringProperty = Config.PROPERTIES.getProperty(property);
		if (stringProperty == null || stringProperty.length() < 1) {
			LOG.warn("Empty or invalid {}, using {}", property, defaultValue);
			return defaultValue;
		} else {
			return stringProperty;
		}
	}

}
