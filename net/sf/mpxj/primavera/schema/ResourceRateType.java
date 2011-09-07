//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.15 at 08:47:18 AM BST 
//

package net.sf.mpxj.primavera.schema;

import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for ResourceRateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceRateType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="CreateUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EffectiveDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="LastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="LastUpdateUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MaxUnitsPerTime" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0.0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PricePerUnit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0.0"/>
 *               &lt;maxInclusive value="9.99999999999999E12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PricePerUnit2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0.0"/>
 *               &lt;maxInclusive value="9.99999999999999E12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PricePerUnit3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0.0"/>
 *               &lt;maxInclusive value="9.99999999999999E12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PricePerUnit4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0.0"/>
 *               &lt;maxInclusive value="9.99999999999999E12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PricePerUnit5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0.0"/>
 *               &lt;maxInclusive value="9.99999999999999E12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ResourceId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ResourceName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ResourceObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ShiftPeriodObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "ResourceRateType", propOrder =
{
   "createDate",
   "createUser",
   "effectiveDate",
   "lastUpdateDate",
   "lastUpdateUser",
   "maxUnitsPerTime",
   "objectId",
   "pricePerUnit",
   "pricePerUnit2",
   "pricePerUnit3",
   "pricePerUnit4",
   "pricePerUnit5",
   "resourceId",
   "resourceName",
   "resourceObjectId",
   "shiftPeriodObjectId"
}) @SuppressWarnings("all") public class ResourceRateType
{

   @XmlElementRef(name = "CreateDate", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Date> createDate;
   @XmlElement(name = "CreateUser") protected String createUser;
   @XmlElement(name = "EffectiveDate", type = String.class) @XmlJavaTypeAdapter(Adapter1.class) @XmlSchemaType(name = "dateTime") protected Date effectiveDate;
   @XmlElementRef(name = "LastUpdateDate", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Date> lastUpdateDate;
   @XmlElement(name = "LastUpdateUser") protected String lastUpdateUser;
   @XmlElementRef(name = "MaxUnitsPerTime", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> maxUnitsPerTime;
   @XmlElement(name = "ObjectId") protected Integer objectId;
   @XmlElementRef(name = "PricePerUnit", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> pricePerUnit;
   @XmlElementRef(name = "PricePerUnit2", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> pricePerUnit2;
   @XmlElementRef(name = "PricePerUnit3", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> pricePerUnit3;
   @XmlElementRef(name = "PricePerUnit4", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> pricePerUnit4;
   @XmlElementRef(name = "PricePerUnit5", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> pricePerUnit5;
   @XmlElement(name = "ResourceId") protected String resourceId;
   @XmlElement(name = "ResourceName") protected String resourceName;
   @XmlElement(name = "ResourceObjectId") protected Integer resourceObjectId;
   @XmlElementRef(name = "ShiftPeriodObjectId", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Integer> shiftPeriodObjectId;

   /**
    * Gets the value of the createDate property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Date }{@code >}
    *     
    */
   public JAXBElement<Date> getCreateDate()
   {
      return createDate;
   }

   /**
    * Sets the value of the createDate property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Date }{@code >}
    *     
    */
   public void setCreateDate(JAXBElement<Date> value)
   {
      this.createDate = ((JAXBElement<Date>) value);
   }

   /**
    * Gets the value of the createUser property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getCreateUser()
   {
      return createUser;
   }

   /**
    * Sets the value of the createUser property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setCreateUser(String value)
   {
      this.createUser = value;
   }

   /**
    * Gets the value of the effectiveDate property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public Date getEffectiveDate()
   {
      return effectiveDate;
   }

   /**
    * Sets the value of the effectiveDate property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setEffectiveDate(Date value)
   {
      this.effectiveDate = value;
   }

   /**
    * Gets the value of the lastUpdateDate property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Date }{@code >}
    *     
    */
   public JAXBElement<Date> getLastUpdateDate()
   {
      return lastUpdateDate;
   }

   /**
    * Sets the value of the lastUpdateDate property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Date }{@code >}
    *     
    */
   public void setLastUpdateDate(JAXBElement<Date> value)
   {
      this.lastUpdateDate = ((JAXBElement<Date>) value);
   }

   /**
    * Gets the value of the lastUpdateUser property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getLastUpdateUser()
   {
      return lastUpdateUser;
   }

   /**
    * Sets the value of the lastUpdateUser property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setLastUpdateUser(String value)
   {
      this.lastUpdateUser = value;
   }

   /**
    * Gets the value of the maxUnitsPerTime property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getMaxUnitsPerTime()
   {
      return maxUnitsPerTime;
   }

   /**
    * Sets the value of the maxUnitsPerTime property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setMaxUnitsPerTime(JAXBElement<Double> value)
   {
      this.maxUnitsPerTime = ((JAXBElement<Double>) value);
   }

   /**
    * Gets the value of the objectId property.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getObjectId()
   {
      return objectId;
   }

   /**
    * Sets the value of the objectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setObjectId(Integer value)
   {
      this.objectId = value;
   }

   /**
    * Gets the value of the pricePerUnit property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getPricePerUnit()
   {
      return pricePerUnit;
   }

   /**
    * Sets the value of the pricePerUnit property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setPricePerUnit(JAXBElement<Double> value)
   {
      this.pricePerUnit = ((JAXBElement<Double>) value);
   }

   /**
    * Gets the value of the pricePerUnit2 property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getPricePerUnit2()
   {
      return pricePerUnit2;
   }

   /**
    * Sets the value of the pricePerUnit2 property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setPricePerUnit2(JAXBElement<Double> value)
   {
      this.pricePerUnit2 = ((JAXBElement<Double>) value);
   }

   /**
    * Gets the value of the pricePerUnit3 property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getPricePerUnit3()
   {
      return pricePerUnit3;
   }

   /**
    * Sets the value of the pricePerUnit3 property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setPricePerUnit3(JAXBElement<Double> value)
   {
      this.pricePerUnit3 = ((JAXBElement<Double>) value);
   }

   /**
    * Gets the value of the pricePerUnit4 property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getPricePerUnit4()
   {
      return pricePerUnit4;
   }

   /**
    * Sets the value of the pricePerUnit4 property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setPricePerUnit4(JAXBElement<Double> value)
   {
      this.pricePerUnit4 = ((JAXBElement<Double>) value);
   }

   /**
    * Gets the value of the pricePerUnit5 property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getPricePerUnit5()
   {
      return pricePerUnit5;
   }

   /**
    * Sets the value of the pricePerUnit5 property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setPricePerUnit5(JAXBElement<Double> value)
   {
      this.pricePerUnit5 = ((JAXBElement<Double>) value);
   }

   /**
    * Gets the value of the resourceId property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getResourceId()
   {
      return resourceId;
   }

   /**
    * Sets the value of the resourceId property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setResourceId(String value)
   {
      this.resourceId = value;
   }

   /**
    * Gets the value of the resourceName property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getResourceName()
   {
      return resourceName;
   }

   /**
    * Sets the value of the resourceName property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setResourceName(String value)
   {
      this.resourceName = value;
   }

   /**
    * Gets the value of the resourceObjectId property.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getResourceObjectId()
   {
      return resourceObjectId;
   }

   /**
    * Sets the value of the resourceObjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setResourceObjectId(Integer value)
   {
      this.resourceObjectId = value;
   }

   /**
    * Gets the value of the shiftPeriodObjectId property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
    *     
    */
   public JAXBElement<Integer> getShiftPeriodObjectId()
   {
      return shiftPeriodObjectId;
   }

   /**
    * Sets the value of the shiftPeriodObjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
    *     
    */
   public void setShiftPeriodObjectId(JAXBElement<Integer> value)
   {
      this.shiftPeriodObjectId = ((JAXBElement<Integer>) value);
   }

}
