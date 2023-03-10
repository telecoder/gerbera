//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//
package gerbera.fdsnws.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigInteger;

/**
 * Sample rate expressed as number of samples in a number of seconds.
 *
 * <p>
 * Java class for SampleRateRatioType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="SampleRateRatioType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NumberSamples" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="NumberSeconds" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SampleRateRatioType", propOrder = {
	"numberSamples",
	"numberSeconds"
})
public class SampleRateRatioType {

	@XmlElement(name = "NumberSamples", required = true)
	protected BigInteger numberSamples;
	@XmlElement(name = "NumberSeconds", required = true)
	protected BigInteger numberSeconds;

	/**
	 * Gets the value of the numberSamples property.
	 *
	 * @return possible object is {@link BigInteger }
	 *
	 */
	public BigInteger getNumberSamples() {
		return numberSamples;
	}

	/**
	 * Sets the value of the numberSamples property.
	 *
	 * @param value allowed object is {@link BigInteger }
	 *
	 */
	public void setNumberSamples(BigInteger value) {
		this.numberSamples = value;
	}

	/**
	 * Gets the value of the numberSeconds property.
	 *
	 * @return possible object is {@link BigInteger }
	 *
	 */
	public BigInteger getNumberSeconds() {
		return numberSeconds;
	}

	/**
	 * Sets the value of the numberSeconds property.
	 *
	 * @param value allowed object is {@link BigInteger }
	 *
	 */
	public void setNumberSeconds(BigInteger value) {
		this.numberSeconds = value;
	}

}