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

import java.util.ArrayList;
import java.util.List;

/**
 * An operating agency and associated contact persons. Since the Contact element
 * is a generic type that represents any contact person, it also has its own
 * optional Agency element. It is expected that typically the contact’s optional
 * Agency tag will match the Operator Agency. Only contacts appropriate for the
 * enclosing element should be include in the Operator tag.
 *
 * <p>
 * Java class for OperatorType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="OperatorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Agency" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Contact" type="{http://www.fdsn.org/xml/station/1}PersonType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="WebSite" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperatorType", propOrder = {
	"agency",
	"contact",
	"webSite"
})
public class OperatorType {

	@XmlElement(name = "Agency", required = true)
	protected String agency;
	@XmlElement(name = "Contact")
	protected List<PersonType> contact;
	@XmlElement(name = "WebSite")
	@XmlSchemaType(name = "anyURI")
	protected String webSite;

	/**
	 * Gets the value of the agency property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getAgency() {
		return agency;
	}

	/**
	 * Sets the value of the agency property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setAgency(String value) {
		this.agency = value;
	}

	/**
	 * Gets the value of the contact property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the contact property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getContact().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link PersonType }
	 *
	 *
	 */
	public List<PersonType> getContact() {
		if (contact == null) {
			contact = new ArrayList<PersonType>();
		}
		return this.contact;
	}

	/**
	 * Gets the value of the webSite property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getWebSite() {
		return webSite;
	}

	/**
	 * Sets the value of the webSite property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setWebSite(String value) {
		this.webSite = value;
	}

}
