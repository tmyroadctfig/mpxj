/*
 * file:       MpxCreate.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software Limited 2002-2003
 * date:       08/02/2003
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

package net.sf.mpxj.sample;

import java.text.SimpleDateFormat;

import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.Relation;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.utility.NumberUtility;
import net.sf.mpxj.writer.ProjectWriter;



/**
 * This example illustrates creation of an MPX or an MSPDI file from scratch.
 * The output type of the file generated by this class depends on the file
 * name extension supplied by the user. A .xml extension will generate an
 * MSPDI file, anything else will generate an MPX file.
 */
public class MpxjCreate
{
   /**
    * Main method.
    *
    * @param args array of command line arguments
    */
   public static void main (String[] args)
   {
      try
      {
         if (args.length != 1)
         {
            System.out.println ("Usage: MpxCreate <output file name>");
         }
         else
         {
            create (args[0]);
         }
      }

      catch (Exception ex)
      {
         ex.printStackTrace(System.out);
      }
   }

   /**
    * Creates a writer which will generate the required type of output file.
    *
    * @param filename file name
    * @return ProjectWriter instance
    */
   private static ProjectWriter getWriter (String filename)
   {
      ProjectWriter result;
      String suffix;

      if (filename.length() < 4)
      {
         suffix = ".MPX";
      }
      else
      {
         suffix = filename.substring(filename.length()-4).toUpperCase();
      }

      if (suffix.equals(".XML") == true)
      {
         result = new MSPDIWriter ();
      }
      else
      {
         result = new MPXWriter ();
      }

      return (result);
   }

