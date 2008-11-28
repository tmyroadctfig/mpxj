/*
 * file:        MppEmbeddedTest.java
 * author:      Jon Iles
 * copyright:   (c) Packwood Software Limited 2008
 * date:        15/03/2008
 */

/*
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */

package net.sf.mpxj.junit;

import java.util.List;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpp.RTFEmbeddedObject;

/**
 * Test to handle MPP file content embedded in note fields. 
 */
public class MppEmbeddedTest extends MPXJTestCase
{
   /**
    * Test MPP9 file.
    * 
    * @throws Exception
    */
   public void testMpp9Embedded() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setPreserveNoteFormatting(true);
      ProjectFile mpp = reader.read(m_basedir + "/mpp9embedded.mpp");
      testEmbeddedObjects(mpp);
   }

   /**
    * Test MPP12 file.
    * 
    * @throws Exception
    */
   public void testMpp12Embedded() throws Exception
   {
      MPPReader reader = new MPPReader();
      reader.setPreserveNoteFormatting(true);
      ProjectFile mpp = reader.read(m_basedir + "/mpp12embedded.mpp");
      testEmbeddedObjects(mpp);
   }

   /**
    * Tests common to all file types.
    * 
    * @param file project file
    */
   private void testEmbeddedObjects(ProjectFile file)
   {
      Task task = file.getTaskByID(Integer.valueOf(1));
      assertEquals("Task 1", task.getName());
      String notes = task.getNotes();
      assertNotNull(notes);
      List<List<RTFEmbeddedObject>> list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNull(list);

      task = file.getTaskByID(Integer.valueOf(2));
      assertEquals("Task 2", task.getName());
      notes = task.getNotes();
      assertNotNull(notes);
      list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNotNull(list);
      assertEquals(1, list.size());
      List<RTFEmbeddedObject> objectList = list.get(0);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));

      task = file.getTaskByID(Integer.valueOf(3));
      assertEquals("Task 3", task.getName());
      notes = task.getNotes();
      assertNotNull(notes);
      list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNotNull(list);
      assertEquals(1, list.size());
      objectList = list.get(0);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));

      task = file.getTaskByID(Integer.valueOf(4));
      assertEquals("Task 4", task.getName());
      notes = task.getNotes();
      assertNotNull(notes);
      list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNotNull(list);
      assertEquals(1, list.size());
      objectList = list.get(0);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));

      task = file.getTaskByID(Integer.valueOf(5));
      assertEquals("Task 5", task.getName());
      notes = task.getNotes();
      assertNotNull(notes);
      list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNotNull(list);
      assertEquals(1, list.size());
      objectList = list.get(0);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));

      task = file.getTaskByID(Integer.valueOf(6));
      assertEquals("Task 6", task.getName());
      notes = task.getNotes();
      assertNotNull(notes);
      list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNotNull(list);
      assertEquals(2, list.size());
      objectList = list.get(0);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));
      objectList = list.get(1);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));

      task = file.getTaskByID(Integer.valueOf(7));
      assertEquals("Task 7", task.getName());
      notes = task.getNotes();
      assertNotNull(notes);
      list = RTFEmbeddedObject.getEmbeddedObjects(notes);
      assertNotNull(list);
      assertEquals(2, list.size());
      objectList = list.get(0);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));
      objectList = list.get(1);
      assertEquals(4, objectList.size());
      assertEquals("Package", new String(objectList.get(0).getData(), 0, 7));
      assertEquals("METAFILEPICT", new String(objectList.get(2).getData(), 0, 12));
   }
}
