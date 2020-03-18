package com.intergraph.dude.scripting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.intergraph.dude.extensions.*;
import com.intergraph.tools.net.json.JSONException;
import com.intergraph.tools.net.json.JSONObject;
import com.intergraph.tools.utils.Assertion;
import com.intergraph.tools.utils.Str;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.browsing.Browser;
import com.intergraph.web.core.browsing.MapSelectionCurator;
import com.intergraph.web.core.data.Project;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.extension.search.DataHeader;
import com.intergraph.web.extension.search.Datatype;
import com.intergraph.web.extension.search.SearchController;
import com.intergraph.web.extension.search.SearchResult;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;
import com.intergraph.web.plugin.offlinedatasupport.database.IDatabaseConnector.GetRangeException;
import com.intergraph.web.plugin.offlinedatasupport.database.Metadata;
import com.intergraph.web.plugin.webbrowser.WebBrowser;
import com.intergraph.web.plugin.webbrowser.handler.IScriptingObject;
import com.intergraph.web.plugin.webbrowser.handler.ScriptingObject;
import com.intergraph.web.viewer.data.GBounds;
import com.intergraph.web.viewer.data.GLayerFilter;
import com.intergraph.web.viewer.data.GPrimitive;
import com.intergraph.web.viewer.map.GMap;

@ScriptingObject(name = "SearchNFilterExtension")
public class SearchNFilterExtensionScriptingObject implements IScriptingObject
{
	private H2DatabaseConnector databaseConnector = (H2DatabaseConnector) DatabaseConnectorFactory.getInstance().getDatabaseConnector(
			H2DatabaseConnector.class);
	
	public SearchNFilterExtensionScriptingObject()
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

	//function for ONLINE & OFFLINE PROJECT ONLY
	public void setMessageAtStatusbar(String myMsg)
	{
		GMap map = ApplicationContext.getBrowser().getMap();
		map.getViewerConstraints().showStatusMessage(myMsg);
	}
	
	public void OfflineSetActiveFeature(String toActiveFeatID)
	{
		//Clear map selection and activate
		//ApplicationContext.getBrowser().getMapSelectionCurator().clearSelectedElements();
		ApplicationContext.getProject().getFeatureByTitleOrID(toActiveFeatID).setActive(true);
		
	}
	
	
	//function for OFFLINE Feature ONLY
	public int setFeatureFilter(String toActiveFeatName, String filterValue, boolean bFitSelection)
	{
		try
		{
			//set Application references
			Project project = ApplicationContext.getProject();
			Browser browser = ApplicationContext.getBrowser();
			
			//Make this Feature exist in this project, else exit this function
			Feature toActivate = project.getFeatureByTitleOrID(toActiveFeatName);
			if (toActivate == null) return 0;
			
			
			//Start Query to get Elements to be filtered
			ArrayList<Object> FiltrElements;
			FiltrElements = getFeatureFilterList(toActiveFeatName,filterValue);
			
			if (FiltrElements.size() <= 0 ) {
				//if not result found then hide feature
				project.getFeatureByTitle(toActivate.getName()).setVisible(false);
				//still need empty the filter
				ArrayList<Object> myids = new ArrayList<Object>();
				myids.add(-100);
				setFeatureFilterByList(toActiveFeatName,myids);
				
			} else {
			
				//Call to filter this feature based on resultset
				setFeatureFilterByList(toActiveFeatName,FiltrElements);
				project.getFeatureByTitle(toActivate.getName()).setVisible(true);
				//toActivate.reload();
				if (bFitSelection){
					//Now zoom location
					Object myObj[] = FiltrElements.toArray(); 
					
					//Before fit select, activate this feature
					//project.getFeatureByTitle(toActivate.getName()).setActive(true);
					
					//Add IDs to MapSelection
					//MapSelectionCurator mapSelectionCurator = browser.getMapSelectionCurator();
					//mapSelectionCurator.clearSelectedElements();
					//mapSelectionCurator.select(myObj);
					//offlineFitSelection();
		            
					//19Sept2014 : Zoom fit without activate the feature.
					//get filter and gat map range, and then zoom it without activate the feature.
					GBounds fitbounds =  toActivate.getRangeforKeys(myObj);
					 // grow MBR by using the features fit factor
	                fitbounds.grow(toActivate.getFitFactor());
	                
	                GMap map = browser.getMap();
	                long scale = map.getScaleForBounds(fitbounds);
	                
	                // restrict the scale to the features min- & max scale
	                scale = Math.min(toActivate.getMaxScale(), Math.max(toActivate.getMinScale(), scale));
	                map.setCenterAndScale(fitbounds.getCenterH(), fitbounds.getCenterV(), scale);
	                toActivate.reloadWithDependencies();
	                map.refresh();
				}
			}
			
			return FiltrElements.size();
			
			
		} 
		catch (Exception e)
		{
			Log.getLogger().log(Level.SEVERE, "setFeatureFilter: ERROR :: ", e);
			return 0;
		}
	
	}
	