   /**
    * This method creates a summary task, two sub-tasks and a milestone,
    * all with the appropriate constraints between them. The tasks are
    * assigned to two resources. Note that Microsoft Project is fussy
    * about the order in which things appear in the file. If you are going
    * to assign resources to tasks, the resources must appear in the
    * file before the tasks.
    *
    * @param filename output file name
    */
   private static void create (String filename)
      throws Exception
   {
      //
      // Create a simple date format to allow us to
      // easily set date values.
      //
      SimpleDateFormat df = new SimpleDateFormat ("dd/MM/yyyy");

      //
      // Create an empty MPX or MSPDI file. The filename is passed to
      // this method purely to allow it to determine the type of
      // file to create.
      //
      ProjectFile file = new ProjectFile();

      //
      // Uncomment these lines to test the use of alternative
      // delimiters and separators for MPX file output.
      //
      //file.setDelimiter(';');
      //file.setDecimalSeparator(',');
      //file.setThousandsSeparator('.');

      //
      // Configure the file to automatically generate identifiers for tasks.
      //
      file.setAutoTaskID(true);
      file.setAutoTaskUniqueID(true);

      //
      // Configure the file to automatically generate identifiers for resources.
      //
      file.setAutoResourceID(true);
      file.setAutoResourceUniqueID(true);

      //
      // Configure the file to automatically generate outline levels
      // and outline numbers.
      //
      file.setAutoOutlineLevel(true);
      file.setAutoOutlineNumber(true);

      //
      // Configure the file to automatically generate WBS labels
      //
      file.setAutoWBS(true);

      //
      // Configure the file to automatically generate identifiers for calendars
      // (not strictly necessary here, but required if generating MSPDI files)
      //
      file.setAutoCalendarUniqueID(true);

      //
      // Add a default calendar called "Standard"
      //
      ProjectCalendar calendar = file.addDefaultBaseCalendar();
      
      //
      // Add a holiday to the calendar to demonstrate calendar exceptions
      //
      ProjectCalendarException exception = calendar.addCalendarException();
      exception.setFromDate(df.parse("13/03/2006"));
      exception.setToDate(df.parse("13/03/2006"));
      exception.setWorking(false);
      
      //
      // Retrieve the project header and set the start date. Note Microsoft
      // Project appears to reset all task dates relative to this date, so this
      // date must match the start date of the earliest task for you to see
      // the expected results. If this value is not set, it will default to
      // today's date.
      //
      ProjectHeader header = file.getProjectHeader();
      header.setStartDate(df.parse("01/01/2003"));

      //
      // Add resources
      //
      Resource resource1 = file.addResource();
      resource1.setName("Resource1");

      Resource resource2 = file.addResource();
      resource2.setName("Resource2");

      //
      // This next line is not required, it is here simply to test the
      // output file format when alternative separators and delimiters
      // are used.
      //
      resource2.setMaxUnits(Double.valueOf(50.0));

      //
      // Create a summary task
      //
      Task task1 = file.addTask();
      task1.setName ("Summary Task");

      //
      // Create the first sub task
      //
      Task task2 = task1.addTask();
      task2.setName ("First Sub Task");
      task2.setDuration (Duration.getInstance (10.5, TimeUnit.DAYS));
      task2.setStart (df.parse("01/01/2003"));

      //
      // We'll set this task up as being 50% complete. If we have no resource
      // assignments for this task, this is enough information for MS Project.
      // If we do have resource assignments, the assignment record needs to
      // contain the corresponding work and actual work fields set to the
      // correct values in order for MS project to mark the task as complete
      // or partially complete.
      //
      task2.setPercentageComplete(NumberUtility.getDouble(50.0));
      task2.setActualStart(df.parse("01/01/2003"));

      //
      // Create the second sub task
      //
      Task task3 = task1.addTask();
      task3.setName ("Second Sub Task");
      task3.setStart (df.parse("11/01/2003"));
      task3.setDuration (Duration.getInstance (10, TimeUnit.DAYS));

      //
      // Link these two tasks
      //
      Relation rel1 = task3.addPredecessor (task2);
      rel1.setType(RelationType.FINISH_START);

      //
      // Add a milestone
      //
      Task milestone1 = task1.addTask();
      milestone1.setName ("Milestone");
      milestone1.setStart (df.parse("21/01/2003"));
      milestone1.setDuration (Duration.getInstance (0, TimeUnit.DAYS));
      Relation rel2 = milestone1.addPredecessor (task3);
      rel2.setType (RelationType.FINISH_START);

      //
      // This final task has a percent complete value, but no
      // resource assignments. This is an interesting case it it requires
      // special processing to generate the MSPDI file correctly.
      //
      Task task4 = file.addTask();
      task4.setName ("Last Task");
      task4.setDuration (Duration.getInstance (8, TimeUnit.DAYS));
      task4.setStart (df.parse("01/01/2003"));
      task4.setPercentageComplete(NumberUtility.getDouble(70.0));
      task4.setActualStart(df.parse("01/01/2003"));

      //
      // Assign resources to tasks
      //
      ResourceAssignment assignment1 = task2.addResourceAssignment (resource1);
      ResourceAssignment assignment2 = task3.addResourceAssignment (resource2);

      //
      // As the first task is partially complete, and we are adding
      // a resource assignment, we must set the work and actual work
      // fields in the assignment to appropriate values, or MS Project
      // won't recognise the task as being complete or partially complete
      //
      assignment1.setWork(Duration.getInstance (80, TimeUnit.HOURS));
      assignment1.setActualWork(Duration.getInstance (40, TimeUnit.HOURS));

      //
      // If we were just generating an MPX file, we would already have enough
      // attributes set to create the file correctly. If we want to generate
      // an MSPDI file, we must also set the assignment start dates and
      // the remaining work attribute. The assignment start dates will normally
      // be the same as the task start dates.
      //
      assignment1.setRemainingWork(Duration.getInstance (40, TimeUnit.HOURS));
      assignment2.setRemainingWork(Duration.getInstance (80, TimeUnit.HOURS));
      assignment1.setStart(df.parse("01/01/2003"));
      assignment2.setStart(df.parse("11/01/2003"));

      //
      // Write the file
      //
      ProjectWriter writer = getWriter(filename);
      writer.write (file, filename);     
   }

}

