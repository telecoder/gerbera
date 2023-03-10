package gerbera.fdsnws.station;

import gerbera.config.Config;
import gerbera.datalink.DataLinkClient;
import gerbera.datalink.XMLStreamParser;
import gerbera.fdsnws.common.Query;
import gerbera.fdsnws.datamodel.ChannelType;
import gerbera.fdsnws.datamodel.NetworkType;
import gerbera.fdsnws.datamodel.ObjectFactory;
import gerbera.fdsnws.datamodel.RootType;
import gerbera.fdsnws.datamodel.StationType;

import static gerbera.fdsnws.station.StationQueryLevel.*;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Julian Peña.
 */
public class StationService {

	/**
	 * Station web service version.
	 */
	public static final String VERSION = "1.1.1";

	/**
	 * FDSN schema version.
	 */
	private static final BigDecimal FDSN_SCHEMA_VERSION = new BigDecimal(1.0);

	/**
	 * FDSN XML Source.
	 */
	private static final String SOURCE = "gerbera";

	/**
	 * XML Marshaling stuff
	 */
	private Marshaller marshaller;
	private JAXBContext jaxbContext;

	/**
	 * Factory for creating pojos for FDSN Station XML schema derived classes.
	 */
	private final ObjectFactory FDSN_FACTORY = new ObjectFactory();

	/**
	 * DataLink client.
	 */
	private final DataLinkClient datalink;

	/**
	 * Our logger.
	 */
	private final Logger LOG = LoggerFactory.getLogger("Station");

	public StationService(Vertx vertx) {
		datalink = new DataLinkClient(vertx);
	}

	/**
	 * Starts the service.
	 *
	 * @return A future that will succeed if the service starts successfully.
	 */
	public Future start() {

		LOG.info("Starting");

		// initialize xml stuff
		try {
			jaxbContext = JAXBContext.newInstance(RootType.class);
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException ex) {
			LOG.error("Failed to start JAXB marshaling");
			LOG.error(ex.getMessage());
			return Future.failedFuture("Service failed to start");
		}

		return datalink
				.connect(Config.ringserverAddress(), Config.ringserverPort())
				.compose(c -> datalink.doID("gerbera:station"))
				.onFailure(f -> LOG.error("Station service failed"));
	}

	/**
	 *
	 * @return The FDSN Station specification version that this service
	 * implements.
	 */
	public String version() {
		return VERSION;
	}

	/**
	 *
	 * @return The WADL document describing this web service. PENDING
	 * IMPLEMENTATION.
	 */
	public String wadl() {
		return "WADL not available";
	}

	/**
	 *
	 * @param query
	 * @param handler
	 * @return
	 */
	public Future query(Query query, Handler<Buffer> handler) {

		if (!query.isValid()) {
			return Future.failedFuture(query.getError());
		}

		Promise promise = Promise.promise();

		getStreams(query)
				.onSuccess(networks -> {

					if (query.getFormat() == StationQueryFormat.TEXT) {
						toText(query.getLevel(), networks, handler);
						promise.complete();
						return;
					}

					// default is xml
					toXml(networks, handler)
							.onSuccess(s -> promise.complete())
							.onFailure(f -> promise.fail(f.getMessage()));

				})
				.onFailure(f -> {
					LOG.error("Failed to query FDSN networks");
					LOG.error(f.getMessage());
					promise.fail("Failed to query FDSN networks");
				});

		return promise.future();
	}

	private Future<List<NetworkType>> getStreams(Query query) {

		Promise<List<NetworkType>> promise = Promise.promise();

		datalink
				.doINFO("STREAMS", null)
				.onSuccess(datalinkResponse -> {

					List<NetworkType> results = new ArrayList<>();

					// key is N
					Map<String, NetworkType> networks = new HashMap<>();

					// key is N_S
					Map<String, StationType> stations = new HashMap<>();

					// key is N_S_C
					Map<String, ChannelType> channels = new HashMap<>();

					XMLStreamParser parser = new XMLStreamParser();
					parser
							.parse(datalinkResponse)
							.stream()
							.map(stream -> stream.getNSLC())
							.filter(nslc -> filter(nslc.N(), query.getNetworks()))
							.filter(nslc -> filter(nslc.S(), query.getStations()))
							.filter(nslc -> filter(nslc.C(), query.getChannels()))
							.filter(nslc -> filter(nslc.L(), query.getLocations()))
							.forEach(nslc -> {

								StationQueryLevel level = query.getLevel();
								String key = nslc.N();

								NetworkType network;
								if (networks.containsKey(key)) {
									network = networks.get(key);
								} else {
									network = new NetworkType();
									network.setCode(nslc.N());
									networks.put(key, network);
								}

								if (level == NETWORK) {
									return;
								}

								StationType station = null;
								if (level == STATION || level == CHANNEL
										|| level == RESPONSE) {
									key += "_" + nslc.S();
									if (stations.containsKey(key)) {
										station = stations.get(key);
									} else {
										station = new StationType();
										station.setCode(nslc.S());
										stations.put(key, station);
										network.getStation().add(station);
									}
								}

								if (level == STATION) {
									return;
								}

								if (level == CHANNEL || level == RESPONSE) {
									key += "_" + nslc.C();
									ChannelType channel;
									if (channels.containsKey(key)) {
										channel = channels.get(key);
									} else {
										channel = new ChannelType();
										channel.setCode(nslc.C());
										channels.put(key, channel);
									}
									channel.setLocationCode(nslc.L());
									station.getChannel().add(channel);
								}
							});

					results.addAll(networks.values());
					promise.complete(results);

				})
				.onFailure(f -> promise.fail(f.getMessage()));

		return promise.future();
	}

