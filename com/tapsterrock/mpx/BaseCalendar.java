/*
 * file:       BaseCalendar.java
 * author:     Scott Melville
 *             Jon Iles
 * copyright:  (c) Tapster Rock Limited 2002-2003
 * date:       15/08/2002
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

package com.tapsterrock.mpx;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class represents the Base Calendar Definition record. It is used to
 * define the working and non-working days of the week. The default calendar
 * defined Monday to Friday as working days.
 *
 * @todo sort out individual day methods
 * @todo sort out numeric boolean values
 */
public class BaseCalendar extends MPXRecord
{
   /**
    * Default constructor.
    *
    * @param file the parent file to which this record belongs.
    */
   BaseCalendar (MPXFile file)
   {
      this (file, Record.EMPTY_RECORD);
   }

   /**
    * Constructor used to create an instance of this class from data
    * taken from an MPXFile record.
    *
    * @param file the MPXFile object to which this record belongs.
    * @param record record containing the data for this object.
    */
   BaseCalendar (MPXFile file, Record record)
   {
      super (file, MAX_FIELDS);

      setName(record.getString(0));
      setWorkingDay(1, record.getNumericBoolean(1));
      setWorkingDay(2, record.getNumericBoolean(2));
      setWorkingDay(3, record.getNumericBoolean(3));
      setWorkingDay(4, record.getNumericBoolean(4));
      setWorkingDay(5, record.getNumericBoolean(5));
      setWorkingDay(6, record.getNumericBoolean(6));
      setWorkingDay(7, record.getNumericBoolean(7));
   }

   /**
    * Used to add exceptions to the calendar. The MPX standard defines
    * a limit of 250 exceptions per calendar.
    *
    * @return <tt>BaseCalendarException</tt>
    * @throws MPXException if limit on number of exceptions is reached
    */
   public BaseCalendarException addBaseCalendarException ()
      throws MPXException
   {
      return (addBaseCalendarException (Record.EMPTY_RECORD));
   }

   /**
    * Used to add exceptions to the calendar. The MPX standard defines
    * a limit of 250 exceptions per calendar.
    *
    * @param record data from the MPX file for this object.
    * @return <tt>BaseCalendarException</tt>
    * @throws MPXException if limit on number of exceptions is reached
    */
   BaseCalendarException addBaseCalendarException (Record record)
      throws MPXException
   {
      if (m_exceptions.size() == MAX_EXCEPTIONS)
      {
         throw new MPXException (MPXException.MAXIMUM_RECORDS);
      }

      BaseCalendarException bce = new BaseCalendarException(getParentFile(), record);
      m_exceptions.add(bce);
      return (bce);
   }

   /**
    * This method retrieves a list of exceptions to the current
    * base calendar.
    *
    * @return List of base calendar exceptions
    */
   public LinkedList getBaseCalendarExceptions ()
   {
      return (m_exceptions);
   }

   /**
    * Used to add working hours to the calendar. Note that the MPX file
    * definitiona allows a maximum of 7 calendar hours records to be added to
    * a single calendar.
    *
    * @return <tt>BaseCalendarHours</tt>
    * @throws MPXException if maximum number of records is exceeded
    */
   public BaseCalendarHours addBaseCalendarHours(int day)
      throws MPXException
   {
      BaseCalendarHours bch = new BaseCalendarHours (getParentFile(), Record.EMPTY_RECORD);

      bch.setDay (day);
      --day;

      if (day < 0 || day > m_hours.length)
      {
         throw new MPXException (MPXException.MAXIMUM_RECORDS);
      }

      m_hours[day] = bch;

      return (bch);
   }

   /**
    * Used to add working hours to the calendar. Note that the MPX file
    * definitiona allows a maximum of 7 calendar hours records to be added to
    * a single calendar.
    *
    * @param record data from the MPX file for this object.
    * @return <tt>BaseCalendarHours</tt>
    * @throws MPXException if maximum number of records is exceeded
    */
   BaseCalendarHours addBaseCalendarHours (Record record)
      throws MPXException
   {
      BaseCalendarHours bch = new BaseCalendarHours(getParentFile(), record);
      int day = bch.getDayValue()-1;

      if (day < 0 || day > m_hours.length)
      {
         throw new MPXException (MPXException.MAXIMUM_RECORDS);
      }

      m_hours[day] = bch;

      return (bch);
   }

   /**
    * This method retrieves the base calendar hours for the specified day.
    *
    * @param day Day number
    * @return Base calendar hours
    */
   public BaseCalendarHours getBaseCalendarHours (int day)
   {
      return (m_hours[day-1]);
   }


   /**
    * Calendar name
    *
    * @return - name of calendar
    */
   public String getName()
   {
      return (String)get(NAME);
   }

   /**
    * Calendar name
    *
    * @param val - string calendar name
    */
   public void setName(String val)
   {
      put (NAME, val);
   }

   /**
    * This method generates a string in MPX format representing the
    * contents of this record.
    *
    * @return string containing the data for this record in MPX format.
    */
   public String toString ()
   {
      StringBuffer buf = new StringBuffer(super.toString(RECORD_NUMBER));

      for (int loop=0; loop < m_hours.length; loop++)
      {
         if (m_hours[loop] != null)
         {
            buf.append (m_hours[loop].toString());
         }
      }

      if (m_exceptions.isEmpty() == false)
      {
         Iterator iter = m_exceptions.iterator();
         while (iter.hasNext() == true)
         {
            buf.append((iter.next()).toString());
         }
      }

      return (buf.toString());
   }

