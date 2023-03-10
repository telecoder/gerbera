package gerbera.datalink;

import gerbera.fdsnws.common.NSLC;

/**
 * Class for the DataLink 'Stream" abstraction used in server INFO responses.
 *
 * @author Julian Peña.
 */
public class DataLinkStream {

	private String DataLatency;
	private String EarliestPacketDataEndTime;
	private String EarliestPacketDataStartTime;
	private String EarliestPacketID;
	private String LatestPacketDataEndTime;
	private String LatestPacketDataStartTime;
	private String LatestPacketID;
	private String Name;

	public DataLinkStream() {
	}

	public String getDataLatency() {
		return DataLatency;
	}

	public void setDataLatency(String DataLatency) {
		this.DataLatency = DataLatency;
	}

	public String getEarliestPacketDataEndTime() {
		return EarliestPacketDataEndTime;
	}

	public void setEarliestPacketDataEndTime(String EarliestPacketDataEndTime) {
		this.EarliestPacketDataEndTime = EarliestPacketDataEndTime;
	}

	public String getEarliestPacketDataStartTime() {
		return EarliestPacketDataStartTime;
	}

	public void setEarliestPacketDataStartTime(String EarliestPacketDataStartTime) {
		this.EarliestPacketDataStartTime = EarliestPacketDataStartTime;
	}

	public String getEarliestPacketID() {
		return EarliestPacketID;
	}

	public void setEarliestPacketID(String EarliestPacketID) {
		this.EarliestPacketID = EarliestPacketID;
	}

	public String getLatestPacketDataEndTime() {
		return LatestPacketDataEndTime;
	}

	public void setLatestPacketDataEndTime(String LatestPacketDataEndTime) {
		this.LatestPacketDataEndTime = LatestPacketDataEndTime;
	}

	public String getLatestPacketDataStartTime() {
		return LatestPacketDataStartTime;
	}

	public void setLatestPacketDataStartTime(String LatestPacketDataStartTime) {
		this.LatestPacketDataStartTime = LatestPacketDataStartTime;
	}

	public String getLatestPacketID() {
		return LatestPacketID;
	}

	public void setLatestPacketID(String LatestPacketID) {
		this.LatestPacketID = LatestPacketID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public NSLC getNSLC() {
		String[] split = Name.substring(0, Name.indexOf("/")).split("_");
		return new NSLC(split[0], split[1], split[2], split[3]);
	}

}
