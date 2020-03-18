package com.intergraph.dude.extensions;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.intergraph.tools.ui.DefaultDialog;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;

public class CustomerDialog extends DefaultDialog implements ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4394441390044319177L;
	public static final String C_GMSC_FEAT_ID ="_ID_$$_GMSC_$$";
	public static final String C_GMSC_GEOM	="_GEOMETRY_$$_GMSC_$$";
	//public static final String C_MY_CUST_TBL	="FEATURE_446";
	
	private H2DatabaseConnector databaseConnector = (H2DatabaseConnector) DatabaseConnectorFactory.getInstance().getDatabaseConnector(
			H2DatabaseConnector.class);
	
	private String FeatID;
	private String FeatType;
	
	
	//public method to get the FeatID variable
	  public String getFeatID(){
	       return this.FeatID;
	  }

	  //public method to set the FeatID variable
	  public void setFeatID(String FeatID){
	       this.FeatID = FeatID;
	  }
	  
	//public method to get the FeatType variable
	  public String getFeatType(){
	       return this.FeatType;
	  }

	  //public method to set the FeatID variable
	  public void setFeatType(String FType){
	       this.FeatType = FType;
	  }
	  
	public void valueChanged(ListSelectionEvent e) {
//	    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
//
//	    int firstIndex = e.getFirstIndex();
//	    int lastIndex = e.getLastIndex();
//	    boolean isAdjusting = e.getValueIsAdjusting();
//	    System.out.println("Event for indexes " + firstIndex + " - " + lastIndex + "; isAdjusting is "
//	        + isAdjusting + "; selected indexes:");
//
//	    if (lsm.isSelectionEmpty()) {
//	      System.out.println(" <none>");
//	    } else {
//	      // Find out which indexes are selected.
//	      int minIndex = lsm.getMinSelectionIndex();
//	      int maxIndex = lsm.getMaxSelectionIndex();
//	      for (int i = minIndex; i <= maxIndex; i++) {
//	        if (lsm.isSelectedIndex(i)) {
//	          System.out.println(" " + i + ": value is -" );
//	        }
//	      }
//	    }
	  }
	

	  public CustomerDialog() {
		  
			super(ApplicationContext.getMainFrame(), "CustomerDialog");
			
		}
	  
	
		protected JComponent getDisplayView() {

		  
		  try (Connection connection = databaseConnector.getConnection())
			{
			  
			  
			  String FeatTbl = "FEATURE_" + FeatType;
			  String sqlStatement = "select _ID_$$_GMSC_$$, BCRM_VOLTAGE_DESCRIPTION,PREMISE_ID,SW_ID,REGION,CGIS_DNI_NO,LAST_NAME,BCRM_INSTALLATION_TYPE,CONTRACT_ACCOUNT_NUMBER,CGIS_CITY,BCRM_PREMISE_TYPE,REMARKS,FIRST_NAME,BCRM_REG_STRUCTURE_GROUP,JOB_NAME,JOB_STATUS,DEVICE_SERIAL_NUMBER,ZIP,ADDRESS_NUMBER,BCRM_CONNECTION_STATUS,BCRM_VOLTAGE_CODE from "+ FeatTbl + " where CGIS_DNI_NO = '" + FeatID + "'";
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet rs = h2dbStatement.executeQuery())
					{
						// It creates and displays the table
					    JTable table = new JTable(buildTableModel(rs));
					    
					    // close db related connection
					    if (rs !=null) rs.close();
					    if (h2dbStatement != null) h2dbStatement.close();
					    if (connection != null) connection.close();
					    
					    table.getSelectionModel().addListSelectionListener(new ActiveMyJob(FeatID));
					    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					    resizeColumnWidth(table);
					    // table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					    
					    JScrollPane scrollPane = new JScrollPane(table);
						
						return scrollPane;
						
					}
				}
			}
		
		  	
		catch (SQLException e)
		{
			Log.getLogger().log(Level.SEVERE, "", e);
			
		}
		return null;
		
	  }
	  
		public void resizeColumnWidth(JTable table) {
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
		}
		
	  public static DefaultTableModel buildTableModel(ResultSet rs)
		        throws SQLException {

		    ResultSetMetaData metaData = rs.getMetaData();
		    String[] myColName = { "ID", "Account No","Name", "DNI No.","No of Phases","Business Code","Customer Status","Type","Remark"};
		    // names of columns
		    Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();
		    //for (int column = 1; column <= columnCount; column++) {
		    //    columnNames.add(metaData.getColumnName(column));
		    //}

		    for (int icol=0; icol < myColName.length; icol++) {
		    	columnNames.add(myColName[icol]);
		    }
		    // data of the table
		    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		    while (rs.next()) {
		        Vector<Object> vector = new Vector<Object>();
		        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
		            vector.add(rs.getObject(columnIndex));
		        }
		        data.add(vector);
		    }

		    return new DefaultTableModel(data, columnNames);

		}
	 
	  @Override
		protected boolean isUserInputValid() {
			return true;
		}

}
