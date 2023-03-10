//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//
package gerbera.fdsnws.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This type represents a Station epoch. It is common to only have a single
 * station epoch with the station's creation and termination dates as the epoch
 * start and end dates.
 *
 * <p>
 * Java class for StationType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="StationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.fdsn.org/xml/station/1}BaseNodeType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Latitude" type="{http://www.fdsn.org/xml/station/1}LatitudeType"/&gt;
 *         &lt;element name="Longitude" type="{http://www.fdsn.org/xml/station/1}LongitudeType"/&gt;
 *         &lt;element name="Elevation" type="{http://www.fdsn.org/xml/station/1}DistanceType"/&gt;
 *         &lt;element name="Site" type="{http://www.fdsn.org/xml/station/1}SiteType"/&gt;
 *         &lt;element name="WaterLevel" type="{http://www.fdsn.org/xml/station/1}FloatType" minOccurs="0"/&gt;
 *         &lt;element name="Vault" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Geology" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Equipment" type="{http://www.fdsn.org/xml/station/1}EquipmentType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Operator" type="{http://www.fdsn.org/xml/station/1}OperatorType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="CreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="TerminationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="TotalNumberChannels" type="{http://www.fdsn.org/xml/station/1}CounterType" minOccurs="0"/&gt;
 *         &lt;element name="SelectedNumberChannels" type="{http://www.fdsn.org/xml/station/1}CounterType" minOccurs="0"/&gt;
 *         &lt;element name="ExternalReference" type="{http://www.fdsn.org/xml/station/1}ExternalReferenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Channel" type="{http://www.fdsn.org/xml/station/1}ChannelType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StationType", propOrder = {
	"latitude",
	"longitude",
	"elevation",
	"site",
	"waterLevel",
	"vault",
	"geology",
	"equipment",
	"operator",
	"creationDate",
	"terminationDate",
	"totalNumberChannels",
	"selectedNumberChannels",
	"externalReference",
	"channel"
})
public class StationType
		extends BaseNodeType {

	@XmlElement(name = "Latitude", required = true)
	protected LatitudeType latitude;
	@XmlElement(name = "Longitude", required = true)
	protected LongitudeType longitude;
	@XmlElement(name = "Elevation", required = true)
	protected DistanceType elevation;
	@XmlElement(name = "Site", required = true)
	protected SiteType site;
	@XmlElement(name = "WaterLevel")
	protected FloatType waterLevel;
	@XmlElement(name = "Vault")
	protected String vault;
	@XmlElement(name = "Geology")
	protected String geology;
	@XmlElement(name = "Equipment")
	protected List<EquipmentType> equipment;
	@XmlElement(name = "Operator")
	protected List<OperatorType> operator;
	@XmlElement(name = "CreationDate")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar creationDate;
	@XmlElement(name = "TerminationDate")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar terminationDate;
	@XmlElement(name = "TotalNumberChannels")
	protected BigInteger totalNumberChannels;
	@XmlElement(name = "SelectedNumberChannels")
	protected BigInteger selectedNumberChannels;
	@XmlElement(name = "ExternalReference")
	protected List<ExternalReferenceType> externalReference;
	@XmlElement(name = "Channel")
	protected List<ChannelType> channel;

	/**
	 * Gets the value of the latitude property.
	 *
	 * @return possible object is {@link LatitudeType }
	 *
	 */
	public LatitudeType getLatitude() {
		return latitude;
	}

	/**
	 * Sets the value of the latitude property.
	 *
	 * @param value allowed object is {@link LatitudeType }
	 *
	 */
	public void setLatitude(LatitudeType value) {
		this.latitude = value;
	}

	/**
	 * Gets the value of the longitude property.
	 *
	 * @return possible object is {@link LongitudeType }
	 *
	 */
	public LongitudeType getLongitude() {
		return longitude;
	}

	/**
	 * Sets the value of the longitude property.
	 *
	 * @param value allowed object is {@link LongitudeType }
	 *
	 */
	public void setLongitude(LongitudeType value) {
		this.longitude = value;
	}

	/**
	 * Gets the value of the elevation property.
	 *
	 * @return possible object is {@link DistanceType }
	 *
	 */
	public DistanceType getElevation() {
		return elevation;
	}

	/**
	 * Sets the value of the elevation property.
	 *
	 * @param value allowed object is {@link DistanceType }
	 *
	 */
	public void setElevation(DistanceType value) {
		this.elevation = value;
	}

	/**
	 * Gets the value of the site property.
	 *
	 * @return possible object is {@link SiteType }
	 *
	 */
	public SiteType getSite() {
		return site;
	}

	/**
	 * Sets the value of the site property.
	 *
	 * @param value allowed object is {@link SiteType }
	 *
	 */
	public void setSite(SiteType value) {
		this.site = value;
	}

	/**
	 * Gets the value of the waterLevel property.
	 *
	 * @return possible object is {@link FloatType }
	 *
	 */
	public FloatType getWaterLevel() {
		return waterLevel;
	}

	/**
	 * Sets the value of the waterLevel property.
	 *
	 * @param value allowed object is {@link FloatType }
	 *
	 */
	public void setWaterLevel(FloatType value) {
		this.waterLevel = value;
	}

	/**
	 * Gets the value of the vault property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getVault() {
		return vault;
	}

	/**
	 * Sets the value of the vault property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setVault(String value) {
		this.vault = value;
	}

	/**
	 * Gets the value of the geology property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getGeology() {
		return geology;
	}

	/**
	 * Sets the value of the geology property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setGeology(String value) {
		this.geology = value;
	}

	/**
	 * Gets the value of the equipment property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the equipment property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getEquipment().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link EquipmentType }
	 *
	 *
	 */
	public List<EquipmentType> getEquipment() {
		if (equipment == null) {
			equipment = new ArrayList<EquipmentType>();
		}
		return this.equipment;
	}

	/**
	 * Gets the value of the operator property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the operator property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getOperator().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link OperatorType }
	 *
	 *
	 */
	public List<OperatorType> getOperator() {
		if (operator == null) {
			operator = new ArrayList<OperatorType>();
		}
		return this.operator;
	}

	/**
	 * Gets the value of the creationDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the value of the creationDate property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setCreationDate(XMLGregorianCalendar value) {
		this.creationDate = value;
	}

	/**
	 * Gets the value of the terminationDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getTerminationDate() {
		return terminationDate;
	}

	/**
	 * Sets the value of the terminationDate property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setTerminationDate(XMLGregorianCalendar value) {
		this.terminationDate = value;
	}

	/**
	 * Gets the value of the totalNumberChannels property.
	 *
	 * @return possible object is {@link BigInteger }
	 *
	 */
	public BigInteger getTotalNumberChannels() {
		return totalNumberChannels;
	}

	/**
	 * Sets the value of the totalNumberChannels property.
	 *
	 * @param value allowed object is {@link BigInteger }
	 *
	 */
	public void setTotalNumberChannels(BigInteger value) {
		this.totalNumberChannels = value;
	}

	/**
	 * Gets the value of the selectedNumberChannels property.
	 *
	 * @return possible object is {@link BigInteger }
	 *
	 */
	public BigInteger getSelectedNumberChannels() {
		return selectedNumberChannels;
	}

	/**
	 * Sets the value of the selectedNumberChannels property.
	 *
	 * @param value allowed object is {@link BigInteger }
	 *
	 */
	public void setSelectedNumberChannels(BigInteger value) {
		this.selectedNumberChannels = value;
	}

	/**
	 * Gets the value of the externalReference property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the externalReference property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getExternalReference().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link ExternalReferenceType }
	 *
	 *
	 */
	public List<ExternalReferenceType> getExternalReference() {
		if (externalReference == null) {
			externalReference = new ArrayList<ExternalReferenceType>();
		}
		return this.externalReference;
	}

	/**
	 * Gets the value of the channel property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the channel property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getChannel().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link ChannelType }
	 *
	 *
	 */
	public List<ChannelType> getChannel() {
		if (channel == null) {
			channel = new ArrayList<ChannelType>();
		}
		return this.channel;
	}

}