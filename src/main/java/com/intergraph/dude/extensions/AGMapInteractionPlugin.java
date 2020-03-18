package com.intergraph.dude.extensions;

import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.utils.disptach.ActionDispatcher;
import com.intergraph.tools.utils.disptach.RPAction;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.ActionLocation;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.web.core.browsing.MapSelectionCurator;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;
import com.intergraph.web.plugin.quickpick.QuickPickPlugin;
import com.intergraph.web.plugin.tooltip.ExtendedTooltipPlugin;
import com.intergraph.web.viewer.map.GMap;
import com.intergraph.web.viewer.map.MapState;

@Plugin(alias = "AGMapInteraction", vendor = "Intergraph Corp.")
public class AGMapInteractionPlugin extends AbstractPlugin {

	private static final RPAction	action	= null;
	
	/**
	 * Fit Me - Fit current Active Element
	 * 
	 */
//	@Action(icon="expand-25.svg",actionLocation=ActionLocation.FAVORITES, activeRequired = true, minimumSelection = 1, maximumSelection = 1)
//	public void FitMe() {
//		// get selected elements
//		MapSelectionCurator mapSelectionCurator = ApplicationContext.getBrowser().getMapSelectionCurator();
//		//GMap map = ApplicationContext.getBrowser().getMap();
//		
//		Object[] selectedElementIds = mapSelectionCurator.getSelectedElements();
//		
//		if (selectedElementIds == null) return;
//		 if (!ApplicationContext.getInstance().isOffline())
//         {
//                ApplicationContext.getBrowser().fitSelection();
//              
//         }
//         else
//         {
//                offlineFitSelection();
//         }
//		 
//	}
	
	
	@Action(icon = "cursor7-blue.svg", actionLocation = ActionLocation.FAVORITES, activeRequired = true, minimumSelection = 1)
	public void ClearCurrentSelection()
	{
		// get selected elements
		MapSelectionCurator mapSelectionCurator = ApplicationContext.getBrowser().getMapSelectionCurator();
		Object[] selectedElementIds = mapSelectionCurator.getSelectedElements();
		
		if (selectedElementIds == null) return;
		
		//Else clear all selection
		 ApplicationContext.getBrowser().clearMapSelection();
		
	}
	
	
	@Action(icon = "selector-25.svg", actionLocation = ActionLocation.FAVORITES)
	public void SelectFeatureByPoint()
	{
		//abort everything first then only start
			GMap map = context.getMap();
			
			map.getMeasurementController().abortAll();
			if (map.getState() != MapState.IDLE)
				map.setState(MapState.IDLE);
			 ApplicationContext.getBrowser().clearMapSelection();
			 
			// MapSelectionTools myTools = new MapSelectionTools();
			 //myTools.setQuickPickMode(true);
			 //myTools.MAPSELECTPOINT(action);
			QuickPickPlugin myQuickPick =  (QuickPickPlugin) ApplicationContext.getPluginCurator().getByName("QuickPickPlugin");
			myQuickPick.quickPickWithPoint(action);
			
	}

//	@Action(icon = "map-25.svg", actionLocation = ActionLocation.FAVORITES)
//	public void SelectFeatureByMapExtent()
//	{
//		//abort everything first then only start
//			GMap map = context.getMap();
//			map.getMeasurementController().abortAll();
//			if (map.getState() != MapState.IDLE)
//				map.setState(MapState.IDLE);
//			 ApplicationContext.getBrowser().clearMapSelection();
//			 
//			// MapSelectionTools myTools = new MapSelectionTools();
//			 myTools.setQuickPickMode(true);
//			 myTools.MAPSELECTMAPRANGE(action);
//	}
//	
	@Action(icon = "selectorRect-25.svg", actionLocation = ActionLocation.FAVORITES)
	public void SelectFeatureByRectangular()
	{
		//abort everything first then only start
			GMap map = context.getMap();
			map.getMeasurementController().abortAll();
			if (map.getState() != MapState.IDLE)
				map.setState(MapState.IDLE);
			 ApplicationContext.getBrowser().clearMapSelection();
			 
			// MapSelectionTools myTools = new MapSelectionTools();
			// myTools.setQuickPickMode(true);
			// myTools.MAPSELECTRECTANGULAR(action);
			//ApplicationContext.getDesktop().getMainFrame().setTitle("HIHI CATHY");
			QuickPickPlugin myQuickPick =  (QuickPickPlugin) ApplicationContext.getPluginCurator().getByName("QuickPickPlugin");
			myQuickPick.quickPickWithRectangle(action);
			
	}
	
//	@Action(icon = "selectorPolygon-25.svg", actionLocation = ActionLocation.FAVORITES)
//	public void SelectFeatureByPolygon()
//	{
//		//abort everything first then only start
//			GMap map = context.getMap();
//			map.getMeasurementController().abortAll();
//			if (map.getState() != MapState.IDLE)
//				map.setState(MapState.IDLE);
//			 ApplicationContext.getBrowser().clearMapSelection();
//	
//			// MapSelectionTools myTools = new MapSelectionTools();
//			 myTools.setQuickPickMode(true);
//			 myTools.MAPSELECTPOLYGONAL(action);
//	}
	
