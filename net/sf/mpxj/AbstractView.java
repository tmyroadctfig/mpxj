/*
 * file:       AbstractView.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2003
 * date:       27/10/2003
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

package net.sf.mpxj;


/**
 * This abstract class implements functionality common to all views.
 */
public abstract class AbstractView implements View
{
   /**
    * {@inheritDoc}
    */
   public Integer getID ()
   {
      return (m_id);
   }

   /**
    * {@inheritDoc}
    */
   public String getName ()
   {
      return (m_name);
   }

   /**
    * {@inheritDoc}
    */   
   public ViewType getType ()
   {
      return (m_type);
   }
   
   /**
    * This method dumps the contents of this View as a String.
    * Note that this facility is provided as a debugging aid.
    *    
    * @return formatted contents of this view
    */   
   @Override public String toString ()
   {
      return ("[View id=" + m_id + " type=" + m_type + " name=" + m_name +"]");
   }

   protected Integer m_id;
   protected String m_name;
   protected ViewType m_type;
}
