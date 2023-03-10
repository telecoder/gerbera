package gerbera.datalink;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * XML stream parser for DataLink INFO responses.
 *
 * THIS IMPLEMENTATION IS STILL INCOMPLETE AS IS DOES NOT PROCESS INCOMING BYTES
 * PROGRESSIVELY, JUST COMPLETE DATALINK INFO RESPONSES.
 *
 * @author Julian Peña.
 */
public class XMLStreamParser {

	private final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser;
	private final AsyncByteArrayFeeder feeder;

	private final List<DataLinkStream> streams;

	// Stream XML Attributes Names
	private static final String DATA_LATENCY = "DataLatency";
	private static final String EARLIEST_PACKET_DATA_END_TIME = "EarliestPacketDataEndTime";
	private static final String EARLIEST_PACKET_DATA_START_TIME = "EarliestPacketDataStartTime";
	private static final String EARLIEST_PACKET_ID = "EarliestPacketID";
	private static final String LATEST_PACKET_DATA_END_TIME = "LatestPacketDataEndTime";
	private static final String LATEST_PACKET_DATA_START_TIME = "LatestPacketDataStartTime";
	private static final String LATEST_PACKET_ID = "LatestPacketID";
	private static final String NAME = "Name";

	/**
	 * Stream XML attributes indexes as reported by ringserver. It is assumed
	 * that the indexes remain the same for every stream in the DataLink INFO
	 * response.
	 */
	private final Map<String, Integer> indexes = new HashMap<>();
	private boolean haveIndexes = false;

	/**
	 * Our logger.
	 */
	private final Logger LOG = LoggerFactory.getLogger("XML");

	public XMLStreamParser() {

		AsyncXMLInputFactory factory = new InputFactoryImpl();
		parser = factory.createAsyncForByteArray();
		feeder = parser.getInputFeeder();

		streams = new ArrayList<>();
	}

	/**
	 * Adds bytes to the internal input.
	 *
	 * @param input
	 * @throws XMLStreamException
	 */
	public void parse(byte[] input) throws XMLStreamException {
		feeder.feedInput(input, 0, input.length);
		feeder.endOfInput();
		process();
	}

	/**
	 * Processes the complete payload of a DataLink INFO packet at once.
	 *
	 * @param infoResponse
	 * @return A Future that will eventually contain a list of DataLink streams.
	 * This future will fail in case of any error.
	 */
	public List<DataLinkStream> parse(DataLinkResponse infoResponse) {
		try {
			parse(infoResponse.getData());
			return getStreams();
		} catch (XMLStreamException ex) {
			// TODO log
			return List.of();
		}
	}

	private void process() throws XMLStreamException {
		while (parser.hasNext()) {
			if (parser.next() == XMLStreamConstants.START_ELEMENT) {
				if (parser.getName().getLocalPart().equals("Stream")) {
					handleStream();
				}
			}
		}
		LOG.info("done: {} streams parsed", streams.size());
	}

	private void handleStream() {

		if (!haveIndexes) {
			fillIndexes();
		}

		try {
			DataLinkStream stream = new DataLinkStream();

			stream.setDataLatency(parser.getAttributeValue(indexes.get(DATA_LATENCY)));
			stream.setEarliestPacketDataStartTime(parser.getAttributeValue(indexes.get(EARLIEST_PACKET_DATA_START_TIME)));
			stream.setEarliestPacketDataEndTime(parser.getAttributeValue(indexes.get(EARLIEST_PACKET_DATA_END_TIME)));
			stream.setEarliestPacketID(parser.getAttributeValue(indexes.get(EARLIEST_PACKET_ID)));
			stream.setLatestPacketDataStartTime(parser.getAttributeValue(indexes.get(LATEST_PACKET_DATA_START_TIME)));
			stream.setLatestPacketDataEndTime(parser.getAttributeValue(indexes.get(LATEST_PACKET_DATA_END_TIME)));
			stream.setLatestPacketID(parser.getAttributeValue(indexes.get(LATEST_PACKET_ID)));
			stream.setName(parser.getAttributeValue(indexes.get(NAME)));

			streams.add(stream);
		} catch (Exception e) {
			// probably bad XML format or incomplete payload ...
			LOG.error("error: {}", e.getMessage());
		}
	}

	private void fillIndexes() {

		try {
			indexes.put(DATA_LATENCY, parser.getAttributeIndex(null, DATA_LATENCY));
			indexes.put(EARLIEST_PACKET_DATA_START_TIME, parser.getAttributeIndex(null, EARLIEST_PACKET_DATA_START_TIME));
			indexes.put(EARLIEST_PACKET_DATA_END_TIME, parser.getAttributeIndex(null, EARLIEST_PACKET_DATA_END_TIME));
			indexes.put(EARLIEST_PACKET_ID, parser.getAttributeIndex(null, EARLIEST_PACKET_ID));
			indexes.put(LATEST_PACKET_DATA_START_TIME, parser.getAttributeIndex(null, LATEST_PACKET_DATA_START_TIME));
			indexes.put(LATEST_PACKET_DATA_END_TIME, parser.getAttributeIndex(null, LATEST_PACKET_DATA_END_TIME));
			indexes.put(LATEST_PACKET_ID, parser.getAttributeIndex(null, LATEST_PACKET_ID));
			indexes.put(NAME, parser.getAttributeIndex(null, NAME));

			haveIndexes = true;
		} catch (Exception e) {
			// probably bad XML format or incomplete payload ...
			LOG.error("error: {}", e.getMessage());
		}

	}

	/**
	 *
	 * @return A copy of the list of streams successfully parsed from the
	 * DataLink INFO response.
	 */
	public List<DataLinkStream> getStreams() {
		return List.copyOf(streams);
	}

}
