//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.02 at 09:18:51 PM BST 
//

package net.sf.mpxj.mspdi.schema;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings("all") public class Adapter10 extends XmlAdapter<String, Integer>
{

   public Integer unmarshal(String value)
   {
      return (net.sf.mpxj.mspdi.DatatypeConverter.parseResourceUID(value));
   }

   public String marshal(Integer value)
   {
      return (net.sf.mpxj.mspdi.DatatypeConverter.printResourceUID(value));
   }

}
