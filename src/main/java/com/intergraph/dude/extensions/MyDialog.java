package com.intergraph.dude.extensions;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.intergraph.tools.ui.DefaultDialog;
import com.intergraph.tools.utils.Assertion;
import com.intergraph.tools.utils.Str;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.data.Project;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.extension.search.DataHeader;
import com.intergraph.web.extension.search.Datatype;
import com.intergraph.web.extension.search.SearchController;
import com.intergraph.web.extension.search.SearchResult;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;
import com.intergraph.web.plugin.offlinedatasupport.database.Metadata;

public class MyDialog extends DefaultDialog implements ListSelectionListener {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3700827419941990488L;
	public static final String C_GMSC_FEAT_ID ="_ID_$$_GMSC_$$";
	public static final String C_GMSC_GEOM	="_GEOMETRY_$$_GMSC_$$";
	private DefaultTableModel	tableModel;
	private JTable OffLinetable;
	
	private H2DatabaseConnector databaseConnector = (H2DatabaseConnector) DatabaseConnectorFactory.getInstance().getDatabaseConnector(
			H2DatabaseConnector.class);
	protected Component frame;
	
	
	@Override
	protected List<JButton> getButtons() {
		return Arrays.asList(getCloseButton());
	}
	
	

	  public MyDialog() {
			super(ApplicationContext.getMainFrame(), "MyDialog");
			
		}
	  