	@Action(icon = "ExtTooltip_Point.svg", actionLocation = ActionLocation.FAVORITES)
	public void ExtendedTooltipByPoint()
	{
		//abort everything first then only start
			GMap map = context.getMap();
			map.getMeasurementController().abortAll();
			if (map.getState() != MapState.IDLE)
				map.setState(MapState.IDLE);

			//ApplicationContext.getDesktop().getMainFrame().setTitle("HIHI CATHY");
			ExtendedTooltipPlugin myToolTips =  (ExtendedTooltipPlugin) ApplicationContext.getPluginCurator().getByName("ExtendedTooltipPlugin");
			myToolTips.showTooltipsAtPoint();
	
	}
	
	@Action(icon = "ExtTooltip_Circle.svg", actionLocation = ActionLocation.FAVORITES)
	public void ExtendedTooltipByCircle()
	{
		//abort everything first then only start
			GMap map = context.getMap();
			map.getMeasurementController().abortAll();
			if (map.getState() != MapState.IDLE)
				map.setState(MapState.IDLE);

			//ApplicationContext.getDesktop().getMainFrame().setTitle("HIHI CATHY");
			ExtendedTooltipPlugin myToolTips =  (ExtendedTooltipPlugin) ApplicationContext.getPluginCurator().getByName("ExtendedTooltipPlugin");

			myToolTips.showTooltipsInCircle();
			
	}
	
	@Action(icon = "ExtTooltip_Rect.svg", actionLocation = ActionLocation.FAVORITES)
	public void ExtendedTooltipByRectangular()
	{
		//abort everything first then only start
			GMap map = context.getMap();
			map.getMeasurementController().abortAll();
			if (map.getState() != MapState.IDLE)
				map.setState(MapState.IDLE);

			//ApplicationContext.getDesktop().getMainFrame().setTitle("HIHI CATHY");
			ExtendedTooltipPlugin myToolTips =  (ExtendedTooltipPlugin) ApplicationContext.getPluginCurator().getByName("ExtendedTooltipPlugin");
			myToolTips.showTooltipsInArea();
			
	}
	
	@Action(icon = "ExtTooltip_Line.svg", actionLocation = ActionLocation.FAVORITES)
	public void ExtendedTooltipByLine()
	{
		//abort everything first then only start
			GMap map = context.getMap();
			map.getMeasurementController().abortAll();
			if (map.getState() != MapState.IDLE)
				map.setState(MapState.IDLE);

			//ApplicationContext.getDesktop().getMainFrame().setTitle("HIHI CATHY");
			ExtendedTooltipPlugin myToolTips =  (ExtendedTooltipPlugin) ApplicationContext.getPluginCurator().getByName("ExtendedTooltipPlugin");
			myToolTips.showTooltipsInLine();
			
	}
	
	/**
	 * Clear And Pan
	 * 
	 */
	@Action(icon = "palm.svg", actionLocation = ActionLocation.FAVORITES)
	public void ClearAndPan()
	{
		//abort everything first then only start
		
		GMap map = context.getMap();
		map.getMeasurementController().abortAll();
		if (map.getState() != MapState.IDLE)
			map.setState(MapState.IDLE);
		//Else clear all selection
		 context.getBrowser().clearMapSelection();
	}
	

