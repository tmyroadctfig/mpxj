/*
 * file:       MSPDIFile.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2002-2003
 * date:       20/02/2003
 */

/*
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */

package com.tapsterrock.mspdi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tapsterrock.mpx.AccrueType;
import com.tapsterrock.mpx.BookingType;
import com.tapsterrock.mpx.ConstraintType;
import com.tapsterrock.mpx.CurrencySymbolPosition;
import com.tapsterrock.mpx.EarnedValueMethod;
import com.tapsterrock.mpx.MPXCalendar;
import com.tapsterrock.mpx.MPXCalendarException;
import com.tapsterrock.mpx.MPXCalendarHours;
import com.tapsterrock.mpx.MPXCurrency;
import com.tapsterrock.mpx.MPXDuration;
import com.tapsterrock.mpx.MPXException;
import com.tapsterrock.mpx.MPXFile;
import com.tapsterrock.mpx.MPXRate;
import com.tapsterrock.mpx.Priority;
import com.tapsterrock.mpx.ProjectHeader;
import com.tapsterrock.mpx.Relation;
import com.tapsterrock.mpx.RelationList;
import com.tapsterrock.mpx.Resource;
import com.tapsterrock.mpx.ResourceAssignment;
import com.tapsterrock.mpx.ResourceType;
import com.tapsterrock.mpx.Task;
import com.tapsterrock.mpx.TaskType;
import com.tapsterrock.mpx.TimeUnit;
import com.tapsterrock.mpx.WorkContour;
import com.tapsterrock.mpx.WorkGroup;
import com.tapsterrock.mspdi.schema.ObjectFactory;
import com.tapsterrock.mspdi.schema.Project;

/**
 * This class is used to represent a Microsoft Project Data Interchange
 * (MSPDI) XML file. This implementation allows the file to be read,
 * and the data it contains exported as a set of MPX objects.
 * These objects can be interrogated to retrieve any required data,
 * or stored as an MPX file.
 */
public class MSPDIFile extends MPXFile
{
   /**
    * This constructor allows a new MSPDI file to be created from scratch.
    */
   public MSPDIFile ()
   {
      super ();
   }

   /**
    * Copy constructor. WARNING: this provides a shallow copy only.
    * This allows a "generic" MPX file to be "specialised" as an MSPDI
    * file. The contents of the file can then be written out as XML
    * data rather than as MPX data.
    *
    * @param file File to be copied
    */
   public MSPDIFile (MPXFile file)
   {
      super (file);
   }

   /**
    * Constructor allowing an MSPDI file to be read from an input stream
    *
    * @param stream an input stream
    * @throws MPXException on file read errors
    */
   public MSPDIFile (InputStream stream)
      throws MPXException
   {
      super (stream);
   }

   /**
    * Constructor allowing an MSPDI file to be read from a file object.
    *
    * @param file File object
    * @throws MPXException on file read errors
    */
   public MSPDIFile (File file)
      throws MPXException
   {
      super (file);
   }

   /**
    * Constructor allowing an MSPDI file to be read from a named file.
    *
    * @param filename File name
    * @throws MPXException on file read errors
    */
   public MSPDIFile (String filename)
      throws MPXException
   {
      super(filename);
   }

   /**
    * Initialise member variables here. This avoid problems with uninitialised
    * data being used when reading an MSPDI file using one of the MSPDIFile 
    * class constructors.
    */
   protected void configure ()
   {
      super.configure();
      m_compatibleOutput = true;
      m_compatibleInput = true;      
   }
   
   /**
    * This method is used to set a flag that determines whether
    * XML generated by this class is adjusted to be compatible with
    * Microsoft Project 2002.
    *
    * @param flag Compatibility flag
    */
   public void setMicrosoftProjectCompatibleOutput (boolean flag)
   {
      m_compatibleOutput = flag;
   }

   /**
    * This method retrieves a flag indicating whether the XML
    * output by this clas is compatible with Microsoft Project 2002.
    *
    * @return Boolean flag
    */
   public boolean getMicrosoftProjectCompatibleOutput ()
   {
      return (m_compatibleOutput);
   }

   /**
    * Sets a flag indicating that this class will attempt to correct
    * and read XML which is not compliant with the XML Schema. This
    * behaviour matches that of Microsoft Project when reading the
    * same data.
    * 
    * @param flag input compatibility flag
    */
   public void setMicrosoftProjectCompatibleInput (boolean flag)
   {
      m_compatibleInput = flag;
   }

   /**
    * Retrieves a flag indicating that this class will attempt to correct
    * and read XML which is not compliant with the XML Schema. This
    * behaviour matches that of Microsoft Project when reading the
    * same data.
    *
    * @return Boolean flag
    */
   public boolean getMicrosoftProjectCompatibleInput ()
   {
      return (m_compatibleInput);
   }
   
   /**
    * This method is used to recursively remove any empty element nodes
    * found in the XML document.
    * 
    * @param parent parent node
    * @param node child node
    */
   private void removeEmptyElementNodes (Node parent, Node node)
   {
      if (node.hasChildNodes() == true)
      {
         NodeList list = node.getChildNodes();
         for (int loop=0; loop < list.getLength(); loop++)
         {
            removeEmptyElementNodes(node, list.item(loop));
         }
      }
      else
      {
         if (node.getNodeType() == Node.ELEMENT_NODE)
         {
            parent.removeChild(node);
         }
      }
   }
   
   /**
    * This method brings together all of the processing required to
    * extract data from an MSPDI file and populate the MPX data structures.
    *
    * @param stream Input stream
    * @throws MPXException on file read errors
    */
   public void read (InputStream stream)
      throws MPXException
   {
      try
      {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         DocumentBuilder db = dbf.newDocumentBuilder();
         Document doc = db.parse(stream);  
         
         //
         // If we are matching the behaviour of MS project, then we need to
         // remove empty element nodes to avoid schema validation problems.
         //
         if (m_compatibleInput == true)
         {                     
            removeEmptyElementNodes(doc, doc);
         }
         
         JAXBContext context = JAXBContext.newInstance ("com.tapsterrock.mspdi.schema");
         Unmarshaller unmarshaller = context.createUnmarshaller ();

         //
         // If we are matching the behaviour of MS project, then we need to
         // ignore validation warnings.
         //         
         if (m_compatibleInput == true)
         {
            unmarshaller.setEventHandler 
            (
               new ValidationEventHandler() 
               {
                  public boolean handleEvent (ValidationEvent event)
                  {
                     return (true);
                  }
               }
            );
         }
         
         Project project = (Project)unmarshaller.unmarshal (doc);
         HashMap calendarMap = new HashMap ();
                          
         readProjectHeader (project);
         readProjectExtendedAttributes(project);
         readCalendars (project, calendarMap);
         readResources (project, calendarMap);
         readTasks (project);
         readAssignments (project);
      }
      
      catch (ParserConfigurationException ex)
      {
         throw new MPXException ("Failed to parse file", ex);
      }
      
      catch (JAXBException ex)
      {
         throw new MPXException ("Failed to parse file", ex);
      }

      catch (SAXException ex)
      {
         throw new MPXException ("Failed to parse file", ex);
      }      
      
      catch (IOException ex)
      {
         throw new MPXException ("Failed to parse file", ex);
      }      
   }

   /**
    * This method extracts project header data from an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    */
   private void readProjectHeader (Project project)
   {
      ProjectHeader header = getProjectHeader ();
      
      header.setMinutesPerDay(getInteger(project.getMinutesPerDay()));
      header.setSplitInProgressTasks(project.isSplitsInProgressTasks());
      header.setManager(project.getManager());
      header.setDefaultEndTime(getTime(project.getDefaultFinishTime()));
      header.setFiscalYearStart(project.isFiscalYearStart());
      //header.setRemoveFileProperties();
      header.setDefaultTaskEarnedValueMethod(EarnedValueMethod.getInstance(getInt(project.getDefaultTaskEVMethod())));
      header.setDefaultStartTime(getTime(project.getDefaultStartTime()));
      header.setFinishDate(getDate (project.getFinishDate()));
      //header.setFinishDate();
      //header.setMoveCompletedEndsBack();   
      header.setDefaultWorkUnits(getMpxWorkTimeUnits (project.getWorkFormat()));
      //header.setBaselineForEarnedValue();    
      //header.setCalendarUID();    
      header.setSubject(project.getSubject());      
      //header.setNewTasksEstimated();    
      //header.setSpreadActualCost();    
      //header.setDefaultFixedCostAccrual();    
      //header.setMultipleCriticalPaths();    
      //header.setAutoAddNewResourcesAndTasks();  
      header.setDefaultOvertimeRate(new MPXRate(project.getDefaultOvertimeRate(), TimeUnit.HOURS));
      header.setStartDate(getDate (project.getStartDate()));
      //header.setStartDate();
      //header.setFYStartDate(); 
      header.setSymbolPosition (getMpxSymbolPosition(project.getCurrencySymbolPosition()));
      //header.setLastSaved();    
      //header.setStatusDate();    
      //header.setMoveRemainingStartsBack();    
      //header.setAutolink();   
      header.setProjectTitle(project.getTitle());
      header.setCompany(project.getCompany());
      //header.setExtendedCreationDate();
      header.setDefaultDurationUnits(getMpxDurationTimeUnits(project.getDurationFormat()));
      //header.setMicrosoftProjectServerURL();    
      //header.setNewTaskStartDate();
      //header.setHonorConstraints();
      header.setDaysPerMonth(getInteger(project.getDaysPerMonth()));
      header.setAuthor(project.getAuthor());
      //header.setAdminProject();    
      //header.setScheduleFromStart();
      header.setMinutesPerWeek(getInteger(project.getMinutesPerWeek()));  
      header.setCurrentDate(getDate (project.getCurrentDate()));
      header.setCurrencyDigits (getInteger(project.getCurrencyDigits()));
      //header.setInsertedProjectsLikeSummary();    
      //header.setName();
      //header.setSpreadPercentComplete();
      //header.setWeekStartDay();
      header.setDefaultStandardRate(new MPXRate(project.getDefaultStandardRate(), TimeUnit.HOURS));
      //header.setMoveCompletedEndsForward();
      header.setCurrencySymbol (project.getCurrencySymbol());
      header.setUpdatingTaskStatusUpdatesResourceStatus(project.isTaskUpdatesResource());
      //header.setEditableActualCosts();
      //header.setUID();
      //header.setCriticalSlackLimit();
      //header.setRevision();
      //header.setNewTasksEffortDriven();
      //header.setEarnedValueMethod();
      //header.setMoveRemainingStartsForward();
      //header.setDefaultTaskType();
      header.setProjectExternallyEdited(project.isProjectExternallyEdited());
      //header.setActualsInSync();
      header.setCategory(project.getCategory());
      //header.setCreationDate();
   }

   /**
    * This method extracts calandar data from an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    * @param map Map of calendar UIDs to names
    * @throws MPXException on file read errors
    */
   private void readCalendars (Project project, HashMap map)
      throws MPXException
   {
      Project.CalendarsType calendars = project.getCalendars();
      if (calendars != null)
      {
         List calendar = calendars.getCalendar();
         Iterator iter = calendar.iterator();

         while (iter.hasNext() == true)
         {
            readCalendar ((Project.CalendarsType.CalendarType)iter.next(), map);
         }

         updateBaseCalendarNames (map);
      }
   }

   /**
    * The way calendars are stored in an MPP8 file means that there
    * can be forward references between the base calendar unique ID for a
    * derived calendar, and the base calendar itself. To get around this,
    * we initially populatethe base calendar name attribute with the
    * base calendar unique ID, and now in this method we can convert those
    * ID values into the correct names.
    *
    * @param map map of calendar ID values and calendar objects
    */
   private static void updateBaseCalendarNames (HashMap map)
   {
      Iterator iter = map.keySet().iterator();
      MPXCalendar cal;
      MPXCalendar baseCal;
      String baseCalendarName;

      while (iter.hasNext() == true)
      {
         cal = (MPXCalendar)map.get(iter.next());
         baseCalendarName = cal.getBaseCalendarName();
         if (baseCalendarName != null)
         {
            baseCal = (MPXCalendar)map.get(new BigInteger (baseCalendarName));
            if (baseCal != null)
            {
               cal.setBaseCalendarName(baseCal.getName());
            }
         }
      }
   }

   /**
    * This method extracts data for a single calandar from an MSPDI file.
    *
    * @param calendar Calendar data
    * @param map Map of calendar UIDs to names
    * @throws MPXException on file read errors
    */
   private void readCalendar (Project.CalendarsType.CalendarType calendar, HashMap map)
      throws MPXException
   {
      MPXCalendar bc;
      Iterator iter;

      if (calendar.isIsBaseCalendar() == true)
      {
         bc = addBaseCalendar();
      }
      else
      {
         bc = addResourceCalendar();
      }

      bc.setUniqueID(calendar.getUID().intValue());
      bc.setName(calendar.getName());
      BigInteger baseCalendarID = calendar.getBaseCalendarUID();
      if (baseCalendarID != null)
      {
         bc.setBaseCalendarName(baseCalendarID.toString());
      }

      Project.CalendarsType.CalendarType.WeekDaysType days = calendar.getWeekDays();
      if (days != null)
      {
         List day = days.getWeekDay();
         iter = day.iterator();

         while (iter.hasNext() == true)
         {
            readDay (bc, (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType)iter.next());
         }
      }

      map.put (calendar.getUID(), bc);
   }


   /**
    * This method extracts data for a single day from an MSPDI file.
    *
    * @param calendar Calendar data
    * @param day Day data
    * @throws MPXException on file read errors
    */
   private void readDay (MPXCalendar calendar, Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType day)
      throws MPXException
   {
      BigInteger dayType = day.getDayType();
      if (dayType != null)
      {
         if (dayType.intValue() == 0)
         {
            readExceptionDay (calendar, day);
         }
         else
         {
            readNormalDay (calendar, day);
         }
      }
   }

   /**
    * This method extracts data for a normal working day from an MSPDI file.
    *
    * @param calendar Calendar data
    * @param day Day data
    * @throws MPXException on file read errors
    */
   private void readNormalDay (MPXCalendar calendar, Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType day)
      throws MPXException
   {
      int dayNumber = day.getDayType().intValue();

      calendar.setWorkingDay(dayNumber, day.isDayWorking());
      MPXCalendarHours hours = calendar.addCalendarHours(dayNumber);

      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType times = day.getWorkingTimes();
      if (times != null)
      {
         Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType period;
         List time = times.getWorkingTime();
         Iterator iter = time.iterator();

         if (iter.hasNext() == true)
         {
            period = (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType)iter.next();
            hours.setFromTime1(getTime(period.getFromTime()));
            hours.setToTime1(getTime(period.getToTime()));
         }

         if (iter.hasNext() == true)
         {
            period = (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType)iter.next();
            hours.setFromTime2(getTime(period.getFromTime()));
            hours.setToTime2(getTime(period.getToTime()));
         }

         if (iter.hasNext() == true)
         {
            period = (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType)iter.next();
            hours.setFromTime3(getTime(period.getFromTime()));
            hours.setToTime3(getTime(period.getToTime()));
         }
      }
   }


