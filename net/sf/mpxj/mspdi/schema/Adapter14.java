//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.10 at 06:58:28 AM BST 
//

package net.sf.mpxj.mspdi.schema;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import net.sf.mpxj.BookingType;

@SuppressWarnings("all") public class Adapter14 extends XmlAdapter<String, BookingType>
{

   public BookingType unmarshal(String value)
   {
      return (net.sf.mpxj.mspdi.DatatypeConverter.parseBookingType(value));
   }

   public String marshal(BookingType value)
   {
      return (net.sf.mpxj.mspdi.DatatypeConverter.printBookingType(value));
   }

}