	//OpenForm
	@Action(icon = "Brick-Edit-32.png", actionLocation = ActionLocation.PLUGINTAB, activeRequired = true, minimumSelection = 1, maximumSelection = 1)
	public void OfflineEditWorkFlowForm(){
		
		//Cath: Always get the default StyleID first
		//get curr Active Feature
		try {
			
			if (isEditableState()) {
				String ActiveFeatId = context.getBrowser().getActiveFeatureID().toString();
		    	String Cmd = DefaultEditCmd.valueOf("FEATURE_" + ActiveFeatId).toString();
		    	ActionDispatcher.fireAction(Cmd.toString());
		    	
			}
		}
		catch (Exception e)
        {
			String ActiveFeatId2 = context.getBrowser().getActiveFeatureID().toString();
	    	Feature feat = ApplicationContext.getProject().getFeatureByTitleOrID(ActiveFeatId2);
			String FeatName = feat.getName();
    	 GUIToolkit.showWarning("Sorry, edit function is not available for " + FeatName +".");
       }
		
	}
	
	private boolean isEditableState()
	{
		if ( context.getBrowser().getActiveFeature().getFeature().getLoader().isOfflineSupported()) {
			//String ActiveFeatId = context.getBrowser().getActiveFeatureID().toString();
			//GUIToolkit.showInfo("Feature " + ActiveFeatId + "is Offline supported");
			if (!ApplicationContext.getInstance().isOffline()) {
				GUIToolkit.showInfo("Sorry, this function are only available during offline.");
				return false;
			}
		};
		return true;
	}
	//OpenForm
	@Action(icon = "Brick-Add-32.png", actionLocation = ActionLocation.PLUGINTAB, activeRequired = true)
	public void OfflineAddWorkFlowForm(){
		
		//Cath: Always get the default StyleID first
		//get curr Active Feature
		try {
			//clear selection 
			if (isEditableState()) {
				ApplicationContext.getBrowser().clearMapSelection();
				String ActiveFeatId = ApplicationContext.getBrowser().getActiveFeatureID().toString();
		    	String Cmd = DefaultAddCmd.valueOf("FEATURE_" + ActiveFeatId).toString();
		    	ActionDispatcher.fireAction(Cmd.toString());
			}
		}
		catch (Exception e)
        {
			String ActiveFeatId2 = context.getBrowser().getActiveFeatureID().toString();
	    	Feature feat = ApplicationContext.getProject().getFeatureByTitleOrID(ActiveFeatId2);
			String FeatName = feat.getName();
    	 GUIToolkit.showWarning("Sorry, add/new function is not available for " + FeatName +".");
       }
		
	}
	
	
	
		
//	 private void offlineFitSelection()
//	    {
//	          Browser browser = ApplicationContext.getBrowser();
//	          Object[] selectedElements = browser.getMapSelectionCurator().getSelectedElements();
//	          
//	          if (!Assertion.isValid(selectedElements)) // nothing selected
//	                 return;
//	          
//	          Feature activeFeature = browser.getActiveFeature();
//	          try
//	          {
//	                 List<GPrimitive> selectedPrimitives = databaseConnector.getPrimitivesById(activeFeature.getUniqueId(), selectedElements);
//	                 
//	                 if (!Assertion.isValid(selectedPrimitives)) // no primitives found
//	                        return;
//	                 
//	                 GBounds fitbounds = GPrimitive.getMBR(selectedPrimitives.toArray(new GPrimitive[selectedPrimitives.size()]));
//	                 
//	                 // grow MBR by using the features fit factor
//	                 fitbounds.grow(activeFeature.getFitFactor());
//	                 
//	                 GMap map = browser.getMap();
//	                 long scale = map.getScaleForBounds(fitbounds);
//	                 
//	                 // restrict the scale to the features min- & maxscale
//	                 scale = Math.min(activeFeature.getMaxScale(), Math.max(activeFeature.getMinScale(), scale));
//	                 
//	                 map.setCenterAndScale(fitbounds.getCenterH(), fitbounds.getCenterV(), scale);
//	                 
//	          }
//	          catch (GetRangeException | SQLException e)
//	          {
//	                 Log.getLogger().log(Level.SEVERE, "", e);
//	          }
//	    } 

	
	 
	
}
