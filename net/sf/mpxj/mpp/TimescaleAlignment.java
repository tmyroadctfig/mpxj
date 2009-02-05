/*
 * file:       TimescaleAlignment.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2005
 * date:       Apr 7, 2005
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

import java.util.EnumSet;

import net.sf.mpxj.utility.MpxjEnum;
import net.sf.mpxj.utility.NumberUtility;

/**
 * Class representing the label alignment on a Gantt chart timescale.
 */
public enum TimescaleAlignment implements MpxjEnum
{
   LEFT(0, "Left"),
   CENTER(1, "Center"),
   RIGHT(2, "Right");

   /**
    * Private constructor.
    * 
    * @param type int version of the enum
    * @param name enum name
    */
   private TimescaleAlignment(int type, String name)
   {
      m_value = type;
      m_name = name;
   }

   /**
    * Retrieve an instance of the enum based on its int value.
    *
    * @param type int type
    * @return enum instance
    */
   public static TimescaleAlignment getInstance(int type)
   {
      if (type < 0 || type >= TYPE_VALUES.length)
      {
         type = CENTER.getValue();
      }
      return (TYPE_VALUES[type]);
   }

   /**
    * Retrieve an instance of the enum based on its int value.
    *
    * @param type int type
    * @return enum instance
    */
   public static TimescaleAlignment getInstance(Number type)
   {
      int value;
      if (type == null)
      {
         value = -1;
      }
      else
      {
         value = NumberUtility.getInt(type);
      }
      return (getInstance(value));
   }

   /**
    * Accessor method used to retrieve the numeric representation of the enum. 
    *
    * @return int representation of the enum
    */
   public int getValue()
   {
      return (m_value);
   }

   /**
    * Retrieve the name of this alignment. Note that this is not
    * localised.
    *
    * @return name of this alignment type
    */
   public String getName()
   {
      return (m_name);
   }

   /**
    * Generate a string representation of this instance.
    *
    * @return string representation of this instance
    */
   @Override public String toString()
   {
      return (getName());
   }

   /**
    * Array mapping int types to enums.
    */
   private static final TimescaleAlignment[] TYPE_VALUES = new TimescaleAlignment[3];
   static
   {
      for (TimescaleAlignment e : EnumSet.range(TimescaleAlignment.LEFT, TimescaleAlignment.RIGHT))
      {
         TYPE_VALUES[e.getValue()] = e;
      }
   }

   /**
    * Internal representation of the enum int type.
    */
   private int m_value;
   private String m_name;
}