	/**
	 *
	 * @param element The string to be tested.
	 * @param patterns A list of patterns to test the string against. MUST not
	 * be null.
	 * @return True if the element matches at least one pattern, false
	 * otherwise.
	 */
	private boolean filter(String element, List<String> patterns) {
		return patterns
				.stream()
				.anyMatch(pattern -> PatternMatcher.test(pattern, element));
	}

	private void toText(StationQueryLevel level, List<NetworkType> networks,
			Handler<Buffer> handler) {

		for (NetworkType network : networks) {

			switch (level) {

				case NETWORK -> {
					var buffer = Buffer.buffer();
					buffer
							.appendString(network.getCode() + "|")
							.appendString("description|")
							.appendString("starttime|")
							.appendString("endtime|")
							.appendString(network.getStation().size() + "")
							.appendString("\n");
					handler.handle(buffer);
				}

				case STATION -> {
					Buffer buffer;
					for (StationType station : network.getStation()) {
						buffer = Buffer.buffer();
						buffer
								.appendString(network.getCode() + "|")
								.appendString(station.getCode() + "|")
								.appendString("0|") // latitude
								.appendString("0|") // longitude
								.appendString("0|") // elevation
								.appendString("site name|")
								.appendString("0|") // start time
								.appendString("0|") // end time
								.appendString("\n");
						handler.handle(buffer);
					}
				}

				case CHANNEL -> {
					Buffer buffer;
					for (StationType station : network.getStation()) {
						for (ChannelType channel : station.getChannel()) {
							buffer = Buffer.buffer();
							buffer
									.appendString(network.getCode() + "|")
									.appendString(station.getCode() + "|")
									.appendString(channel.getLocationCode() + "|")
									.appendString(channel.getCode() + "|")
									.appendString("0|") // latitude
									.appendString("0|") // longitude
									.appendString("0|") // elevation
									.appendString("0|") // depth
									.appendString("0|") // azymuth
									.appendString("0|") // dip
									.appendString("-|") // sensor description
									.appendString("0|") // scale
									.appendString("0|") // scale frequency
									.appendString("-|") // scale units
									.appendString("-|") // sample rate
									.appendString("0|") // start time
									.appendString("0|") // end time
									.appendString("\n");
							handler.handle(buffer);
						}
					}
				}

				case RESPONSE ->
					LOG.info("Response information not available");
			}
		}
	}

	private Future<?> toXml(List<NetworkType> networks, Handler<Buffer> handler) {

		XMLStreamWriter xmlOut;
		try {
			xmlOut = XMLOutputFactory
					.newFactory()
					.createXMLStreamWriter(new StationOutputStream(handler));
		} catch (XMLStreamException ex) {
			LOG.error("Failed to create XML Factory");
			LOG.error(ex.getMessage());
			return Future.failedFuture("Failed to create XML output");
		}

		Promise promise = Promise.promise();

		RootType root = FDSN_FACTORY.createRootType();
		root.setSource(SOURCE);
		root.setSchemaVersion(FDSN_SCHEMA_VERSION);

		try {
			var factory = DatatypeFactory.newInstance();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
			root.setCreated(factory.newXMLGregorianCalendar(calendar));
		} catch (DatatypeConfigurationException ex) {
			LOG.error("Failed to create Gregorian Calendar");
			LOG.error(ex.getMessage());
		}

		List<NetworkType> networkList = root.getNetwork();
		networkList.addAll(networks);

		try {
			marshaller.marshal(root, xmlOut);
			promise.complete();
		} catch (JAXBException ex) {
			LOG.error("Failed to marshall root object");
			LOG.error(ex.getMessage());
			promise.fail("Failed to create XML output");
		}

		return promise.future();
	}

}
