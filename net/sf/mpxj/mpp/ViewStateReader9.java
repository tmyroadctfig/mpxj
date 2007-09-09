/*
 * file:       ViewStateReader9.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2007
 * date:       Jan 07, 2007
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

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * This class allows the saved state of a view to be read from an MPP9 file.
 */
public final class ViewStateReader9 extends ViewStateReader
{
   /**
    * {@inheritDoc}
    */
   @Override protected Props getProps (Var2Data varData)
      throws IOException
   {
      Props props = null;
      byte[] propsData = varData.getByteArray(PROPS_ID, PROPS_TYPE);
      if (propsData != null)
      {
         props = new Props9(new ByteArrayInputStream(propsData));
      }
      return (props);
   }
   
   
   private static final Integer PROPS_ID = new Integer (1);
   private static final Integer PROPS_TYPE = new Integer (1);
}
