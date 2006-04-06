/*
 * file:       DateUtility.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2005
 * date:       Jan 18, 2006
 */

package net.sf.mpxj.utility;

import java.util.Calendar;
import java.util.Date;

import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;

/**
 * Utility methods for manipulating dates.
 */
public final class DateUtility
{
   /**
    * Constructor.
    */
   private DateUtility ()
   {
      // private constructor to prevent instantiation
   }

   /**
    * Returns a new Date instance whose value
    * represents the start of the day (i.e. the time of day is 00:00:00.000)
    *
    * @param date date to convert
    * @return day start date
    */
   public static Date getDayStartDate (Date date)
   {
      if (date != null)
      {
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.SECOND, 0);
         cal.set(Calendar.MILLISECOND, 0);
         date = cal.getTime();
      }
      return (date);
   }

   /**
    * Returns a new Date instance whose value
    * represents the end of the day (i.e. the time of days is 11:59:59.999)
    *
    * @param date date to convert
    * @return day start date
    */
   public static Date getDayEndDate (Date date)
   {
      if (date != null)
      {
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         cal.set(Calendar.MILLISECOND, 999);
         cal.set(Calendar.SECOND, 59);
         cal.set(Calendar.MINUTE, 59);
         cal.set(Calendar.HOUR_OF_DAY, 23);
         date = cal.getTime();
      }
      return (date);
   }
   
   /**
    * This method resets the date part of a date time value to
    * a standard date (1/1/1). This is used to allow times to
    * be compared and manipulated.
    * 
    * @param date date time value
    * @return date time with date set to a standard value
    */
   public static Date getCanonicalTime (Date date)
   {
      if (date != null)
      {
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         cal.set(Calendar.DAY_OF_YEAR, 1);
         cal.set(Calendar.YEAR, 1);
         date = cal.getTime();
      }
      return (date);
   }
   
   /**
    * This method compares a target date with a date range. The method will
    * return 0 if the date is within the range, less than zero if the date
    * is before the range starts, and greater than zero if the date is after
    * the range ends.
    * 
    * @param startDate range start date
    * @param endDate range end date
    * @param targetDate target date
    * @return comparison result
    */
   public static int compare (Date startDate, Date endDate, Date targetDate)
   {
      return (compare(startDate, endDate, targetDate.getTime()));
   }

   /**
    * This method compares a target date with a date range. The method will
    * return 0 if the date is within the range, less than zero if the date
    * is before the range starts, and greater than zero if the date is after
    * the range ends.
    * 
    * @param startDate range start date
    * @param endDate range end date
    * @param targetDate target date in milliseconds
    * @return comparison result
    */   
   public static int compare (Date startDate, Date endDate, long targetDate)
   {
      int result = 0;
      if (targetDate < startDate.getTime())
      {
         result = -1;
      }
      else
      {
         if (targetDate > endDate.getTime())
         {
            result = 1;
         }
      }
      return (result);
   }

   /**
    * This utility method calculates the difference in working
    * time between two dates, given the context of a task.
    * 
    * @param task parent task
    * @param date1 first date
    * @param date2 second date
    * @param format required format for the resulting duration
    * @return difference in working time between the two dates
    */
   public static Duration getVariance (Task task, Date date1, Date date2, TimeUnit format)
   {
      Duration variance = null;
      
      if (date1 != null & date2 != null)
      {
         ProjectCalendar calendar = task.getCalendar();
         if (calendar == null)
         {
            ProjectFile file = task.getParentFile();
            calendar = file.getBaseCalendar(file.getProjectHeader().getCalendarName());
         }
         
         if (calendar != null)
         {
            variance = calendar.getWork(date1, date2, format);
         }         
      }
      
      if (variance == null)
      {
         variance = Duration.getInstance(0, format);
      }
      
      return (variance);
   }   
}
