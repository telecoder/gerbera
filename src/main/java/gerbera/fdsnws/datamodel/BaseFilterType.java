//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//
package gerbera.fdsnws.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/**
 * The BaseFilterType is derived by all filters.
 *
 * <p>
 * Java class for BaseFilterType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="BaseFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="InputUnits" type="{http://www.fdsn.org/xml/station/1}UnitsType"/&gt;
 *         &lt;element name="OutputUnits" type="{http://www.fdsn.org/xml/station/1}UnitsType"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="resourceId" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseFilterType", propOrder = {
	"description",
	"inputUnits",
	"outputUnits",
	"any"
})
@XmlSeeAlso({
	PolesZerosType.class,
	FIRType.class,
	CoefficientsType.class,
	ResponseListType.class,
	PolynomialType.class
})
public class BaseFilterType {

	@XmlElement(name = "Description")
	protected String description;
	@XmlElement(name = "InputUnits", required = true)
	protected UnitsType inputUnits;
	@XmlElement(name = "OutputUnits", required = true)
	protected UnitsType outputUnits;
	@XmlAnyElement(lax = true)
	protected List<Object> any;
	@XmlAttribute(name = "resourceId")
	protected String resourceId;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Gets the value of the description property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the inputUnits property.
	 *
	 * @return possible object is {@link UnitsType }
	 *
	 */
	public UnitsType getInputUnits() {
		return inputUnits;
	}

	/**
	 * Sets the value of the inputUnits property.
	 *
	 * @param value allowed object is {@link UnitsType }
	 *
	 */
	public void setInputUnits(UnitsType value) {
		this.inputUnits = value;
	}

	/**
	 * Gets the value of the outputUnits property.
	 *
	 * @return possible object is {@link UnitsType }
	 *
	 */
	public UnitsType getOutputUnits() {
		return outputUnits;
	}

	/**
	 * Sets the value of the outputUnits property.
	 *
	 * @param value allowed object is {@link UnitsType }
	 *
	 */
	public void setOutputUnits(UnitsType value) {
		this.outputUnits = value;
	}

	/**
	 * Gets the value of the any property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the any property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getAny().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list null	null	null	 {@link Element }
     * {@link Object }
	 *
	 *
	 */
	public List<Object> getAny() {
		if (any == null) {
			any = new ArrayList<Object>();
		}
		return this.any;
	}

	/**
	 * Gets the value of the resourceId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * Sets the value of the resourceId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setResourceId(String value) {
		this.resourceId = value;
	}

	/**
	 * Gets the value of the name property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed
	 * property on this class.
	 *
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string
	 * value of the attribute.
	 *
	 * the map returned by this method is live, and you can add new attribute by
	 * updating the map directly. Because of this design, there's no setter.
	 *
	 *
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

}