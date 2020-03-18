package com.intergraph.dude.extensions;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.intergraph.tools.ui.DefaultDialog;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;

public class MyDialogNew extends DefaultDialog implements ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6072069398811140996L;
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
	
	public MyDialogNew()
	{	
		super(ApplicationContext.getMainFrame(), "MyDialogNew");
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected JComponent getDisplayView()
	{
		/* find all feature 		 */
		
		/* Find all tables in Client H2 database and do count at each table*/
		Object[][] data = new Object[][] { };
		this.tableModel = new DefaultTableModel(data, new String[] { "No",
				"Feature Name ", "Online","Offline"});
		OffLinetable = new JTable(this.tableModel);
		
		Object[] myRs = getH2TableList();
		
		int totalOnline = 0;
		int totalOffline = 0;
		int rowCounter = 1;
		
		if (myRs != null) {
			for (int i = 0; i < myRs.length; i ++){
				
				String TblName = getFeatureName(myRs[i].toString());
				
				//hide LandBase
				if(TblName.length() > 0){
					
					//Add summary of transaction for OFFLINE				
					int count1 = getTotalCount(myRs[i].toString());
					int iOnline = count1;
					int iOffline = 0;
					//remove checking
					iOffline = count1;
					
					//add to TOTAL
					totalOnline = totalOnline + iOnline;
					totalOffline = totalOffline + iOffline;					
					
					//buang perkataan (Site) dari FeatName
					String FeatName = RemoveSite(TblName);
					this.tableModel.addRow(new Object [] {rowCounter,FeatName , iOnline, iOffline}); 
					rowCounter++;
				}
				
			}
			
			//add totalColumn at the Top
			this.tableModel.insertRow(0, new Object[] {0, "TOTAL", totalOnline, totalOffline});
		}		  
		resizeColumnWidth(OffLinetable);
		JScrollPane scrollPane = new JScrollPane(OffLinetable);
		return scrollPane;
	}
	
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
	
	@Override
	protected boolean isUserInputValid()
	{
		return false;
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
	
	private int getTotalCount(String TblName) {
		int iResult = 0;
		
		try (Connection connection = databaseConnector.getConnection())
		{
			String sqlStatement = "SELECT * FROM " + TblName;
			
			try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
			{
				try (ResultSet resultSet = h2dbStatement.executeQuery())
				{
					if (resultSet != null)
					{
						java.sql.ResultSetMetaData rsmd = resultSet.getMetaData();
						for(int i = 1; i < rsmd.getColumnCount(); i++){
							String colName = rsmd.getColumnName(i); 
							Log.getLogger().log(Level.INFO, "", "test " + colName);
						}						
						
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
				return FeatName;
			}			
		}
		catch (Exception e){
			
			//do nothing
			return "";
		}
	}
	
	//check if feature is Land Base
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
}