	//function for online and offline reload feature
	public void ReloadFeature(String toActiveFeatName){
		//set Application references
		Project project = ApplicationContext.getProject();
		
		//Make this Feature exist in this project, else exit this function
		Feature toActivate = project.getFeatureByTitleOrID(toActiveFeatName);
		if (toActivate == null) return;
		
		//Reload feature now.
		toActivate.reload();
		
	}
	
	//function for ONLINE and OFFLINE Feature ONLY
	public void ZoomFitFeatureByKeys(String toActiveFeatName, String fid_List){
		try
		{
			//set Application references
			Project project = ApplicationContext.getProject();
			Browser browser = ApplicationContext.getBrowser();
			
			//Make this Feature exist in this project, else exit this function
			Feature toActivate = project.getFeatureByTitleOrID(toActiveFeatName);
			if (toActivate == null) return;
			
			//Start Query to get Elements to be filtered
			ArrayList<Object> FiltrElements= new ArrayList<Object>();
			
			String[] ids = fid_List.split(",");
			for (int i=0; i< ids.length; i++) {
				FiltrElements.add(ids[i]);
			}
			
			//Now zoom location
			Object myObj[] = FiltrElements.toArray(); 
			
			GBounds fitbounds =  toActivate.getRangeforKeys(myObj);
			 // grow MBR by using the features fit factor
	       fitbounds.grow(toActivate.getFitFactor());
	           
	       GMap map = browser.getMap();
	       long scale = map.getScaleForBounds(fitbounds);
	           
		   // restrict the scale to the features min- & maxscale
		   scale = Math.min(toActivate.getMaxScale(), Math.max(toActivate.getMinScale(), scale));
		   
		   map.setCenterAndScale(fitbounds.getCenterH(), fitbounds.getCenterV(), scale);
		   return;
		} 
		catch (Exception e)
		{
			Log.getLogger().log(Level.SEVERE, "setFeatureFilter: ERROR :: ", e);
			return;
		}
	}
	
	
	
	//function for OFFLINE Feature ONLY
    private void offlineFitSelection()
    {
          Browser browser = ApplicationContext.getBrowser();
          Object[] selectedElements = browser.getMapSelectionCurator().getSelectedElements();
          
          if (!Assertion.isValid(selectedElements)) // nothing selected
                 return;
          
          Feature activeFeature = browser.getActiveFeature();
          try
          {
        	  
        	  
                 List<GPrimitive> selectedPrimitives = databaseConnector.getPrimitivesById(activeFeature.getUniqueId(), selectedElements);
                 
                 if (!Assertion.isValid(selectedPrimitives)) // no primitives found
                        return;
                 
                 GBounds fitbounds = GPrimitive.getMBR(selectedPrimitives.toArray(new GPrimitive[selectedPrimitives.size()]));
                 
                 // grow MBR by using the features fit factor
                 fitbounds.grow(activeFeature.getFitFactor());
                 
                 GMap map = browser.getMap();
                 long scale = map.getScaleForBounds(fitbounds);
                 
                 // restrict the scale to the features min- & maxscale
                 scale = Math.min(activeFeature.getMaxScale(), Math.max(activeFeature.getMinScale(), scale));
                 
                 map.setCenterAndScale(fitbounds.getCenterH(), fitbounds.getCenterV(), scale);
                 
          }
          catch (GetRangeException | SQLException e)
          {
                 Log.getLogger().log(Level.SEVERE, "offlineFitSelection()::", e);
          }
    } 

	public void setFeatureFilterByList(String featureId, ArrayList<Object> ids)
	{
		try
		{
			Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
			feature.clearFilter();	
			
			//Apply Filter
			if (ids.size() > 0) {
				feature.addFilter(createNotInFilter(ids));
				feature.reload();
				
			}
		}
		catch (Exception e)
		{
			Log.getLogger().log(Level.SEVERE, "setFeatureFilterByList::", e);
		}
	}
	
