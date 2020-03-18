package com.intergraph.dude.extensions;

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

import com.intergraph.tools.ui.DefaultDialog;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.plugin.offlinedatasupport.database.DatabaseConnectorFactory;
import com.intergraph.web.plugin.offlinedatasupport.database.H2DatabaseConnector;

public class ActiveMyJob extends DefaultDialog implements ListSelectionListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4616940609332604599L;
	public static final String C_GMSC_FEAT_ID ="_ID_$$_GMSC_$$";
	public static final String C_GMSC_GEOM	="_GEOMETRY_$$_GMSC_$$";

	private String FeatID;
	
	private H2DatabaseConnector databaseConnector = (H2DatabaseConnector) DatabaseConnectorFactory.getInstance().getDatabaseConnector(
			H2DatabaseConnector.class);
	
	
	
	public void valueChanged(ListSelectionEvent e) {
//	    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
//
//	  //  int firstIndex = e.getFirstIndex();
//	  //  int lastIndex = e.getLastIndex();
//	    
//	  //  boolean isAdjusting = e.getValueIsAdjusting();
//	   // System.out.println("Event for indexes " + firstIndex + " - " + lastIndex + "; isAdjusting is "
//	   //     + isAdjusting + "; selected indexes:");
//
//	    if (!lsm.isSelectionEmpty()) {
//	     
//	      // Find out which indexes are selected.
//	      int minIndex = lsm.getMinSelectionIndex();
//	      int maxIndex = lsm.getMaxSelectionIndex();
//	     
//          
//	      for (int i = minIndex; i <= maxIndex; i++) {
//	        if (lsm.isSelectedIndex(i)) {
//	          System.out.println(" " + i + ": value is -" + lsm.toString());
//	        }
//	      }//for
//	    } // if List has selection.
	    
	  }
	

	  public ActiveMyJob(String sFeatID) {
			super(ApplicationContext.getMainFrame(), "ActiveMyJob");
			
		}
	  
	  protected JComponent getDisplayView() {

		  try (Connection connection = databaseConnector.getConnection())
			{
			    String sqlStatement = "SELECT _ID_$$_GMSC_$$ as ID , JOB_NAME as Design , CASE WHEN ACTIVATED=1 THEN 'Yes' ELSE '-' END as \"Is Activated\"  FROM FEATURE_" + FeatID.toString() + " WHERE TO_SITE=1 ORDER BY ACTIVATED DESC, JOB_NAME";
				try (PreparedStatement h2dbStatement = connection.prepareStatement(sqlStatement))
				{
					try (ResultSet rs = h2dbStatement.executeQuery())
					{
						// It creates and displays the table
					    JTable table = new JTable(buildTableModel(rs));
					    table.getSelectionModel().addListSelectionListener(new ActiveMyJob(FeatID));
					    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						JScrollPane scrollPane = new JScrollPane(table);
						// Close recordset and query
						if (rs !=null)  rs.close(); 
						if (h2dbStatement !=null) h2dbStatement.close(); 
						if (connection !=null ) connection.close();
						
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
	  
	  public static DefaultTableModel buildTableModel(ResultSet rs)
		        throws SQLException {

		    ResultSetMetaData metaData = rs.getMetaData();

		    // names of columns
		    Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();
		    for (int column = 1; column <= columnCount; column++) {
		        columnNames.add(metaData.getColumnName(column));
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
