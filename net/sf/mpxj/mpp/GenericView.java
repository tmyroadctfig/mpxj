/*
 * file:       GenericView.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software Limited 2003
 * date:       06/04/2005
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

import net.sf.mpxj.ViewType;

/**
 * This class represents a view of a set of project data that has been
 * instantiated within an MS Project file. View data is instantiated when a user
 * first looks at a view in MS Project. Each "screen" in MS Project, for example
 * the Gantt Chart, the Resource Sheet and so on are views. If a user has not
 * looked at a view (for example the Resource Usage view), information about
 * that view will not be present in the MPP file.
 */
public class GenericView extends AbstractMppView
{
   /**
    * Extract the view data from the view data block.
    *
    * @param data view data
    */
   public GenericView(byte[] data)
   {
      m_id = Integer.valueOf(MPPUtility.getInt(data, 0));
      m_name = removeAmpersand(MPPUtility.getUnicodeString(data, 4));
      m_type = ViewType.getInstance(MPPUtility.getShort(data, 112));
   }
}
