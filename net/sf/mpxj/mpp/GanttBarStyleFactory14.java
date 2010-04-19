/*
 * file:       GanttarStyleFactory14.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2010
 * date:       19/04/2010
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

import net.sf.mpxj.MPPTaskField14;

/**
 * Reads Gantt bar styles from an MPP14 file.
 */
public class GanttBarStyleFactory14 implements GanttBarStyleFactory
{
   /**
    * {@inheritDoc}
    */
   public GanttBarStyle[] processDefaultStyles(Props props)
   {
      GanttBarStyle[] barStyles = null;
      byte[] barStyleData = props.getByteArray(PROPERTIES);
      if (barStyleData != null)
      {
         barStyles = new GanttBarStyle[MPPUtility.getShort(barStyleData, 2245)];

         int styleOffset = 2255;

         for (int loop = 0; loop < barStyles.length; loop++)
         {
            GanttBarStyle style = new GanttBarStyle();
            barStyles[loop] = style;

            style.setName(MPPUtility.getUnicodeString(barStyleData, styleOffset + 91));

            style.setLeftText(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 67)));
            style.setRightText(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 71)));
            style.setTopText(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 75)));
            style.setBottomText(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 79)));
            style.setInsideText(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 83)));

            style.setStartShape(GanttBarStartEndShape.getInstance(barStyleData[styleOffset + 15] % 25));
            style.setStartType(GanttBarStartEndType.getInstance(barStyleData[styleOffset + 15] / 25));
            style.setStartColor(MPPUtility.getColor(barStyleData, styleOffset + 16));

            style.setMiddleShape(GanttBarMiddleShape.getInstance(barStyleData[styleOffset]));
            style.setMiddlePattern(GanttBarMiddlePattern.getInstance(barStyleData[styleOffset + 1]));
            style.setMiddleColor(MPPUtility.getColor(barStyleData, styleOffset + 2));

            style.setEndShape(GanttBarStartEndShape.getInstance(barStyleData[styleOffset + 28] % 25));
            style.setEndType(GanttBarStartEndType.getInstance(barStyleData[styleOffset + 28] / 25));
            style.setEndColor(MPPUtility.getColor(barStyleData, styleOffset + 29));

            style.setFromField(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 41)));
            style.setToField(MPPTaskField14.getInstance(MPPUtility.getShort(barStyleData, styleOffset + 45)));

            extractFlags(style, GanttBarShowForTasks.NORMAL, MPPUtility.getLong(barStyleData, styleOffset + 49));
            extractFlags(style, GanttBarShowForTasks.NOT_NORMAL, MPPUtility.getLong(barStyleData, styleOffset + 57));

            style.setRow((MPPUtility.getShort(barStyleData, styleOffset + 65) + 1));

            styleOffset += 195;
         }

      }
      return barStyles;
   }

   /**
    * Extract the flags indicating which task types this bar style
    * is relevant for. Note that this work for the "normal" task types
    * and the "negated" task types (e.g. Normal Task, Not Normal task). 
    * The set of values used is determined by the baseCriteria argument.
    * 
    * @param style parent bar style
    * @param baseCriteria determines if the normal or negatated enums are used
    * @param flagValue flag data
    */
   private void extractFlags(GanttBarStyle style, GanttBarShowForTasks baseCriteria, long flagValue)
   {
      int index = 0;
      long flag = 0x0001;

      while (index < 64)
      {
         if ((flagValue & flag) != 0)
         {
            GanttBarShowForTasks enumValue = GanttBarShowForTasks.getInstance(baseCriteria.getValue() + index);

            style.addShowForTasks(enumValue);
         }

         flag = flag << 1;

         index++;
      }
   }

   private static final Integer PROPERTIES = Integer.valueOf(574619656);
}
