package com.intergraph.dude.scripting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import com.intergraph.dude.extensions.*;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;
import com.intergraph.web.plugin.webbrowser.WebBrowser;
import com.intergraph.web.plugin.webbrowser.handler.IScriptingObject;
import com.intergraph.web.plugin.webbrowser.handler.ScriptingObject;

@ScriptingObject(name = "DynamicActiveJobValue")

public class DynamicActiveJobScriptingObject implements IScriptingObject
{
	
	public static final String C_GMSC_FEAT_ID ="_ID_$$_GMSC_$$";
	public static final String C_GMSC_GEOM	="_GEOMETRY_$$_GMSC_$$";
	public String MyJobName = "";
	
	private H2DatabaseConnector databaseConnector = (H2DatabaseConnector) DatabaseConnectorFactory.getInstance().getDatabaseConnector(
			H2DatabaseConnector.class);
	
	
	public DynamicActiveJobScriptingObject()
	{
		
	}
	
	private WebBrowser	webBrowser;
	
	@Override
	public WebBrowser getWebBrowser()
	{
		return webBrowser;
	}
	
	@Override
	public void setWebBrowser(WebBrowser webBrowser)
	{
		this.webBrowser = webBrowser;
		
	}

	public void setDesktopTitleActiveJobName(String JobName)
	{
		MyJobName = JobName;
		refreshDesktopTitle();
		
	}

	
	
	public void refreshDesktopTitle()
	{
		String myProjectName = ApplicationContext.getProject().getMetadata().getName().toString();
		
		String myTitle= PluginTaskOnStartup.myTitle + " :: Project[" + myProjectName +"] :: Current active design :-  " + MyJobName;
		ApplicationContext.getDesktop().getMainFrame().setTitle(myTitle);
		
	}
	
