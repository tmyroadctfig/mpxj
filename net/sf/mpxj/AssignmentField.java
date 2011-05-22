/*
 * file:       AssignmentField.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2011
 * date:       14/04/2011
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

import java.util.EnumSet;
import java.util.Locale;

/**
 * Instances of this type represent Assignment fields.
 */
public enum AssignmentField implements FieldType
{
   START(DataType.DATE), // Must always be first value

   ACTUAL_COST(DataType.CURRENCY),
   ACTUAL_WORK(DataType.WORK),
   COST(DataType.CURRENCY),
   ASSIGNMENT_DELAY(DataType.DELAY),
   VARIABLE_RATE_UNITS(DataType.WORK_UNITS),
   ASSIGNMENT_UNITS(DataType.UNITS),
   WORK(DataType.WORK),
   BASELINE_START(DataType.DATE),
   ACTUAL_START(DataType.DATE),
   BASELINE_FINISH(DataType.DATE),
   ACTUAL_FINISH(DataType.DATE),
   BASELINE_WORK(DataType.WORK),
   OVERTIME_WORK(DataType.WORK),
   BASELINE_COST(DataType.CURRENCY),
   WORK_CONTOUR(DataType.STRING),
   REMAINING_WORK(DataType.WORK),
   LEVELING_DELAY_UNITS(DataType.TIME_UNITS),
   LEVELING_DELAY(DataType.DURATION, LEVELING_DELAY_UNITS),
   DELAY(DataType.DURATION),
   UNIQUE_ID(DataType.INTEGER),
   TASK_UNIQUE_ID(DataType.INTEGER),
   RESOURCE_UNIQUE_ID(DataType.INTEGER),
   PLANNED_WORK_DATA(DataType.BINARY),
   COMPLETE_WORK_DATA(DataType.BINARY),

