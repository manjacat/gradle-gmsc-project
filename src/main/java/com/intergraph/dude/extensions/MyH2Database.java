package com.intergraph.dude.extensions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;

public class MyH2Database
{
	
	private H2DatabaseConnector databaseConnector = (H2DatabaseConnector) DatabaseConnectorFactory.getInstance().getDatabaseConnector(
			H2DatabaseConnector.class);
	
	private Object[] getH2TableList() {
		  
		Object[] myArr = null;
        
		try (Connection connection = databaseConnector.getConnection())
		{
			String sqlStatement = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME like 'FEATURE%' AND TABLE_NAME NOT like '%_HATBOX' ORDER BY TABLE_NAME";
			try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet rs = h2dbStatement.executeQuery())
				{
					
					ArrayList<String> record = new ArrayList<String>();
		            while (rs.next()) {
				            for (int i = 1; i <= 1; i++) {
				                    String value = rs.getString(i);
				                    record.add(value);
				            }
				    }        
		            if (rs != null) rs.close();
		            if (record.size() > 0) {
		            	myArr = record.toArray();
		            }
				    if (h2dbStatement!= null) h2dbStatement.close();
				}
				
			}
			
			if (connection !=null ) connection.close();
			return  myArr;
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
  }
  
	public ArrayList<String> getH2TableListHasChanges() {
		
		 Object[] myRs = getH2TableList();
		 ArrayList<String> featList = new ArrayList<String>();
		  if (myRs != null) {
				  for (int i = 0; i < myRs.length; i ++){
					  	//int iCount = getH2TblCount(myRs[i].toString());
					  	//Add summary of transaction for OFFLINE
					  	int iCount = 0;
						try (Connection connection = databaseConnector.getConnection())
						{
							//SQL Statement to query if there are changes on ADD or Update
							String sqlStatement = "SELECT sum( " + GMSCH2Metadata.NEW.columnName + " + " +  GMSCH2Metadata.CHANGED.columnName + ") TOTAL_RECORD FROM " + myRs[i].toString()
													+ " WHERE (" + GMSCH2Metadata.NEW.columnName + " > 0 AND " +  GMSCH2Metadata.CHANGED.columnName + " = 0 AND " +  GMSCH2Metadata.DELETED.columnName + " = 0 ) "
													+ " OR (" + GMSCH2Metadata.CHANGED.columnName + " > 0 AND " +  GMSCH2Metadata.NEW.columnName + " = 0 AND " +  GMSCH2Metadata.DELETED.columnName + " = 0 ) ";
							try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
							{
								try (ResultSet resultSet = h2dbStatement.executeQuery())
								{
									if (resultSet != null)
									{
										while (resultSet.next()){
											iCount = resultSet.getInt("TOTAL_RECORD");
											if (iCount > 0) {
												//add into String
												featList.add(myRs[i].toString());
											}
										}
										resultSet.close();
										
									}
								}
								
								h2dbStatement.close();
							}
							if (connection !=null ) connection.close();
							
						}catch (SQLException e)
						{
							Log.getLogger().log(Level.SEVERE, "", e);
							return null;
						}
					 
				  }
				  
			  }
		  //finally return
		  return featList;
		  
	}
	
	public boolean HasChanges (String FeatID, String FID) {
		boolean bReturn = false;
		
		//H2 db query if feature has changes
		try (Connection connection = databaseConnector.getConnection())
		{
			//SQL Statement to query if there are changes on ADD or Update
			String sqlStatement = "SELECT count(*) TOTAL_RECORD FROM FEATURE_" + FeatID.trim() + " where " + GMSCH2Metadata.ID.columnName + " = " + FID + " and (" + GMSCH2Metadata.NEW.columnName + " >0 or " + GMSCH2Metadata.CHANGED.columnName+" >0)";
			try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = h2dbStatement.executeQuery())
				{
					if (resultSet != null)
					{
						while (resultSet.next()){
							int iCount = resultSet.getInt("TOTAL_RECORD");
							if (iCount > 0) {
								//add into String
								bReturn = true;
							}
						}
						resultSet.close();
					}
				}
				
				h2dbStatement.close();
			}
			if (connection !=null ) connection.close();
		} catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return false;
		}
		return bReturn;
	}
	
}