   public void setOfflineActivateJob(String FeatId, String Fid){
	  PreparedStatement updStmt_deactivate = null;
	  PreparedStatement updStmt_activate = null;
	  int  ireturn = -1;
	  try (Connection connection = databaseConnector.getConnection()) 
		{
			connection.setAutoCommit(false);
			try 
			{
				//Deactivate
				updStmt_deactivate = connection.prepareStatement("UPDATE " + getFeatureTableName(FeatId) + " SET ACTIVATED = ? where TO_SITE = 1");
				updStmt_deactivate.setInt(1, 0);
				ireturn= updStmt_deactivate.executeUpdate();
				// System.out.println("total run: " + ireturn);
				 Log.getLogger().log(Level.INFO, "setOfflineActivateJob :Deactivate Job -  Updated "+ ireturn+ "record(s)");
					
				//Activated
				String stmt="UPDATE " + getFeatureTableName(FeatId) + " set ACTIVATED= ? where _ID_$$_GMSC_$$="+Fid;
				
				updStmt_activate = connection.prepareStatement(stmt);
				updStmt_activate.setInt(1, 1);
				ireturn= updStmt_activate.executeUpdate();
				// System.out.println("total run: " + ireturn);
				 Log.getLogger().log(Level.INFO, "setOfflineActivateJob :Activate Job - Updated "+ ireturn+ "record(s)");
				
				connection.commit();
				 if (updStmt_deactivate != null)   updStmt_deactivate.close(); 
				 if (updStmt_activate != null)  updStmt_activate.close(); 
			} catch (SQLException e ) {
				
				//if any error happen
				Log.getLogger().log(Level.SEVERE, "", e);
			        if (connection != null) {
			            try {
			            	Log.getLogger().log(Level.SEVERE, "","Transaction is being rolled back");
			                connection.rollback();
			            } catch(SQLException excep) {
			            	Log.getLogger().log(Level.SEVERE, "", e);
			            }
			        }//if stmt
			 } finally {
			
				 connection.setAutoCommit(true);
				 connection.close();
			 }
			
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			
		}
	
   }//function
		
		
   public Entry[] getJobList(String FeatId){
	   Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(FeatId);		
		String sqlStatement = String.format("Select %s, JOB_NAME From %s where TO_SITE=1",getPrimaryKeyName(feature), getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		//executeQuery
		ArrayList<Entry> record = new ArrayList<Entry>();
		
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();
			
			while (resultSet.next())
			{
				record.add(new Entry(resultSet.getObject(1).toString(), resultSet.getObject(2).toString()));
			}
						
			// Close recordset and query
			if (resultSet !=null)  resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection !=null ) connection.close();
			
			// convert it to array
			Entry [] array = record.toArray( new Entry[ record.size() ] );
				        
			//return fid;
			return array;
						
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		} 
   }
	
   public void DisplayCustomerInfo(String featureId, String FeatureType) {
	   CustomerDialog dialog = new CustomerDialog();

	   	dialog.setFeatType(FeatureType);
	    dialog.setFeatID(featureId);
		//Object[] selectedElementIds = mapSelectionCurator.getSelectedElements();
		//GPrimitive[] selectedPrimitives = map.getElements(mapSelectionCurator.getActiveFeature().getUniqueId(), selectedElementIds);
	   
	     dialog.setBounds(50, 50, 650, 100);
		 dialog.setModal(true);
		
		dialog.setTitle("Customer List:");
		dialog.setVisible(true);
		
   }
   
   public void DisplayLoadingInfo(String featureId) {
	   CustomerDialog dialog = new CustomerDialog();

	    dialog.setFeatID(featureId);
		//Object[] selectedElementIds = mapSelectionCurator.getSelectedElements();
		//GPrimitive[] selectedPrimitives = map.getElements(mapSelectionCurator.getActiveFeature().getUniqueId(), selectedElementIds);
	   
	     dialog.setBounds(50, 50, 650, 100);
		 dialog.setModal(true);
		
		dialog.setTitle("Loading List:");
		dialog.setVisible(true);
		
   }
   
   public String getCurrentUser()
   {
	  
	   return ApplicationContext.getClientCredentials().getUserId().toString();
   }
   
   
   
   public String getOfflineJobType(String featureId)
   {
	   Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select distinct JOB_TYPE From %s where ACTIVATED=1 and TO_SITE = 1", getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		
		//executeQuery
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			//Handler the Filter Result and return resultSet
			//Handler the Filter Result and return resultSet
			String  sReturn="";
			while (resultSet.next())
			{
				if (resultSet.getObject(1) != null) sReturn =resultSet.getObject(1).toString();
				
			}
			
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();
			
			return sReturn;
					
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
   }
   

   public String getOfflineJobStatus(String featureId)
   {
	   Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select distinct JOB_STATUS From %s where ACTIVATED=1 where TO_SITE = 1", getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		
		//executeQuery
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			//Handler the Filter Result and return resultSet
			String  sReturn="";
			while (resultSet.next())
			{
				if (resultSet.getObject(1) != null) sReturn =resultSet.getObject(1).toString();
				
			}
			
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();
			
			return sReturn;
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
   }
   
	
   
   public String getOfflineJobOperationArea(String featureId)
   {
	   Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select distinct CGIS_OP_AREA From %s where ACTIVATED=1 and TO_SITE = 1", getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		
		//executeQuery
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			//Handler the Filter Result and return resultSet
			String  sReturn="";
			while (resultSet.next())
			{
				if (resultSet.getObject(1) != null) sReturn =resultSet.getObject(1).toString();
				
			}
			
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();
			
			
			return sReturn;
					
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
   }
   
	public String getJobName(String featureId)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select JOB_NAME From %s where ACTIVATED=1 and TO_SITE = 1", getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		
		//executeQuery
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			//Handler the Filter Result and return resultSet
			//Handler the Filter Result and return resultSet
			String  sReturn="";
			while (resultSet.next())
			{
				if (resultSet.getObject(1) != null) sReturn =resultSet.getObject(1).toString();
				
			}
			
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();
			
			
			return sReturn;
					
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
	}
			
	public String getJobUser(String featureId)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select ASSIGNED_TO From %s where ACTIVATED=1 and TO_SITE = 1", getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		
		//executeQuery
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			//Handler the Filter Result and return resultSet
			//Handler the Filter Result and return resultSet
			String  sReturn="";
			while (resultSet.next())
			{
				if (resultSet.getObject(1) != null) sReturn =resultSet.getObject(1).toString();
				
			}
			
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();
			
			
			return sReturn;
					
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
	}
	
	public String getJobID(String featureId)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select %s From %s where ACTIVATED=1 and TO_SITE = 1", getPrimaryKeyName(feature), getFeatureTableName(feature.getUniqueId()));
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		//executeQuery
		
		
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			//Handler the Filter Result and return resultSet
			String  fid="";
			int iCount=0;
			while (resultSet.next())
			{
				if (iCount > 0) fid +=",";
				fid +=resultSet.getObject(1).toString();
				iCount++;
			}
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();
			return fid;
				
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "%s", e);
			return null;
		}
	}
	

	private static String getPrimaryKeyName(Feature feature)
	{
		return String.format("_%s_$$_GMSC_$$", feature.getPrimaryKey());
	}
	
	
	private String getFeatureTableName(String uniqueFeatureId)
	{
		// replaces in case of a UUID the - with _
		return "FEATURE_" + uniqueFeatureId.replace("-", "_");
	}
	
	//pass featureID, get how many rows by SWID
	public String getRowCountbySwid(String featureId, String vSwid)
	{
		String retString = "";
		
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select SW_ID From " 
			+ getFeatureTableName(feature.getUniqueId()) 
			+ " WHERE SW_ID = '"
			+ vSwid + "'"
			+ " AND " + GMSCH2Metadata.DELETED.columnName + " = 0");
		Log.getLogger().log(Level.FINE, "DynamicActiveJobValue.getRowCountbySwid: SQLStatement: " + sqlStatement);
		PreparedStatement primitiveStatement = null;
		ResultSet resultSet = null;
		
		//executeQuery
		try (Connection connection = databaseConnector.getConnection())
		{
			primitiveStatement = connection.prepareStatement(sqlStatement);
			resultSet = primitiveStatement.executeQuery();

			int counter = 0;
			while (resultSet.next())
			{
				counter++;
			}
			
			Log.getLogger().log(Level.FINE, "DynamicActiveJobValue.getRowCountbySwid: counter total is: " + Integer.toString(counter));
			retString = Integer.toString(counter);
			
			//after query, close connection
			if (resultSet !=null) resultSet.close(); 
			if (primitiveStatement !=null) primitiveStatement.close(); 
			if (connection != null) connection.close();

			
			return retString;
					
		}
		catch (Exception e)
		{
			Log.getLogger().log(Level.SEVERE, "DynamicActiveJobValue.getRowCountbySwid:: " + e.getStackTrace().toString());
			return retString;
		}
	}
	
}



