/*
 * file:       GraphicalIndicatorReader.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2006
 * date:       16-Feb-2006
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

import java.util.Date;

import net.sf.mpxj.DataType;
import net.sf.mpxj.Duration;
import net.sf.mpxj.FieldType;
import net.sf.mpxj.GraphicalIndicator;
import net.sf.mpxj.GraphicalIndicatorCriteria;
import net.sf.mpxj.MPPResourceField;
import net.sf.mpxj.MPPTaskField;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.TaskField;
import net.sf.mpxj.TestOperator;

/**
 * This class allows graphical indicator definitions to be read from an MPP
 * file.
 */
public final class GraphicalIndicatorReader
{
   /**
    * The main entry point for processing graphical indicator definitions.
    * 
    * @param file parent project file
    * @param props properties data
    */
   public void process (ProjectFile file, Props props)
   {
      m_file = file;
      m_data = props.getByteArray(Props.GRAPHICAL_INDICATOR_DATA);
      
      if (m_data != null)
      {
         int columnsCount = MPPUtility.getInt(m_data, 4);
         m_headerOffset = 8;      
         for (int loop=0; loop < columnsCount; loop++)
         {
            processColumns();
         }
      }
   }

   /**
    * Processes graphical indicator definitions for each column.
    */
   private void processColumns ()
   {
      int fieldType = MPPUtility.getShort(m_data, m_headerOffset);
      m_headerOffset +=2 ;
      
      // unknown bytes
      m_headerOffset += 1;
      
      int entityType = MPPUtility.getByte(m_data, m_headerOffset);
      m_headerOffset += 1;
      
      m_dataOffset = MPPUtility.getInt(m_data, m_headerOffset);
      m_headerOffset += 4;
      
      FieldType type = null;
      switch (entityType)
      {
         case 0x0B:               
         {
            type = MPPTaskField.getInstance(fieldType);
            break;
         }
         
         case 0x0C:               
         {
            type = MPPResourceField.getInstance(fieldType);
            break;
         }            
      }
      
      
      //System.out.println("Header: " + type);
      //System.out.println(MPPUtility.hexdump(m_data, m_dataOffset, 36, false, 16, ""));
      
      GraphicalIndicator indicator = new GraphicalIndicator(m_file);
      indicator.setFieldType(type);
      int flags = m_data[m_dataOffset];
      indicator.setProjectSummaryInheritsFromSummaryRows((flags & 0x08) != 0);
      indicator.setSummaryRowsInheritFromNonSummaryRows((flags & 0x04) != 0);
      indicator.setDisplayGraphicalIndicators((flags & 0x02) != 0);
      indicator.setShowDataValuesInToolTips((flags & 0x01) != 0);
      m_dataOffset += 20;         
      
      //int nonSummaryRowOffset = MPPUtility.getInt(m_data, m_dataOffset) -36;
      m_dataOffset += 4;

      int summaryRowOffset = MPPUtility.getInt(m_data, m_dataOffset) - 36;
      m_dataOffset += 4;

      int projectSummaryOffset = MPPUtility.getInt(m_data, m_dataOffset) - 36;
      m_dataOffset += 4;
      
      int dataSize = MPPUtility.getInt(m_data, m_dataOffset) - 36;
      m_dataOffset += 4;


      //System.out.println("Data");
      //System.out.println(MPPUtility.hexdump(m_data, m_dataOffset, dataSize, false, 16, ""));
      
      int maxNonSummaryRowOffset = m_dataOffset+summaryRowOffset;      
      int maxSummaryRowOffset = m_dataOffset+projectSummaryOffset;      
      int maxProjectSummaryOffset = m_dataOffset+dataSize;
      
      while (m_dataOffset+2 < maxNonSummaryRowOffset)
      {
         indicator.addNonSummaryRowCriteria(processCriteria (type));
      }

      while (m_dataOffset+2 < maxSummaryRowOffset)
      {
         indicator.addSummaryRowCriteria(processCriteria (type));
      }

      while (m_dataOffset+2 < maxProjectSummaryOffset)
      {
         indicator.addProjectSummaryCriteria(processCriteria (type));
      }
                     
      m_file.addGraphicalIndicator(type, indicator);      
   }

