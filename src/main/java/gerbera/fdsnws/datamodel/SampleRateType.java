//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//
package gerbera.fdsnws.datamodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Sample rate in samples per second.
 *
 * <p>
 * Java class for SampleRateType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="SampleRateType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;restriction base="&lt;http://www.fdsn.org/xml/station/1&gt;FloatType"&gt;
 *       &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}string" fixed="SAMPLES/S" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SampleRateType")
public class SampleRateType
		extends FloatType {

}