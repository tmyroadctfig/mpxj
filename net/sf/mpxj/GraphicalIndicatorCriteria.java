/*
 * file:       GraphicalIndicatorCriteria.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2006
 * date:       15/02/2006
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
 * This class represents the criteria used to determine if a graphical
 * indicator is displayed in place of an attribute value.
 */
public final class GraphicalIndicatorCriteria extends GenericCriteria
{
   /**
    * Constructor.
    * 
    * @param projectFile parent project file
    */
   public GraphicalIndicatorCriteria(ProjectFile projectFile)
   {
      super(projectFile);
   }

   /**
    * Retrieve the number of the indicator to be displayed.
    * 
    * @return indicator number
    */
   public int getIndicator()
   {
      return m_indicator;
   }

   /**
    * Set the number of the indicator to be displayed.
    * 
    * @param indicator indicator number
    */
   public void setIndicator(int indicator)
   {
      m_indicator = indicator;
   }

   /**
    * Evaluate this criteria to determine if a graphical indicator should
    * be displayed. This method will return -1 if no indicator should
    * be displayed, or it will return a positive integer identifying the
    * required indicator.
    * 
    * @param container field container
    * @return boolean flag
    */
   public int evaluate(FieldContainer container)
   {
      return (evaluateCriteria(container) ? m_indicator : -1);
   }

   /**
    * {@inheritDoc}
    */
   @Override public String toString()
   {
      StringBuffer sb = new StringBuffer();
      sb.append("[GraphicalIndicatorCriteria indicator=");
      sb.append(m_indicator);
      sb.append(" criteria=");
      sb.append(super.toString());
      sb.append("]");
      return (sb.toString());
   }

   private int m_indicator;
}