	//function for OFFLINE Feature ONLY
//	public Feature setFeatureFilter(String featureId, String formData) 
//	{
//		
//		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
//		feature.clearFilter();		
//		String sqlStatement = String.format("Select %s From %s where %s", getPrimaryKeyName(feature), getFeatureTableName(feature.getUniqueId()), formData);
//		executeFilterQuery(sqlStatement, formData, feature);
//		feature.reload();
//		return feature;
//		
//	}
//	
	//function for OFFLINE Feature ONLY
//	private int executeFilterQuery(String sqlStatement, String formData, Feature feature) 
//	{		
//			int iResult = 0;
//		
//		
//			try (Connection connection = databaseConnector.getConnection())
//			{
//				try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
//				{
//					try (ResultSet resultSet = primitiveStatement.executeQuery())
//					{
//						
//							iResult = handleFilterResult(feature, resultSet);
//							if (resultSet != null) resultSet.close();
//							if (primitiveStatement != null) primitiveStatement.close();
//							if (connection != null) connection.close();
//							return iResult;
//					}
//				}
//			}
//			
//			catch (SQLException e)
//			{
//				Log.getLogger().log(Level.SEVERE, "setFeatureFilter::", e);
//				return 0;
//			}
//		
//		
//	}
	
//	private int handleFilterResult(Feature feature, ResultSet resultSet)
//	{
//		ArrayList<Object> ids = new ArrayList<Object>();
//		try
//		{
//			while (resultSet.next())
//			{
//				ids.add(resultSet.getObject(1));
//			}
//			if (ids.size() > 0)
//				feature.addFilter(createNotInFilter(ids));
//			
//			return ids.size();
//		}
//		catch (SQLException e)
//		{
//			Log.getLogger().log(Level.SEVERE, "handleFilterResult::", e);
//			return -1;
//		}
		
		
//	}
	
	public String getFeatureInfo(String featureId, String formColumn, String formData)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select %s,%s From %s where %s", getPrimaryKeyName(feature), formColumn, getFeatureTableName(feature.getUniqueId()), formData);
		//executeQuery
		