   /**
    * Method indicating whether a day is a working or non-working day.
    *
    * @param day number of required day (1=Sunday, 7=Saturday)
    * @return true if this is a working day
    */
   public boolean isWorkingDay (int day)
   {
      return (getNumericBooleanValue(day));
   }

   /**
    * This is a convenience method provided to allow a day to be set
    * as working or non-working, by using the day number to
    * identify the required day.
    *
    * @param day number of required day (1=Sunday, 7=Saturday)
    * @param working flag indicating if the day is a working day
    */
   public void setWorkingDay (int day, boolean working)
   {
      put (day, NumericBoolean.getInstance(working));
   }

   /**
    * This is a convenience method provided to allow a day to be set
    * as working or non-working, by using the day number to
    * identify the required day.
    *
    * @param day number of required day (1=Sunday, 7=Saturday)
    * @param working flag indicating if the day is a working day
    */
   public void setWorkingDay (int day, NumericBoolean working)
   {
      put (day, working);
   }

   /**
    * This is a convenience method used to add a default set of calendar
    * hours to a calendar.
    *
    * @throws MPXException normally thrown on parse errors
    */
   public void addDefaultBaseCalendarHours ()
      throws MPXException
   {
      try
      {
         BaseCalendarHours hours;
         SimpleDateFormat df = new SimpleDateFormat ("HH:mm");
         df.setTimeZone(TimeZone.getTimeZone("GMT"));
         Date from1 = df.parse ("08:00");
         Date to1 = df.parse ("12:00");
         Date from2 = df.parse ("13:00");
         Date to2 = df.parse ("17:00");

         hours = addBaseCalendarHours (BaseCalendarHours.SUNDAY);

         hours = addBaseCalendarHours (BaseCalendarHours.MONDAY);
         hours.setFromTime1 (from1);
         hours.setToTime1 (to1);
         hours.setFromTime2 (from2);
         hours.setToTime2 (to2);

         hours = addBaseCalendarHours (BaseCalendarHours.TUESDAY);
         hours.setFromTime1 (from1);
         hours.setToTime1 (to1);
         hours.setFromTime2 (from2);
         hours.setToTime2 (to2);

         hours = addBaseCalendarHours (BaseCalendarHours.WEDNESDAY);
         hours.setFromTime1 (from1);
         hours.setToTime1 (to1);
         hours.setFromTime2 (from2);
         hours.setToTime2 (to2);

         hours = addBaseCalendarHours (BaseCalendarHours.THURSDAY);
         hours.setFromTime1 (from1);
         hours.setToTime1 (to1);
         hours.setFromTime2 (from2);
         hours.setToTime2 (to2);

         hours = addBaseCalendarHours (BaseCalendarHours.FRIDAY);
         hours.setFromTime1 (from1);
         hours.setToTime1 (to1);
         hours.setFromTime2 (from2);
         hours.setToTime2 (to2);

         hours = addBaseCalendarHours (BaseCalendarHours.SATURDAY);
      }

      catch (ParseException ex)
      {
         throw new MPXException (MPXException.INVALID_TIME, ex);
      }
   }

   /**
    * This method is provided to allow an absolute period of time
    * represented by start and end dates into a duration in working
    * days based on this calendar instance.
    *
    * @param startDate start of the period
    * @param endDate end of the period
    * @return new MPXDuration object
    * @throws MPXException if an invalid day is specified
    */
   public MPXDuration getDuration (Date startDate, Date endDate)
      throws MPXException
   {
      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      int day = cal.get(Calendar.DAY_OF_WEEK);
      int days = getDaysInRange (startDate, endDate);
      int duration = 0;

      while (days > 0)
      {
         if (isWorkingDay(day) == true)
         {
            ++duration;
         }

         --days;

         ++day;
         if (day > 7)
         {
            day = 1;
         }
      }

      return (new MPXDuration (duration, TimeUnit.DAYS));
   }


   /**
    * This method calculates the absolute number of days between two dates.
    * Note that where two date objects are provided that fall on the same
    * day, this method will return one not zero.
    *
    * @param startDate Start date
    * @param endDate End date
    * @return number of days in the date range
    */
   private int getDaysInRange (Date startDate, Date endDate)
   {
      long start = startDate.getTime() / MS_PER_DAY;
      long end = endDate.getTime() / MS_PER_DAY;
      long diff = end - start;
      if (diff < 0)
      {
         diff = -diff;
      }

      return ((int)(diff + 1));
   }

   /**
    * Listof exceptions to the base calendar.
    */
   private LinkedList m_exceptions = new LinkedList();

   /**
    * List of working hours for the base calendar.
    */
   private BaseCalendarHours[] m_hours = new BaseCalendarHours[7];

   /**
    * Constant representing the number of milliseconds in a day.
    */
   private static final long MS_PER_DAY = (long)(1000 * 60 * 60 * 24);

   /**
    * Constant used to retrieve the name of the calendar
    */
   private static final int NAME = 0;

   /**
    * Maximum number of fields in this record.
    */
   private static final int MAX_FIELDS = 8;

   /**
    * Constant representing maximum number of BaseCalendarException records
    * per BaseCalendar.
    */
   static final int MAX_EXCEPTIONS = 250;

   /**
    * Constant containing the record number associated with this record.
    */
   static final int RECORD_NUMBER = 20;
}