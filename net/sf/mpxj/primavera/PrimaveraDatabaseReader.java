/*
 * file:       PrimaveraDatabaseReader.java
 * author:     Jon Iles
 * copyright:  (c) Packwood Software 2010
 * date:       22/03/2010
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

package net.sf.mpxj.primavera;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.mpxj.Day;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.listener.ProjectListener;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.utility.NumberUtility;

/**
 * This class provides a generic front end to read project data from
 * a database.
 */
public final class PrimaveraDatabaseReader implements ProjectReader
{
   /**
    * {@inheritDoc}
    */
   @Override public void addProjectListener(ProjectListener listener)
   {
      if (m_projectListeners == null)
      {
         m_projectListeners = new LinkedList<ProjectListener>();
      }
      m_projectListeners.add(listener);
   }

   /**
    * Populates a Map instance representing the IDs and names of
    * projects available in the current database.
    * 
    * @return Map instance containing ID and name pairs
    * @throws MPXJException
    */
   public Map<Integer, String> listProjects() throws MPXJException
   {
      try
      {
         Map<Integer, String> result = new HashMap<Integer, String>();

         List<Row> rows = getRows("select proj_id, proj_short_name from " + m_schema + "project where delete_date is null");
         for (Row row : rows)
         {
            Integer id = row.getInteger("proj_id");
            String name = row.getString("proj_short_name");
            result.put(id, name);
         }

         return result;
      }

      catch (SQLException ex)
      {
         throw new MPXJException(MPXJException.READ_ERROR, ex);
      }
   }

   /**
    * Read a project from the current data source.
    * 
    * @return ProjectFile instance
    * @throws MPXJException
    */
   public ProjectFile read() throws MPXJException
   {
      try
      {
         m_reader = new PrimaveraReader();
         ProjectFile project = m_reader.getProject();
         project.addProjectListeners(m_projectListeners);

         processProjectHeader();
         processCalendars();
         processResources();
         processTasks();
         processPredecessors();
         processAssignments();

         m_reader = null;
         project.updateStructure();

         return (project);
      }

      catch (SQLException ex)
      {
         throw new MPXJException(MPXJException.READ_ERROR, ex);
      }

      finally
      {
         if (m_allocatedConnection && m_connection != null)
         {
            try
            {
               m_connection.close();
            }

            catch (SQLException ex)
            {
               // silently ignore errors on close
            }

            m_connection = null;
         }
      }
   }

   /**
    * Convenience method which allows all projects in the database to
    * be read in a single operation.
    * 
    * @return list of ProjectFile instances
    * @throws MPXJException
    */
   public List<ProjectFile> readAll() throws MPXJException
   {
      List<ProjectFile> result = new LinkedList<ProjectFile>();
      Map<Integer, String> projects = listProjects();
      for (Integer id : projects.keySet())
      {
         setProjectID(id.intValue());
         result.add(read());
      }
      return result;
   }

   /**
    * Select the project header row from the database.
    * 
    * @throws SQLException
    */
   private void processProjectHeader() throws SQLException
   {
      //
      // Process common attributes
      //
      List<Row> rows = getRows("select * from " + m_schema + "project where proj_id=?", m_projectID);
      m_reader.processProjectHeader(rows);

      //
      // Process PMDB-specific attributes
      //
      rows = getRows("select * from " + m_schema + "prefer join " + m_schema + "currtype on currtype.curr_id =prefer.curr_id where prefer.delete_date is null");
      if (!rows.isEmpty())
      {
         Row row = rows.get(0);
         ProjectHeader ph = m_reader.getProject().getProjectHeader();
         ph.setCreationDate(row.getDate("create_date"));
         ph.setLastSaved(row.getDate("update_date"));
         ph.setMinutesPerDay(Integer.valueOf(row.getInt("day_hr_cnt") * 60));
         ph.setMinutesPerWeek(Double.valueOf(row.getDouble("week_hr_cnt").doubleValue() * 60));
         ph.setWeekStartDay(Day.getInstance(row.getInt("week_start_day_num")));

         m_reader.processDefaultCurrency(row);
      }
   }

   /**
    * Process resources.
    * 
    * @throws SQLException
    */
   private void processResources() throws SQLException
   {
      List<Row> rows = getRows("select * from " + m_schema + "rsrc where delete_date is null and rsrc_id in (select rsrc_id from " + m_schema + "taskrsrc t where proj_id=? and delete_date is null) order by rsrc_seq_num", m_projectID);
      m_reader.processResources(rows);
   }

   /**
    * Process tasks.
    * 
    * @throws SQLException
    */
   private void processTasks() throws SQLException
   {
      List<Row> wbs = getRows("select * from " + m_schema + "projwbs where proj_id=? and delete_date is null order by parent_wbs_id,seq_num", m_projectID);
      List<Row> tasks = getRows("select * from " + m_schema + "task where proj_id=? and delete_date is null", m_projectID);
      m_reader.processTasks(wbs, tasks);
   }

   /**
    * Process predecessors.
    * 
    * @throws SQLException
    */
   private void processPredecessors() throws SQLException
   {
      List<Row> rows = getRows("select * from " + m_schema + "taskpred where proj_id=? and delete_date is null", m_projectID);
      m_reader.processPredecessors(rows);
   }

