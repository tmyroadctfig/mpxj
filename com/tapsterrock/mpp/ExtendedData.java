/*
 * file:       ExtendedData.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2002-2003
 * date:       23/05/2003
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
 
package com.tapsterrock.mpp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents the extended data structure which is used to 
 * hold additional non-core data items associated with tasks and resources.
 */
final class ExtendedData
{
   /**
    * Constructor. Given the var data block, and the offset of the extended
    * data block within the var data, the constructor extracts each data item
    * of extended data and inserts it into a Map using it's type as the key.
    * 
    * @param varData Var data block
    * @param offset Offset of extended data within the var data block
    */
   ExtendedData (FixDeferFix varData, int offset)
   {
      if (offset >= 0)
      {         
         m_data= varData;
         byte[] data = varData.getByteArray(offset);
         int index = 0;
         int size;
         int type;
         byte[] item;
            
         while (index < data.length)
         {
            size = MPPUtility.getInt(data, index);
            index += 4;
               
            type = MPPUtility.getInt(data, index);
            index += 4;
               
            m_map.put(new Integer (type), new ByteArray(data, index, size));
            index += size;
         }         
      }         
   }   

   /**
    * Retrieves a string value from the extended data
    * 
    * @param type Type identifier
    * @return string value
    */      
   public String getString (Integer type)
   {
      String result = null;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = m_data.getString(getOffset(item.byteArrayValue()));
      }
         
      return (result);
   }

   /**
    * Retrieves a string value from the extended data
    * 
    * @param type Type identifier
    * @return string value
    */      
   public String getUnicodeString (Integer type)
   {
      String result = null;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = m_data.getUnicodeString(getOffset(item.byteArrayValue()));
      }
         
      return (result);
   }

   /**
    * Retrieves a short int value from the extended data
    * 
    * @param type Type identifier
    * @return short int value
    */      
   public int getShort (Integer type)
   {
      int result = 0;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = MPPUtility.getShort(item.byteArrayValue());
      }
         
      return (result);         
   }

   /**
    * Retrieves an integer value from the extended data
    * 
    * @param type Type identifier
    * @return integer value
    */            
   public int getInt (Integer type)
   {
      int result = 0;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = MPPUtility.getInt(item.byteArrayValue());
      }
         
      return (result);         
   }

   /**
    * Retrieves a long value from the extended data
    * 
    * @param type Type identifier
    * @return long value
    */      
   public long getLong (Integer type)
   {
      long result = 0;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = MPPUtility.getLong6(item.byteArrayValue());
      }
         
      return (result);         
   }

   /**
    * Retrieves a double value from the extended data
    * 
    * @param type Type identifier
    * @return double value
    */      
   public double getDouble (Integer type)
   {
      double result = 0;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = MPPUtility.getDouble(item.byteArrayValue());
      }
         
      return (result);         
   }

   /**
    * Retrieves a timestamp from the extended data
    * 
    * @param type Type identifier
    * @return timestamp
    */      
   public Date getTimestamp (Integer type)
   {
      Date result = null;
         
      ByteArray item = (ByteArray)m_map.get (type);         
      if (item != null)
      {
         result = MPPUtility.getTimestamp(item.byteArrayValue());
      }
         
      return (result);         
   }

   /**
    * Convert an integer into an offset.
    * 
    * @param data four byte integer value
    * @return offset value
    */
   private int getOffset (byte[] data)
   {
      return (-1 - MPPUtility.getInt(data));      
   }      

   /**
    * Used for debugging. Outputs the contents of the extended data
    * block as a formatted string.
    */
   public String toString ()
   {
      StringWriter sw = new StringWriter ();
      PrintWriter pw = new PrintWriter (sw);
   
      pw.println ("BEGIN ExtendedData");
         
      Iterator iter = m_map.keySet().iterator();
      ByteArray item;
      Integer type;
      
      while (iter.hasNext() == true)
      {  
         type = (Integer)iter.next();
         item = (ByteArray)m_map.get(type);         
         pw.println ("Type: " + type + " Data:" + MPPUtility.hexdump(item.byteArrayValue(), false));   
      }
          
      pw.println ("END ExtendedData");
      pw.println ();
      pw.close();
      
      return (sw.toString());      
   }
        
   private FixDeferFix m_data;
   private HashMap m_map = new HashMap ();
}               