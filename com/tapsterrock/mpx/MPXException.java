/*
 * file:       MPXException.java
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

/**
 * Standard exception type thrown by the MPXJ library.
 */
public class MPXException extends Exception
{
  /**
   * Default constructor.
   */
   public MPXException ()
   {
      super ();
   }

   /**
    * Constructor allowing a message to be added to this exception.
    *
    * @param message message
    */
   public MPXException (String message)
   {
      super (message);
   }

   /**
    * Constructor for an exception containing a message and an embedded
    * exception.
    *
    * @param message message
    * @param exception original exception
    */
   public MPXException (String message, Exception exception)
   {
      super (message);
      m_exception = exception;
   }

   /**
    * Returns the embedded exception
    *
    * @return exception
    */
   public Exception getException ()
   {
      return (m_exception);
   }

   /**
    * Embedded exception
    */
   private Exception m_exception;

   /**
    * Maximum records error message.
    */
   public static final String MAXIMUM_RECORDS = "Maximum number of records of this type exist";

   /**
    * Invalid time unit error message.
    */
   public static final String INVALID_TIME_UNIT = "Invalid time unit";

   /**
    * Invalid time error message.
    */
   public static final String INVALID_TIME = "Invalid time";

   /**
    * Invalid date error message.
    */
   public static final String INVALID_DATE = "Invalid date";

   /**
    * Invalid number error message.
    */
   public static final String INVALID_NUMBER = "Invalid number";

   /**
    * Invalid duration error message.
    */
   public static final String INVALID_DURATION = "Invalid duration";

   /**
    * Invalid file error message.
    */
   public static final String INVALID_FILE = "Invalid file format";

   /**
    * Invalid record error message.
    */
   public static final String INVALID_RECORD = "Invalid record";

   /**
    * Read error message.
    */
   public static final String READ_ERROR = "Error reading file";

}
