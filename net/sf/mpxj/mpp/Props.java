/*
 * file:       Props.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software Limited 2003
 * date:       27/05/2003
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

package net.sf.mpxj.mpp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class represents the common structure of Props files found in
 * Microsoft Project MPP files. The MPP8 and MPP9 file formats both
 * implement Props files slightly differently, so this class contains
 * the shared implementation detail, with specific implementations for
 * MPP8 and MPP9 Props files found in the Props8 and Props9 classes.
 */
class Props extends MPPComponent
{
   /**
    * Retrieve property data as a byte array.
    *
    * @param type Type identifier
    * @return  byte array of data
    */
   public byte[] getByteArray (Integer type)
   {
      return (m_map.get (type));
   }

   /**
    * Retrieves a byte value from the property data.
    *
    * @param type Type identifier
    * @return byte value
    */
   public byte getByte (Integer type)
   {
      byte result = 0;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = item[0];
      }

      return (result);
   }

   /**
    * Retrieves a short int value from the property data.
    *
    * @param type Type identifier
    * @return short int value
    */
   public int getShort (Integer type)
   {
      int result = 0;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getShort(item);
      }

      return (result);
   }

   /**
    * Retrieves an integer value from the property data.
    *
    * @param type Type identifier
    * @return integer value
    */
   public int getInt (Integer type)
   {
      int result = 0;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getInt(item);
      }

      return (result);
   }

   /**
    * Retrieves a double value from the property data.
    *
    * @param type Type identifier
    * @return double value
    */
   public double getDouble (Integer type)
   {
      double result = 0;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getDouble(item);
      }

      return (result);
   }

   /**
    * Retrieves a timestamp from the property data.
    *
    * @param type Type identifier
    * @return timestamp
    */
   public Date getTime (Integer type)
   {
      Date result = null;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getTime(item);
      }

      return (result);
   }

   /**
    * Retrieves a timestamp from the property data.
    *
    * @param type Type identifier
    * @return timestamp
    */   
   public Date getTimestamp (Integer type)
   {
      Date result = null;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getTimestamp(item);
      }

      return (result);
   }
   
   /**
    * Retrieves a boolean value from the property data.
    *
    * @param type Type identifier
    * @return boolean value
    */
   public boolean getBoolean (Integer type)
   {
      boolean result = false;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = !(MPPUtility.getShort(item) == 0);
      }

      return (result);
   }

   /**
    * Retrieves a string value from the property data.
    *
    * @param type Type identifier
    * @return string value
    */
   public String getUnicodeString (Integer type)
   {
      String result = null;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getUnicodeString(item);
      }

      return (result);
   }

   /**
    * Retrieves a date value from the property data.
    *
    * @param type Type identifier
    * @return string value
    */   
   public Date getDate (Integer type)
   {
      Date result = null;

      byte[] item = m_map.get (type);
      if (item != null)
      {
         result = MPPUtility.getDate(item, 0);
      }

      return (result);
   }

   /**
    * Retrieve the set of keys represented by this instance.
    * 
    * @return key set
    */
   public Set<Integer> keySet()
   {
      return (m_map.keySet());
   }
   

   /**
    * This method dumps the contents of this properties block as a String.
    * Note that this facility is provided as a debugging aid.
    *
    * @return formatted contents of this block
    */
   @Override public String toString ()
   {
      StringWriter sw = new StringWriter ();
      PrintWriter pw = new PrintWriter (sw);

      pw.println ("BEGIN Props");

      for (Integer key : m_map.keySet())
      {
         pw.println ("   Key: " + key + " Value: ");
         pw.println (MPPUtility.hexdump(m_map.get(key), true, 16, "      "));
      }

      pw.println ("END Props");

      pw.println ();
      pw.close();
      return (sw.toString());
   }
   
   
   /**
    * Data types.
    */
   public static final Integer PROJECT_START_DATE = new Integer(37748738);
   public static final Integer PROJECT_FINISH_DATE = new Integer(37748739);
   public static final Integer SCHEDULE_FROM = new Integer (37748740);
   public static final Integer DEFAULT_CALENDAR_NAME = new Integer(37748750);
   public static final Integer CURRENCY_SYMBOL = new Integer (37748752);
   public static final Integer CURRENCY_PLACEMENT = new Integer (37748753);
   public static final Integer CURRENCY_DIGITS = new Integer (37748754);
  
   
   public static final Integer DURATION_UNITS = new Integer (37748757);
   public static final Integer WORK_UNITS = new Integer (37748758);
   public static final Integer TASK_UPDATES_RESOURCE = new Integer (37748761);
   public static final Integer SPLIT_TASKS = new Integer (37748762);
   public static final Integer START_TIME = new Integer (37748764);
   public static final Integer MINUTES_PER_DAY = new Integer (37748765);
   public static final Integer MINUTES_PER_WEEK = new Integer (37748766);
   public static final Integer STANDARD_RATE = new Integer (37748767);
   public static final Integer OVERTIME_RATE = new Integer (37748768);
   public static final Integer END_TIME = new Integer (37748769);

   public static final Integer CURRENCY_CODE = new Integer (37753787);
   
   public static final Integer CALCULATE_MULTIPLE_CRITICAL_PATHS = new Integer (37748793);

   public static final Integer TASK_FIELD_NAME_ALIASES = new Integer (1048577);
   public static final Integer RESOURCE_FIELD_NAME_ALIASES = new Integer (1048578);

   public static final Integer TASK_FIELD_CUSTOM_VALUE_LISTS = new Integer (37753744);   

   public static final Integer PASSWORD_FLAG = new Integer (893386752);
   
   public static final Integer PROTECTION_PASSWORD_HASH = new Integer (893386756);
   
   public static final Integer WRITE_RESERVATION_PASSWORD_HASH = new Integer (893386757);
   
   public static final Integer ENCRYPTION_CODE = new Integer (893386759);
   
   public static final Integer STATUS_DATE = new Integer(37748805);
   
   public static final Integer SUBPROJECT_COUNT = new Integer(37748868);
   public static final Integer SUBPROJECT_DATA = new Integer(37748898);
   public static final Integer SUBPROJECT_TASK_COUNT = new Integer(37748900);

   public static final Integer GRAPHICAL_INDICATOR_DATA = new Integer(37753744);
   
   public static final Integer FONT_BASES = new Integer (54525952);

   public static final Integer AUTO_FILTER = new Integer (893386767);
   
   public static final Integer PROJECT_FILE_PATH = new Integer (893386760);
   
   public static final Integer HYPERLINK_BASE = new Integer (37748810);
   
   protected TreeMap<Integer, byte[]> m_map = new TreeMap<Integer, byte[]> ();
}