   TASK_ID(DataType.INTEGER),
   RESOURCE_ID(DataType.INTEGER),
   TASK_NAME(DataType.STRING),
   RESOURCE_NAME(DataType.STRING),
   REGULAR_WORK(DataType.WORK),
   ACTUAL_OVERTIME_WORK(DataType.WORK),
   REMAINING_OVERTIME_WORK(DataType.WORK),
   PEAK(DataType.UNITS),
   OVERTIME_COST(DataType.CURRENCY),
   REMAINING_COST(DataType.CURRENCY),
   ACTUAL_OVERTIME_COST(DataType.CURRENCY),
   REMAINING_OVERTIME_COST(DataType.CURRENCY),
   BCWS(DataType.CURRENCY),
   BCWP(DataType.CURRENCY),
   ACWP(DataType.CURRENCY),
   SV(DataType.CURRENCY),
   COST_VARIANCE(DataType.CURRENCY),
   PERCENT_WORK_COMPLETE(DataType.PERCENTAGE),
   PROJECT(DataType.STRING),
   NOTES(DataType.STRING),
   CONFIRMED(DataType.BOOLEAN),
   RESPONSE_PENDING(DataType.BOOLEAN),
   UPDATE_NEEDED(DataType.BOOLEAN),
   TEAM_STATUS_PENDING(DataType.BOOLEAN),
   COST_RATE_TABLE(DataType.STRING),
   TEXT1(DataType.STRING),
   TEXT2(DataType.STRING),
   TEXT3(DataType.STRING),
   TEXT4(DataType.STRING),
   TEXT5(DataType.STRING),
   TEXT6(DataType.STRING),
   TEXT7(DataType.STRING),
   TEXT8(DataType.STRING),
   TEXT9(DataType.STRING),
   TEXT10(DataType.STRING),
   START1(DataType.DATE),
   START2(DataType.DATE),
   START3(DataType.DATE),
   START4(DataType.DATE),
   START5(DataType.DATE),
   FINISH1(DataType.DATE),
   FINISH2(DataType.DATE),
   FINISH3(DataType.DATE),
   FINISH4(DataType.DATE),
   FINISH5(DataType.DATE),
   NUMBER1(DataType.NUMERIC),
   NUMBER2(DataType.NUMERIC),
   NUMBER3(DataType.NUMERIC),
   NUMBER4(DataType.NUMERIC),
   NUMBER5(DataType.NUMERIC),
   DURATION1(DataType.DURATION),
   DURATION2(DataType.DURATION),
   DURATION3(DataType.DURATION),
   COST1(DataType.CURRENCY),
   COST2(DataType.CURRENCY),
   COST3(DataType.CURRENCY),
   FLAG10(DataType.BOOLEAN),
   FLAG1(DataType.BOOLEAN),
   FLAG2(DataType.BOOLEAN),
   FLAG3(DataType.BOOLEAN),
   FLAG4(DataType.BOOLEAN),
   FLAG5(DataType.BOOLEAN),
   FLAG6(DataType.BOOLEAN),
   FLAG7(DataType.BOOLEAN),
   FLAG8(DataType.BOOLEAN),
   FLAG9(DataType.BOOLEAN),
   LINKED_FIELDS(DataType.BOOLEAN),
   OVERALLOCATED(DataType.BOOLEAN),
   TASK_SUMMARY_NAME(DataType.STRING),
   HYPERLINK(DataType.STRING),
   HYPERLINK_ADDRESS(DataType.STRING),
   HYPERLINK_SUBADDRESS(DataType.STRING),
   HYPERLINK_HREF(DataType.STRING),
   COST4(DataType.CURRENCY),
   COST5(DataType.CURRENCY),
   COST6(DataType.CURRENCY),
   COST7(DataType.CURRENCY),
   COST8(DataType.CURRENCY),
   COST9(DataType.CURRENCY),
   COST10(DataType.CURRENCY),
   DATE1(DataType.DATE),
   DATE2(DataType.DATE),
   DATE3(DataType.DATE),
   DATE4(DataType.DATE),
   DATE5(DataType.DATE),
   DATE6(DataType.DATE),
   DATE7(DataType.DATE),
   DATE8(DataType.DATE),
   DATE9(DataType.DATE),
   DATE10(DataType.DATE),
   DURATION4(DataType.DURATION),
   DURATION5(DataType.DURATION),
   DURATION6(DataType.DURATION),
   DURATION7(DataType.DURATION),
   DURATION8(DataType.DURATION),
   DURATION9(DataType.DURATION),
   DURATION10(DataType.DURATION),
   FINISH6(DataType.DATE),
   FINISH7(DataType.DATE),
   FINISH8(DataType.DATE),
   FINISH9(DataType.DATE),
   FINISH10(DataType.DATE),
   FLAG11(DataType.BOOLEAN),
   FLAG12(DataType.BOOLEAN),
   FLAG13(DataType.BOOLEAN),
   FLAG14(DataType.BOOLEAN),
   FLAG15(DataType.BOOLEAN),
   FLAG16(DataType.BOOLEAN),
   FLAG17(DataType.BOOLEAN),
   FLAG18(DataType.BOOLEAN),
   FLAG19(DataType.BOOLEAN),
   FLAG20(DataType.BOOLEAN),
   NUMBER6(DataType.NUMERIC),
   NUMBER7(DataType.NUMERIC),
   NUMBER8(DataType.NUMERIC),
   NUMBER9(DataType.NUMERIC),
   NUMBER10(DataType.NUMERIC),
   NUMBER11(DataType.NUMERIC),
   NUMBER12(DataType.NUMERIC),
   NUMBER13(DataType.NUMERIC),
   NUMBER14(DataType.NUMERIC),
   NUMBER15(DataType.NUMERIC),
   NUMBER16(DataType.NUMERIC),
   NUMBER17(DataType.NUMERIC),
   NUMBER18(DataType.NUMERIC),
   NUMBER19(DataType.NUMERIC),
   NUMBER20(DataType.NUMERIC),
   START6(DataType.DATE),
   START7(DataType.DATE),
   START8(DataType.DATE),
   START9(DataType.DATE),
   START10(DataType.DATE),
   TEXT11(DataType.STRING),
   TEXT12(DataType.STRING),
   TEXT13(DataType.STRING),
   TEXT14(DataType.STRING),
   TEXT15(DataType.STRING),
   TEXT16(DataType.STRING),
   TEXT17(DataType.STRING),
   TEXT18(DataType.STRING),
   TEXT19(DataType.STRING),
   TEXT20(DataType.STRING),
   TEXT21(DataType.STRING),
   TEXT22(DataType.STRING),
   TEXT23(DataType.STRING),
   TEXT24(DataType.STRING),
   TEXT25(DataType.STRING),
   TEXT26(DataType.STRING),
   TEXT27(DataType.STRING),
   TEXT28(DataType.STRING),
   TEXT29(DataType.STRING),
   TEXT30(DataType.STRING),
   INDEX(DataType.INTEGER),
   CV(DataType.CURRENCY),
   WORK_VARIANCE(DataType.WORK),
   START_VARIANCE(DataType.DURATION),
   FINISH_VARIANCE(DataType.DURATION),
   VAC(DataType.CURRENCY),
   FIXED_MATERIAL_ASSIGNMENT(DataType.STRING),
   RESOURCE_TYPE(DataType.RESOURCE_TYPE),
   HYPERLINK_SCREEN_TIP(DataType.STRING),
   WBS(DataType.STRING),
   BASELINE1_WORK(DataType.WORK),
   BASELINE1_COST(DataType.CURRENCY),
   BASELINE1_START(DataType.DATE),
   BASELINE1_FINISH(DataType.DATE),
   BASELINE2_WORK(DataType.WORK),
   BASELINE2_COST(DataType.CURRENCY),
   BASELINE2_START(DataType.DATE),
   BASELINE2_FINISH(DataType.DATE),
   BASELINE3_WORK(DataType.WORK),
   BASELINE3_COST(DataType.CURRENCY),
   BASELINE3_START(DataType.DATE),
   BASELINE3_FINISH(DataType.DATE),
   BASELINE4_WORK(DataType.WORK),
   BASELINE4_COST(DataType.CURRENCY),
   BASELINE4_START(DataType.DATE),
   BASELINE4_FINISH(DataType.DATE),
   BASELINE5_WORK(DataType.WORK),
   BASELINE5_COST(DataType.CURRENCY),
   BASELINE5_START(DataType.DATE),
   BASELINE5_FINISH(DataType.DATE),
   BASELINE6_WORK(DataType.WORK),
   BASELINE6_COST(DataType.CURRENCY),
   BASELINE6_START(DataType.DATE),
   BASELINE6_FINISH(DataType.DATE),
   BASELINE7_WORK(DataType.WORK),
   BASELINE7_COST(DataType.CURRENCY),
   BASELINE7_START(DataType.DATE),
   BASELINE7_FINISH(DataType.DATE),
   BASELINE8_WORK(DataType.WORK),
   BASELINE8_COST(DataType.CURRENCY),
   BASELINE8_START(DataType.DATE),
   BASELINE8_FINISH(DataType.DATE),
   BASELINE9_WORK(DataType.WORK),
   BASELINE9_COST(DataType.CURRENCY),
   BASELINE9_START(DataType.DATE),
   BASELINE9_FINISH(DataType.DATE),
   BASELINE10_WORK(DataType.WORK),
   BASELINE10_COST(DataType.CURRENCY),
   BASELINE10_START(DataType.DATE),
   BASELINE10_FINISH(DataType.DATE),
   TASK_OUTLINE_NUMBER(DataType.STRING),
   ENTERPRISE_COST1(DataType.CURRENCY),
   ENTERPRISE_COST2(DataType.CURRENCY),
   ENTERPRISE_COST3(DataType.CURRENCY),
   ENTERPRISE_COST4(DataType.CURRENCY),
   ENTERPRISE_COST5(DataType.CURRENCY),
   ENTERPRISE_COST6(DataType.CURRENCY),
   ENTERPRISE_COST7(DataType.CURRENCY),
   ENTERPRISE_COST8(DataType.CURRENCY),
   ENTERPRISE_COST9(DataType.CURRENCY),
   ENTERPRISE_COST10(DataType.CURRENCY),
   ENTERPRISE_DATE1(DataType.DATE),
   ENTERPRISE_DATE2(DataType.DATE),
   ENTERPRISE_DATE3(DataType.DATE),
   ENTERPRISE_DATE4(DataType.DATE),
   ENTERPRISE_DATE5(DataType.DATE),
   ENTERPRISE_DATE6(DataType.DATE),
   ENTERPRISE_DATE7(DataType.DATE),
   ENTERPRISE_DATE8(DataType.DATE),
   ENTERPRISE_DATE9(DataType.DATE),
   ENTERPRISE_DATE10(DataType.DATE),
   ENTERPRISE_DATE11(DataType.DATE),
   ENTERPRISE_DATE12(DataType.DATE),
   ENTERPRISE_DATE13(DataType.DATE),
   ENTERPRISE_DATE14(DataType.DATE),
   ENTERPRISE_DATE15(DataType.DATE),
   ENTERPRISE_DATE16(DataType.DATE),
   ENTERPRISE_DATE17(DataType.DATE),
   ENTERPRISE_DATE18(DataType.DATE),
   ENTERPRISE_DATE19(DataType.DATE),
   ENTERPRISE_DATE20(DataType.DATE),
   ENTERPRISE_DATE21(DataType.DATE),
   ENTERPRISE_DATE22(DataType.DATE),
   ENTERPRISE_DATE23(DataType.DATE),
   ENTERPRISE_DATE24(DataType.DATE),
   ENTERPRISE_DATE25(DataType.DATE),
   ENTERPRISE_DATE26(DataType.DATE),
   ENTERPRISE_DATE27(DataType.DATE),
   ENTERPRISE_DATE28(DataType.DATE),
   ENTERPRISE_DATE29(DataType.DATE),
   ENTERPRISE_DATE30(DataType.DATE),
   ENTERPRISE_DURATION1(DataType.DURATION),
   ENTERPRISE_DURATION2(DataType.DURATION),
   ENTERPRISE_DURATION3(DataType.DURATION),
   ENTERPRISE_DURATION4(DataType.DURATION),
   ENTERPRISE_DURATION5(DataType.DURATION),
   ENTERPRISE_DURATION6(DataType.DURATION),
   ENTERPRISE_DURATION7(DataType.DURATION),
   ENTERPRISE_DURATION8(DataType.DURATION),
   ENTERPRISE_DURATION9(DataType.DURATION),
   ENTERPRISE_DURATION10(DataType.DURATION),
   ENTERPRISE_FLAG1(DataType.BOOLEAN),
   ENTERPRISE_FLAG2(DataType.BOOLEAN),
   ENTERPRISE_FLAG3(DataType.BOOLEAN),
   ENTERPRISE_FLAG4(DataType.BOOLEAN),
   ENTERPRISE_FLAG5(DataType.BOOLEAN),
   ENTERPRISE_FLAG6(DataType.BOOLEAN),
   ENTERPRISE_FLAG7(DataType.BOOLEAN),
   ENTERPRISE_FLAG8(DataType.BOOLEAN),
   ENTERPRISE_FLAG9(DataType.BOOLEAN),
   ENTERPRISE_FLAG10(DataType.BOOLEAN),
   ENTERPRISE_FLAG11(DataType.BOOLEAN),
   ENTERPRISE_FLAG12(DataType.BOOLEAN),
   ENTERPRISE_FLAG13(DataType.BOOLEAN),
   ENTERPRISE_FLAG14(DataType.BOOLEAN),
   ENTERPRISE_FLAG15(DataType.BOOLEAN),
   ENTERPRISE_FLAG16(DataType.BOOLEAN),
   ENTERPRISE_FLAG17(DataType.BOOLEAN),
   ENTERPRISE_FLAG18(DataType.BOOLEAN),
   ENTERPRISE_FLAG19(DataType.BOOLEAN),
   ENTERPRISE_FLAG20(DataType.BOOLEAN),
   ENTERPRISE_NUMBER1(DataType.NUMERIC),
   ENTERPRISE_NUMBER2(DataType.NUMERIC),
   ENTERPRISE_NUMBER3(DataType.NUMERIC),
   ENTERPRISE_NUMBER4(DataType.NUMERIC),
   ENTERPRISE_NUMBER5(DataType.NUMERIC),
   ENTERPRISE_NUMBER6(DataType.NUMERIC),
   ENTERPRISE_NUMBER7(DataType.NUMERIC),
   ENTERPRISE_NUMBER8(DataType.NUMERIC),
   ENTERPRISE_NUMBER9(DataType.NUMERIC),
   ENTERPRISE_NUMBER10(DataType.NUMERIC),
   ENTERPRISE_NUMBER11(DataType.NUMERIC),
   ENTERPRISE_NUMBER12(DataType.NUMERIC),
   ENTERPRISE_NUMBER13(DataType.NUMERIC),
   ENTERPRISE_NUMBER14(DataType.NUMERIC),
   ENTERPRISE_NUMBER15(DataType.NUMERIC),
   ENTERPRISE_NUMBER16(DataType.NUMERIC),
   ENTERPRISE_NUMBER17(DataType.NUMERIC),
   ENTERPRISE_NUMBER18(DataType.NUMERIC),
   ENTERPRISE_NUMBER19(DataType.NUMERIC),
   ENTERPRISE_NUMBER20(DataType.NUMERIC),
   ENTERPRISE_NUMBER21(DataType.NUMERIC),
   ENTERPRISE_NUMBER22(DataType.NUMERIC),
   ENTERPRISE_NUMBER23(DataType.NUMERIC),
   ENTERPRISE_NUMBER24(DataType.NUMERIC),
   ENTERPRISE_NUMBER25(DataType.NUMERIC),
   ENTERPRISE_NUMBER26(DataType.NUMERIC),
   ENTERPRISE_NUMBER27(DataType.NUMERIC),
   ENTERPRISE_NUMBER28(DataType.NUMERIC),
   ENTERPRISE_NUMBER29(DataType.NUMERIC),
   ENTERPRISE_NUMBER30(DataType.NUMERIC),
   ENTERPRISE_NUMBER31(DataType.NUMERIC),
   ENTERPRISE_NUMBER32(DataType.NUMERIC),
   ENTERPRISE_NUMBER33(DataType.NUMERIC),
   ENTERPRISE_NUMBER34(DataType.NUMERIC),
   ENTERPRISE_NUMBER35(DataType.NUMERIC),
   ENTERPRISE_NUMBER36(DataType.NUMERIC),
   ENTERPRISE_NUMBER37(DataType.NUMERIC),
   ENTERPRISE_NUMBER38(DataType.NUMERIC),
   ENTERPRISE_NUMBER39(DataType.NUMERIC),
   ENTERPRISE_NUMBER40(DataType.NUMERIC),
   ENTERPRISE_TEXT1(DataType.STRING),
   ENTERPRISE_TEXT2(DataType.STRING),
   ENTERPRISE_TEXT3(DataType.STRING),
   ENTERPRISE_TEXT4(DataType.STRING),
   ENTERPRISE_TEXT5(DataType.STRING),
   ENTERPRISE_TEXT6(DataType.STRING),
   ENTERPRISE_TEXT7(DataType.STRING),
   ENTERPRISE_TEXT8(DataType.STRING),
   ENTERPRISE_TEXT9(DataType.STRING),
   ENTERPRISE_TEXT10(DataType.STRING),
   ENTERPRISE_TEXT11(DataType.STRING),
   ENTERPRISE_TEXT12(DataType.STRING),
   ENTERPRISE_TEXT13(DataType.STRING),
   ENTERPRISE_TEXT14(DataType.STRING),
   ENTERPRISE_TEXT15(DataType.STRING),
   ENTERPRISE_TEXT16(DataType.STRING),
   ENTERPRISE_TEXT17(DataType.STRING),
   ENTERPRISE_TEXT18(DataType.STRING),
   ENTERPRISE_TEXT19(DataType.STRING),
   ENTERPRISE_TEXT20(DataType.STRING),
   ENTERPRISE_TEXT21(DataType.STRING),
   ENTERPRISE_TEXT22(DataType.STRING),
   ENTERPRISE_TEXT23(DataType.STRING),
   ENTERPRISE_TEXT24(DataType.STRING),
   ENTERPRISE_TEXT25(DataType.STRING),
   ENTERPRISE_TEXT26(DataType.STRING),
   ENTERPRISE_TEXT27(DataType.STRING),
   ENTERPRISE_TEXT28(DataType.STRING),
   ENTERPRISE_TEXT29(DataType.STRING),
   ENTERPRISE_TEXT30(DataType.STRING),
   ENTERPRISE_TEXT31(DataType.STRING),
   ENTERPRISE_TEXT32(DataType.STRING),
   ENTERPRISE_TEXT33(DataType.STRING),
   ENTERPRISE_TEXT34(DataType.STRING),
   ENTERPRISE_TEXT35(DataType.STRING),
   ENTERPRISE_TEXT36(DataType.STRING),
   ENTERPRISE_TEXT37(DataType.STRING),
   ENTERPRISE_TEXT38(DataType.STRING),
   ENTERPRISE_TEXT39(DataType.STRING),
   ENTERPRISE_TEXT40(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE1(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE2(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE3(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE4(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE5(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE6(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE7(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE8(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE9(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE10(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE11(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE12(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE13(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE14(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE15(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE16(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE17(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE18(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE19(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE20(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE21(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE22(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE23(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE24(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE25(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE26(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE27(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE28(DataType.STRING),
   ENTERPRISE_RESOURCE_OUTLINE_CODE29(DataType.STRING),
   ENTERPRISE_RESOURCE_RBS(DataType.STRING),
   RESOURCE_REQUEST_TYPE(DataType.STRING),
   ENTERPRISE_TEAM_MEMBER(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE20(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE21(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE22(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE23(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE24(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE25(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE26(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE27(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE28(DataType.STRING),
   ENTERPRISE_RESOURCE_MULTI_VALUE29(DataType.STRING),
   ACTUAL_WORK_PROTECTED(DataType.WORK),
   ACTUAL_OVERTIME_WORK_PROTECTED(DataType.WORK),
   CREATED(DataType.DATE),
   ASSIGNMENT_GUID(DataType.GUID),
   ASSIGNMENT_TASK_GUID(DataType.GUID),
   ASSIGNMENT_RESOURCE_GUID(DataType.GUID),
   SUMMARY(DataType.STRING),
   OWNER(DataType.STRING),
   BUDGET_WORK(DataType.WORK),
   BUDGET_COST(DataType.CURRENCY),
   BASELINE_BUDGET_WORK(DataType.WORK),
   BASELINE_BUDGET_COST(DataType.CURRENCY),
   BASELINE1_BUDGET_WORK(DataType.WORK),
   BASELINE1_BUDGET_COST(DataType.CURRENCY),
   BASELINE2_BUDGET_WORK(DataType.WORK),
   BASELINE2_BUDGET_COST(DataType.CURRENCY),
   BASELINE3_BUDGET_WORK(DataType.WORK),
   BASELINE3_BUDGET_COST(DataType.CURRENCY),
   BASELINE4_BUDGET_WORK(DataType.WORK),
   BASELINE4_BUDGET_COST(DataType.CURRENCY),
   BASELINE5_BUDGET_WORK(DataType.WORK),
   BASELINE5_BUDGET_COST(DataType.CURRENCY),
   BASELINE6_BUDGET_WORK(DataType.WORK),
   BASELINE6_BUDGET_COST(DataType.CURRENCY),
   BASELINE7_BUDGET_WORK(DataType.WORK),
   BASELINE7_BUDGET_COST(DataType.CURRENCY),
   BASELINE8_BUDGET_WORK(DataType.WORK),
   BASELINE8_BUDGET_COST(DataType.CURRENCY),
   BASELINE9_BUDGET_WORK(DataType.WORK),
   BASELINE9_BUDGET_COST(DataType.CURRENCY),
   BASELINE10_BUDGET_WORK(DataType.WORK),
   BASELINE10_BUDGET_COST(DataType.CURRENCY),

   FINISH(DataType.DATE); // Must always be last value

   /**
    * Constructor.
    * 
    * @param dataType field data type
    * @param unitsType units type
    */
   private AssignmentField(DataType dataType, FieldType unitsType)
   {
      m_dataType = dataType;
      m_unitsType = unitsType;
   }

   /**
    * Constructor.
    * 
    * @param dataType field data type
    */
   private AssignmentField(DataType dataType)
   {
      this(dataType, null);
   }

   /**
    * {@inheritDoc}
    */
   public String getName()
   {
      return (getName(Locale.ENGLISH));
   }

   /**
    * {@inheritDoc}
    */
   public String getName(Locale locale)
   {
      String[] titles = LocaleData.getStringArray(locale, LocaleData.ASSIGNMENT_COLUMNS);
      String result = null;

      if (m_value >= 0 && m_value < titles.length)
      {
         result = titles[m_value];
      }

      return (result);
   }

   /**
    * {@inheritDoc}
    */
   public int getValue()
   {
      return (m_value);
   }

   /**
    * {@inheritDoc}
    */
   public DataType getDataType()
   {
      return (m_dataType);
   }

   /**
    * {@inheritDoc}
    */
   public FieldType getUnitsType()
   {
      return m_unitsType;
   }

   /**
    * Retrieves the string representation of this instance.
    *
    * @return string representation
    */
   @Override public String toString()
   {
      return (getName());
   }

   /**
    * This method takes the integer enumeration of a resource field
    * and returns an appropriate class instance.
    *
    * @param type integer resource field enumeration
    * @return ResourceField instance
    */
   public static AssignmentField getInstance(int type)
   {
      AssignmentField result = null;

      if (type >= 0 && type < MAX_VALUE)
      {
         result = TYPE_VALUES[type];
      }

      return (result);
   }

   public static final int MAX_VALUE = EnumSet.allOf(AssignmentField.class).size();
   private static final AssignmentField[] TYPE_VALUES = new AssignmentField[MAX_VALUE];
   static
   {
      int value = 0;
      for (AssignmentField e : EnumSet.allOf(AssignmentField.class))
      {
         e.m_value = value++;
         TYPE_VALUES[e.getValue()] = e;
      }
   }

   private int m_value;
   private DataType m_dataType;
   private FieldType m_unitsType;
}
