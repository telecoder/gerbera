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

/**
 * This type contains a URI and description for external data that users may
 * want to reference in StationXML.
 *
 * <p>
 * Java class for ExternalReferenceType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ExternalReferenceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExternalReferenceType", propOrder = {
	"uri",
	"description"
})
public class ExternalReferenceType {

	@XmlElement(name = "URI", required = true)
	@XmlSchemaType(name = "anyURI")
	protected String uri;
	@XmlElement(name = "Description", required = true)
	protected String description;

	/**
	 * Gets the value of the uri property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * Sets the value of the uri property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setURI(String value) {
		this.uri = value;
	}

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

}