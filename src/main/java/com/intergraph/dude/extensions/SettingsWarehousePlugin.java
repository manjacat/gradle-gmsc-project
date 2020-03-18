package com.intergraph.dude.extensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointZShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PointShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonZShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineZShape;
import org.opengis.geom.SpatialReferenceIdentifier;

import com.intergraph.services.emea._2011._03.types.CoordinateSystem;
import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.ui.dock.Dock;
import com.intergraph.tools.ui.dock.DockLayout.DockConstraint;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.ActionLocation;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.data.GeodesyManager;
import com.intergraph.web.core.data.Project;
import com.intergraph.web.core.data.ProjectMetadata;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;
import com.intergraph.web.core.warehouse.SettingWarehouse;
import com.intergraph.web.core.warehouse.featurestorage.Storage;
import com.intergraph.web.core.warehouse.featurestorage.StorageException;
import com.intergraph.web.ui.SmartClientShortAccess;
import com.intergraph.web.viewer.data.GAction;
import com.intergraph.web.viewer.data.GBounds;
import com.intergraph.web.viewer.data.GCoordinate;
import com.intergraph.web.viewer.data.GPrimitive;
import com.intergraph.web.viewer.data.primitives.GPoint;
import com.intergraph.web.viewer.data.primitives.GPolygon;
import com.intergraph.web.viewer.data.primitives.GPolyline;
import com.intergraph.web.viewer.map.style.GFeatureTypeStyleManager;
import com.ionicsoft.sref.EPSGID;

import net.miginfocom.swing.MigLayout;

@Plugin(alias = "SettingsWarehouse", vendor = "Antaragrafik Sdn. Bhd.")
public class SettingsWarehousePlugin extends AbstractPlugin 
{
	private Dock userDock;
	private JPanel dockContentPane;
	private boolean bAllowMultipleSel;
	private static final UUID	sStyleId	= UUID.fromString("d0c11722-2e5d-400d-97fc-e9d7f230ee1f");	