	  private Object[] getH2TableList() {
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

				         // Close recordset and query
						if (rs !=null)  rs.close(); 
						if (h2dbStatement !=null) h2dbStatement.close(); 
						if (connection !=null ) connection.close();
						
			            Object[] myArr = null;
			            if (record.size() > 0) {
			            	myArr = record.toArray();
			            }
					    return  myArr;
					    
					}
				}
				
			}
			catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "", e);
				return null;
			}
	  }
	  

	  
		public void resizeColumnWidth(JTable table) {
			try {
		    final TableColumnModel columnModel = table.getColumnModel();
		    for (int column = 0; column < table.getColumnCount(); column++) {
		        int width = 50; // Min width
		        for (int row = 0; row < table.getRowCount(); row++) {
		            TableCellRenderer renderer = table.getCellRenderer(row, column);
		            Component comp = table.prepareRenderer(renderer, row, column);
		            width = Math.max(comp.getPreferredSize().width +1 , width);
		        }
		        columnModel.getColumn(column).setPreferredWidth(width);
		    }
			} catch (Exception e) {
				Log.getLogger().log(Level.SEVERE, "", e);
			}
		}
		
		private int getCountDeleted(String TblName) {
			int iResult = 0;
			try (Connection connection = databaseConnector.getConnection())
			{
				String sqlStatement = "SELECT sum(" + GMSCH2Metadata.DELETED.columnName + ") DELETED FROM " + TblName+" WHERE " + GMSCH2Metadata.NEW.columnName + "=0 and "+ GMSCH2Metadata.CHANGED.columnName + "=0";
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet resultSet = h2dbStatement.executeQuery())
					{
						if (resultSet != null)
						{
							while (resultSet.next()){
								iResult = resultSet.getInt("DELETED");
							}
							resultSet.close();
							
						}//end if
					}
					

					// Close recordset and query
					if (h2dbStatement !=null) h2dbStatement.close(); 
					if (connection !=null ) connection.close();
					
				}
			}catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "", e);
				return 0;
			}

			return iResult;
		}
		
		
		private int getCountChanged(String TblName) {
			int iResult = 0;
			try (Connection connection = databaseConnector.getConnection())
			{
				String sqlStatement = "SELECT sum(" + GMSCH2Metadata.CHANGED.columnName + ") UPDATED FROM " + TblName+" WHERE " + GMSCH2Metadata.NEW.columnName + "=0 and "+ GMSCH2Metadata.DELETED.columnName + "=0";
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet resultSet = h2dbStatement.executeQuery())
					{
						if (resultSet != null)
						{
							while (resultSet.next()){
								iResult = resultSet.getInt("UPDATED");
							}
							resultSet.close();
							
						}//end if
					}
					
					if (h2dbStatement !=null) h2dbStatement.close();
					if (connection !=null ) connection.close();
					
				}
			}catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "", e);
				return 0;
			}

			return iResult;
		}
		
		
		private int getCountAdded(String TblName) {
			int iResult = 0;
			try (Connection connection = databaseConnector.getConnection())
			{
				String sqlStatement = "SELECT sum( " + GMSCH2Metadata.NEW.columnName + ") ADDED FROM " + TblName+" WHERE " + GMSCH2Metadata.CHANGED.columnName + "=0 and "+ GMSCH2Metadata.DELETED.columnName + "=0";
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet resultSet = h2dbStatement.executeQuery())
					{
						if (resultSet != null)
						{
							while (resultSet.next()){
								iResult = resultSet.getInt("ADDED");
							}
							resultSet.close();
							
						}//end if
					}
					
					if (h2dbStatement !=null) h2dbStatement.close();
					if (connection !=null ) connection.close();
					
				}
			}catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "", e);
				return 0;
			}

			return iResult;
		}
		
		private int getTotalCount(String TblName) {
			int iResult = 0;
			
			
			try (Connection connection = databaseConnector.getConnection())
			{
				String sqlStatement = "SELECT COUNT(*) TOTAL_RECORD FROM " + TblName;
				
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet resultSet = h2dbStatement.executeQuery())
					{
						if (resultSet != null)
						{
							while (resultSet.next()){
								iResult = resultSet.getInt("TOTAL_RECORD");
							}
							resultSet.close();
							
						}//end if
					}
					
					if (h2dbStatement !=null) h2dbStatement.close();
					if (connection !=null ) connection.close();
					
				}
			}catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "", e);
				return 0;
			}

			return iResult;
		}
		
	  @Override
		protected JComponent getDisplayView() {

		  /* Find all tables in Client H2 database and do count at each table*/
		  Object[][] data = new Object[][] { };
		  this.tableModel = new DefaultTableModel(data, new String[] { "",
		                  "Feature Name ", "Total Records","Added","Updated","Deleted"});
		  OffLinetable = new JTable(this.tableModel);
		  
		  Object[] myRs = getH2TableList();
		  int rowCounter = 1;
		  
		  if (myRs != null) {
				  for (int i = 0; i < myRs.length; i ++){
					  	
					    //String TblName = myRs[i].toString() + " - " + getFeatureName(myRs[i].toString());
						String TblName = getFeatureName(myRs[i].toString());
						if(TblName.length() > 0){
							//int iCount = getH2TblCount(myRs[i].toString());
						  	//Add summary of transaction for OFFLINE
						  	int iCount = getTotalCount(myRs[i].toString());
							int iAdded = getCountAdded(myRs[i].toString());
							int iUpdated = getCountChanged(myRs[i].toString());
							int iDeleted = getCountDeleted(myRs[i].toString());
							this.tableModel.addRow(new Object [] {rowCounter,TblName , iCount, iAdded, iUpdated, iDeleted}); 
							rowCounter++;
						}					  
				  }
				  
			  }
		  
		   
		  	resizeColumnWidth(OffLinetable);
		  	OffLinetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		  	
		  	//When selection changes, provide user with row numbers for
	        //both view and model.
		  	OffLinetable.getSelectionModel().addListSelectionListener(
	                new ListSelectionListener() {
	                    public void valueChanged(ListSelectionEvent event) {
	                        int viewRow = OffLinetable.getSelectedRow();
	                        // if having selection, check if selected row have changes item
	                        if (viewRow > 0) {
	                            
	                           // int modelRow =  OffLinetable.convertRowIndexToModel(viewRow);
	                           // System.out.println("value : " + OffLinetable.getValueAt(viewRow, 1));
	                            //process if selected with changes e.g. added, updated then auto display search result
	                            int icount = Integer.parseInt(OffLinetable.getValueAt(viewRow, 3).toString());
	                            icount += Integer.parseInt(OffLinetable.getValueAt(viewRow, 4).toString());
	                            if (icount > 0) {
	                            	  //System.out.println("Proceed Search");
	                            	  int answ = JOptionPane.showConfirmDialog(
	                            			    frame,
	                            			    "Would you like to list the changes (Added & Updated ONLY)?",
	                            			    "Offline DB Summary",
	                            			    JOptionPane.YES_NO_OPTION);

	                            	 // System.out.println("reply : " + answ);
	                            	  
	                            	  if (answ <=0) {
	                            		//if user reply yes, then proceed display  
	                            		  //1. get feature_name
	                            		  //2. H2DB query on added and updated
	                            		  //3. Put the resultset into Search Controller
	                            		  String[] sFeatureName =  OffLinetable.getValueAt(viewRow, 1).toString().split("-");
	                            		//  System.out.println("Return Value :" + sFeatureName[0].trim()+":");
	                            		  ListChangesIntoSearchController(sFeatureName[0].trim());
	                            		  //Auto Close MyDialog
	                            		   dispose();
	                            	  }
	                    
	                            }
	                        }
	                    }
	                }
	        );


			//table.getSelectionModel().addListSelectionListener(new MyDialog());
			
			JScrollPane scrollPane = new JScrollPane(OffLinetable);
			return scrollPane;
	  }
	  
	//function for OFFLINE Feature ONLY
		private static String getPrimaryKeyName(Feature feature)
		{
			return String.format("_%s_$$_GMSC_$$", feature.getPrimaryKey());
		}
		
	  private void ListChangesIntoSearchController(String FeatureName) {
		  try (Connection connection = databaseConnector.getConnection())
			{
				
				String sqlStatement = "SELECT CASE " + GMSCH2Metadata.NEW.columnName + " WHEN 1 THEN 'Added' END AS CHANGES, * FROM " + FeatureName
						+ " WHERE " + GMSCH2Metadata.NEW.columnName + " > 0 AND " + GMSCH2Metadata.CHANGED.columnName +" = 0 AND " + GMSCH2Metadata.DELETED.columnName + " = 0 UNION SELECT CASE " + GMSCH2Metadata.CHANGED.columnName +" WHEN 1 THEN 'Updated' END AS CHANGES, * FROM " + FeatureName
						+ " WHERE " + GMSCH2Metadata.CHANGED.columnName +" > 0 AND " + GMSCH2Metadata.NEW.columnName +" = 0 AND " + GMSCH2Metadata.DELETED.columnName + " = 0";
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet resultSet = h2dbStatement.executeQuery())
					{
						if (resultSet != null)
						{
							//if got result, call Handler Search Controller
							String[]  str = FeatureName.split("_");
							String toActiveFeatName = str[1].trim();
							
							//set Application references
							Project project = ApplicationContext.getProject();
							Feature toActivate = ApplicationContext.getProject().getFeatureByTitleOrID(toActiveFeatName);
							
							project.getFeatureByTitle(toActivate.getName()).setVisible(true);
							if (toActivate != null)
										project.getFeature(toActivate.getID()).setActive(true);
									
							handleSearchResult(toActivate,resultSet);
							
						}
						resultSet.close();
					}
					
					if (h2dbStatement != null) h2dbStatement.close();
					if (connection !=null ) connection.close();
				}
			}catch (SQLException e)
			{
				Log.getLogger().log(Level.SEVERE, "", e);
				
			}
	  }
	  
	  private void handleSearchResult(Feature activeFeature, ResultSet queryResult)
		{
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
					Log.getLogger().log(Level.SEVERE, "", e);
				}
				if (searchResult == null)
					return;
				
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
					searchController.setAppendResult(false);
					OfflineSearch OffSrch = new OfflineSearch(activeFeature.getID(), searchResult,"List changes");
					
					searchController.executeSearch(OffSrch, null, null);
				}
			}
			
			if (Assertion.isValid(elementSelection))
			{
				ApplicationContext.getBrowser().getMapSelectionCurator().select(elementSelection.toArray(new Object[elementSelection.size()]));
				ApplicationContext.getBrowser().fitSelection();
			}
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
	  
	  public void valueChanged(ListSelectionEvent e) {
//		    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
//		    
//		    System.out.println("total table : " + this.tableModel.getRowCount());
//		    
//		    int firstIndex = e.getFirstIndex();
//		    int lastIndex = e.getLastIndex();
//		    boolean isAdjusting = e.getValueIsAdjusting();
//		    System.out.println("Event for indexes " + firstIndex + " - " + lastIndex + "; isAdjusting is "
//		        + isAdjusting + "; selected indexes:");
//
//		    if (lsm.isSelectionEmpty()) {
//		      System.out.println(" <none>");
//		    } else {
//		      // Find out which indexes are selected.
//		      int minIndex = lsm.getMinSelectionIndex();
//		      int maxIndex = lsm.getMaxSelectionIndex();
//		      for (int i = minIndex; i <= maxIndex; i++) {
//		        if (lsm.isSelectedIndex(i)) {
//		          System.out.println(" " + i  +":" + this.tableModel.getValueAt(i, 1).toString());
//		        }
//		      }
//		    }
		  }
	  
	  private String getFeatureName(String H2TableName)
	  {
		  String FeatName = "";
		  String[] temp;
		  /* delimiter */
		  String delimiter = "_";
		  try
		  {
			  /* given string will be split by the argument delimiter provided. */
				temp = H2TableName.split(delimiter);
				if (temp.length >=2 ){
					//Feature feat = ApplicationContext.getProject().getFeatureByTitleOrID(temp[1]);
					// int iFeatID = Integer.parseInt(temp[1]);
					
					
					Feature feat = ApplicationContext.getProject().getFeatureByTitleOrID(temp[1].toString());
					FeatName = feat.getName();				  				 
				}
				
				if(IsLandBase(FeatName)){
					return "";
				}
				else{
					return RemoveSite(FeatName);
				}	

			}
		  catch (Exception e){
		  
			     //do nothing
				 return "";
		  }
		  
		  
	  }
	  
	//copied from MyDialogNew
	  private String RemoveSite(String featName){
			try {
				featName = featName.replace("(SNC-Site)","");
				featName = featName.replace("(Site)","");
				featName = featName.replace("- Site","");
			}
			catch (Exception e){
				Log.getLogger().log(Level.SEVERE, "", e);
				featName = "";
			}		
			return featName;
		}
	  
	  //copied from MyDialogNew
	  //check if feature is LandBase
      //if Land Base, remove from table
	  private boolean IsLandBase(String featName){
			
			String[] landBase ={
					//DUDE (Site)
					"LV Feeder Point Label (Site)",
					"POI Label (Site)",
					"Point of Interest (Site)",
					"Section Label (Site)",
					"Railroad (Site)",
					"Road Centerline (Site)",
					"Cadastral Lot (Site)",
					"PK Boundary (Site)",
					"Section Boundary (Site)",
					"Mukim Boundary (Site)",
					"State Boundary (Site)",
					"White Area Text (Site)",
					"White Area Line (Site)",
					"White Area Polygon (Site)",
					"My SW Design (Site)",
					"Customer (Site)",
					//DIST-SNC (Site)
					"My SNC Design (SNC-Site)",
					"POI Label (SNC-Site)",
					"Point of Interest (SNC-Site)",
					"Section Label (SNC-Site)",
					"Railroad (SNC-Site)",
					"Road Centerline (SNC-Site)",
					"Cadastral Lot (SNC-Site)",
					"PK Boundary (Site)",
					"Section Boundary (SNC-Site)",
					"Mukim Boundary (SNC-Site)",
					"State Boundary (SNC-Site)",
					"Customer (SNC-Site)"
					
			};
			
			boolean hasLandBase = Arrays.asList(landBase).contains(featName);
			
			if(hasLandBase){
				return true;	
			}
			else{
				return false;
			}		
		}
	  
	  @Override
		protected boolean isUserInputValid() {
			return true;
		}
}