		try (Connection connection = databaseConnector.getConnection())
		{
			try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = primitiveStatement.executeQuery())
				{
						//Handler the Filter Result and return resultSet
						String  fid="";
						int iCount=0;
						while (resultSet.next())
						{
							if (iCount > 0) fid +=",";
							fid +=resultSet.getObject(formColumn).toString();
							iCount++;
						}
						if (resultSet != null) resultSet.close();
						if (primitiveStatement != null)primitiveStatement.close();
						if (connection !=null) connection.close();
						return fid;
						
				} catch (NullPointerException e)
				{
					Log.getLogger().log(Level.SEVERE, "getFeatureInfo::", e);
					return "";
				}
				
			}
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
	}
	
	public String getFeatureSWID(String featureId, String formData)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select SW_ID From %s where %s", getFeatureTableName(feature.getUniqueId()), formData);
		//executeQuery
		
		
		try (Connection connection = databaseConnector.getConnection())
		{
			try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = primitiveStatement.executeQuery())
				{
						//Handler the Filter Result and return resultSet
						String  fid="";
						int iCount=0;
						while (resultSet.next())
						{
							if (iCount > 0) fid +=",";
							fid +=resultSet.getObject(1).toString();
							iCount++;
						}
						if (resultSet != null) resultSet.close();
						if (primitiveStatement != null)primitiveStatement.close();
						if (connection !=null) connection.close();
						return fid;
						
				}
				
			}
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "getFeatureID::", e);
			return null;
		}
	}
	
	public String getFeatureID(String featureId, String formData)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select %s From %s where %s", getPrimaryKeyName(feature), getFeatureTableName(feature.getUniqueId()), formData);
		//executeQuery
		
		
		try (Connection connection = databaseConnector.getConnection())
		{
			try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = primitiveStatement.executeQuery())
				{
						//Handler the Filter Result and return resultSet
						String  fid="";
						int iCount=0;
						while (resultSet.next())
						{
							if (iCount > 0) fid +=",";
							fid +=resultSet.getObject(1).toString();
							iCount++;
						}
						if (resultSet != null) resultSet.close();
						if (primitiveStatement != null)primitiveStatement.close();
						if (connection !=null) connection.close();
						return fid;
						
				}
				
			}
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "getFeatureID::", e);
			return null;
		}
	}
	
	public String getFeatureIDList(String featureId, String formData) 
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select %s From %s where %s", getPrimaryKeyName(feature), getFeatureTableName(feature.getUniqueId()), formData);
		
		//executeQuery
		
		try (Connection connection = databaseConnector.getConnection())
		{
			try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = primitiveStatement.executeQuery())
				{
						//Handler the Filter Result and return resultSet
						String  fid="";
						int iCount=0;
						while (resultSet.next())
						{
							if (iCount > 0) fid +=",";
							fid +=resultSet.getObject(1).toString();
							iCount++;
						}
						if (resultSet != null) resultSet.close();
						if (primitiveStatement != null)primitiveStatement.close();
						if (connection !=null) connection.close();
						return fid;
						
				}
				
			}
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			return null;
		}
	}
	
	
	public ArrayList<Object> getFeatureFilterList(String featureId, String formData) 
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);		
		String sqlStatement = String.format("Select %s From %s where %s", getPrimaryKeyName(feature), getFeatureTableName(feature.getUniqueId()), formData);
		
		//executeQuery
		
		try (Connection connection = databaseConnector.getConnection())
		{
			try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = primitiveStatement.executeQuery())
				{
						//Handler the Filter Result and return resultSet
						ArrayList<Object> ids = new ArrayList<Object>();
						while (resultSet.next())
						{
							ids.add(resultSet.getObject(1));
						}
						if (resultSet != null) resultSet.close();
						if (primitiveStatement != null)primitiveStatement.close();
						if (connection !=null) connection.close();
						return ids;
						
				}
				
			}
		}
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "getFeatureIDList::", e);
			return null;
		}
	}


	
	public int executeSearch(String FeatID, String formData) throws JSONException
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(FeatID.toString());
		
		String sqlStatement = String.format("Select * From %s where ", getFeatureTableName(feature.getUniqueId()));
		int iResult = executeQuery(sqlStatement, formData, feature);
		return iResult;
		
	}
	
	//function for OFFLINE Feature ONLY
	private int executeQuery(String sqlStatement, String formData, Feature feature) throws JSONException
	{	
		int iResult = 0;
		JSONObject jsonData = new JSONObject(formData);
		

		
		String[] keys = JSONObject.getNames(jsonData);
		ArrayList<String> usedKeys = new ArrayList<String>();
		for (int i = 0; i < keys.length; i++)
		{
			if (jsonData.get(keys[i]) != null && !jsonData.get(keys[i]).toString().isEmpty())
			{
				usedKeys.add(keys[i]);
				//sqlStatement += String.format("%s=?", keys[i]);
				sqlStatement += String.format("UPPER(%s) LIKE UPPER(?)", keys[i]);
				if (i < keys.length - 1)
					sqlStatement += " and ";
			}
		}
		if (sqlStatement.endsWith(" and "))
		{
			sqlStatement = sqlStatement.substring(0, sqlStatement.length() - 5);
		}
		
		
		
			try
			{
				try (Connection connection = databaseConnector.getConnection())
				{
					try (PreparedStatement primitiveStatement = connection.prepareStatement(sqlStatement))
					{
						for (String key : usedKeys)
							primitiveStatement.setObject(usedKeys.indexOf(key) + 1, jsonData.get(key));
						try (ResultSet resultSet = primitiveStatement.executeQuery())
						{
								iResult = handleSearchResult(feature, resultSet);
								if (resultSet != null) resultSet.close();
								if (primitiveStatement != null) primitiveStatement.close();
								if (connection !=null) connection.close();
						}
					}
				}
				return iResult;
			}
			
			catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "executeQuery::", e);
				return -1;
			}
		
			
	}
	
	
	private int handleSearchResult(Feature activeFeature, ResultSet queryResult)
	{
		int iReturnResult = 0;
		List<Object> elementSelection = new ArrayList<Object>();
		if (activeFeature != null && activeFeature.isLocateable() && Str.valid(activeFeature.getPrimaryKey()) && queryResult != null)
		{
			SearchResult searchResult = null;
			try
			{
				searchResult = convertSearchResult(queryResult, activeFeature);
				
			}
			catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "handleSearchResult::", e);
			}
			if (searchResult == null)
				return -1;
			
			int primaryKeyIndex = searchResult.getIndexOf(getPrimaryKeyName(activeFeature));
			if (primaryKeyIndex > -1)
			{
				for (List<Object> row : searchResult.getDataRows())
				{
					
					elementSelection.add(row.get(primaryKeyIndex));
				}
			}
			
			if (searchResult.getDataRows().size() > 0)
			{
				SearchController searchController = ApplicationContext.getProject().getSearchController();
				
				//if (searchController == null) searchController = new SearchController(null);
				
				searchController.setAppendResult(false);
				searchController.executeSearch(new OfflineSearch(activeFeature.getID(), searchResult, "Search Result - " + activeFeature.getName()), null, null);
				//searchController.addSearchListener(new SearchControllerListSelectionHandlers());
				iReturnResult=searchResult.getDataRows().size();
			}
		}
		
		if (Assertion.isValid(elementSelection))
		{
			ApplicationContext.getBrowser().getMapSelectionCurator().select(elementSelection.toArray(new Object[elementSelection.size()]));
			ApplicationContext.getBrowser().fitSelection();
		}
		
		return iReturnResult;
	}
	
	private SearchResult convertSearchResult(ResultSet resultSet, Feature resultFeature) throws SQLException
	{
		if (resultSet == null)
			return null;
		
		List<List<Object>> dataRows = new ArrayList<List<Object>>();
		ResultSetMetaData metadata = resultSet.getMetaData();
		while (resultSet.next())
		{
			List<Object> dataRow = new ArrayList<Object>();
			
			for (int i = 1; i <= metadata.getColumnCount(); i++)
			{
				Metadata geometryMetadata = Metadata.valueByColumnName(metadata.getColumnName(i), null);
				if (geometryMetadata == null)
					dataRow.add(resultSet.getObject(i));
				else if (geometryMetadata == Metadata.ID)
					dataRow.add(resultSet.getObject(i));
			}
			dataRows.add(dataRow);
		}
		
		List<DataHeader> dataHeaders = new ArrayList<DataHeader>();
		for (int i = 1; i <= metadata.getColumnCount(); i++)
		{
			Metadata geometryMetadata = Metadata.valueByColumnName(metadata.getColumnName(i), null);
			if (geometryMetadata == null)
				dataHeaders.add(new DataHeader(metadata.getColumnName(i), Datatype.STRING));
			else if (geometryMetadata == Metadata.ID)
				dataHeaders.add(new DataHeader(resultFeature.getPrimaryKey(), Datatype.STRING));
		}
		
		return new SearchResult(dataHeaders, dataRows, resultFeature);
	}
	
	//function for OFFLINE Feature ONLY
	private static String getPrimaryKeyName(Feature feature)
	{
		return String.format("_%s_$$_GMSC_$$", feature.getPrimaryKey());
	}
	
	public void clearFilter(String featureId)
	{
		Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);
		feature.clearFilter();
		feature.reload();
	}
	
	private GLayerFilter createNotInFilter(List<? extends Object> ids)
	{
		if (!Assertion.isValid(ids))
			return null;
		
		String[] toFilter = new String[ids.size()];
		for (int i = 0; i < ids.size(); i++)
		{
			toFilter[i] = ids.get(i).toString();
		}
		
		return new NotInIDFilter(toFilter);
	}
	
	//function for OFFLINE Feature ONLY
	private String getFeatureTableName(String uniqueFeatureId)
	{
		// replaces in case of a UUID the - with _
		return "FEATURE_" + uniqueFeatureId.replace("-", "_");
	}
	
	public int filterFeatureByID(String toActiveFeatName, String sID_List, boolean bFitSelection)
	{   //this module for Online mode where have to parsein a list string of IDs
		// convert the string of IDs into ArrayList
		try
		{
			//set Application references
			Project project = ApplicationContext.getProject();
			Browser browser = ApplicationContext.getBrowser();
			
			//Make this Feature to Activate
			Feature toActivate = ApplicationContext.getProject().getFeatureByTitleOrID(toActiveFeatName);
			
			project.getFeatureByTitle(toActivate.getName()).setVisible(true);
			if (toActivate != null)
				project.getFeature(toActivate.getID()).setActive(true);
			
			 if (browser.getActiveFeature() == null)
                 return 0;
			//Start Query to get Elements to be filtered
				ArrayList<Object> FiltrElements = new ArrayList<Object>(Arrays.asList(sID_List.split(",")));
				
				if (FiltrElements.size() <= 0 ) {
					//if not result found then hide feature
					project.getFeatureByTitle(toActivate.getName()).setVisible(false);
					
				} else {
				
					//Call to filter this feature based on resultset
					setFeatureFilterByList(toActiveFeatName,FiltrElements);
					
					if (bFitSelection){
						Object myObj[] = FiltrElements.toArray(); 
						//Add IDs to MapSelection
						MapSelectionCurator mapSelectionCurator = browser.getMapSelectionCurator();
						mapSelectionCurator.clearSelectedElements();
						mapSelectionCurator.select(myObj);
						
						 ApplicationContext.getBrowser().fitSelection();
			             if (!ApplicationContext.getInstance().isOffline())
			             {
			                    ApplicationContext.getBrowser().fitSelection();
			             }
			             else
			             {
			                    offlineFitSelection();
			             }

					}
				}
				
				return FiltrElements.size();
		} 
		catch (Exception e)
		{
			Log.getLogger().log(Level.SEVERE, "setFeatureFilter: ERROR :: ", e);
			return 0;
		}
	
	}
}



