//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//
package gerbera.fdsnws.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Container for a comment or log entry. Corresponds to SEED blockettes 31, 51
 * and 59.
 *
 * <p>
 * Java class for CommentType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="CommentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BeginEffectiveTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="EndEffectiveTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="Author" type="{http://www.fdsn.org/xml/station/1}PersonType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.fdsn.org/xml/station/1}CounterType" /&gt;
 *       &lt;attribute name="subject" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommentType", propOrder = {
	"value",
	"beginEffectiveTime",
	"endEffectiveTime",
	"author"
})
public class CommentType {

	@XmlElement(name = "Value", required = true)
	protected String value;
	@XmlElement(name = "BeginEffectiveTime")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar beginEffectiveTime;
	@XmlElement(name = "EndEffectiveTime")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar endEffectiveTime;
	@XmlElement(name = "Author")
	protected List<PersonType> author;
	@XmlAttribute(name = "id")
	protected BigInteger id;
	@XmlAttribute(name = "subject")
	protected String subject;

	/**
	 * Gets the value of the value property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the beginEffectiveTime property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getBeginEffectiveTime() {
		return beginEffectiveTime;
	}

	/**
	 * Sets the value of the beginEffectiveTime property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setBeginEffectiveTime(XMLGregorianCalendar value) {
		this.beginEffectiveTime = value;
	}

	/**
	 * Gets the value of the endEffectiveTime property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getEndEffectiveTime() {
		return endEffectiveTime;
	}

	/**
	 * Sets the value of the endEffectiveTime property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setEndEffectiveTime(XMLGregorianCalendar value) {
		this.endEffectiveTime = value;
	}

	/**
	 * Gets the value of the author property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the author property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getAuthor().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link PersonType }
	 *
	 *
	 */
	public List<PersonType> getAuthor() {
		if (author == null) {
			author = new ArrayList<PersonType>();
		}
		return this.author;
	}

	/**
	 * Gets the value of the id property.
	 *
	 * @return possible object is {@link BigInteger }
	 *
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 * @param value allowed object is {@link BigInteger }
	 *
	 */
	public void setId(BigInteger value) {
		this.id = value;
	}

	/**
	 * Gets the value of the subject property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the value of the subject property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setSubject(String value) {
		this.subject = value;
	}

}