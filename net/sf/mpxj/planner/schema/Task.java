//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.09.11 at 10:06:48 PM BST 
//

package net.sf.mpxj.planner.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "", propOrder =
{
   "properties",
   "constraint",
   "predecessors",
   "task"
}) @XmlRootElement(name = "task") @SuppressWarnings("all") public class Task
{

   @XmlAttribute(required = true) @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String id;
   @XmlAttribute(required = true) @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String name;
   @XmlAttribute @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String note;
   @XmlAttribute @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String effort;
   @XmlAttribute(required = true) @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String start;
   @XmlAttribute(required = true) @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String end;
   @XmlAttribute(name = "work-start") @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String workStart;
   @XmlAttribute @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String duration;
   @XmlAttribute @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String work;
   @XmlAttribute(name = "percent-complete") @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String percentComplete;
   @XmlAttribute @XmlJavaTypeAdapter(NormalizedStringAdapter.class) protected String priority;
   @XmlAttribute @XmlJavaTypeAdapter(CollapsedStringAdapter.class) protected String type;
   @XmlAttribute @XmlJavaTypeAdapter(CollapsedStringAdapter.class) protected String scheduling;
   protected Properties properties;
   protected Constraint constraint;
   protected Predecessors predecessors;
   protected List<Task> task;

   /**
    * Gets the value of the id property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getId()
   {
      return id;
   }

   /**
    * Sets the value of the id property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setId(String value)
   {
      this.id = value;
   }

   /**
    * Gets the value of the name property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getName()
   {
      return name;
   }

   /**
    * Sets the value of the name property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setName(String value)
   {
      this.name = value;
   }

   /**
    * Gets the value of the note property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getNote()
   {
      return note;
   }

   /**
    * Sets the value of the note property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setNote(String value)
   {
      this.note = value;
   }

   /**
    * Gets the value of the effort property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getEffort()
   {
      return effort;
   }

   /**
    * Sets the value of the effort property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setEffort(String value)
   {
      this.effort = value;
   }

   /**
    * Gets the value of the start property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getStart()
   {
      return start;
   }

   /**
    * Sets the value of the start property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setStart(String value)
   {
      this.start = value;
   }

   /**
    * Gets the value of the end property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getEnd()
   {
      return end;
   }

   /**
    * Sets the value of the end property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setEnd(String value)
   {
      this.end = value;
   }

   /**
    * Gets the value of the workStart property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getWorkStart()
   {
      return workStart;
   }

   /**
    * Sets the value of the workStart property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setWorkStart(String value)
   {
      this.workStart = value;
   }

   /**
    * Gets the value of the duration property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getDuration()
   {
      return duration;
   }

   /**
    * Sets the value of the duration property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setDuration(String value)
   {
      this.duration = value;
   }

   /**
    * Gets the value of the work property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getWork()
   {
      return work;
   }

   /**
    * Sets the value of the work property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setWork(String value)
   {
      this.work = value;
   }

   /**
    * Gets the value of the percentComplete property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getPercentComplete()
   {
      return percentComplete;
   }

   /**
    * Sets the value of the percentComplete property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setPercentComplete(String value)
   {
      this.percentComplete = value;
   }

   /**
    * Gets the value of the priority property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getPriority()
   {
      return priority;
   }

   /**
    * Sets the value of the priority property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setPriority(String value)
   {
      this.priority = value;
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
      if (type == null)
      {
         return "normal";
      }
      else
      {
         return type;
      }
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

   /**
    * Gets the value of the scheduling property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getScheduling()
   {
      if (scheduling == null)
      {
         return "fixed-work";
      }
      else
      {
         return scheduling;
      }
   }

   /**
    * Sets the value of the scheduling property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setScheduling(String value)
   {
      this.scheduling = value;
   }

   /**
    * Gets the value of the properties property.
    * 
    * @return
    *     possible object is
    *     {@link Properties }
    *     
    */
   public Properties getProperties()
   {
      return properties;
   }

   /**
    * Sets the value of the properties property.
    * 
    * @param value
    *     allowed object is
    *     {@link Properties }
    *     
    */
   public void setProperties(Properties value)
   {
      this.properties = value;
   }

   /**
    * Gets the value of the constraint property.
    * 
    * @return
    *     possible object is
    *     {@link Constraint }
    *     
    */
   public Constraint getConstraint()
   {
      return constraint;
   }

   /**
    * Sets the value of the constraint property.
    * 
    * @param value
    *     allowed object is
    *     {@link Constraint }
    *     
    */
   public void setConstraint(Constraint value)
   {
      this.constraint = value;
   }

   /**
    * Gets the value of the predecessors property.
    * 
    * @return
    *     possible object is
    *     {@link Predecessors }
    *     
    */
   public Predecessors getPredecessors()
   {
      return predecessors;
   }

   /**
    * Sets the value of the predecessors property.
    * 
    * @param value
    *     allowed object is
    *     {@link Predecessors }
    *     
    */
   public void setPredecessors(Predecessors value)
   {
      this.predecessors = value;
   }

   /**
    * Gets the value of the task property.
    * 
    * <p>
    * This accessor method returns a reference to the live list,
    * not a snapshot. Therefore any modification you make to the
    * returned list will be present inside the JAXB object.
    * This is why there is not a <CODE>set</CODE> method for the task property.
    * 
    * <p>
    * For example, to add a new item, do as follows:
    * <pre>
    *    getTask().add(newItem);
    * </pre>
    * 
    * 
    * <p>
    * Objects of the following type(s) are allowed in the list
    * {@link Task }
    * 
    * 
    */
   public List<Task> getTask()
   {
      if (task == null)
      {
         task = new ArrayList<Task>();
      }
      return this.task;
   }

}