   /**
    * This method extracts data for an exception day from an MSPDI file.
    *
    * @param calendar Calendar data
    * @param day Day data
    * @throws MPXException on file read errors
    */
   private void readExceptionDay (MPXCalendar calendar, Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType day)
      throws MPXException
   {
      MPXCalendarException exception = calendar.addCalendarException();

      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.TimePeriodType timePeriod = day.getTimePeriod();
      exception.setFromDate(getDate(timePeriod.getFromDate()));
      exception.setToDate(getDate(timePeriod.getToDate()));

      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType times = day.getWorkingTimes();
      if (times != null)
      {
         Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType period;
         List time = times.getWorkingTime();
         Iterator iter = time.iterator();

         if (iter.hasNext() == true)
         {
            period = (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType)iter.next();
            exception.setFromTime1(getTime(period.getFromTime()));
            exception.setToTime1(getTime(period.getToTime()));
         }

         if (iter.hasNext() == true)
         {
            period = (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType)iter.next();
            exception.setFromTime2(getTime(period.getFromTime()));
            exception.setToTime2(getTime(period.getToTime()));
         }

         if (iter.hasNext() == true)
         {
            period = (Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType)iter.next();
            exception.setFromTime3(getTime(period.getFromTime()));
            exception.setToTime3(getTime(period.getToTime()));
         }
      }
   }

   /**
    * This method extracts project extended attribute data from an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    * @throws MPXException on file read errors
    */
   private void readProjectExtendedAttributes (Project project)
      throws MPXException
   {
      Project.ExtendedAttributesType attributes = project.getExtendedAttributes();
      if (attributes != null)
      {
         List attribute = attributes.getExtendedAttribute();
         Iterator iter = attribute.iterator();

         while (iter.hasNext() == true)
         {
            readFieldAlias ((Project.ExtendedAttributesType.ExtendedAttributeType)iter.next());
         }
      }
   }

   /**
    * Read a single field alias from an extended attribute.
    *
    * @param attribute extended attribute
    */
   private void readFieldAlias (Project.ExtendedAttributesType.ExtendedAttributeType attribute)
   {
      String alias = attribute.getAlias();

      if (alias != null && alias.length() != 0)
      {
         Integer id = new Integer (attribute.getFieldID());
         int prefix = id.intValue() / 100000;

         switch (prefix)
         {
            case TASK_FIELD_PREFIX:
            {
               Integer taskField = (Integer)TASK_FIELD_XML_TO_MPX_MAP.get(id);
               if (taskField != null)
               {
                  setTaskFieldAlias (taskField.intValue(), attribute.getAlias());
               }
               break;
            }

            case RESOURCE_FIELD_PREFIX:
            {
               Integer resourceField = (Integer)RESOURCE_FIELD_XML_TO_MPX_MAP.get(id);
               if (resourceField != null)
               {
                  setResourceFieldAlias (resourceField.intValue(), attribute.getAlias());
               }
               break;
            }
         }
      }
   }

   /**
    * This method extracts resource data from an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    * @param calendarMap Map of calendar UIDs to names
    * @throws MPXException on file read errors
    */
   private void readResources (Project project, HashMap calendarMap)
      throws MPXException
   {
      Project.ResourcesType resources = project.getResources();
      if (resources != null)
      {
         List resource = resources.getResource();
         Iterator iter = resource.iterator();
         while (iter.hasNext() == true)
         {
            readResource ((Project.ResourcesType.ResourceType)iter.next(), calendarMap);
         }
      }
   }

   /**
    * This method extracts data for a single resource from an MSPDI file.
    *
    * @param xml Resource data
    * @param calendarMap Map of calendar UIDs to names
    * @throws MPXException on file read errors
    */
   private void readResource (Project.ResourcesType.ResourceType xml, HashMap calendarMap)
      throws MPXException
   {
      Resource mpx = addResource();

      mpx.setAccrueAt(AccrueType.getInstance(xml.getAccrueAt()));
      mpx.setActveDirectoryGUID(xml.getActiveDirectoryGUID());
      mpx.setActualCost(getMpxCurrency(xml.getActualCost()));
      mpx.setActualOvertimeCost(getMpxCurrency(xml.getActualOvertimeCost()));
      mpx.setActualOvertimeWork(getDuration(xml.getActualOvertimeWork()));
      mpx.setActualOvertimeWorkProtected(getDuration(xml.getActualOvertimeWorkProtected()));
      mpx.setActualWork(getDuration (xml.getActualWork()));
      mpx.setActualWorkProtected(getDuration(xml.getActualWorkProtected()));
      mpx.setACWP(new Double(xml.getACWP()));
      mpx.setAvailableFrom(getDate(xml.getAvailableFrom()));
      mpx.setAvailableTo(getDate(xml.getAvailableTo()));
      mpx.setBCWS(new Double(xml.getBCWS()));
      mpx.setBCWP(new Double(xml.getBCWP()));
      mpx.setBookingType(BookingType.getInstance(getInt(xml.getBookingType())));
      //mpx.setBaseCalendar ();
      //mpx.setBaselineCost();
      //mpx.setBaselineWork();
      mpx.setCanLevel(xml.isCanLevel());
      mpx.setCode(xml.getCode());
      mpx.setCost(getMpxCurrency(xml.getCost()));
      mpx.setCostPerUse(getMpxCurrency(xml.getCostPerUse()));
      mpx.setCostVariance(new Double(xml.getCostVariance()/100));
      mpx.setCreationDate(getDate(xml.getCreationDate()));
      mpx.setCV(new Double(xml.getCV()));
      mpx.setEmailAddress(xml.getEmailAddress());
      mpx.setFinish(getDate(xml.getFinish()));
      mpx.setGroup(xml.getGroup());
      mpx.setHyperlink(xml.getHyperlink());
      mpx.setHyperlinkAddress(xml.getHyperlinkAddress());      
      mpx.setHyperlinkSubAddress(xml.getHyperlinkSubAddress());      
      mpx.setID(getInteger(xml.getID()));
      mpx.setInitials(xml.getInitials());
      mpx.setIsEnterprise(xml.isIsEnterprise());
      mpx.setIsGeneric(xml.isIsGeneric());
      mpx.setIsInactive(xml.isIsInactive());
      mpx.setIsNull(xml.isIsNull());      
      //mpx.setLinkedFields();
      mpx.setMaterialLabel(xml.getMaterialLabel());
      mpx.setMaxUnits(new Double(xml.getMaxUnits()*100));
      mpx.setName(xml.getName());      
      if (xml.getNotes() != null && xml.getNotes().length() != 0)
      {
         mpx.setNotes(xml.getNotes());
      }      
      mpx.setNtAccount(xml.getNTAccount());
      //mpx.setObjects();
      mpx.setOverAllocated(xml.isOverAllocated());
      mpx.setOvertimeCost(getMpxCurrency(xml.getOvertimeCost()));
      mpx.setOvertimeRate(getHourlyRate(xml.getOvertimeRate()));
      mpx.setOvertimeRateFormat(TimeUnit.getInstance(getInt(xml.getOvertimeRateFormat())-1));
      mpx.setOvertimeWork(getDuration (xml.getOvertimeWork()));
      mpx.setPeakUnits(new Double(xml.getPeakUnits() * 100));
      mpx.setPercentWorkComplete(xml.getPercentWorkComplete());
      mpx.setPhonetics(xml.getPhonetics());
      mpx.setRegularWork(getDuration(xml.getRegularWork()));
      mpx.setRemainingCost(getMpxCurrency(xml.getRemainingCost()));
      mpx.setRemainingOvertimeCost(getMpxCurrency(xml.getRemainingOvertimeCost()));
      mpx.setRemainingWork(getDuration (xml.getRemainingWork()));
      mpx.setRemainingOvertimeWork(getDuration(xml.getRemainingOvertimeWork()));
      mpx.setStandardRate(getHourlyRate(xml.getStandardRate()));
      mpx.setStandardRateFormat(TimeUnit.getInstance(getInt(xml.getStandardRateFormat())-1));
      mpx.setStart(getDate(xml.getStart()));
      mpx.setSV(new Double(xml.getSV()));
      mpx.setType(ResourceType.getInstance(getInt(xml.getType())));
      mpx.setUniqueID(getInteger(xml.getUID()));
      mpx.setWork(getDuration (xml.getWork()));
      mpx.setWorkGroup(WorkGroup.getInstance(getInt(xml.getWorkGroup())));
      mpx.setWorkVariance(new MPXDuration (xml.getWorkVariance()/1000, TimeUnit.MINUTES));

      readResourceExtendedAttributes (xml, mpx);
      
      attachResourceCalendar(mpx, (MPXCalendar)calendarMap.get(xml.getCalendarUID()));
   }

   /**
    * This method processes any extended attributes associated with a resource.
    * 
    * @param xml MSPDI resource instance
    * @param mpx MPX resource instance
    */
   private void readResourceExtendedAttributes (Project.ResourcesType.ResourceType xml, Resource mpx)
   {
      List extendedAttributes = xml.getExtendedAttribute();
      Iterator iter = extendedAttributes.iterator();
      Project.ResourcesType.ResourceType.ExtendedAttributeType attrib;
      Integer xmlFieldID;
      Integer mpxFieldID;
      int dataType;
      Object value;
      
      while (iter.hasNext() == true)
      {
         attrib = (Project.ResourcesType.ResourceType.ExtendedAttributeType)iter.next();
         xmlFieldID = new Integer (attrib.getFieldID());
         mpxFieldID = (Integer)RESOURCE_FIELD_XML_TO_MPX_MAP.get(xmlFieldID);
         dataType = ((Integer)RESOURCE_FIELD_MPX_TO_TYPE_MAP.get(mpxFieldID)).intValue();
         
         switch (dataType)
         {
            case STRING_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), attrib.getValue());
               break;
            }
                        
            case DATE_ATTRIBUTE:
            {
               mpx.setDate(mpxFieldID.intValue(), parseXsdDateTime(attrib.getValue()));
               break;
            }
            
            case CURRENCY_ATTRIBUTE:
            {
               mpx.setCurrency(mpxFieldID.intValue(), new Double(Double.parseDouble(attrib.getValue())/100));
               break;
            }
            