	@Override
	public void loadOnStart() throws Exception 
	{
		if(SettingWarehouse.get("ShowUserInfo", false))
			bAllowMultipleSel = false;
			SmartClientShortAccess.getMapDockManager().addDock(getUserDock(), DockConstraint.TOP);
		
		ApplicationContext.getBrowser().getMap().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				//nothing to do				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				//nothing to do				
				try {
				if(! bAllowMultipleSel) {
					if (SwingUtilities.isRightMouseButton(e)) return;
					//get last id first, clear map, reselect the id
					 Object[] selectedElements = ApplicationContext.getBrowser().getMapSelectionCurator().getSelectedElements();
					 
					 if (selectedElements.length >= 1) {
						 
						// Object[] lastId = new Object[1];
						// lastId[0] = selectedElements[selectedElements.length-1];
			        	 context.getBrowser().clearMapSelection();
			        	 //ApplicationContext.getBrowser().getMapSelectionCurator().select(lastId);
			        	
			         }
				}
				} catch (Exception ex){
					Log.getLogger().log(Level.SEVERE, "Multiple Select - mousePressed  : ERROR :: ", ex.getMessage());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//nothing to do				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				//nothing to do				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//nothing to do		
				
			}
			
			
			
		});
	}	
	
	@Action(actionLocation=ActionLocation.PLUGINTAB)
	public void HandleUserInfo()
	{
		
		if(userDock == null)
		{
			SettingWarehouse.put("ShowUserInfo", true);
			SmartClientShortAccess.getMapDockManager().addDock(getUserDock(), DockConstraint.TOP);
		}
		else
		{
			SettingWarehouse.put("ShowUserInfo", false);
			SmartClientShortAccess.getMapDockManager().removeDock(userDock);
			userDock = null;
		}

	}
	
	private Dock getUserDock()
	{
		if(userDock!=null)
			return userDock;
		
		// get Project definition
		Project currentProject = ApplicationContext.getProject();
		/**
		 *  get ProjectMetadata which contains information like the name,
		 *  id, bounds, coordinate system and so on.
		 */
		ProjectMetadata projectMetadata = currentProject.getMetadata();
		dockContentPane = new JPanel(new MigLayout("ins 0", "[][grow]"));
		dockContentPane.setOpaque(false);		
		dockContentPane.add(new JLabel("Welcome " + ApplicationContext.getCredentials().getLoggedOnUser() + "@"+ projectMetadata.getName()), "grow, wrap");
		
		JCheckBox MultiSelButton = new JCheckBox("Multiple element selection");
		MultiSelButton.setSelected(bAllowMultipleSel);
		
		//Register a listener for the check boxes.
		MultiSelButton.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
              if ( e.getStateChange() == ItemEvent.SELECTED) 
            	  bAllowMultipleSel=true ;
              else 
            	  bAllowMultipleSel=false;
               
            }
        });
        
	    dockContentPane.add(MultiSelButton, "grow, wrap");	   
	   
	    //Button for Overlay Shape - Only display if the is CAS or CAS-Site Project
	    if (projectMetadata.getId().equals("cc4ca7a1-b471-46c2-8063-88668ec52271") || projectMetadata.getId().equals("87d3d825-1a5d-4249-98c3-ab06d1bcf81b"))
	    {
	    	try {
	    	
		    JButton cmdEditButton = new JButton("Overlay Shape File");
		    cmdEditButton.addActionListener(new ActionListener() { 
		    	  public void actionPerformed(ActionEvent e) { 
		    		  OpenShapeFileDialog();
		    	  } 
		    	} );
		    
		    dockContentPane.add(cmdEditButton, "grow, wrap");	   
	    	} catch (Exception e)
		    {
			 GUIToolkit.showWarning("Sorry, error :" + e.getMessage());
		    }
	    	
		 }

	    return userDock = new Dock("Settings", dockContentPane);
		   
	}
	
	private void ReadShpFile(String fName) throws InvalidShapeFileException, IOException {
		
		try {
			FileInputStream is = new FileInputStream(fName);
			ShapeFileReader r = new ShapeFileReader(is);
			 GCoordinate outCoord;
			 
		    ShapeFileHeader h = r.getHeader();
		    Log.getLogger().log(Level.INFO ,"Header info " + h.toString());
		    Log.getLogger().log(Level.INFO ,"The shape type of this files is " + h.getShapeType());

		    List<GPrimitive> ListofShp = new ArrayList<GPrimitive>();
		    
		    int total = 0;
		    AbstractShape s;
		    while ((s = r.next()) != null) {

		      switch (s.getShapeType()) {
		      case POINT:
		        PointShape aPoint = (PointShape) s;
		        // Do something with the point shape...
		        Log.getLogger().log(Level.INFO ,"Shape #" + total + ": Point - "+ aPoint.getX() + ", " + aPoint.getY());
		        outCoord = transformCoordinate(new GCoordinate( aPoint.getY(),  aPoint.getX()));
		        ListofShp.add(new GPoint(new GAction(UUID.randomUUID()), GFeatureTypeStyleManager.getInstance().getStyle(sStyleId),outCoord));
		         
		        break;
		      case MULTIPOINT_Z:
		        MultiPointZShape aMultiPointZ = (MultiPointZShape) s;
		        // Do something with the MultiPointZ shape...
		        Log.getLogger().log(Level.INFO ,"Shape #" + total + ": PointZ - "+ aMultiPointZ.getBoxMinX() + ", " + aMultiPointZ.getBoxMinY());
		       
		        outCoord = transformCoordinate(new GCoordinate(aMultiPointZ.getBoxMinY(),  aMultiPointZ.getBoxMinX()));
		        ListofShp.add(new GPoint(new GAction(UUID.randomUUID()), GFeatureTypeStyleManager.getInstance().getStyle(sStyleId),outCoord));
		       
		        break;
		      case POLYLINE:
		    	  PolylineShape aPolyline = (PolylineShape) s;
		    	  Log.getLogger().log(Level.INFO ,"Shape #" + total + ": Polyline - "
				            + aPolyline.getNumberOfParts() + " parts and "
				            + aPolyline.getNumberOfPoints() + " points");
		    	  for (int i = 0; i < aPolyline.getNumberOfParts(); i++) {
		    		  PointData[] points = aPolyline.getPointsOfPart(i);
		    		  Log.getLogger().log(Level.INFO ,"- part " + i + " has " + points.length
			              + " points.");
			          
			          List<GCoordinate> coordinates = new ArrayList<GCoordinate>();
			          
			          for (int j=0; j< points.length; j++)
			          {
			        	  Log.getLogger().log(Level.INFO ,"- point : " + j + " : " + points[j].getX() + ","+ points[j].getY() );
			        	  
			        	  outCoord = transformCoordinate(new GCoordinate(points[j].getY(),  points[j].getX()));
			        	  coordinates.add(outCoord);
			        	  Log.getLogger().log(Level.INFO ," --> " + outCoord.getX() + ","+ outCoord.getY() );
			          }
			          
			          ListofShp.add(new GPolyline(new GAction(UUID.randomUUID()), GFeatureTypeStyleManager.getInstance().getStyle(sStyleId),coordinates));
			          
		    	  }
		    	  break;
		      case POLYLINE_Z:
		    	  PolylineZShape aPolyZline = (PolylineZShape) s;
		    	  Log.getLogger().log(Level.INFO ,"Shape #" + total + ": Polyline - "
				            + aPolyZline.getNumberOfParts() + " parts and "
				            + aPolyZline.getNumberOfPoints() + " points");
		    	  for (int i = 0; i < aPolyZline.getNumberOfParts(); i++) {
		    		  PointData[] points = aPolyZline.getPointsOfPart(i);
		    		  Log.getLogger().log(Level.INFO ,"- part " + i + " has " + points.length
			              + " points.");
			          
			          List<GCoordinate> coordinates = new ArrayList<GCoordinate>();
			          
			          for (int j=0; j< points.length; j++)
			          {
			        	  Log.getLogger().log(Level.INFO ,"- point : " + j + " : " + points[j].getX() + ","+ points[j].getY() );
			        	  
			        	  outCoord = transformCoordinate(new GCoordinate(points[j].getY(),  points[j].getX()));
			        	  coordinates.add(outCoord);
			        	  Log.getLogger().log(Level.INFO ," --> " + outCoord.getX() + ","+ outCoord.getY() );
			          }
			          
			          ListofShp.add(new GPolyline(new GAction(UUID.randomUUID()), GFeatureTypeStyleManager.getInstance().getStyle(sStyleId),coordinates));
			          
		    	  }
		    	  break;
		      case POLYGON :
		    	  PolygonShape aPolygon = (PolygonShape) s;
		    	  Log.getLogger().log(Level.INFO ,"Shape #" + total + ": Polygon - "
			            + aPolygon.getNumberOfParts() + " parts and "
			            + aPolygon.getNumberOfPoints() + " points");
			        for (int i = 0; i < aPolygon.getNumberOfParts(); i++) {
			          PointData[] points = aPolygon.getPointsOfPart(i);
			          Log.getLogger().log(Level.INFO ,"- part " + i + " has " + points.length
			              + " points.");
			          
			          List<GCoordinate> coordinates = new ArrayList<GCoordinate>();
			          
			          for (int j=0; j< points.length; j++)
			          {
			        	  Log.getLogger().log(Level.INFO ,"- point : " + j + " : " + points[j].getX() + ","+ points[j].getY() );
			        	  
			        	  outCoord = transformCoordinate(new GCoordinate(points[j].getY(),  points[j].getX()));
			        	  coordinates.add(outCoord);
			        	  Log.getLogger().log(Level.INFO ," --> " + outCoord.getX() + ","+ outCoord.getY() );
			          }
			          
			          ListofShp.add(new GPolygon(new GAction(UUID.randomUUID()), GFeatureTypeStyleManager.getInstance().getStyle(sStyleId),coordinates));
			          
			        }
			        break;
		      case POLYGON_Z :
		    	  
		    	  //Draw new polygon
		    	  PolygonZShape aPolygonZ = (PolygonZShape) s;
		    	  Log.getLogger().log(Level.INFO ,"Shape #" + total + ": PolygonZ - "
			            + aPolygonZ.getNumberOfParts() + " parts and "
			            + aPolygonZ.getNumberOfPoints() + " points");
			        for (int i = 0; i < aPolygonZ.getNumberOfParts(); i++) {
			          PointData[] points = aPolygonZ.getPointsOfPart(i);
			          Log.getLogger().log(Level.INFO ,"- part " + i + " has " + points.length
			              + " points.");
			          
			          List<GCoordinate> coordinates = new ArrayList<GCoordinate>();
			          
			          for (int j=0; j< points.length; j++)
			          {
			        	  Log.getLogger().log(Level.INFO ,"- point : " + j + " : " + points[j].getX() + ","+ points[j].getY() );
			        	  
			        	  outCoord = transformCoordinate(new GCoordinate(points[j].getY(),  points[j].getX()));
			        	  
			        	  coordinates.add(outCoord);
			        	  Log.getLogger().log(Level.INFO ," --> " + outCoord.getX() + ","+ outCoord.getY() );
			          }
			          
			          ListofShp.add(new GPolygon(new GAction(UUID.randomUUID()), GFeatureTypeStyleManager.getInstance().getStyle(sStyleId),coordinates));
			          
			        }
			     
		        break;
		        
		      default:
		    	  GUIToolkit.showInfo("Read other type of shape. This application will not support");
		    	  
		      }
		      total++;
		      
		    }

		   // System.out.println("Total shapes read: " + total);
		    GUIToolkit.showInfo("Total shapes read: " + total);
		    configureMap(ListofShp,fName);
		    is.close();
		    
			}
			catch (Exception e)
		    {
			 GUIToolkit.showWarning("Error :" + e.getMessage());
			 Log.getLogger().log(Level.SEVERE, "ReadShpFile  : ERROR :: ", e);
		    }
		}
		
		
		private void configureMap(List<GPrimitive> gPrimitive, String FileName) throws StorageException
		{
			
			try {
				Storage storage = ApplicationContext.getBrowser().getFeatureWarehouse().getTemporaryStorage(FileName, sStyleId);
				storage.getFeature().setVisible(true);
				storage.clear();
				//Add in the list primitive
				Iterator <GPrimitive> iPrimitive = gPrimitive.iterator();
				while (iPrimitive.hasNext())
				{
					GPrimitive aPrimitive = iPrimitive.next();
					storage.add(aPrimitive);
				}
			
				//Zoom Fit
				GBounds boundsToFit = new GBounds();
				for (GPrimitive primitive : gPrimitive) {
					boundsToFit.union(primitive.getBounds());
				}
				
				context.getBrowser().setMapBounds(boundsToFit);
			}catch (Exception e)
	        {
	    	 GUIToolkit.showError("Sorry, No able to overlay!");
	    	 Log.getLogger().log(Level.SEVERE, "configureMap  : ERROR :: ", e);
	        }
			
		
		}
		
		public void OpenShapeFileDialog(){
			
			//Cath: Prompt function limitation and option for user if to proceed
			//If proceed choose shp file only
			//Check projection, if all meeting requirement only proceed
			try {
					boolean bOptYes = GUIToolkit.showYesNoConfirmDialog("Please note, this function only is limitted to read SHP file's geometry only, and coordinate projection must be in WGS84, Do you still want to proceed or not?");
				
					if (bOptYes) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
						FileNameExtensionFilter filter = new FileNameExtensionFilter("SHAPE FILES", "shp", "Shape");
						fileChooser.setFileFilter(filter);
						int result = fileChooser.showOpenDialog(ApplicationContext.getMainFrame());
						if (result == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();
							Log.getLogger().log(Level.INFO ,"Selected file: " + selectedFile.getAbsolutePath());
							ReadShpFile(selectedFile.getAbsolutePath());
						}
					}
					
			}
			catch (Exception e)
	        {
	    	 GUIToolkit.showError("Sorry, error occur in this function, either file format or coordinate system!");
	    	 Log.getLogger().log(Level.SEVERE, "OpenShapeFileDialog  : ERROR :: ", e);
	        }
			
		}
		
		private GCoordinate transformCoordinate(GCoordinate coordinateToTransform) {

			try {
				// get the display coordinate-system of the current project
				CoordinateSystem viewCoordinateSystem = ApplicationContext.getGeodesyManager().getDataCoordinateSystem();
				
				// get corresponding SpatialReferenceIdentifier
				SpatialReferenceIdentifier sourceSpatialReferenceIdentifier = GeodesyManager.getSpatialReferenceIdentifier(viewCoordinateSystem);
	
				 Log.getLogger().log(Level.INFO ,"Source coordinate-system: " + sourceSpatialReferenceIdentifier.toString() + " EPSG:" + sourceSpatialReferenceIdentifier.getEPSGID());
				 
				//transform the given coordinate from the display coordinate-system to WSG84
				return GeodesyManager.transformCoordinate(coordinateToTransform, EPSGID.WGS84, sourceSpatialReferenceIdentifier);
			}
			catch (Exception e)
	        {
		    	 GUIToolkit.showWarning("Sorry, edit function is not available for current feature.");
		    	 Log.getLogger().log(Level.SEVERE, "OpenShapeFileDialog  : ERROR :: ", e);
		    	 return null;
	        }

		}
}
