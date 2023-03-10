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
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Response: FIR filter. Corresponds to SEED blockette 61. FIR filters are also
 * commonly documented using the CoefficientsType element.
 *
 * <p>
 * Java class for FIRType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="FIRType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.fdsn.org/xml/station/1}BaseFilterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Symmetry"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *               &lt;enumeration value="NONE"/&gt;
 *               &lt;enumeration value="EVEN"/&gt;
 *               &lt;enumeration value="ODD"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NumeratorCoefficient" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;double"&gt;
 *                 &lt;attribute name="i" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
@XmlType(name = "FIRType", propOrder = {
	"symmetry",
	"numeratorCoefficient"
})
public class FIRType
		extends BaseFilterType {

	@XmlElement(name = "Symmetry", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String symmetry;
	@XmlElement(name = "NumeratorCoefficient")
	protected List<FIRType.NumeratorCoefficient> numeratorCoefficient;

	/**
	 * Gets the value of the symmetry property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getSymmetry() {
		return symmetry;
	}

	/**
	 * Sets the value of the symmetry property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setSymmetry(String value) {
		this.symmetry = value;
	}

	/**
	 * Gets the value of the numeratorCoefficient property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the numeratorCoefficient property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getNumeratorCoefficient().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
     * {@link FIRType.NumeratorCoefficient }
	 *
	 *
	 */
	public List<FIRType.NumeratorCoefficient> getNumeratorCoefficient() {
		if (numeratorCoefficient == null) {
			numeratorCoefficient = new ArrayList<FIRType.NumeratorCoefficient>();
		}
		return this.numeratorCoefficient;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 *
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 *
	 * <pre>
	 * &lt;complexType&gt;
	 *   &lt;simpleContent&gt;
	 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;double"&gt;
	 *       &lt;attribute name="i" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
	 *     &lt;/extension&gt;
	 *   &lt;/simpleContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 *
	 *
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
		"value"
	})
	public static class NumeratorCoefficient {

		@XmlValue
		protected double value;
		@XmlAttribute(name = "i")
		protected BigInteger i;

		/**
		 * Gets the value of the value property.
		 *
		 */
		public double getValue() {
			return value;
		}

		/**
		 * Sets the value of the value property.
		 *
		 */
		public void setValue(double value) {
			this.value = value;
		}

		/**
		 * Gets the value of the i property.
		 *
		 * @return possible object is {@link BigInteger }
		 *
		 */
		public BigInteger getI() {
			return i;
		}

		/**
		 * Sets the value of the i property.
		 *
		 * @param value allowed object is {@link BigInteger }
		 *
		 */
		public void setI(BigInteger value) {
			this.i = value;
		}

	}

}