            case BOOLEAN_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), (attrib.getValue().equals("1")?Boolean.TRUE:Boolean.FALSE));
               break;
            }
            
            case NUMERIC_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), new Double(attrib.getValue()));
               break;
            }
            
            case DURATION_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), getDuration(attrib.getValue()));
               break;
            }            
         }         
      }
   }

   /**
    * This method extracts task data from an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    * @throws MPXException on file read errors
    */
   private void readTasks (Project project)
      throws MPXException
   {
      Project.TasksType tasks = project.getTasks();
      if (tasks != null)
      {
         List task = tasks.getTask();
         Iterator iter = task.iterator();
         while (iter.hasNext() == true)
         {
            readTask ((Project.TasksType.TaskType)iter.next());
         }

         iter = task.iterator();
         while (iter.hasNext() == true)
         {
            readPredecessors ((Project.TasksType.TaskType)iter.next());
         }
      }

      updateStructure ();
   }


   /**
    * This method extracts data for a single task from an MSPDI file.
    *
    * @param xml Task data
    * @throws MPXException on file read errors
    */
   private void readTask (Project.TasksType.TaskType xml)
      throws MPXException
   {
      Task mpx = addTask ();

      mpx.setActualCost(getMpxCurrency (xml.getActualCost()));
      mpx.setActualDuration(getDuration (xml.getActualDuration()));
      mpx.setActualFinish(getDate (xml.getActualFinish()));
      mpx.setActualOvertimeCost(getMpxCurrency(xml.getActualOvertimeCost()));
      mpx.setActualOvertimeWork(getDuration (xml.getActualOvertimeWork()));
      mpx.setActualOvertimeWorkProtected(getDuration(xml.getActualOvertimeWorkProtected()));
      mpx.setActualStart(getDate (xml.getActualStart()));
      mpx.setActualWork(getDuration (xml.getActualWork()));
      mpx.setActualWorkProtected(getDuration(xml.getActualWorkProtected()));
      mpx.setACWP(new Double(xml.getACWP()));
      //mpx.setBaselineCost();
      //mpx.setBaselineDuration();
      //mpx.setBaselineFinish();
      //mpx.setBaselineStart();
      //mpx.setBaselineWork();
      //mpx.setBCWP();
      //mpx.setBCWS();
      mpx.setCalendarName(getTaskCalendarName(xml));
      //mpx.setConfirmed();
      mpx.setConstraintDate(getDate(xml.getConstraintDate()));
      mpx.setConstraintType(ConstraintType.getInstance(xml.getConstraintType()));
      mpx.setContact(xml.getContact());
      mpx.setCost(getMpxCurrency(xml.getCost()));
      //mpx.setCost1();
      //mpx.setCost2();
      //mpx.setCost3();
      //mpx.setCostVariance();
      mpx.setCreateDate(getDate(xml.getCreateDate()));      
      mpx.setCritical(xml.isCritical());
      mpx.setCV(xml.getCV()/100);
      mpx.setDeadline(getDate(xml.getDeadline()));
      //mpx.setDelay();
      mpx.setDuration(getDuration (xml.getDuration()));
      mpx.setDurationFormat(getMpxDurationTimeUnits(xml.getDurationFormat()));
      //mpx.setDuration1();
      //mpx.setDuration2();
      //mpx.setDuration3();
      //mpx.setDurationVariance();
      mpx.setEarlyFinish(getDate(xml.getEarlyFinish()));
      mpx.setEarlyStart(getDate(xml.getEarlyStart()));
      mpx.setEarnedValueMethod(EarnedValueMethod.getInstance(getInt(xml.getEarnedValueMethod())));
      mpx.setEffortDriven(xml.isEffortDriven());
      mpx.setEstimated(xml.isEstimated());
      mpx.setExternalTask(xml.isExternalTask());
      mpx.setExternalTaskProject(xml.getExternalTaskProject());
      mpx.setFinish(getDate(xml.getFinish()));
      //mpx.setFinish1();
      //mpx.setFinish2();
      //mpx.setFinish3();
      //mpx.setFinish4();
      //mpx.setFinish5();
      mpx.setFinishVariance(getMinutesDuration(xml.getFinishVariance()));
      //mpx.setFixed();
      mpx.setFixedCost(xml.getFixedCost()/100);
      mpx.setFixedCostAccrual(AccrueType.getInstance(xml.getFixedCostAccrual(), Locale.ENGLISH));
      //mpx.setFlag1();
      //mpx.setFlag2();
      //mpx.setFlag3();
      //mpx.setFlag4();
      //mpx.setFlag5();
      //mpx.setFlag6();
      //mpx.setFlag7();
      //mpx.setFlag8();
      //mpx.setFlag9();
      //mpx.setFlag10();
      mpx.setFreeSlack(getMinutesDuration(xml.getFreeSlack()));
      mpx.setHideBar(xml.isHideBar());
      mpx.setHyperlink(xml.getHyperlink());
      mpx.setHyperlinkAddress(xml.getHyperlinkAddress());
      mpx.setHyperlinkSubAddress(xml.getHyperlinkSubAddress());
      mpx.setID(getInteger(xml.getID()));
      mpx.setIgnoreResourceCalendar(xml.isIgnoreResourceCalendar());
      mpx.setLateFinish(getDate(xml.getLateFinish()));
      mpx.setLateStart(getDate(xml.getLateStart()));
      mpx.setLevelAssignments(xml.isLevelAssignments());
      mpx.setLevelingCanSplit(xml.isLevelingCanSplit());
      mpx.setLevelingDelayFormat(getMpxDurationTimeUnits(xml.getLevelingDelayFormat()));
      if (xml.getLevelingDelay() != null && mpx.getLevelingDelayFormat() != null)
      {
         mpx.setLevelingDelay(new MPXDuration (xml.getLevelingDelay().doubleValue(), mpx.getLevelingDelayFormat()));
      }
      
      
      //mpx.setLinkedFields();
      //mpx.setMarked();
      mpx.setMilestone(xml.isMilestone());
      mpx.setName(xml.getName());
      if (xml.getNotes() != null && xml.getNotes().length() != 0)
      {
         mpx.setNotes(xml.getNotes());
      }
      //mpx.setNumber1();
      //mpx.setNumber2();
      //mpx.setNumber3();
      //mpx.setNumber4();
      //mpx.setNumber5();
      //mpx.setObjects();
      mpx.setNull(xml.isIsNull());
      mpx.setOutlineLevel(getInteger(xml.getOutlineLevel()));
      mpx.setOutlineNumber(xml.getOutlineNumber());
      mpx.setOverAllocated(xml.isOverAllocated());
      mpx.setOvertimeCost(getMpxCurrency(xml.getOvertimeCost()));
      mpx.setOvertimeWork(getDuration(xml.getOvertimeWork()));
      mpx.setPercentageComplete(xml.getPercentComplete());
      mpx.setPercentageWorkComplete(xml.getPercentWorkComplete());
      mpx.setPhysicalPercentComplete(getInteger(xml.getPhysicalPercentComplete()));
      mpx.setPreleveledFinish(getDate(xml.getPreLeveledFinish()));
      mpx.setPreleveledStart(getDate(xml.getPreLeveledStart()));
      mpx.setPriority(getMpxPriority(xml.getPriority()));
      //mpx.setProject();
      mpx.setRecurring(xml.isRecurring());
      mpx.setRegularWork(getDuration(xml.getRegularWork()));
      mpx.setRemainingCost(getMpxCurrency(xml.getRemainingCost()));
      mpx.setRemainingDuration(getDuration(xml.getRemainingDuration()));
      mpx.setRemainingOvertimeCost(getMpxCurrency(xml.getRemainingOvertimeCost()));
      mpx.setRemainingOvertimeWork(getDuration (xml.getRemainingOvertimeWork()));
      mpx.setRemainingWork(getDuration (xml.getRemainingWork()));
      //mpx.setResourceGroup();
      //mpx.setResourceInitials();
      //mpx.setResourceNames();
      mpx.setResume(getDate(xml.getResume()));
      mpx.setResumeValid(xml.isResumeValid());
      //mpx.setResumeNoEarlierThan();
      mpx.setRollup(xml.isRollup());
      mpx.setStart(getDate(xml.getStart()));
      //mpx.setStart1();
      //mpx.setStart2();
      //mpx.setStart3();
      //mpx.setStart4();
      //mpx.setStart5();
      mpx.setStartVariance(getMinutesDuration(xml.getStartVariance()));
      mpx.setStop(getDate(xml.getStop()));
      mpx.setSubproject(xml.isIsSubproject());
      mpx.setSubprojectName(xml.getSubprojectName());
      mpx.setSubprojectReadOnly(xml.isIsSubprojectReadOnly());
      //mpx.setSuccessors();
      mpx.setSummary(xml.isSummary());
      //mpx.setSV();
      //mpx.setText1();
      //mpx.setText2();
      //mpx.setText3();
      //mpx.setText4();
      //mpx.setText5();
      //mpx.setText6();
      //mpx.setText7();
      //mpx.setText8();
      //mpx.setText9();
      //mpx.setText10();
      mpx.setTotalSlack(getMinutesDuration(xml.getTotalSlack()));
      mpx.setType(TaskType.getInstance(getInt(xml.getType())));
      mpx.setUniqueID(getInteger(xml.getUID()));
      //mpx.setUpdateNeeded();
      mpx.setWBS(xml.getWBS());
      mpx.setWBSLevel (xml.getWBSLevel());
      mpx.setWork(getDuration(xml.getWork()));
      mpx.setWorkVariance(new MPXDuration (xml.getWorkVariance()/1000, TimeUnit.MINUTES));
      
      readTaskExtendedAttributes(xml, mpx);
   }

   /**
    * This method processes any extended attributes associated with a task.
    * 
    * @param xml MSPDI task instance
    * @param mpx MPX task instance
    */
   private void readTaskExtendedAttributes (Project.TasksType.TaskType xml, Task mpx)
   {
      List extendedAttributes = xml.getExtendedAttribute();
      Iterator iter = extendedAttributes.iterator();
      Project.TasksType.TaskType.ExtendedAttributeType attrib;
      Integer xmlFieldID;
      Integer mpxFieldID;
      int dataType;
      Object value;
      
      while (iter.hasNext() == true)
      {
         attrib = (Project.TasksType.TaskType.ExtendedAttributeType)iter.next();
         xmlFieldID = new Integer (attrib.getFieldID());
         mpxFieldID = (Integer)TASK_FIELD_XML_TO_MPX_MAP.get(xmlFieldID);
         dataType = ((Integer)TASK_FIELD_MPX_TO_TYPE_MAP.get(mpxFieldID)).intValue();
         
         switch (dataType)
         {
            case STRING_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), attrib.getValue());
               break;
            }
                        
            case DATE_ATTRIBUTE:
            {
               mpx.setDate(mpxFieldID.intValue(), parseXsdDateTime(attrib.getValue()));
               break;
            }
            
            case CURRENCY_ATTRIBUTE:
            {
               mpx.setCurrency(mpxFieldID.intValue(), new Double(Double.parseDouble(attrib.getValue())/100));
               break;
            }
            
            case BOOLEAN_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), (attrib.getValue().equals("1")?Boolean.TRUE:Boolean.FALSE));
               break;
            }
            
            case NUMERIC_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), new Double(attrib.getValue()));
               break;
            }
            
            case DURATION_ATTRIBUTE:
            {
               mpx.set(mpxFieldID.intValue(), getDuration(attrib.getValue()));
               break;
            }            
         }         
      }
   }
   
   /**
    * This method is used to retrieve the name of the calendar associated
    * with a task. If no calendar is associated with a task, this method
    * returns null.
    *
    * @param task MSPDI task
    * @return name of calendar associated with this task
    */
   private String getTaskCalendarName (Project.TasksType.TaskType task)
   {
      String name = null;

      BigInteger calendarID = task.getCalendarUID();
      if (calendarID != null)
      {
         MPXCalendar calendar = getBaseCalendarByUniqueID(calendarID.intValue());
         if (calendar != null)
         {
            name = calendar.getName();
         }
      }

      return (name);
   }


   /**
    * This method extracts predecessor data from an MSPDI file.
    *
    * @param task Task data
    */
   private void readPredecessors (Project.TasksType.TaskType task)
   {
      BigInteger uid = task.getUID();
      if (uid != null)
      {
         Task currTask = getTaskByUniqueID(uid.intValue());
         if (currTask != null)
         {
            List predecessors = task.getPredecessorLink();
            Iterator iter = predecessors.iterator();

            while (iter.hasNext() == true)
            {
               readPredecessor (currTask, (Project.TasksType.TaskType.PredecessorLinkType)iter.next());
            }
         }
      }
   }

   /**
    * This method extracts data for a single predecessor from an MSPDI file.
    *
    * @param currTask Current task object
    * @param link Predecessor data
    */
   private void readPredecessor (Task currTask, Project.TasksType.TaskType.PredecessorLinkType link)
   {
      BigInteger uid = link.getPredecessorUID();
      if (uid != null)
      {
         Task prevTask = getTaskByUniqueID(uid.intValue());
         if (prevTask != null)
         {
            int type;
            if (link.getType() != null)
            {
               type = link.getType().intValue();
            }
            else
            {
               type = Relation.FINISH_START;
            }

            int lag;

            if (link.getLinkLag() != null)
            {
               lag = link.getLinkLag().intValue() / (10*60);
            }
            else
            {
               lag = 0;
            }

            Relation rel = currTask.addPredecessor(prevTask);
            rel.setType(type);
            rel.setDuration(new MPXDuration (lag, TimeUnit.HOURS));
         }
      }
   }

   /**
    * This method extracts assignment data from an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    * @throws MPXException on file read errors
    */
   private void readAssignments (Project project)
      throws MPXException
   {
      Project.AssignmentsType assignments = project.getAssignments();
      if (assignments != null)
      {
         List assignment = assignments.getAssignment();
         Iterator iter = assignment.iterator();
         while (iter.hasNext() == true)
         {
            readAssignment ((Project.AssignmentsType.AssignmentType)iter.next());
         }
      }
   }


   /**
    * This method extracts data for a single assignment from an MSPDI file.
    *
    * @param assignment Assignment data
    * @throws MPXException on file read errors
    */
   private void readAssignment (Project.AssignmentsType.AssignmentType assignment)
      throws MPXException
   {
      BigInteger taskUID = assignment.getTaskUID();
      BigInteger resourceUID = assignment.getResourceUID();
      if (taskUID != null && resourceUID != null)
      {
         Task task = getTaskByUniqueID(taskUID.intValue());
         Resource resource = getResourceByUniqueID(resourceUID.intValue());

         if (task != null && resource != null)
         {
            ResourceAssignment mpx = task.addResourceAssignment(resource);

            mpx.setActualCost(getMpxCurrency(assignment.getActualCost()));
            //assignment.getActualFinish()
            //assignment.getActualOvertimeCost()
            //assignment.getActualOvertimeWork()
            //assignment.getActualOvertimeWorkProtected()
            //assignment.getActualStart()
            mpx.setActualWork(getDuration(assignment.getActualWork()));
            //assignment.getActualWorkProtected()
            //assignment.getACWP()
            //assignment.getBaseline()
            //assignment.getBCWP()
            //assignment.getBCWS()
            //assignment.getBookingType()
            mpx.setCost(getMpxCurrency(assignment.getCost()));
            //assignment.getCostRateTable()
            //assignment.getCostVariance()
            //assignment.getCreationDate()
            //assignment.getCV()
            mpx.setDelay(getMinutesDuration(assignment.getDelay()));
            //assignment.getExtendedAttribute()
            mpx.setFinish(getDate(assignment.getFinish()));
            //assignment.getFinishVariance()
            //assignment.getHyperlink()
            //assignment.getHyperlinkAddress()
            //assignment.getHyperlinkSubAddress()
            //assignment.getLevelingDelay()
            //assignment.getLevelingDelayFormat()
            //assignment.getNotes()
            //assignment.getOvertimeCost()
            mpx.setOvertimeWork(getDuration(assignment.getOvertimeWork()));
            //assignment.getPercentWorkComplete()
            //mpx.setPlannedCost();
            //mpx.setPlannedWork();
            //assignment.getRegularWork()
            //assignment.getRemainingCost()
            //assignment.getRemainingOvertimeCost()
            //assignment.getRemainingOvertimeWork()
            mpx.setRemainingWork(getDuration(assignment.getRemainingWork()));
            //assignment.getResume()
            mpx.setStart(getDate(assignment.getStart()));
            //assignment.getStartVariance()
            //assignment.getStop()
            //assignment.getTimephasedData()
            mpx.setUnits(assignment.getUnits()*100);
            //assignment.getVAC()
            mpx.setWork(getDuration(assignment.getWork()));
            mpx.setWorkContour(WorkContour.getInstance(getInt(assignment.getWorkContour())));
            //assignment.getWorkVariance()
         }
      }
   }

   /**
    * Utility method used to convert a BigInteger into an Integer.
    *
    * @param value BigInteger value
    * @return Integer value
    */
   private Integer getInteger (BigInteger value)
   {
      Integer result = null;

      if (value != null)
      {
         result = new Integer (value.intValue());
      }

      return (result);
   }
   
   /**
    * Utility method used to convert a BigInteger into an int.
    * Handles null values.
    * 
    * @param value BigInteger value
    * @return Integer value
    */
   private int getInt (BigInteger value)
   {
      int result;

      if (value != null)
      {
         result = value.intValue();
      }
      else
      {
         result = 0;
      }
      
      return (result);
   }
   
   /**
    * Utility method to convert an accrure type instance into its int
    * equivalent. Handles null values.
    * 
    * @param type accrue type object
    * @return accrue type value
    */
   private int getAccrueType (AccrueType type)
   {
      return ((type==null?AccrueType.PRORATED:type.getType()));
   }

   private BigInteger getResourceType (ResourceType type)
   {
      return (type==null?BigInteger.valueOf(ResourceType.WORK_VALUE):BigInteger.valueOf(type.getValue()));
   }

   private BigInteger getWorkGroup (WorkGroup workGroup)
   {
      return (workGroup==null?BigInteger.valueOf(WorkGroup.DEFAULT_VALUE):BigInteger.valueOf(workGroup.getValue()));
   }

   private BigInteger getWorkContour (WorkContour workContour)
   {
      return (workContour==null?BigInteger.valueOf(WorkContour.FLAT_VALUE):BigInteger.valueOf(workContour.getValue()));
   }
   
   private BigInteger getBookingType (BookingType bookingType)
   {
      return (bookingType==null?BigInteger.valueOf(BookingType.COMMITTED_VALUE):BigInteger.valueOf(bookingType.getValue()));
   }

   private BigInteger getTaskType (TaskType taskType)
   {
      return (taskType==null?BigInteger.valueOf(TaskType.FIXED_UNITS_VALUE):BigInteger.valueOf(taskType.getValue()));
   }

   private BigInteger getEarnedValueMethod (EarnedValueMethod earnedValueMethod)
   {
      return (earnedValueMethod==null?BigInteger.valueOf(EarnedValueMethod.PERCENT_COMPLETE_VALUE):BigInteger.valueOf(earnedValueMethod.getValue()));
   }
   
   /**
    * Utility method to convert a time unit instance into its int
    * equivalent. Handles null values.
    * 
    * @param unit time unit instance
    * @return accrue type value
    */   
   private int getTimeUnit (TimeUnit unit)
   {
      return (unit==null?TimeUnit.DAYS_VALUE:unit.getValue());
   }
   
   /**
    * Retrieve a boolean value from a Boolean object. Handles
    * null values.
    * 
    * @param value boolean value
    * @return boolean value
    */
   private boolean getBoolean (Boolean value)
   {
      return ((value==null?false:value.booleanValue()));
   }
   
   /**
    * Utility method used to convert a Number into a BigInteger.
    * 
    * @param value Integer value
    * @return BigInteger value
    */
   private BigInteger getBigInteger (Number value)
   {
      BigInteger result = null;
      
      if (value != null)
      {
         result = BigInteger.valueOf(value.longValue());
      }
      
      return (result);
   }

   /**
    * Utility method used to convert a Number into a float.
    * 
    * @param value Integer value
    * @return BigInteger value
    */
   private float getFloat (Number value)
   {
      float result;
      
      if (value != null)
      {
         result = value.floatValue();
      }
      else
      {
         result = 0;
      }
      
      return (result);
   }
   
   /**
    * Convenience method to convert from MPXJ units to XML units.
    * 
    * @param units MPXJ units value
    * @return XML units value
    */
   private float getUnits (Number units)
   {
      float result;
      
      if (units == null)
      {
         result = 1.0f;
      }
      else
      {
         result = units.floatValue() / 100;
      }
      
      return (result);
   }
   
   /**
    * Utility to convert a Calendar instance into a Date instance.
    *
    * @param value Calendar value
    * @return Date value
    */
   private Date getDate (Calendar value)
   {
      Date result = null;

      if (value != null)
      {
         Calendar cal = Calendar.getInstance();
         cal.set(Calendar.YEAR, value.get(Calendar.YEAR));
         cal.set(Calendar.MONTH, value.get(Calendar.MONTH));
         cal.set(Calendar.DAY_OF_MONTH, value.get(Calendar.DAY_OF_MONTH));
         cal.set(Calendar.HOUR_OF_DAY, value.get(Calendar.HOUR_OF_DAY));
         cal.set(Calendar.MINUTE, value.get(Calendar.MINUTE));
         cal.set(Calendar.SECOND, value.get(Calendar.SECOND));
         cal.set(Calendar.MILLISECOND, value.get(Calendar.MILLISECOND));
         result = cal.getTime();
      }

      return (result);
   }

   /**
    * Utility to convert a Calendar instance into a Date instance. Note this
    * is used specifically to alleviate a problem where data in the MSPDI
    * file contains only a time component, with no date component. If
    * the getDate method was used in this instance, the returned time component
    * in the calendar would be incorrect.
    *
    * @param value Calendar value
    * @return Date value
    */
   private Date getTime (Calendar value)
   {
      Date result = null;

      if (value != null)
      {
         Calendar cal = Calendar.getInstance();
         cal.set(Calendar.HOUR_OF_DAY, value.get(Calendar.HOUR_OF_DAY));
         cal.set(Calendar.MINUTE, value.get(Calendar.MINUTE));
         cal.set(Calendar.SECOND, value.get(Calendar.SECOND));
         cal.set(Calendar.MILLISECOND, value.get(Calendar.MILLISECOND));
         result = cal.getTime();
      }

      return (result);
   }

   /**
    * Utility to convert a Date instance into a Calendar instance.
    *
    * @param date Date value
    * @return Calendar value
    */
   private Calendar getCalendar (Date date)
   {
      Calendar cal = null;

      if (date != null)
      {
         cal = Calendar.getInstance();
         cal.setTime(date);
      }

      return (cal);
   }

   /**
    * Utility method to convert a BigInteger into
    * work units.
    *
    * @param value BigInteger value
    * @return work units
    */
   private TimeUnit getMpxWorkTimeUnits (BigInteger value)
   {
      TimeUnit result = TimeUnit.HOURS;

      if (value != null)
      {
         switch (value.intValue())
         {
            case 1:
            {
               result = TimeUnit.MINUTES;
               break;
            }

            case 3:
            {
               result = TimeUnit.DAYS;
               break;
            }

            case 4:
            {
               result = TimeUnit.WEEKS;
               break;
            }

            case 5:
            {
               result = TimeUnit.MONTHS;
               break;
            }

            case 7:
            {
               result = TimeUnit.YEARS;
               break;
            }

            default:
            case 2:
            {
               result = TimeUnit.HOURS;
               break;
            }
         }
      }

      return (result);
   }
   
   /**
    * Utility method to convert a work units to a BigInteger value.
    *
    * @param value work units
    * @return BigInteger value
    */
   private BigInteger getXmlWorkUnits (TimeUnit value)
   {
      int result;

      if (value == null)
      {
         value = TimeUnit.HOURS;
      }
      
      switch (value.getValue())
      {
         case TimeUnit.MINUTES_VALUE:
         {
            result = 1;
            break;
         }

         case TimeUnit.DAYS_VALUE:
         {
            result = 3;
            break;
         }

         case TimeUnit.WEEKS_VALUE:
         {
            result = 4;
            break;
         }

         case TimeUnit.MONTHS_VALUE:
         {
            result = 5;
            break;
         }

         case TimeUnit.YEARS_VALUE:
         {
            result = 7;
            break;
         }

         default:
         case TimeUnit.HOURS_VALUE:
         {
            result = 2;
            break;
         }
      }

      return (BigInteger.valueOf(result));
   }
   
   /**
    * Utility method to convert an xsd:duration into an MPXDuration.
    *
    * @param text xsd:duration value
    * @return MPXDuration
    */
   private MPXDuration getDuration (String text)
   {
      MPXDuration result = null;

      if (text != null && text.length() != 0)
      {
         XsdDuration xsd = new XsdDuration (text);
         TimeUnit units = TimeUnit.DAYS;

         if (xsd.getSeconds() != 0 || xsd.getMinutes() != 0)
         {
            units = TimeUnit.MINUTES;
         }

         if (xsd.getHours() != 0)
         {
            units = TimeUnit.HOURS;
         }

         if (xsd.getDays() != 0)
         {
            units = TimeUnit.DAYS;
         }

         if (xsd.getMonths() != 0)
         {
            units = TimeUnit.MONTHS;
         }

         if (xsd.getYears() != 0)
         {
            units = TimeUnit.YEARS;
         }

         int duration = 0;

         switch (units.getValue())
         {
            case TimeUnit.YEARS_VALUE:
            case TimeUnit.ELAPSED_YEARS_VALUE:
            {
               duration += xsd.getYears();
               duration += (xsd.getMonths() / 12);
               duration += (xsd.getDays() / 365);
               duration += (xsd.getHours() / (365 * 24));
               duration += (xsd.getMinutes() / (365 * 24 * 60));
               duration += (xsd.getSeconds() / (365 * 24 * 60 * 60));
               break;
            }

            case TimeUnit.MONTHS_VALUE:
            case TimeUnit.ELAPSED_MONTHS_VALUE:
            {
               duration += (xsd.getYears() * 12);
               duration += xsd.getMonths();
               duration += (xsd.getDays() / 30);
               duration += (xsd.getHours() / (30 * 24));
               duration += (xsd.getMinutes() / (30 * 24 * 60));
               duration += (xsd.getSeconds() / (30 * 24 * 60 * 60));
               break;
            }

            case TimeUnit.WEEKS_VALUE:
            case TimeUnit.ELAPSED_WEEKS_VALUE:
            {
               duration += (xsd.getYears() * 52);
               duration += (xsd.getMonths() * 4);
               duration += (xsd.getDays() / 7);
               duration += (xsd.getHours() / (7 * 24));
               duration += (xsd.getMinutes() / (7 * 24 * 60));
               duration += (xsd.getSeconds() / (7 * 24 * 60 * 60));
               break;
            }

            case TimeUnit.DAYS_VALUE:
            case TimeUnit.ELAPSED_DAYS_VALUE:
            {
               duration += (xsd.getYears() * 365);
               duration += (xsd.getMonths() * 30);
               duration += xsd.getDays();
               duration += (xsd.getHours() / 24);
               duration += (xsd.getMinutes() / (24 * 60));
               duration += (xsd.getSeconds() / (24 * 60 * 60));
               break;
            }

            case TimeUnit.HOURS_VALUE:
            case TimeUnit.ELAPSED_HOURS_VALUE:
            {
               duration += (xsd.getYears() * (365 * 24));
               duration += (xsd.getMonths() * (30 * 24));
               duration += (xsd.getDays() * 24);
               duration += xsd.getHours();
               duration += (xsd.getMinutes() / 60);
               duration += (xsd.getSeconds() / (60 * 60));
               break;
            }

            case TimeUnit.MINUTES_VALUE:
            case TimeUnit.ELAPSED_MINUTES_VALUE:
            {
               duration += (xsd.getYears() * (365 * 24 * 60));
               duration += (xsd.getMonths() * (30 * 24 * 60));
               duration += (xsd.getDays() * (24 * 60));
               duration += (xsd.getHours() * 60);
               duration += xsd.getMinutes();
               duration += (xsd.getSeconds() / 60);
               break;
            }
         }

         result = new MPXDuration (duration, units);
      }

      return (result);
   }

   /**
    * Utility method to convert an MPXDuration into an xsd:duration.
    *
    * Note that Microsoft's xsd:duration parser implementation does not
    * appear to recognise durations other than those expressed in hours.
    * We use the compatibility flag to determine whether the output
    * is adjusted for the benefit of Microsoft Project.
    *
    * @todo The conversion from arbitrary durations to hours does not use a calendar
    * @param duration MPXDuration value
    * @return xsd:duration value
    */
   private String getDuration (MPXDuration duration)
   {
      String result = null;

      if (duration == null)
      {
         result = ZERO_DURATION;
      }
      else
      {
         TimeUnit durationType = duration.getUnits();
         
         if (m_compatibleOutput == false || durationType.getValue() == TimeUnit.HOURS_VALUE || durationType.getValue() == TimeUnit.ELAPSED_HOURS_VALUE)
         {
            result = new XsdDuration(duration).toString();
         }
         else
         {
            double hours = duration.getDuration();

            switch (durationType.getValue())
            {
               case TimeUnit.MINUTES_VALUE:
               case TimeUnit.ELAPSED_MINUTES_VALUE:
               {
                  hours = duration.getDuration() / 60;
                  break;
               }

               case TimeUnit.DAYS_VALUE:
               case TimeUnit.ELAPSED_DAYS_VALUE:
               {
                  hours *= 8;
                  break;
               }

               case TimeUnit.WEEKS_VALUE:
               case TimeUnit.ELAPSED_WEEKS_VALUE:
               {
                  hours *= (8 * 5);
                  break;
               }

               case TimeUnit.MONTHS_VALUE:
               case TimeUnit.ELAPSED_MONTHS_VALUE:
               {
                  hours *= (8 * 5 * 4);
                  break;
               }

               case TimeUnit.YEARS_VALUE:
               case TimeUnit.ELAPSED_YEARS_VALUE:
               {
                  hours *= (8 * 5 * 52);
                  break;
               }
            }

            result = new XsdDuration(new MPXDuration (hours, TimeUnit.HOURS)).toString();
         }
      }

      return (result);
   }

   /**
    * Utility method to convert an MPXDuration into an xsd:duration.
    *
    * @param duration MPXDuration value
    * @return xsd:duration value
    */
   private BigInteger getDurationFormat (MPXDuration duration)
   {
      BigInteger result = null;

      if (duration != null)
      {
         result = getXmlDurationUnits(duration.getUnits());
      }

      return (result);
   }

   /**
    * Utility method to convert a BigInteger into a symbol position.
    *
    * @param position BigInteger position value
    * @return Symbol position
    */
   private CurrencySymbolPosition getMpxSymbolPosition (BigInteger position)
   {
      CurrencySymbolPosition result = CurrencySymbolPosition.BEFORE;

      if (position != null)
      {
         switch (position.intValue())
         {
            case 0:
            {
               result = CurrencySymbolPosition.BEFORE;
               break;
            }

            case 1:
            {
               result = CurrencySymbolPosition.AFTER;
               break;
            }

            case 2:
            {
               result = CurrencySymbolPosition.BEFORE_WITH_SPACE;
               break;
            }

            case 3:
            {
               result = CurrencySymbolPosition.AFTER_WITH_SPACE;
               break;
            }
         }
      }

      return (result);
   }


   /**
    * Utility method to convert a symbol position into a BigInteger.
    *
    * @param position symbol position
    * @return Symbol position
    */
   private BigInteger getXmlSymbolPosition (CurrencySymbolPosition position)
   {
      int result;

      switch (position.getValue())
      {
         default:
         case CurrencySymbolPosition.BEFORE_VALUE:
         {
            result = 0;
            break;
         }

         case CurrencySymbolPosition.AFTER_VALUE:
         {
            result = 1;
            break;
         }

         case CurrencySymbolPosition.BEFORE_WITH_SPACE_VALUE:
         {
            result = 2;
            break;
         }

         case CurrencySymbolPosition.AFTER_WITH_SPACE_VALUE:
         {
            result = 3;
            break;
         }
      }

      return (BigInteger.valueOf(result));
   }


   /**
    * Utility method to convert a BigDecimal into a currency value.
    *
    * @param value BigDecimal value
    * @return Currency value
    */
   private Double getMpxCurrency (BigDecimal value)
   {
      Double result = null;

      if (value != null)
      {
         result = new Double (value.doubleValue() / 100);
      }

      return (result);
   }

   /**
    * Utility method to convert a BigDecimal into a currency value.
    *
    * @param value Currency value
    * @return BigDecimal value
    */
   private BigDecimal getXmlCurrency (Number value)
   {
      BigDecimal result = null;

      if (value != null)
      {
         result = new BigDecimal (value.doubleValue() * 100);
      }
      else
      {
         result = BIGDECIMAL_ZERO;
      }

      return (result);
   }

   /**
    * Utility method to convert a BigInteger value into duration units.
    * Note that we don't differentiate between confirmed and unconfirmed
    * durations. Unrecognised duration types are default to hours.
    *
    * @param value BigInteger value
    * @return Duration units
    */
   private TimeUnit getMpxDurationTimeUnits (BigInteger value)
   {
      TimeUnit result = TimeUnit.HOURS;

      if (value != null)
      {
         switch (value.intValue())
         {
            case 3:
            case 35:
            {
               result = TimeUnit.MINUTES;
               break;
            }

            case 4:
            case 36:
            {
               result = TimeUnit.ELAPSED_MINUTES;
               break;
            }

            case 5:
            case 37:
            {
               result = TimeUnit.HOURS;
               break;
            }

            case 6:
            case 38:
            {
               result = TimeUnit.ELAPSED_HOURS;
               break;
            }

            case 7:
            case 39:
            {
               result = TimeUnit.DAYS;
               break;
            }

            case 8:
            case 40:
            {
               result = TimeUnit.ELAPSED_DAYS;
               break;
            }

            case 9:
            case 41:
            {
               result = TimeUnit.WEEKS;
               break;
            }

            case 10:
            case 42:
            {
               result = TimeUnit.ELAPSED_WEEKS;
               break;
            }

            case 11:
            case 43:
            {
               result = TimeUnit.MONTHS;
               break;
            }

            case 12:
            case 44:
            {
               result = TimeUnit.ELAPSED_MONTHS;
               break;
            }

            case 19:
            case 51:
            {
               result = TimeUnit.PERCENT;
               break;
            }

            case 20:
            case 52:
            {
               result = TimeUnit.ELAPSED_PERCENT;
               break;
            }
         }
      }

      return (result);
   }

   /**
    * Utility method to convert duration units into a BigInteger value.
    * Note that we don't differentiate between confirmed and unconfirmed
    * durations. Unrecognised duration types are default to hours.
    *
    * @param value Duration units
    * @return BigInteger value
    */
   private BigInteger getXmlDurationUnits (TimeUnit value)
   {
      int result;

      if (value == null)
      {
         value = TimeUnit.HOURS;
      }
      
      switch (value.getValue())
      {
         case TimeUnit.MINUTES_VALUE:
         {
            result = 3;
            break;
         }

         case TimeUnit.ELAPSED_MINUTES_VALUE:
         {
            result = 4;
            break;
         }

         case TimeUnit.ELAPSED_HOURS_VALUE:
         {
            result = 6;
            break;
         }

         case TimeUnit.DAYS_VALUE:
         {
            result = 7;
            break;
         }

         case TimeUnit.ELAPSED_DAYS_VALUE:
         {
            result = 8;
            break;
         }

         case TimeUnit.WEEKS_VALUE:
         {
            result = 9;
            break;
         }

         case TimeUnit.ELAPSED_WEEKS_VALUE:
         {
            result = 10;
            break;
         }

         case TimeUnit.MONTHS_VALUE:
         {
            result = 11;
            break;
         }

         case TimeUnit.ELAPSED_MONTHS_VALUE:
         {
            result = 12;
            break;
         }

         case TimeUnit.PERCENT_VALUE:
         {
            result = 19;
            break;
         }

         case TimeUnit.ELAPSED_PERCENT_VALUE:
         {
            result = 20;
            break;
         }

         default:
         case TimeUnit.HOURS_VALUE:
         {
            result = 5;
            break;
         }
      }
      
      return (BigInteger.valueOf(result));
   }


   /**
    * Utility method to convert a BigInteger value
    * into a priority.
    *
    * @param priority BigInteger value
    * @return Priority value
    */
   private Priority getMpxPriority (BigInteger priority)
   {
      int result = Priority.MEDIUM;

      if (priority != null)
      {
         if (priority.intValue() >= 1000)
         {
            result = Priority.DO_NOT_LEVEL;
         }
         else
         {
            result = (priority.intValue() / 100)-1;
         }
      }

      return (Priority.getInstance (result));
   }

   /**
    * Utility method to convert a priority into a BigInteger value.
    *
    * @param priority Priority value
    * @return BigInteger value
    */
   private BigInteger getXmlPriority (Priority priority)
   {
      int result = Priority.MEDIUM;

      if (priority != null)
      {
         result = (priority.getPriority()+1) * 100;
      }

      return (BigInteger.valueOf(result));
   }

   /**
    * Utility method to convert a duration expressed in minutes * 1000
    * as a BigInteger into an MPXDuration.
    *
    * @param value BigInteger value
    * @return MPXDuration
    */
   private MPXDuration getMinutesDuration (BigInteger value)
   {
      MPXDuration result = null;

      if (value != null)
      {
         result = new MPXDuration (value.intValue()/1000, TimeUnit.MINUTES);
      }

      return (result);
   }

   /**
    * This method retrieves the cost component of a rate and handles
    * the case where the rate is a null value.
    *
    * @param rate MPXRate value
    * @return The cost component of an MPXRate
    */
   private double getRateCost (MPXRate rate)
   {
      double amount = 0;

      if (rate != null)
      {
         amount = rate.getAmount();
      }

      return (amount);
   }

   /**
    * This method converts an hourly rate expressed as a BigDecimal into
    * an MPXRate object, handling the case where the rate value is null.
    *
    * @param value Hourly rate
    * @return MPXRate instance
    */
   private MPXRate getHourlyRate (BigDecimal value)
   {
      MPXRate result = null;

      if (value != null)
      {
         result = new MPXRate (value, TimeUnit.HOURS);
      }

      return (result);
   }

   /**
    * This method converts an MPXDuration value into a duration
    * expressed in minutes.
    *
    * @todo This conversion does not use a calendar and is therefore arbitrary.
    * @param duration MPXDuration value
    * @return Duration value in minutes
    */
   private double getDurationInMinutes (MPXDuration duration)
   {
      double result = 0;

      if (duration != null)
      {
         result = duration.getDuration();

         switch (duration.getUnits().getValue())
         {
            case TimeUnit.HOURS_VALUE:
            case TimeUnit.ELAPSED_HOURS_VALUE:
            {
               result *= 60;
               break;
            }

            case TimeUnit.DAYS_VALUE:
            case TimeUnit.ELAPSED_DAYS_VALUE:
            {
               result *= (60 * 8);
               break;
            }

            case TimeUnit.WEEKS_VALUE:
            case TimeUnit.ELAPSED_WEEKS_VALUE:
            {
               result *= (60 * 8 * 5);
               break;
            }

            case TimeUnit.MONTHS_VALUE:
            case TimeUnit.ELAPSED_MONTHS_VALUE:
            {
               result *= (60 * 8 * 5 * 4);
               break;
            }

            case TimeUnit.YEARS_VALUE:
            case TimeUnit.ELAPSED_YEARS_VALUE:
            {
               result *= (60 * 8 * 5 * 52);
               break;
            }
         }
      }

      return (result);
   }

   /**
    * This is the main output method provided by this class. It allows
    * project data to be written to an output stream as XML data formatted
    * according to the MSPDI XML Schema.
    *
    * @param stream Output stream
    *
    * @throws IOException on write errors
    */
   public void write (OutputStream stream)
      throws IOException
   {
      try
      {
         JAXBContext context = JAXBContext.newInstance ("com.tapsterrock.mspdi.schema");
         Marshaller marshaller = context.createMarshaller();
         marshaller.setProperty (Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

         ObjectFactory factory = new ObjectFactory ();
         Project project = factory.createProject();

         writeProjectHeader (project);
         writeProjectExtendedAttributes (factory, project);
         writeCalendars (factory, project);
         writeResources (factory, project);
         writeTasks (factory, project);
         writeAssignments (factory, project);

         if (m_compatibleOutput == true)
         {
            stream = new CompatabilityOutputStream (stream);
         }

         marshaller.marshal (project, stream);
      }

      catch (JAXBException ex)
      {
         throw new IOException (ex.toString());
      }
   }

   
   /**
    * This method writes project header data to an MSPDI file.
    *
    * @param project Root node of the MSPDI file
    */
   private void writeProjectHeader (Project project)
   {
      ProjectHeader header = getProjectHeader ();

      project.setMinutesPerDay(getBigInteger(header.getMinutesPerDay()));
      project.setSplitsInProgressTasks(header.getSplitInProgressTasks());
      project.setManager(header.getManager());      
      project.setDefaultFinishTime(getCalendar (header.getDefaultEndTime()));
      project.setFiscalYearStart(header.getFiscalYearStart());
      project.setDefaultTaskEVMethod(getEarnedValueMethod(header.getDefaultTaskEarnedValueMethod()));
      project.setDefaultStartTime(getCalendar (header.getDefaultStartTime()));
      project.setFinishDate(getCalendar(header.getFinishDate()));
      project.setMoveCompletedEndsBack(false);
      project.setWorkFormat(getXmlWorkUnits(header.getDefaultWorkUnits()));
      project.setBaselineForEarnedValue(BigInteger.ZERO);
      project.setCalendarUID(BigInteger.ONE);
      project.setSubject(header.getSubject());
      project.setNewTasksEstimated(true);
      project.setSpreadActualCost(false);
      project.setDefaultFixedCostAccrual(BigInteger.valueOf(AccrueType.END));
      project.setMultipleCriticalPaths(false);
      project.setAutoAddNewResourcesAndTasks(true);
      project.setDefaultOvertimeRate((float)getRateCost(header.getDefaultOvertimeRate()));
      project.setStartDate(getCalendar(header.getStartDate()));
      project.setCurrencySymbolPosition(getXmlSymbolPosition (header.getSymbolPosition()));
      project.setFYStartDate(BigInteger.ONE);
      //project.setLastSaved();
      //project.setStatusDate();
      project.setMoveRemainingStartsBack(false);
      project.setAutolink(true);
      project.setTitle(header.getProjectTitle());
      project.setCompany(header.getCompany());
      //project.setExtendedCreationDate();
      project.setDurationFormat(getXmlDurationUnits(header.getDefaultDurationUnits()));      
      project.setMicrosoftProjectServerURL(true);
      project.setNewTaskStartDate(BigInteger.ZERO);     
      project.setHonorConstraints(false);
      project.setDaysPerMonth(getBigInteger(header.getDaysPerMonth()));
      project.setAuthor(header.getAuthor());
      //project.setAdminProject();
      project.setScheduleFromStart(true);     
      project.setMinutesPerWeek(getBigInteger(header.getMinutesPerWeek()));
      project.setCurrentDate(getCalendar(header.getCurrentDate()));            
      project.setCurrencyDigits(BigInteger.valueOf (header.getCurrencyDigits().intValue()));
      project.setInsertedProjectsLikeSummary(false);                  
      project.setName("MPXJ");
      project.setSpreadPercentComplete(false);
      project.setWeekStartDay(BigInteger.ONE);
      project.setDefaultStandardRate((float)getRateCost(header.getDefaultStandardRate()));
      project.setMoveCompletedEndsForward(false);
      project.setCurrencySymbol(header.getCurrencySymbol());
      project.setTaskUpdatesResource(header.getUpdatingTaskStatusUpdatesResourceStatus());
      project.setEditableActualCosts(false);
      //project.setUID()
      project.setCriticalSlackLimit(BigInteger.ZERO);      
      //project.setRevision()
      project.setNewTasksEffortDriven(false);
      //project.setEarnedValueMethod
      project.setMoveRemainingStartsForward(false);
      project.setDefaultTaskType(BigInteger.ZERO);      
      project.setProjectExternallyEdited(header.getProjectExternallyEdited());
      //project.setActualsInSync();
      project.setCategory(header.getCategory());
      //project.setCreationDate();
   }

   /**
    * This method writes project extended attribute data into an MSPDI file.
    *
    * @param factory object factory
    * @param project Root node of the MSPDI file
    * @throws JAXBException
    */
   private void writeProjectExtendedAttributes (ObjectFactory factory, Project project)
      throws JAXBException
   {
      Project.ExtendedAttributesType attributes = factory.createProjectTypeExtendedAttributesType();
      project.setExtendedAttributes(attributes);
      List list = attributes.getExtendedAttribute();

      writeFieldAliases (factory, getTaskFieldAliasMap(), TASK_FIELD_MPX_TO_XML_MAP, TASK_FIELD_MPX_TO_NAME_MAP, list);
      writeFieldAliases (factory, getResourceFieldAliasMap(), RESOURCE_FIELD_MPX_TO_XML_MAP, RESOURCE_FIELD_MPX_TO_NAME_MAP, list);
   }

   /**
    * This method handles writing field alias data into the MSPDI file.
    *
    * @param factory object factory
    * @param fieldAliasMap map of MPX field numbers to their aliases
    * @param mpxXmlMap map of mpx field numbers to MSPDI field numbers
    * @param mpxNameMap map of mpx field names to MSPDI field numbers
    * @param list list of extended attributes
    * @throws JAXBException
    */
   private void writeFieldAliases (ObjectFactory factory, HashMap fieldAliasMap, HashMap mpxXmlMap, HashMap mpxNameMap, List list)
      throws JAXBException
   {
      Iterator iter = mpxNameMap.keySet().iterator();
      Integer key;
      Integer fieldID;
      String name;
      String alias;
      
      while (iter.hasNext() == true)
      {
         key = (Integer)iter.next();
         fieldID = (Integer)mpxXmlMap.get(key);
         name = (String)mpxNameMap.get(key);         
         alias = (String)fieldAliasMap.get(key);
         
         Project.ExtendedAttributesType.ExtendedAttributeType attribute = factory.createProjectTypeExtendedAttributesTypeExtendedAttributeType();
         list.add(attribute);
         attribute.setFieldID(fieldID.toString());
         attribute.setFieldName(name);
         attribute.setAlias(alias);
      }
   }
   
   /**
    * This method writes calandar data to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param project Root node of the MSPDI file
    * @throws JAXBException on xml creation errors
    */
   private void writeCalendars (ObjectFactory factory, Project project)
      throws JAXBException
   {
      //
      // First step, find all of the base calendars and resource calendars,
      // add them to a list ready for processing, and create a map between
      // names and unique IDs
      //
      LinkedList calendarList = new LinkedList(getBaseCalendars ());
      Iterator iter = getAllResources().iterator();
      MPXCalendar cal;

      while (iter.hasNext() == true)
      {
         cal = ((Resource)iter.next()).getResourceCalendar();
         if (cal != null)
         {
            calendarList.add(cal);
         }
      }


      //
      // Create the new MSPDI calendar list
      //
      Project.CalendarsType calendars = factory.createProjectTypeCalendarsType();
      project.setCalendars (calendars);
      List calendar = calendars.getCalendar();

      //
      // Process each calendar in turn
      //
      iter = calendarList.iterator();
      factory.createProjectTypeCalendarsTypeCalendarType();

      while (iter.hasNext() == true)
      {
         cal = (MPXCalendar)iter.next();
         calendar.add (writeCalendar (factory, cal));
      }
   }

   /**
    * This method writes data for a single calandar to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param bc Base calendar data
    * @return New MSPDI calendar instance
    * @throws JAXBException on xml creation errors
    */
   private Project.CalendarsType.CalendarType writeCalendar (ObjectFactory factory, MPXCalendar bc)
      throws JAXBException
   {
      //
      // Create a calendar
      //
      Project.CalendarsType.CalendarType calendar = factory.createProjectTypeCalendarsTypeCalendarType();
      calendar.setUID(BigInteger.valueOf(bc.getUniqueID()));
      calendar.setIsBaseCalendar(bc.isBaseCalendar());

      if (bc.isBaseCalendar() == false)
      {
         MPXCalendar base = bc.getBaseCalendar();
         calendar.setBaseCalendarUID(BigInteger.valueOf(base.getUniqueID()));
      }

      calendar.setName(bc.getName());

      //
      // Create a list of normal days
      //
      Project.CalendarsType.CalendarType.WeekDaysType days = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysType();
      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType times;
      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.WorkingTimesType.WorkingTimeType time;
      MPXCalendarHours bch;
      List timesList;

      calendar.setWeekDays (days);
      List dayList = days.getWeekDay();

      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType day;
      int loop;
      int workingFlag;

      for (loop=1; loop < 8; loop++)
      {
         workingFlag = bc.getWorkingDay(loop);

         if (workingFlag != MPXCalendar.DEFAULT)
         {
            day = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayType();
            dayList.add(day);
            day.setDayType(BigInteger.valueOf(loop));
            day.setDayWorking(workingFlag == MPXCalendar.WORKING);

            if (workingFlag == MPXCalendar.WORKING)
            {
               times = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesType ();
               day.setWorkingTimes(times);
               timesList = times.getWorkingTime();

               bch = bc.getCalendarHours (loop);
               if (bch != null)
               {
                  time = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesTypeWorkingTimeType ();
                  timesList.add (time);

                  time.setFromTime(getCalendar(bch.getFromTime1()));
                  time.setToTime(getCalendar(bch.getToTime1()));

                  time = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesTypeWorkingTimeType ();
                  timesList.add (time);

                  time.setFromTime(getCalendar(bch.getFromTime2()));
                  time.setToTime(getCalendar(bch.getToTime2()));

                  time = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesTypeWorkingTimeType ();
                  timesList.add (time);

                  time.setFromTime(getCalendar(bch.getFromTime3()));
                  time.setToTime(getCalendar(bch.getToTime3()));
               }
            }
         }
      }

      //
      // Create a list of exceptions
      //
      List exceptions = bc.getCalendarExceptions ();
      Iterator iter = exceptions.iterator();
      MPXCalendarException exception;
      Project.CalendarsType.CalendarType.WeekDaysType.WeekDayType.TimePeriodType period;
      boolean working;

      while (iter.hasNext() == true)
      {
         exception = (MPXCalendarException)iter.next();
         working = exception.getWorkingValue();

         day = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayType();
         dayList.add(day);
         day.setDayType(BIGINTEGER_ZERO);
         day.setDayWorking(working);

         period = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeTimePeriodType();
         day.setTimePeriod(period);
         period.setFromDate(getCalendar(exception.getFromDate()));
         period.setToDate(getCalendar (exception.getToDate()));

         if (working == true)
         {
            times = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesType ();
            day.setWorkingTimes(times);
            timesList = times.getWorkingTime();

            time = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesTypeWorkingTimeType ();
            timesList.add (time);

            time.setFromTime(getCalendar(exception.getFromTime1()));
            time.setToTime(getCalendar(exception.getToTime1()));

            time = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesTypeWorkingTimeType ();
            timesList.add (time);

            time.setFromTime(getCalendar(exception.getFromTime2()));
            time.setToTime(getCalendar(exception.getToTime2()));

            time = factory.createProjectTypeCalendarsTypeCalendarTypeWeekDaysTypeWeekDayTypeWorkingTimesTypeWorkingTimeType ();
            timesList.add (time);

            time.setFromTime(getCalendar(exception.getFromTime3()));
            time.setToTime(getCalendar(exception.getToTime3()));
         }
      }

      return (calendar);
   }

   /**
    * This method writes resource data to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param project Root node of the MSPDI file
    * @throws JAXBException on xml creation errors
    */
   private void writeResources (ObjectFactory factory, Project project)
      throws JAXBException
   {
      Project.ResourcesType resources = factory.createProjectTypeResourcesType();
      project.setResources(resources);
      List list = resources.getResource();

      Iterator iter = getAllResources().iterator();
      while (iter.hasNext() == true)
      {
         list.add (writeResource (factory, (Resource)iter.next()));
      }
   }

   /**
    * This method writes data for a single resource to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param mpx Resource data
    * @return New MSPDI resource instance
    * @throws JAXBException on xml creation errors
    */
   private Project.ResourcesType.ResourceType writeResource (ObjectFactory factory, Resource mpx)
      throws JAXBException
   {
      Project.ResourcesType.ResourceType xml = factory.createProjectTypeResourcesTypeResourceType();
      MPXCalendar cal = mpx.getResourceCalendar();
      if (cal != null)
      {
         xml.setCalendarUID(BigInteger.valueOf(cal.getUniqueID()));
      }

      xml.setAccrueAt(BigInteger.valueOf(getAccrueType(mpx.getAccrueAt())));
      xml.setActiveDirectoryGUID(mpx.getActiveDirectoryGUID());
      xml.setActualCost(getXmlCurrency (mpx.getActualCost()));
      xml.setActualOvertimeCost(getXmlCurrency(mpx.getActualOvertimeCost()));
      xml.setActualOvertimeWork(getDuration(mpx.getActualOvertimeWork()));
      xml.setActualOvertimeWorkProtected(getDuration(mpx.getActualOvertimeWorkProtected()));
      xml.setActualWork(getDuration (mpx.getActualWork()));
      xml.setActualWorkProtected(getDuration(mpx.getActualWorkProtected()));
      xml.setACWP(getFloat(mpx.getACWP()));
      xml.setAvailableFrom(getCalendar(mpx.getAvailableFrom()));
      xml.setAvailableTo(getCalendar(mpx.getAvailableTo()));
      xml.setBCWS(getFloat(mpx.getBCWS()));
      xml.setBCWP(getFloat(mpx.getBCWP()));      
      xml.setBookingType(getBookingType(mpx.getBookingType()));
      xml.setCanLevel(mpx.getCanLevel());
      xml.setCode(mpx.getCode());
      xml.setCost(getXmlCurrency(mpx.getCost()));
      xml.setCostPerUse(getXmlCurrency(mpx.getCostPerUse()));
      xml.setCostVariance(getFloat(mpx.getCostVariance())*100);
      xml.setCreationDate(getCalendar(mpx.getCreationDate()));
      xml.setCV(getFloat(mpx.getCV()));
      xml.setEmailAddress(mpx.getEmailAddress());
      xml.setFinish(getCalendar(mpx.getFinish()));
      xml.setGroup(mpx.getGroup());
      xml.setHyperlink(mpx.getHyperlink());
      xml.setHyperlinkAddress(mpx.getHyperlinkAddress());
      xml.setHyperlinkSubAddress(mpx.getHyperlinkSubAddress());
      xml.setID(BigInteger.valueOf(mpx.getIDValue()));
      xml.setInitials(mpx.getInitials());
      xml.setIsEnterprise(mpx.getEnterprise());
      xml.setIsGeneric(mpx.getGeneric());
      xml.setIsInactive(mpx.getInactive());
      xml.setIsNull(mpx.getNull());
      xml.setMaterialLabel(mpx.getMaterialLabel());
      xml.setMaxUnits(getUnits(mpx.getMaxUnits()));
      xml.setName(mpx.getName());
      xml.setNotes(mpx.getNotes());
      xml.setNTAccount(mpx.getNtAccount());
      xml.setOverAllocated(mpx.getOverAllocated());
      xml.setOvertimeCost(getXmlCurrency(mpx.getOvertimeCost()));
      xml.setOvertimeRate(new BigDecimal (getRateCost (mpx.getOvertimeRate())));
      xml.setOvertimeRateFormat(BigInteger.valueOf(getTimeUnit(mpx.getOvertimeRateFormat())+1));
      xml.setOvertimeWork(getDuration (mpx.getOvertimeWork()));
      xml.setPeakUnits(getUnits(mpx.getPeakUnits()));
      xml.setPercentWorkComplete(getBigInteger(mpx.getPercentWorkComplete()));
      xml.setPhonetics(mpx.getPhonetics());
      xml.setRegularWork(getDuration(mpx.getRegularWork()));
      xml.setRemainingCost(getXmlCurrency(mpx.getRemainingCost()));
      xml.setRemainingOvertimeCost(getXmlCurrency(mpx.getRemainingOvertimeCost()));
      xml.setRemainingOvertimeWork(getDuration(mpx.getRemainingOvertimeWork()));
      xml.setRemainingWork(getDuration(mpx.getRemainingWork()));
      xml.setStandardRate(new BigDecimal (getRateCost (mpx.getStandardRate())));
      xml.setStandardRateFormat(BigInteger.valueOf(getTimeUnit(mpx.getStandardRateFormat())+1));
      xml.setStart(getCalendar(mpx.getStart()));
      xml.setSV(getFloat(mpx.getSV()));
      xml.setType(getResourceType(mpx.getType()));
      xml.setUID(BigInteger.valueOf(mpx.getUniqueIDValue()));
      xml.setWork(getDuration(mpx.getWork()));
      xml.setWorkGroup(getWorkGroup(mpx.getWorkGroup()));
      xml.setWorkVariance((float)getDurationInMinutes(mpx.getWorkVariance())*1000);

      writeResourceExtendedAttributes (factory, xml, mpx);
      
      return (xml);
   }

   /**
    * This method writes extended attribute data for a resource.
    * 
    * @param factory JAXB object factory
    * @param xml MSPDI resource
    * @param mpx MPXJ resource
    * @throws JAXBException
    */
   private void writeResourceExtendedAttributes (ObjectFactory factory, Project.ResourcesType.ResourceType xml, Resource mpx)
      throws JAXBException
   {
      Project.ResourcesType.ResourceType.ExtendedAttributeType attrib;
      List extendedAttributes = xml.getExtendedAttribute();
      Object value;
      int mpxFieldID;
      Integer xmlFieldID;
      
      for (int loop=0; loop < RESOURCE_DATA.length; loop++)
      {
         mpxFieldID = ((Integer)RESOURCE_DATA[loop][MPX_FIELD_ID]).intValue();
         value = mpx.get(mpxFieldID);
         
         if (value != null)
         {
            xmlFieldID = (Integer)RESOURCE_DATA[loop][MSPDI_FIELD_ID];
   
            attrib = factory.createProjectTypeResourcesTypeResourceTypeExtendedAttributeType();
            extendedAttributes.add(attrib);
            attrib.setUID(BigInteger.valueOf(loop+1));
            attrib.setFieldID(xmlFieldID.toString());
            
            if (value instanceof Date)
            {
               attrib.setValue(formatXsdDateTime((Date)value));
            }
            else
            {
               if (value instanceof Boolean)
               {
                  attrib.setValue(((Boolean)value).booleanValue()?"1":"0");
               }
               else
               {
                  if (value instanceof MPXDuration)
                  {
                     MPXDuration dur = (MPXDuration)value;
                     attrib.setValue(getDuration(dur));
                     attrib.setDurationFormat(getXmlDurationUnits(dur.getUnits()));
                  }
                  else
                  {
                     if (value instanceof MPXCurrency)
                     {
                        MPXCurrency cur = (MPXCurrency)value;
                        attrib.setValue(Double.toString(cur.doubleValue()*100));
                     }
                     else
                     {
                        attrib.setValue(value.toString());
                     }
                  }
               }
            }
         }
      }
   }
   
   /**
    * This method writes task data to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param project Root node of the MSPDI file
    * @throws JAXBException on xml creation errors
    */
   private void writeTasks (ObjectFactory factory, Project project)
      throws JAXBException
   {
      Project.TasksType tasks = factory.createProjectTypeTasksType();
      project.setTasks (tasks);
      List list = tasks.getTask();

      Iterator iter = getAllTasks().iterator();
      while (iter.hasNext() == true)
      {
         list.add (writeTask (factory, (Task)iter.next()));
      }
   }


   /**
    * This method writes data for a single task to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param mpx Task data
    * @return new task instance
    * @throws JAXBException on xml creation errors
    */
   private Project.TasksType.TaskType writeTask (ObjectFactory factory, Task mpx)
      throws JAXBException
   {
      Project.TasksType.TaskType xml = factory.createProjectTypeTasksTypeTaskType();

      xml.setActualCost(getXmlCurrency(mpx.getActualCost()));
      xml.setActualDuration(getDuration(mpx.getActualDuration()));
      xml.setActualFinish(getCalendar(mpx.getActualFinish()));
      xml.setActualOvertimeCost(getXmlCurrency(mpx.getActualOvertimeCost()));
      xml.setActualOvertimeWork(getDuration(mpx.getActualOvertimeWork()));
      xml.setActualOvertimeWorkProtected(getDuration(mpx.getActualOvertimeWorkProtected()));
      xml.setActualStart(getCalendar(mpx.getActualStart()));
      xml.setActualWork(getDuration(mpx.getActualWork()));
      xml.setActualWorkProtected(getDuration(mpx.getActualWorkProtected()));
      xml.setACWP(getFloat(mpx.getACWP()));
      xml.setBCWP((float)mpx.getBCWPValue());
      xml.setBCWS((float)mpx.getBCWSValue());
      xml.setCalendarUID(getTaskCalendarID(mpx));
      xml.setConstraintDate(getCalendar(mpx.getConstraintDate()));
      xml.setConstraintType(BigInteger.valueOf(mpx.getConstraintTypeValue()));
      xml.setContact(mpx.getContact());
      xml.setCost(getXmlCurrency(mpx.getCost()));
      xml.setCreateDate(getCalendar(mpx.getCreateDate()));
      xml.setCritical(mpx.getCriticalValue());
      xml.setCV((float)(mpx.getCVValue()*100));
      xml.setDeadline(getCalendar(mpx.getDeadline()));
      xml.setDuration(getDuration(mpx.getDuration()));
      xml.setDurationFormat(getXmlDurationUnits(mpx.getDurationFormat()));
      xml.setDurationFormat(getDurationFormat(mpx.getDuration()));
      xml.setEarlyFinish(getCalendar(mpx.getEarlyFinish()));
      xml.setEarlyStart(getCalendar(mpx.getEarlyStart()));
      xml.setEarnedValueMethod(getEarnedValueMethod(mpx.getEarnedValueMethod()));
      xml.setEffortDriven(mpx.getEffortDriven());
      xml.setEstimated(mpx.getEstimated());
      xml.setExternalTask(mpx.getExternalTask());
      xml.setExternalTaskProject(mpx.getExternalTaskProject());
      
      Date finishDate = mpx.getFinish();
      if (finishDate != null)
      {
         xml.setFinish(getCalendar(finishDate));
      }

      xml.setFinishVariance(BigInteger.valueOf((long)getDurationInMinutes(mpx.getFinishVariance())*1000));
      xml.setFixedCost((float)(mpx.getFixedCostValue()*100));
      
      AccrueType fixedCostAccrual = mpx.getFixedCostAccrual();
      if (fixedCostAccrual == null)
      {
         fixedCostAccrual = AccrueType.getInstance(AccrueType.PRORATED);
      }
      xml.setFixedCostAccrual(Integer.toString(fixedCostAccrual.getType()));
      
      xml.setFreeSlack(BigInteger.valueOf((long)getDurationInMinutes(mpx.getFreeSlack())*1000));
      xml.setHideBar(mpx.getHideBarValue());
      xml.setIsNull(mpx.getNull());
      xml.setIsSubproject(mpx.getSubproject());
      xml.setIsSubprojectReadOnly(mpx.getSubprojectReadOnly());
      xml.setHyperlink(mpx.getHyperlink());
      xml.setHyperlinkAddress(mpx.getHyperlinkAddress());
      xml.setHyperlinkSubAddress(mpx.getHyperlinkSubAddress());
      xml.setID(BigInteger.valueOf(mpx.getIDValue()));
      xml.setIgnoreResourceCalendar(mpx.getIgnoreResourceCalendar());
      xml.setLateFinish(getCalendar(mpx.getLateFinish()));
      xml.setLateStart(getCalendar(mpx.getLateStart()));
      xml.setLevelAssignments(mpx.getLevelAssignments());
      xml.setLevelingCanSplit(mpx.getLevelingCanSplit());

      if (mpx.getLevelingDelay() != null)
      {
         xml.setLevelingDelay(BigInteger.valueOf((long)mpx.getLevelingDelay().getDuration()));
         xml.setLevelingDelayFormat(getXmlDurationUnits(mpx.getLevelingDelayFormat()));
      }

      xml.setMilestone(mpx.getMilestoneValue());
      xml.setName(mpx.getName());
      xml.setNotes(mpx.getNotes());
      xml.setOutlineLevel(BigInteger.valueOf(mpx.getOutlineLevelValue()));
      xml.setOutlineNumber(mpx.getOutlineNumber());
      xml.setOverAllocated(mpx.getOverAllocated());
      xml.setOvertimeCost(getXmlCurrency(mpx.getOvertimeCost()));
      xml.setOvertimeWork(getDuration(mpx.getOvertimeWork()));
      xml.setPercentComplete(BigInteger.valueOf((long)mpx.getPercentageCompleteValue()));
      xml.setPercentWorkComplete(BigInteger.valueOf((long)mpx.getPercentageWorkCompleteValue()));
      xml.setPhysicalPercentComplete(getBigInteger(mpx.getPhysicalPercentComplete()));
      xml.setPriority(getXmlPriority(mpx.getPriority()));
      xml.setRecurring(mpx.getRecurring());
      xml.setRegularWork(getDuration(mpx.getRegularWork()));
      xml.setRemainingCost(getXmlCurrency(mpx.getRemainingCost()));

      if (m_compatibleOutput == true && mpx.getRemainingDuration() == null)
      {
         MPXDuration duration = mpx.getDuration();

         if (duration != null)
         {
            double amount = duration.getDuration();
            amount -= ((amount * mpx.getPercentageCompleteValue())/100);
            xml.setRemainingDuration(getDuration(new MPXDuration (amount, duration.getUnits())));
         }
      }
      else
      {
         xml.setRemainingDuration(getDuration(mpx.getRemainingDuration()));
      }

      xml.setRemainingOvertimeCost(getXmlCurrency(mpx.getRemainingOvertimeCost()));
      xml.setRemainingOvertimeWork(getDuration(mpx.getRemainingOvertimeWork()));
      xml.setRemainingWork(getDuration(mpx.getRemainingWork()));
      xml.setResume(getCalendar(mpx.getResume()));
      xml.setResumeValid(mpx.getResumeValid());
      xml.setRollup(mpx.getRollupValue());

      Date startDate = mpx.getStart();
      if (startDate != null)
      {
         xml.setStart(getCalendar(startDate));
      }


      xml.setStartVariance(BigInteger.valueOf((long)getDurationInMinutes(mpx.getStartVariance())*1000));
      xml.setStop(getCalendar (mpx.getStop()));
      xml.setSubprojectName(mpx.getSubprojectName());
      xml.setSummary(mpx.getSummaryValue());
      xml.setTotalSlack(BigInteger.valueOf((long)getDurationInMinutes(mpx.getTotalSlack())*1000));
      xml.setType(getTaskType(mpx.getType()));
      xml.setUID(BigInteger.valueOf(mpx.getUniqueIDValue()));
      xml.setWBS(mpx.getWBS());
      xml.setWBSLevel(mpx.getWBSLevel());
      xml.setWork(getDuration(mpx.getWork()));
      xml.setWorkVariance((float)getDurationInMinutes(mpx.getWorkVariance())*1000);

      writePredecessors (factory, xml, mpx);

      writeTaskExtendedAttributes (factory, xml, mpx);
      
      return (xml);
   }

   /**
    * This method writes extended attribute data for a task.
    * 
    * @param factory JAXB object factory
    * @param xml MSPDI task
    * @param mpx MPXJ task
    * @throws JAXBException
    */
   private void writeTaskExtendedAttributes (ObjectFactory factory, Project.TasksType.TaskType xml, Task mpx)
      throws JAXBException
   {
      Project.TasksType.TaskType.ExtendedAttributeType attrib;
      List extendedAttributes = xml.getExtendedAttribute();
      Object value;
      int mpxFieldID;
      Integer xmlFieldID;
      
      for (int loop=0; loop < TASK_DATA.length; loop++)
      {
         mpxFieldID = ((Integer)TASK_DATA[loop][MPX_FIELD_ID]).intValue();
         value = mpx.get(mpxFieldID);
         
         if (value != null)
         {
            xmlFieldID = (Integer)TASK_DATA[loop][MSPDI_FIELD_ID];
   
            attrib = factory.createProjectTypeTasksTypeTaskTypeExtendedAttributeType();
            extendedAttributes.add(attrib);
            attrib.setUID(BigInteger.valueOf(loop+1));
            attrib.setFieldID(xmlFieldID.toString());
            
            if (value instanceof Date)
            {
               attrib.setValue(formatXsdDateTime((Date)value));
            }
            else
            {
               if (value instanceof Boolean)
               {
                  attrib.setValue(((Boolean)value).booleanValue()?"1":"0");
               }
               else
               {
                  if (value instanceof MPXDuration)
                  {
                     MPXDuration dur = (MPXDuration)value;
                     attrib.setValue(getDuration(dur));
                     attrib.setDurationFormat(getXmlDurationUnits(dur.getUnits()));
                  }
                  else
                  {
                     if (value instanceof MPXCurrency)
                     {
                        MPXCurrency cur = (MPXCurrency)value;
                        attrib.setValue(Double.toString(cur.doubleValue()*100));
                     }
                     else
                     {
                        attrib.setValue(value.toString());
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * This method retrieves the UID for a calendar associated with a task.
    *
    * @param mpx MPX Task instance
    * @return calendar UID
    */
   private BigInteger getTaskCalendarID (Task mpx)
   {
      BigInteger result = null;
      String name = mpx.getCalendarName();
      if (name != null)
      {
         MPXCalendar cal = this.getBaseCalendar(name);
         if (cal != null)
         {
            result = BigInteger.valueOf(cal.getUniqueID());
         }
      }

      if (result == null)
      {
         result = BigInteger.valueOf(-1);
      }

      return (result);
   }

   /**
    * This method writes predecessor data to an MSPDI file.
    * We have to deal with a slight anomaly in this method that is introduced
    * by the MPX file format. It would be possible for someone to create an
    * MPX file with both the predecessor list and the unique ID predecessor
    * list populated... which means that we must process both and avoid adding
    * duplicate predecessors. Also interesting to note is that MSP98 populates
    * the predecessor list, not the unique ID predecessor list, as you might
    * expect.
    *
    * @param factory ObjectFactory instance
    * @param xml MSPDI task data
    * @param mpx MPX task data
    * @throws JAXBException on xml creation errors
    */
   private void writePredecessors (ObjectFactory factory, Project.TasksType.TaskType xml, Task mpx)
      throws JAXBException
   {
      TreeSet set = new TreeSet ();
      int taskID;
      Relation rel;
      List list = xml.getPredecessorLink();
      Iterator iter;

      //
      // Process the list of predecessors specified by Unique ID
      //
      RelationList predecessors = mpx.getUniqueIDPredecessors();
      if (predecessors != null)
      {
         iter = predecessors.iterator();
         while (iter.hasNext() == true)
         {
            rel = (Relation)iter.next();
            taskID = rel.getTaskIDValue();
            set.add(new Integer(taskID));
            list.add (writePredecessor (factory, taskID, rel.getType(), rel.getDuration()));
         }
      }

      //
      // Process the list of predecessors specified by ID.
      // Note that this code ensures that if both lists are populated,
      // we avoid creating duplicate links.
      //
      predecessors = mpx.getPredecessors();
      if (predecessors != null)
      {
         Task task;
         iter = predecessors.iterator();
         while (iter.hasNext() == true)
         {
            rel = (Relation)iter.next();
            task = getTaskByID(rel.getTaskIDValue());
            if (task != null)
            {
               taskID = task.getUniqueIDValue();
               if (set.contains(new Integer(taskID)) == false)
               {
                  list.add (writePredecessor (factory, taskID, rel.getType(), rel.getDuration()));
               }
            }
         }
      }
   }

   /**
    * This method writes a single predecessor link to the MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param taskID The task UID
    * @param type The predecessor type
    * @param lag The lag duration
    * @return A new link to be added to the MSPDI file
    * @throws JAXBException on xml creation errors
    */
   private Project.TasksType.TaskType.PredecessorLinkType writePredecessor (ObjectFactory factory, int taskID, int type, MPXDuration lag)
      throws JAXBException
   {
      Project.TasksType.TaskType.PredecessorLinkType link = factory.createProjectTypeTasksTypeTaskTypePredecessorLinkType();

      link.setPredecessorUID (BigInteger.valueOf(taskID));
      link.setType (BigInteger.valueOf(type));

      if (lag != null && lag.getDuration() != 0)
      {
         link.setLinkLag(BigInteger.valueOf((long)getDurationInMinutes(lag)*10));
         link.setLagFormat(getXmlDurationUnits (lag.getUnits()));
      }

      return (link);
   }


   /**
    * This method writes assignment data to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param project Root node of the MSPDI file
    * @throws JAXBException on xml creation errors
    */
   private void writeAssignments (ObjectFactory factory, Project project)
      throws JAXBException
   {
      int uid = 0;
      Project.AssignmentsType assignments = factory.createProjectTypeAssignmentsType();
      project.setAssignments(assignments);
      List list = assignments.getAssignment();
      Iterator iter = getAllResourceAssignments().iterator();
      while (iter.hasNext() == true)
      {
         list.add(writeAssignment (factory, (ResourceAssignment)iter.next(), uid));
         ++uid;
      }
   }


   /**
    * This method writes data for a single assignment to an MSPDI file.
    *
    * @param factory ObjectFactory instance
    * @param mpx Resource assignment data
    * @param uid Unique ID for the new assignment
    * @return New MSPDI assignment instance
    * @throws JAXBException on xml creation errors
    */
   private Project.AssignmentsType.AssignmentType writeAssignment (ObjectFactory factory, ResourceAssignment mpx, int uid)
      throws JAXBException
   {
      Project.AssignmentsType.AssignmentType xml = factory.createProjectTypeAssignmentsTypeAssignmentType();

      xml.setActualCost(getXmlCurrency (mpx.getActualCost()));
      xml.setActualWork(getDuration (mpx.getActualWork()));
      xml.setCost(getXmlCurrency (mpx.getCost()));
      xml.setDelay(BigInteger.valueOf((long)getDurationInMinutes(mpx.getDelay())*1000));
      xml.setFinish(getCalendar(mpx.getFinish()));
      xml.setOvertimeWork(getDuration(mpx.getOvertimeWork()));
      xml.setRemainingWork(getDuration (mpx.getRemainingWork()));
      xml.setResourceUID(BigInteger.valueOf(mpx.getResourceUniqueIDValue()));
      xml.setStart(getCalendar (mpx.getStart()));
      xml.setTaskUID(BigInteger.valueOf(mpx.getTask().getUniqueIDValue()));
      xml.setUID(BigInteger.valueOf(uid));
      xml.setUnits(getUnits(mpx.getUnits()));
      xml.setWork(getDuration (mpx.getWork()));
      xml.setWorkContour(getWorkContour(mpx.getWorkContour()));
      return (xml);
   }

   /**
    * Formats a date as an xsd date time value.
    * 
    * @param date date
    * @return xsd date time
    */
   private static String formatXsdDateTime (Date date)
   {
      return (getXsdDateTimeFormat().format(date));
   }
   
   /**
    * Parses a date in xsd date time format
    * 
    * @param text xsd date time
    * @return date value
    */
   private static Date parseXsdDateTime (String text)
   {
      Date result;
      
      try
      {
         result = getXsdDateTimeFormat().parse(text);
      }
      
      catch (ParseException ex)
      {
         result = null;
      }
      
      return (result);
   }
   
   /**
    * Retrieves a date format instance suitable for working
    * with xsd date time values.
    * 
    * @return date format instance
    */
   private static DateFormat getXsdDateTimeFormat ()
   {
      DateFormat df = (DateFormat)XSD_DATETIME_FORMAT.get();
      if (df == null)
      {
         df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
         df.setLenient(false);
      }
      return (df);
   }

   private boolean m_compatibleOutput = true;
   private boolean m_compatibleInput = true;
   
   private static final ThreadLocal XSD_DATETIME_FORMAT = new ThreadLocal ();


   /**
    * This class is used to work around a number of problems with
    * Microsoft's XML implementation as used in Microsoft Project 2002.
    * Essentially this class implements a very simple find and replace
    * mechanism, allowing the output stream to be filtered on the fly
    * to change the contents.
    */
   private class CompatabilityOutputStream extends OutputStream
   {
      /**
       * Constructor. Takes the original output stream as an argument.
       *
       * @param parent Original output stream.
       */
      public CompatabilityOutputStream (OutputStream parent)
      {
         m_parent = parent;
         int max = 0;
         for (int loop=0; loop < m_find.length; loop++)
         {
            if (m_find[loop].length > max)
            {
               max = m_find[loop].length;
            }
         }
         m_buffer = new byte[max];
      }

      /**
       * This method writes a byte to the output stream. All of
       * the find and replace filtering takes place in this method.
       *
       * @param b Input byte
       * @throws IOException on write error
       */
      public void write (int b)
         throws IOException
      {
         if (m_match == -1)
         {
            for (int loop=0; loop < m_find.length; loop++)
            {
               if (b == m_find[loop][0])
               {
                  m_match = loop;
                  break;
               }
            }

            if (m_match != -1)
            {
               m_buffer[0] = (byte)b;
               m_index = 1;
            }
            else
            {
               m_parent.write (b);
            }
         }
         else
         {
            int find = m_find[m_match][m_index];

            if ((m_match > 0 && find == '?') || b == find)
            {
               m_buffer[m_index] = (byte)b;
               ++m_index;
               if (m_index == m_find[m_match].length)
               {
                  if (m_replace[m_match] != null)
                  {
                     m_parent.write(m_replace[m_match].getBytes());
                  }
                  m_match = -1;
               }
            }
            else
            {
               m_match = -1;
               m_parent.write(m_buffer, 0, m_index);
               write (b);
            }
         }
      }

      /**
       * This method passes the call on to the original output stream.
       *
       * @throws IOException on write error
       */
      public void flush()
         throws IOException
      {
         m_parent.flush();
      }

      /**
       * This method passes the call on to the original output stream.
       *
       * @throws IOException on write error
       */
      public void close()
         throws IOException
      {
         m_parent.close ();
      }
      
      private OutputStream m_parent;
      private int m_match = -1;
      private byte[] m_buffer;
      private int m_index;

      private byte[][] m_find =
      {
         "ns1:".getBytes(),
         ":ns1".getBytes(),
         ".000".getBytes(),
         "+??:??<".getBytes(),
         "-??:??<".getBytes(),
         "true<".getBytes(),
         "false<".getBytes(),
         ">0.0<".getBytes()
      };

      private String[] m_replace =
      {
         null,
         null,
         null,
         "<",
         "<",
         "1<",
         "0<",
         ">0<"
      };
   }
   
   private static final int TASK_FIELD_PREFIX = 1887;
   private static final int RESOURCE_FIELD_PREFIX = 2055;

   private static final int MSPDI_FIELD_ID = 0;
   private static final int MPX_FIELD_ID = 1;
   private static final int MSPDI_FIELD_NAME = 2;
   private static final int FIELD_DATA_TYPE = 3;

   private static final int STRING_ATTRIBUTE = 1;
   private static final int DATE_ATTRIBUTE = 2;
   private static final int CURRENCY_ATTRIBUTE = 3;   
   private static final int BOOLEAN_ATTRIBUTE = 4;
   private static final int NUMERIC_ATTRIBUTE = 5;
   private static final int DURATION_ATTRIBUTE = 6;

   private static final Integer STRING_ATTRIBUTE_OBJECT = new Integer (STRING_ATTRIBUTE);
   private static final Integer DATE_ATTRIBUTE_OBJECT = new Integer (DATE_ATTRIBUTE);
   private static final Integer CURRENCY_ATTRIBUTE_OBJECT = new Integer (CURRENCY_ATTRIBUTE);   
   private static final Integer BOOLEAN_ATTRIBUTE_OBJECT = new Integer (BOOLEAN_ATTRIBUTE);
   private static final Integer NUMERIC_ATTRIBUTE_OBJECT = new Integer (NUMERIC_ATTRIBUTE);
   private static final Integer DURATION_ATTRIBUTE_OBJECT = new Integer (DURATION_ATTRIBUTE);
   
   private static final Object[][] TASK_DATA =
   {
      {new Integer(188743731), new Integer(Task.TEXT1), "Text1", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743734), new Integer(Task.TEXT2), "Text2", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743737), new Integer(Task.TEXT3), "Text3", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743740), new Integer(Task.TEXT4), "Text4", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743743), new Integer(Task.TEXT5), "Text5", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743746), new Integer(Task.TEXT6), "Text6", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743747), new Integer(Task.TEXT7), "Text7", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743748), new Integer(Task.TEXT8), "Text8", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743749), new Integer(Task.TEXT9), "Text9", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743750), new Integer(Task.TEXT10), "Text10", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743997), new Integer(Task.TEXT11), "Text11", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743998), new Integer(Task.TEXT12), "Text12", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743999), new Integer(Task.TEXT13), "Text13", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744000), new Integer(Task.TEXT14), "Text14", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744001), new Integer(Task.TEXT15), "Text15", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744002), new Integer(Task.TEXT16), "Text16", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744003), new Integer(Task.TEXT17), "Text17", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744004), new Integer(Task.TEXT18), "Text18", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744005), new Integer(Task.TEXT19), "Text19", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744006), new Integer(Task.TEXT20), "Text20", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744007), new Integer(Task.TEXT21), "Text21", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744008), new Integer(Task.TEXT22), "Text22", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744009), new Integer(Task.TEXT23), "Text23", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744010), new Integer(Task.TEXT24), "Text24", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744011), new Integer(Task.TEXT25), "Text25", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744012), new Integer(Task.TEXT26), "Text26", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744013), new Integer(Task.TEXT27), "Text27", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744014), new Integer(Task.TEXT28), "Text28", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744015), new Integer(Task.TEXT29), "Text29", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744016), new Integer(Task.TEXT30), "Text30", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188743732), new Integer(Task.START1), "Start1", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743735), new Integer(Task.START2), "Start2", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743738), new Integer(Task.START3), "Start3", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743741), new Integer(Task.START4), "Start4", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743744), new Integer(Task.START5), "Start5", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743962), new Integer(Task.START6), "Start6", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743964), new Integer(Task.START7), "Start7", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743966), new Integer(Task.START8), "Start8", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743968), new Integer(Task.START9), "Start9", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743970), new Integer(Task.START10), "Start10", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743733), new Integer(Task.FINISH1), "Finish1", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743736), new Integer(Task.FINISH2), "Finish2", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743739), new Integer(Task.FINISH3), "Finish3", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743742), new Integer(Task.FINISH4), "Finish4", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743745), new Integer(Task.FINISH5), "Finish5", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743963), new Integer(Task.FINISH6), "Finish6", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743965), new Integer(Task.FINISH7), "Finish7", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743967), new Integer(Task.FINISH8), "Finish8", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743969), new Integer(Task.FINISH9), "Finish9", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743971), new Integer(Task.FINISH10), "Finish10", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743786), new Integer(Task.COST1), "Cost1", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743787), new Integer(Task.COST2), "Cost2", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743788), new Integer(Task.COST3), "Cost3", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743938), new Integer(Task.COST4), "Cost4", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743939), new Integer(Task.COST5), "Cost5", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743940), new Integer(Task.COST6), "Cost6", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743941), new Integer(Task.COST7), "Cost7", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743942), new Integer(Task.COST8), "Cost8", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743943), new Integer(Task.COST9), "Cost9", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743944), new Integer(Task.COST10), "Cost10", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(188743945), new Integer(Task.DATE1), "Date1", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743946), new Integer(Task.DATE2), "Date2", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743947), new Integer(Task.DATE3), "Date3", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743948), new Integer(Task.DATE4), "Date4", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743949), new Integer(Task.DATE5), "Date5", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743950), new Integer(Task.DATE6), "Date6", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743951), new Integer(Task.DATE7), "Date7", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743952), new Integer(Task.DATE8), "Date8", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743953), new Integer(Task.DATE9), "Date9", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743954), new Integer(Task.DATE10), "Date10", DATE_ATTRIBUTE_OBJECT},
      {new Integer(188743752), new Integer(Task.FLAG1), "Flag1", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743753), new Integer(Task.FLAG2), "Flag2", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743754), new Integer(Task.FLAG3), "Flag3", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743755), new Integer(Task.FLAG4), "Flag4", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743756), new Integer(Task.FLAG5), "Flag5", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743757), new Integer(Task.FLAG6), "Flag6", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743758), new Integer(Task.FLAG7), "Flag7", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743759), new Integer(Task.FLAG8), "Flag8", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743760), new Integer(Task.FLAG9), "Flag9", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743761), new Integer(Task.FLAG10), "Flag10", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743972), new Integer(Task.FLAG11), "Flag11", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743973), new Integer(Task.FLAG12), "Flag12", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743974), new Integer(Task.FLAG13), "Flag13", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743975), new Integer(Task.FLAG14), "Flag14", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743976), new Integer(Task.FLAG15), "Flag15", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743977), new Integer(Task.FLAG16), "Flag16", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743978), new Integer(Task.FLAG17), "Flag17", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743979), new Integer(Task.FLAG18), "Flag18", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743980), new Integer(Task.FLAG19), "Flag19", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743981), new Integer(Task.FLAG20), "Flag20", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(188743767), new Integer(Task.NUMBER1), "Number1", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743768), new Integer(Task.NUMBER2), "Number2", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743769), new Integer(Task.NUMBER3), "Number3", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743770), new Integer(Task.NUMBER4), "Number4", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743771), new Integer(Task.NUMBER5), "Number5", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743982), new Integer(Task.NUMBER6), "Number6", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743983), new Integer(Task.NUMBER7), "Number7", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743984), new Integer(Task.NUMBER8), "Number8", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743985), new Integer(Task.NUMBER9), "Number9", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743986), new Integer(Task.NUMBER10), "Number10", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743987), new Integer(Task.NUMBER11), "Number11", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743988), new Integer(Task.NUMBER12), "Number12", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743989), new Integer(Task.NUMBER13), "Number13", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743990), new Integer(Task.NUMBER14), "Number14", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743991), new Integer(Task.NUMBER15), "Number15", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743992), new Integer(Task.NUMBER16), "Number16", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743993), new Integer(Task.NUMBER17), "Number17", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743994), new Integer(Task.NUMBER18), "Number18", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743995), new Integer(Task.NUMBER19), "Number19", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743996), new Integer(Task.NUMBER20), "Number20", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(188743783), new Integer(Task.DURATION1), "Duration1", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743784), new Integer(Task.DURATION2), "Duration2", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743785), new Integer(Task.DURATION3), "Duration3", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743955), new Integer(Task.DURATION4), "Duration4", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743956), new Integer(Task.DURATION5), "Duration5", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743957), new Integer(Task.DURATION6), "Duration6", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743958), new Integer(Task.DURATION7), "Duration7", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743959), new Integer(Task.DURATION8), "Duration8", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743960), new Integer(Task.DURATION9), "Duration9", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188743961), new Integer(Task.DURATION10), "Duration10", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(188744096), new Integer(Task.OUTLINECODE1), "Outline Code1", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744098), new Integer(Task.OUTLINECODE2), "Outline Code2", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744100), new Integer(Task.OUTLINECODE3), "Outline Code3", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744102), new Integer(Task.OUTLINECODE4), "Outline Code4", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744104), new Integer(Task.OUTLINECODE5), "Outline Code5", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744106), new Integer(Task.OUTLINECODE6), "Outline Code6", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744108), new Integer(Task.OUTLINECODE7), "Outline Code7", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744110), new Integer(Task.OUTLINECODE8), "Outline Code8", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744112), new Integer(Task.OUTLINECODE9), "Outline Code9", STRING_ATTRIBUTE_OBJECT},
      {new Integer(188744114), new Integer(Task.OUTLINECODE10), "Outline Code10", STRING_ATTRIBUTE_OBJECT}      
   };

   private static final Object[][] RESOURCE_DATA =
   {
      {new Integer(205520904), new Integer(Resource.TEXT1), "Text1", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520905), new Integer(Resource.TEXT2), "Text2", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520926), new Integer(Resource.TEXT3), "Text3", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520927), new Integer(Resource.TEXT4), "Text4", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520928), new Integer(Resource.TEXT5), "Text5", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520993), new Integer(Resource.TEXT6), "Text6", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520994), new Integer(Resource.TEXT7), "Text7", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520995), new Integer(Resource.TEXT8), "Text8", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520996), new Integer(Resource.TEXT9), "Text9", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520997), new Integer(Resource.TEXT10), "Text10", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521121), new Integer(Resource.TEXT11), "Text11", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521122), new Integer(Resource.TEXT12), "Text12", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521123), new Integer(Resource.TEXT13), "Text13", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521124), new Integer(Resource.TEXT14), "Text14", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521125), new Integer(Resource.TEXT15), "Text15", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521126), new Integer(Resource.TEXT16), "Text16", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521127), new Integer(Resource.TEXT17), "Text17", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521128), new Integer(Resource.TEXT18), "Text18", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521129), new Integer(Resource.TEXT19), "Text19", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521130), new Integer(Resource.TEXT20), "Text20", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521131), new Integer(Resource.TEXT21), "Text21", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521132), new Integer(Resource.TEXT22), "Text22", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521133), new Integer(Resource.TEXT23), "Text23", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521134), new Integer(Resource.TEXT24), "Text24", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521135), new Integer(Resource.TEXT25), "Text25", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521136), new Integer(Resource.TEXT26), "Text26", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521137), new Integer(Resource.TEXT27), "Text27", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521138), new Integer(Resource.TEXT28), "Text28", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521139), new Integer(Resource.TEXT29), "Text29", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521140), new Integer(Resource.TEXT30), "Text30", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205520998), new Integer(Resource.START1), "Start1", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205520999), new Integer(Resource.START2), "Start2", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521000), new Integer(Resource.START3), "Start3", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521001), new Integer(Resource.START4), "Start4", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521002), new Integer(Resource.START5), "Start5", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521116), new Integer(Resource.START6), "Start6", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521117), new Integer(Resource.START7), "Start7", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521118), new Integer(Resource.START8), "Start8", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521119), new Integer(Resource.START9), "Start9", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521120), new Integer(Resource.START10), "Start10", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521003), new Integer(Resource.FINISH1), "Finish1", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521004), new Integer(Resource.FINISH2), "Finish2", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521005), new Integer(Resource.FINISH3), "Finish3", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521006), new Integer(Resource.FINISH4), "Finish4", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521007), new Integer(Resource.FINISH5), "Finish5", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521086), new Integer(Resource.FINISH6), "Finish6", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521087), new Integer(Resource.FINISH7), "Finish7", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521088), new Integer(Resource.FINISH8), "Finish8", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521089), new Integer(Resource.FINISH9), "Finish9", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521090), new Integer(Resource.FINISH10), "Finish10", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521019), new Integer(Resource.COST1), "Cost1", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521020), new Integer(Resource.COST2), "Cost2", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521021), new Integer(Resource.COST3), "Cost3", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521062), new Integer(Resource.COST4), "Cost4", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521063), new Integer(Resource.COST5), "Cost5", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521064), new Integer(Resource.COST6), "Cost6", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521065), new Integer(Resource.COST7), "Cost7", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521066), new Integer(Resource.COST8), "Cost8", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521067), new Integer(Resource.COST9), "Cost9", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521068), new Integer(Resource.COST10), "Cost10", CURRENCY_ATTRIBUTE_OBJECT},
      {new Integer(205521069), new Integer(Resource.DATE1), "Date1", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521070), new Integer(Resource.DATE2), "Date2", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521071), new Integer(Resource.DATE3), "Date3", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521072), new Integer(Resource.DATE4), "Date4", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521073), new Integer(Resource.DATE5), "Date5", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521074), new Integer(Resource.DATE6), "Date6", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521075), new Integer(Resource.DATE7), "Date7", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521076), new Integer(Resource.DATE8), "Date8", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521077), new Integer(Resource.DATE9), "Date9", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521078), new Integer(Resource.DATE10), "Date10", DATE_ATTRIBUTE_OBJECT},
      {new Integer(205521023), new Integer(Resource.FLAG1), "Flag1", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521024), new Integer(Resource.FLAG2), "Flag2", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521025), new Integer(Resource.FLAG3), "Flag3", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521026), new Integer(Resource.FLAG4), "Flag4", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521027), new Integer(Resource.FLAG5), "Flag5", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521028), new Integer(Resource.FLAG6), "Flag6", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521029), new Integer(Resource.FLAG7), "Flag7", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521030), new Integer(Resource.FLAG8), "Flag8", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521031), new Integer(Resource.FLAG9), "Flag9", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521022), new Integer(Resource.FLAG10), "Flag10", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521091), new Integer(Resource.FLAG11), "Flag11", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521092), new Integer(Resource.FLAG12), "Flag12", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521093), new Integer(Resource.FLAG13), "Flag13", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521094), new Integer(Resource.FLAG14), "Flag14", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521095), new Integer(Resource.FLAG15), "Flag15", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521096), new Integer(Resource.FLAG16), "Flag16", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521097), new Integer(Resource.FLAG17), "Flag17", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521098), new Integer(Resource.FLAG18), "Flag18", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521099), new Integer(Resource.FLAG19), "Flag19", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521100), new Integer(Resource.FLAG20), "Flag20", BOOLEAN_ATTRIBUTE_OBJECT},
      {new Integer(205521008), new Integer(Resource.NUMBER1), "Number1", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521009), new Integer(Resource.NUMBER2), "Number2", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521010), new Integer(Resource.NUMBER3), "Number3", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521011), new Integer(Resource.NUMBER4), "Number4", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521012), new Integer(Resource.NUMBER5), "Number5", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521101), new Integer(Resource.NUMBER6), "Number6", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521102), new Integer(Resource.NUMBER7), "Number7", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521103), new Integer(Resource.NUMBER8), "Number8", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521104), new Integer(Resource.NUMBER9), "Number9", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521105), new Integer(Resource.NUMBER10), "Number10", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521106), new Integer(Resource.NUMBER11), "Number11", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521107), new Integer(Resource.NUMBER12), "Number12", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521108), new Integer(Resource.NUMBER13), "Number13", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521109), new Integer(Resource.NUMBER14), "Number14", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521110), new Integer(Resource.NUMBER15), "Number15", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521111), new Integer(Resource.NUMBER16), "Number16", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521112), new Integer(Resource.NUMBER17), "Number17", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521113), new Integer(Resource.NUMBER18), "Number18", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521114), new Integer(Resource.NUMBER19), "Number19", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521115), new Integer(Resource.NUMBER20), "Number20", NUMERIC_ATTRIBUTE_OBJECT},
      {new Integer(205521013), new Integer(Resource.DURATION1), "Duration1", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521014), new Integer(Resource.DURATION2), "Duration2", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521015), new Integer(Resource.DURATION3), "Duration3", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521079), new Integer(Resource.DURATION4), "Duration4", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521080), new Integer(Resource.DURATION5), "Duration5", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521081), new Integer(Resource.DURATION6), "Duration6", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521082), new Integer(Resource.DURATION7), "Duration7", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521083), new Integer(Resource.DURATION8), "Duration8", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521084), new Integer(Resource.DURATION9), "Duration9", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521085), new Integer(Resource.DURATION10), "Duration10", DURATION_ATTRIBUTE_OBJECT},
      {new Integer(205521174), new Integer(Resource.OUTLINECODE1), "Outline Code1", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521176), new Integer(Resource.OUTLINECODE2), "Outline Code2", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521178), new Integer(Resource.OUTLINECODE3), "Outline Code3", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521180), new Integer(Resource.OUTLINECODE4), "Outline Code4", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521182), new Integer(Resource.OUTLINECODE5), "Outline Code5", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521184), new Integer(Resource.OUTLINECODE6), "Outline Code6", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521186), new Integer(Resource.OUTLINECODE7), "Outline Code7", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521188), new Integer(Resource.OUTLINECODE8), "Outline Code8", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521190), new Integer(Resource.OUTLINECODE9), "Outline Code9", STRING_ATTRIBUTE_OBJECT},
      {new Integer(205521192), new Integer(Resource.OUTLINECODE10), "Outline Code10", STRING_ATTRIBUTE_OBJECT}         
   };
   
   private static final HashMap TASK_FIELD_MPX_TO_NAME_MAP = new HashMap ();
   private static final HashMap TASK_FIELD_XML_TO_MPX_MAP = new HashMap();
   private static final HashMap TASK_FIELD_MPX_TO_XML_MAP = new HashMap();
   private static final HashMap TASK_FIELD_MPX_TO_TYPE_MAP = new HashMap ();
   
   private static final HashMap RESOURCE_FIELD_MPX_TO_NAME_MAP = new HashMap ();
   private static final HashMap RESOURCE_FIELD_XML_TO_MPX_MAP = new HashMap();
   private static final HashMap RESOURCE_FIELD_MPX_TO_XML_MAP = new HashMap();
   private static final HashMap RESOURCE_FIELD_MPX_TO_TYPE_MAP = new HashMap ();
   
   static
   {
      int loop;
      
      for (loop=0; loop < TASK_DATA.length; loop++)
      {
         TASK_FIELD_MPX_TO_NAME_MAP.put(TASK_DATA[loop][MPX_FIELD_ID], TASK_DATA[loop][MSPDI_FIELD_NAME]);
         TASK_FIELD_XML_TO_MPX_MAP.put(TASK_DATA[loop][MSPDI_FIELD_ID], TASK_DATA[loop][MPX_FIELD_ID]);   
         TASK_FIELD_MPX_TO_XML_MAP.put(TASK_DATA[loop][MPX_FIELD_ID], TASK_DATA[loop][MSPDI_FIELD_ID]);
         TASK_FIELD_MPX_TO_TYPE_MAP.put(TASK_DATA[loop][MPX_FIELD_ID], TASK_DATA[loop][FIELD_DATA_TYPE]);
      }
      
      for (loop=0; loop < RESOURCE_DATA.length; loop++)
      {
         RESOURCE_FIELD_MPX_TO_NAME_MAP.put(RESOURCE_DATA[loop][MPX_FIELD_ID], RESOURCE_DATA[loop][MSPDI_FIELD_NAME]);
         RESOURCE_FIELD_XML_TO_MPX_MAP.put(RESOURCE_DATA[loop][MSPDI_FIELD_ID], RESOURCE_DATA[loop][MPX_FIELD_ID]);   
         RESOURCE_FIELD_MPX_TO_XML_MAP.put(RESOURCE_DATA[loop][MPX_FIELD_ID], RESOURCE_DATA[loop][MSPDI_FIELD_ID]);
         RESOURCE_FIELD_MPX_TO_TYPE_MAP.put(RESOURCE_DATA[loop][MPX_FIELD_ID], RESOURCE_DATA[loop][FIELD_DATA_TYPE]);
      }
   }

   private static final String ZERO_DURATION = "PT0H0M0S";
   private static final BigDecimal BIGDECIMAL_ZERO = BigDecimal.valueOf(0);
   private static final BigInteger BIGINTEGER_ZERO = BigInteger.valueOf(0);
   private static final int MS_PER_MINUTE = 1000 * 60;   
}

