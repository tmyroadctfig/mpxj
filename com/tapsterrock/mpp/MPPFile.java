/*
 * file:       MPPFile.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2002-2003
 * date:       03/01/2003
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

package com.tapsterrock.mpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.tapsterrock.mpx.MPXException;
import com.tapsterrock.mpx.MPXFile;
import com.tapsterrock.mpx.Task;

/**
 * This class is used to represent a Microsoft Project MPP file. This
 * implementation allows the file to be read, and the data it contains
 * exported as a set of MPX objects. These objects can be interrogated
 * to retrieve any required data, or stored as an MPX file.
 */
public class MPPFile extends MPXFile
{
   /**
    * Constructor allowing an MPP file to be read from an input stream
    *
    * @param is an input stream
    * @throws MPXException on file read errors
    */
   public MPPFile (InputStream is)
      throws MPXException
   {
      process (is);
   }

   /**
    * Constructor allowing an MPP file to be read from a file object
    *
    * @param file a file object
    * @throws MPXException on file read errors
    */
   public MPPFile (File file)
      throws MPXException
   {
      try
      {
         process (new FileInputStream (file));
      }

      catch (IOException ex)
      {
         throw new MPXException (MPXException.READ_ERROR, ex);
      }
   }

   /**
    * Constructor allowing an MPP file to be read from a named file
    *
    * @param name name of a file
    * @throws MPXException on file read errors
    */
   public MPPFile (String name)
      throws MPXException
   {
      try
      {
         process (new FileInputStream (name));
      }

      catch (IOException ex)
      {
         throw new MPXException (MPXException.READ_ERROR, ex);
      }
   }

   /**
    * This method brings together all of the processing required to
    * read data from an MPP file, and populate an MPXFile object.
    *
    * @param is input stream
    * @throws MPXException on file read errors
    */
   private void process (InputStream is)
      throws MPXException
   {
		try
		{   	
	      //
	      // Open the file system and retrieve the root directory
	      //
	      POIFSFileSystem fs = new POIFSFileSystem (is);
	      DirectoryEntry root = fs.getRoot ();
	
	      //
	      // Retrieve the CompObj data and validate the file format
	      //
	      CompObj compObj = new CompObj (new DocumentInputStream ((DocumentEntry)root.getEntry("\1CompObj")));			         
	
			String format = compObj.getFileFormat();
			if (format.equals("MSProject.MPP9") == true)
			{
				MPP9File.process (this, root);
            m_fileType = 9;
			}
			else
			{
				if (format.equals("MSProject.MPP8") == true)				
				{
               MPP8File.process (this, root);
               m_fileType = 8;
				}
				else
				{
					throw new MPXException (MPXException.INVALID_FILE + ": " + format);					
				}
			}			
         
         //
         // Update the internal structure. We'll take this opportunity to 
         // generate outline numbers for the tasks as they don't appear to
         // be present in the MPP file.
         //
         setAutoOutlineNumber(true);
         updateStructure ();
         setAutoOutlineNumber(false);      
      
         //
         // Perform post-processing to set the summary flag
         //
         LinkedList tasks = getAllTasks();
         Iterator iter = tasks.iterator();     
         Task task;
         
         while (iter.hasNext() == true)
         {
            task = (Task)iter.next();
            task.setSummary(task.getChildTasks().size() != 0);
         }
         
		}
		
		catch (IOException ex)
		{
			throw new MPXException (MPXException.READ_ERROR, ex);
		}		
   }
   
   /**
    * This method retrieves the state of the preserve note formatting flag.
    * 
    * @return boolean flag
    */
   public boolean getPreserveNoteFormatting()
   {
      return (m_preserveNoteFormatting);
   }

   /**
    * This method sets a flag to indicate whether the RTF formatting associated
    * with notes should be preserved or removed. By default the formatting
    * is removed.
    * 
    * @param preserveNoteFormatting
    */
   public void setPreserveNoteFormatting (boolean preserveNoteFormatting)
   {
      m_preserveNoteFormatting = preserveNoteFormatting;
   }

   /**
    * This method retrieves a value representing the type of MPP file
    * that has been read. Currently this method will return the value 8 for 
    * an MPP8 file (Project 98) or 9 for an MPP9 file (Project 2000 and 
    * Project 2002).
    * 
    * @return File type value
    */
   public int getFileType()
   {
      return (m_fileType);
   }


   /**
    * Flag used to indicate whether RTF formatting in notes should
    * be preserved.
    */
   private boolean m_preserveNoteFormatting = false;   
   
   /**
    * This value is used to represent the type of MPP file that 
    * has been read.
    */
   private int m_fileType;
}