   /**
    * Process the graphical indicator criteria for a single column.
    * 
    * @param type field type
    * @return indicator criteria data
    */
   private GraphicalIndicatorCriteria processCriteria (FieldType type)
   {
      GraphicalIndicatorCriteria criteria = new GraphicalIndicatorCriteria(m_file);
      criteria.setField(type);
      
      int indicatorType = MPPUtility.getInt(m_data, m_dataOffset);
      m_dataOffset += 4;            
      criteria.setIndicator(indicatorType);

      if (m_dataOffset+4 < m_data.length)
      {
         int operatorValue = MPPUtility.getInt(m_data, m_dataOffset);
         m_dataOffset += 4;            
         TestOperator operator = (operatorValue==0?TestOperator.IS_ANY_VALUE:TestOperator.getInstance(operatorValue-0x3E7));
         criteria.setOperator(operator);
         
         if (operator != TestOperator.IS_ANY_VALUE)
         {
            processOperandValue(type, criteria);
               
            if (operator == TestOperator.IS_WITHIN || operator == TestOperator.IS_NOT_WITHIN)
            {
               processOperandValue(type, criteria);
            }
         }
      }
      
      return (criteria);      
   }
   
   /**
    * Process an operand value used in the definition of the graphical
    * indicator criteria.
    * 
    * @param type field type
    * @param criteria indicator criteria
    */
   private void processOperandValue (FieldType type, GraphicalIndicatorCriteria criteria)
   {
      boolean valueFlag = (MPPUtility.getInt(m_data, m_dataOffset) == 1);
      m_dataOffset += 4;
      
      if (valueFlag == false)
      {
         // 4 byte int representing the field type, we need the low bytes
         // the high bytes define if this is a task or a resource field
         int field = MPPUtility.getShort(m_data, m_dataOffset);
         m_dataOffset += 4;
         if (type instanceof TaskField)
         {
            criteria.addValue(MPPTaskField.getInstance(field));
         }
         else
         {
            criteria.addValue(MPPResourceField.getInstance(field));
         }         
      }
      else
      {                  
         //int dataTypeValue = MPPUtility.getShort(m_data, m_dataOffset);
         m_dataOffset += 2;
         
         switch (type.getDataType().getType())
         {
            case DataType.DURATION_VALUE: // 0x03
            {
               Duration value = MPPUtility.getAdjustedDuration (m_file, MPPUtility.getInt (m_data, m_dataOffset), MPPUtility.getDurationTimeUnits(MPPUtility.getShort (m_data, m_dataOffset+4)));
               m_dataOffset += 6;
               criteria.addValue(value);
               break;
            }
            
            case DataType.NUMERIC_VALUE: // 0x05
            {
               Double value = new Double(MPPUtility.getDouble(m_data, m_dataOffset));
               m_dataOffset += 8;
               criteria.addValue(value);
               break;
            }

            case DataType.CURRENCY_VALUE: // 0x06
            {
               Double value = new Double(MPPUtility.getDouble(m_data, m_dataOffset)/100);
               m_dataOffset += 8;
               criteria.addValue(value);
               break;
            }
            
            case DataType.STRING_VALUE: // 0x08
            {
               String value = MPPUtility.getUnicodeString(m_data, m_dataOffset);
               m_dataOffset += ((value.length()+1)*2);
               criteria.addValue(value);
               break;
            }
            
            case DataType.BOOLEAN_VALUE: // 0x0B
            {
               int value = MPPUtility.getShort(m_data, m_dataOffset);
               m_dataOffset += 2;
               criteria.addValue(value==1?Boolean.TRUE:Boolean.FALSE);
               break;
            }
            
            case DataType.DATE_VALUE: // 0x13
            {
               Date value = MPPUtility.getTimestamp(m_data, m_dataOffset);
               m_dataOffset += 4;
               criteria.addValue(value);
               break;
            }
         }                              
      }
   }
   
   private byte[] m_data;
   private int m_headerOffset;
   private int m_dataOffset;
   private ProjectFile m_file;   
}
