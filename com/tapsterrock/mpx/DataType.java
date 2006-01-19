/*
 * file:       DataType.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2005
 * date:       Jan 18, 2006
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

/**
 * This class represents the data type of an attribute.
 */
public final class DataType
{
   /**
    * This constructor takes the numeric enumerated representation of a
    * data type and populates the class instance appropriately.
    *
    * @param type int version of the data type
    */
   private DataType (int type)
   {
      m_type = type;
   }

   /**
    * Accessor method used to retrieve the numeric representation of the
    * data type.
    *
    * @return int representation of the data type
    */
   public int getType ()
   {
      return (m_type);
   }

   public static final int DATE_VALUE = 1;
   public static final int CURRENCY_VALUE = 2;
   public static final int UNITS_VALUE = 3;
   public static final int PERCENTAGE_VALUE = 4;
   public static final int ACCRUE_VALUE = 5;
   public static final int CONSTRAINT_VALUE = 6;
   public static final int DURATION_VALUE = 7;
   public static final int RATE_VALUE = 8;
   public static final int PRIORITY_VALUE = 9;
   public static final int RELATION_LIST_VALUE = 10;
   
   public static final DataType DATE = new DataType(DATE_VALUE);
   public static final DataType CURRENCY = new DataType(CURRENCY_VALUE);
   public static final DataType UNITS = new DataType(UNITS_VALUE);
   public static final DataType PERCENTAGE = new DataType(PERCENTAGE_VALUE);
   public static final DataType ACCRUE = new DataType(ACCRUE_VALUE);
   public static final DataType CONSTRAINT = new DataType(CONSTRAINT_VALUE);
   public static final DataType DURATION = new DataType(DURATION_VALUE);
   public static final DataType RATE = new DataType(RATE_VALUE);
   public static final DataType PRIORITY = new DataType(PRIORITY_VALUE);
   public static final DataType RELATION_LIST = new DataType(RELATION_LIST_VALUE);
   
   /**
    * Internal representation of the data type.
    */
   private int m_type;
}