   /**
    * Process calendars.
    * 
    * @throws SQLException
    */
   private void processCalendars() throws SQLException
   {
      List<Row> rows = getRows("select * from " + m_schema + "calendar where (proj_id is null or proj_id=?) and delete_date is null", m_projectID);
      m_reader.processCalendars(rows);
   }

   /**
    * Process resource assignments.
    * 
    * @throws SQLException
    */
   private void processAssignments() throws SQLException
   {
      List<Row> rows = getRows("select * from " + m_schema + "taskrsrc where proj_id=? and delete_date is null", m_projectID);
      m_reader.processAssignments(rows);
   }

   /**
    * Set the ID of the project to be read.
    * 
    * @param projectID project ID
    */
   public void setProjectID(int projectID)
   {
      m_projectID = Integer.valueOf(projectID);
   }

   /**
    * Set the data source. A DataSource or a Connection can be supplied
    * to this class to allow connection to the database.
    * 
    * @param dataSource data source
    */
   public void setDataSource(DataSource dataSource)
   {
      m_dataSource = dataSource;
   }

   /**
    * Sets the connection. A DataSource or a Connection can be supplied
    * to this class to allow connection to the database.
    * 
    * @param connection database connection
    */
   public void setConnection(Connection connection)
   {
      m_connection = connection;
   }

   /**
    * {@inheritDoc}
    */
   @Override public ProjectFile read(String fileName)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   @Override public ProjectFile read(File file)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * {@inheritDoc}
    */
   @Override public ProjectFile read(InputStream inputStream)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Retrieve a number of rows matching the supplied query. 
    * 
    * @param sql query statement
    * @return result set
    * @throws SQLException
    */
   private List<Row> getRows(String sql) throws SQLException
   {
      allocateConnection();

      try
      {
         List<Row> result = new LinkedList<Row>();

         m_ps = m_connection.prepareStatement(sql);
         m_rs = m_ps.executeQuery();
         populateMetaData();
         while (m_rs.next())
         {
            result.add(new ResultSetRow(m_rs, m_meta));
         }

         return (result);
      }

      finally
      {
         releaseConnection();
      }
   }

   /**
    * Retrieve a number of rows matching the supplied query 
    * which takes a single parameter.
    * 
    * @param sql query statement
    * @param var bind variable value
    * @return result set
    * @throws SQLException
    */
   private List<Row> getRows(String sql, Integer var) throws SQLException
   {
      allocateConnection();

      try
      {
         List<Row> result = new LinkedList<Row>();

         m_ps = m_connection.prepareStatement(sql);
         m_ps.setInt(1, NumberUtility.getInt(var));
         m_rs = m_ps.executeQuery();
         populateMetaData();
         while (m_rs.next())
         {
            result.add(new ResultSetRow(m_rs, m_meta));
         }

         return (result);
      }

      finally
      {
         releaseConnection();
      }
   }

   /**
    * Allocates a database connection.
    * 
    * @throws SQLException
    */
   private void allocateConnection() throws SQLException
   {
      if (m_connection == null)
      {
         m_connection = m_dataSource.getConnection();
         m_allocatedConnection = true;
      }
   }

   /**
    * Releases a database connection, and cleans up any resources
    * associated with that connection.
    */
   private void releaseConnection()
   {
      if (m_rs != null)
      {
         try
         {
            m_rs.close();
         }

         catch (SQLException ex)
         {
            // silently ignore errors on close
         }

         m_rs = null;
      }

      if (m_ps != null)
      {
         try
         {
            m_ps.close();
         }

         catch (SQLException ex)
         {
            // silently ignore errors on close
         }

         m_ps = null;
      }
   }

   /**
    * Retrieves basic meta data from the result set.
    * 
    * @throws SQLException
    */
   private void populateMetaData() throws SQLException
   {
      m_meta.clear();

      ResultSetMetaData meta = m_rs.getMetaData();
      int columnCount = meta.getColumnCount() + 1;
      for (int loop = 1; loop < columnCount; loop++)
      {
         String name = meta.getColumnName(loop).toLowerCase();
         Integer type = Integer.valueOf(meta.getColumnType(loop));
         m_meta.put(name, type);
      }
   }

   /**
    * Set the name of the schema containing the Primavera tables.
    * 
    * @param schema schema name.
    */
   public void setSchema(String schema)
   {
      if (schema.charAt(schema.length() - 1) != '.')
      {
         schema = schema + '.';
      }
      m_schema = schema;
   }

   /**
    * Retrieve the name of the schema containing the Primavera tables.
    * 
    * @return schema name
    */
   public String getSchema()
   {
      return m_schema;
   }

   private PrimaveraReader m_reader;
   private Integer m_projectID;
   private String m_schema = "";
   private DataSource m_dataSource;
   private Connection m_connection;
   private boolean m_allocatedConnection;
   private PreparedStatement m_ps;
   private ResultSet m_rs;
   private Map<String, Integer> m_meta = new HashMap<String, Integer>();
   private List<ProjectListener> m_projectListeners;
}