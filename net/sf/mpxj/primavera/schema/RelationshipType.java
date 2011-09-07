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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RelationshipType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelationshipType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="CreateUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsPredecessorBaseline" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsSuccessorBaseline" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Lag" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="LastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="LastUpdateUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PredecessorActivityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PredecessorActivityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PredecessorActivityObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PredecessorActivityType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Task Dependent"/>
 *               &lt;enumeration value="Resource Dependent"/>
 *               &lt;enumeration value="Level of Effort"/>
 *               &lt;enumeration value="Start Milestone"/>
 *               &lt;enumeration value="Finish Milestone"/>
 *               &lt;enumeration value="WBS Summary"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PredecessorProjectId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PredecessorProjectObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SuccessorActivityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SuccessorActivityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SuccessorActivityObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SuccessorActivityType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Task Dependent"/>
 *               &lt;enumeration value="Resource Dependent"/>
 *               &lt;enumeration value="Level of Effort"/>
 *               &lt;enumeration value="Start Milestone"/>
 *               &lt;enumeration value="Finish Milestone"/>
 *               &lt;enumeration value="WBS Summary"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SuccessorProjectId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SuccessorProjectObjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Type" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Finish to Start"/>
 *               &lt;enumeration value="Finish to Finish"/>
 *               &lt;enumeration value="Start to Start"/>
 *               &lt;enumeration value="Start to Finish"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "RelationshipType", propOrder =
{
   "createDate",
   "createUser",
   "isPredecessorBaseline",
   "isSuccessorBaseline",
   "lag",
   "lastUpdateDate",
   "lastUpdateUser",
   "objectId",
   "predecessorActivityId",
   "predecessorActivityName",
   "predecessorActivityObjectId",
   "predecessorActivityType",
   "predecessorProjectId",
   "predecessorProjectObjectId",
   "successorActivityId",
   "successorActivityName",
   "successorActivityObjectId",
   "successorActivityType",
   "successorProjectId",
   "successorProjectObjectId",
   "type"
}) @SuppressWarnings("all") public class RelationshipType
{

   @XmlElementRef(name = "CreateDate", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Date> createDate;
   @XmlElement(name = "CreateUser") protected String createUser;
   @XmlElement(name = "IsPredecessorBaseline") protected Boolean isPredecessorBaseline;
   @XmlElement(name = "IsSuccessorBaseline") protected Boolean isSuccessorBaseline;
   @XmlElementRef(name = "Lag", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Double> lag;
   @XmlElementRef(name = "LastUpdateDate", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Date> lastUpdateDate;
   @XmlElement(name = "LastUpdateUser") protected String lastUpdateUser;
   @XmlElement(name = "ObjectId") protected Integer objectId;
   @XmlElement(name = "PredecessorActivityId") protected String predecessorActivityId;
   @XmlElement(name = "PredecessorActivityName") protected String predecessorActivityName;
   @XmlElement(name = "PredecessorActivityObjectId") protected Integer predecessorActivityObjectId;
   @XmlElement(name = "PredecessorActivityType") protected String predecessorActivityType;
   @XmlElement(name = "PredecessorProjectId") protected String predecessorProjectId;
   @XmlElementRef(name = "PredecessorProjectObjectId", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Integer> predecessorProjectObjectId;
   @XmlElement(name = "SuccessorActivityId") protected String successorActivityId;
   @XmlElement(name = "SuccessorActivityName") protected String successorActivityName;
   @XmlElement(name = "SuccessorActivityObjectId") protected Integer successorActivityObjectId;
   @XmlElement(name = "SuccessorActivityType") protected String successorActivityType;
   @XmlElement(name = "SuccessorProjectId") protected String successorProjectId;
   @XmlElementRef(name = "SuccessorProjectObjectId", namespace = "http://xmlns.oracle.com/Primavera/P6/V7/API/BusinessObjects", type = JAXBElement.class) protected JAXBElement<Integer> successorProjectObjectId;
   @XmlElement(name = "Type") protected String type;

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
    * Gets the value of the isPredecessorBaseline property.
    * 
    * @return
    *     possible object is
    *     {@link Boolean }
    *     
    */
   public Boolean isIsPredecessorBaseline()
   {
      return isPredecessorBaseline;
   }

   /**
    * Sets the value of the isPredecessorBaseline property.
    * 
    * @param value
    *     allowed object is
    *     {@link Boolean }
    *     
    */
   public void setIsPredecessorBaseline(Boolean value)
   {
      this.isPredecessorBaseline = value;
   }

   /**
    * Gets the value of the isSuccessorBaseline property.
    * 
    * @return
    *     possible object is
    *     {@link Boolean }
    *     
    */
   public Boolean isIsSuccessorBaseline()
   {
      return isSuccessorBaseline;
   }

   /**
    * Sets the value of the isSuccessorBaseline property.
    * 
    * @param value
    *     allowed object is
    *     {@link Boolean }
    *     
    */
   public void setIsSuccessorBaseline(Boolean value)
   {
      this.isSuccessorBaseline = value;
   }

   /**
    * Gets the value of the lag property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public JAXBElement<Double> getLag()
   {
      return lag;
   }

   /**
    * Sets the value of the lag property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Double }{@code >}
    *     
    */
   public void setLag(JAXBElement<Double> value)
   {
      this.lag = ((JAXBElement<Double>) value);
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
    * Gets the value of the predecessorActivityId property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getPredecessorActivityId()
   {
      return predecessorActivityId;
   }

   /**
    * Sets the value of the predecessorActivityId property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setPredecessorActivityId(String value)
   {
      this.predecessorActivityId = value;
   }

   /**
    * Gets the value of the predecessorActivityName property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getPredecessorActivityName()
   {
      return predecessorActivityName;
   }

   /**
    * Sets the value of the predecessorActivityName property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setPredecessorActivityName(String value)
   {
      this.predecessorActivityName = value;
   }

   /**
    * Gets the value of the predecessorActivityObjectId property.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getPredecessorActivityObjectId()
   {
      return predecessorActivityObjectId;
   }

   /**
    * Sets the value of the predecessorActivityObjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setPredecessorActivityObjectId(Integer value)
   {
      this.predecessorActivityObjectId = value;
   }

   /**
    * Gets the value of the predecessorActivityType property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getPredecessorActivityType()
   {
      return predecessorActivityType;
   }

   /**
    * Sets the value of the predecessorActivityType property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setPredecessorActivityType(String value)
   {
      this.predecessorActivityType = value;
   }

   /**
    * Gets the value of the predecessorProjectId property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getPredecessorProjectId()
   {
      return predecessorProjectId;
   }

   /**
    * Sets the value of the predecessorProjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setPredecessorProjectId(String value)
   {
      this.predecessorProjectId = value;
   }

   /**
    * Gets the value of the predecessorProjectObjectId property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
    *     
    */
   public JAXBElement<Integer> getPredecessorProjectObjectId()
   {
      return predecessorProjectObjectId;
   }

   /**
    * Sets the value of the predecessorProjectObjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
    *     
    */
   public void setPredecessorProjectObjectId(JAXBElement<Integer> value)
   {
      this.predecessorProjectObjectId = ((JAXBElement<Integer>) value);
   }

   /**
    * Gets the value of the successorActivityId property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getSuccessorActivityId()
   {
      return successorActivityId;
   }

   /**
    * Sets the value of the successorActivityId property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setSuccessorActivityId(String value)
   {
      this.successorActivityId = value;
   }

   /**
    * Gets the value of the successorActivityName property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getSuccessorActivityName()
   {
      return successorActivityName;
   }

   /**
    * Sets the value of the successorActivityName property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setSuccessorActivityName(String value)
   {
      this.successorActivityName = value;
   }

   /**
    * Gets the value of the successorActivityObjectId property.
    * 
    * @return
    *     possible object is
    *     {@link Integer }
    *     
    */
   public Integer getSuccessorActivityObjectId()
   {
      return successorActivityObjectId;
   }

   /**
    * Sets the value of the successorActivityObjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link Integer }
    *     
    */
   public void setSuccessorActivityObjectId(Integer value)
   {
      this.successorActivityObjectId = value;
   }

   /**
    * Gets the value of the successorActivityType property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getSuccessorActivityType()
   {
      return successorActivityType;
   }

   /**
    * Sets the value of the successorActivityType property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setSuccessorActivityType(String value)
   {
      this.successorActivityType = value;
   }

   /**
    * Gets the value of the successorProjectId property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getSuccessorProjectId()
   {
      return successorProjectId;
   }

   /**
    * Sets the value of the successorProjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setSuccessorProjectId(String value)
   {
      this.successorProjectId = value;
   }

   /**
    * Gets the value of the successorProjectObjectId property.
    * 
    * @return
    *     possible object is
    *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
    *     
    */
   public JAXBElement<Integer> getSuccessorProjectObjectId()
   {
      return successorProjectObjectId;
   }

   /**
    * Sets the value of the successorProjectObjectId property.
    * 
    * @param value
    *     allowed object is
    *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
    *     
    */
   public void setSuccessorProjectObjectId(JAXBElement<Integer> value)
   {
      this.successorProjectObjectId = ((JAXBElement<Integer>) value);
   }

   /**
    * Gets the value of the type property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getType()
   {
      return type;
   }

   /**
    * Sets the value of the type property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setType(String value)
   {
      this.type = value;
   }

}
