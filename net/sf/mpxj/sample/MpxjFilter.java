/*
 * file:       MpxjFilter.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2009
 * date:       03/05/2009
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

import net.sf.mpxj.Filter;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.reader.ProjectReaderUtility;

/**
 * This example shows tasks or resources being read from a project file,
 * a filter applied to the list, and the results displayed.
 * Executing this utility without a valid filter name will result in
 * the list of available filters being displayed.
 */
public class MpxjFilter
{
   /**
    * Main method.
    *
    * @param args array of command line arguments
    */
   public static void main(String[] args)
   {
      try
      {
         if (args.length != 2)
         {
            System.out.println("Usage: MpxFilter <input file name> <filter name>");
         }
         else
         {
            filter(args[0], args[1]);
         }
      }

      catch (Exception ex)
      {
         ex.printStackTrace(System.out);
      }
   }

   /**
    * This method opens the named project, applies the named filter
    * and displays the filtered list of tasks or resources. If an
    * invalid filter name is supplied, a list of valid filter names
    * is shown.
    * 
    * @param filename input file name
    * @param filtername input filter name
    */
   private static void filter(String filename, String filtername) throws Exception
   {
      ProjectReader reader = ProjectReaderUtility.getProjectReader(filename);
      ProjectFile project = reader.read(filename);
      Filter filter = project.getFilterByName(filtername);

      if (filter == null)
      {
         displayAvailableFilters(project);
      }
      else
      {
         System.out.println(filter);
         System.out.println();

         if (filter.isTaskFilter())
         {
            processTaskFilter(project, filter);
         }
         else
         {
            processResourceFilter(project, filter);
         }
      }
   }

   /**
    * This utility displays a list of available task filters, and a
    * list of available resource filters.
    * 
    * @param project project file
    */
   private static void displayAvailableFilters(ProjectFile project)
   {
      System.out.println("Unknown filter name supplied.");
      System.out.println("Available task filters:");
      for (Filter filter : project.getAllTaskFilters())
      {
         System.out.println("   " + filter.getName());
      }

      System.out.println("Available resource filters:");
      for (Filter filter : project.getAllResourceFilters())
      {
         System.out.println("   " + filter.getName());
      }

   }

   /**
    * Apply a filter to the list of all tasks, and show the results.
    * 
    * @param project project file
    * @param filter filter
    */
   private static void processTaskFilter(ProjectFile project, Filter filter)
   {
      for (Task task : project.getAllTasks())
      {
         if (filter.evaluate(task))
         {
            System.out.println(task.getID() + "," + task.getUniqueID() + "," + task.getName());
         }
      }
   }

   /**
    * Apply a filter to the list of all resources, and show the results.
    * 
    * @param project project file
    * @param filter filter
    */
   private static void processResourceFilter(ProjectFile project, Filter filter)
   {
      for (Resource resource : project.getAllResources())
      {
         if (filter.evaluate(resource))
         {
            System.out.println(resource.getID() + "," + resource.getUniqueID() + "," + resource.getName());
         }
      }
   }

}
