package com.hexagon.gmsc.sample;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.ui.dock.Dock;
import com.intergraph.tools.ui.dock.DockLayout.DockConstraint;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.ActionLocation;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;
import com.intergraph.web.core.warehouse.SettingWarehouse;
import com.intergraph.web.ui.SmartClientShortAccess;

import net.miginfocom.swing.MigLayout;

@Plugin(alias = "AGMapSliderPlugin", vendor = "RedPlanet Corp.")
public class AGMapSliderPlugin extends AbstractPlugin {

	//Temp: for testing only.
	//seems like I don't need to create this. will remove later
	
	private Dock userDock;
	private JPanel dockContentPane;
	static String testMessage = "Tokuchi calling the Slider";	
	
	@Action(actionLocation=ActionLocation.PLUGINTAB)
	public void RunSlider()
	{
		GUIToolkit.showInfo(testMessage);
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
		//Project currentProject = ApplicationContext.getProject();
		//DefaultMapProducer mapProducer = context.getMap().getMapProducer();
		//GMap gmap = context.getMap();
		//ProjectMetadata projectMetadata = currentProject.getMetadata();
		dockContentPane = new JPanel(new MigLayout("ins 0", "[][grow]"));
		dockContentPane.setOpaque(false);		
		//dockContentPane.add(new JLabel("Welcome " + ApplicationContext.getCredentials().getLoggedOnUser() + "@"+ projectMetadata.getName()), "grow, wrap");
		JSlider slider = new JSlider(0, 25000, 10000);
		slider.setOrientation(SwingConstants.HORIZONTAL);
		slider.setPreferredSize(new Dimension(350, 100));
		dockContentPane.add(slider);
		
		//JCheckBox MultiSelButton = new JCheckBox("Multiple element selection");		
		
	    return userDock = new Dock("Settings", dockContentPane);
		   
	}
	
	//@Action(icon = "interface-circle.svg", actionLocation = ActionLocation.FAVORITES)
	//public void TokuchiTest()
	//{
	//	//GUIToolkit.showInfo(testMessage);
	//	DefaultMapProducer mapProducer = context.getMap().getMapProducer();
	//	GMap gmap = context.getMap();
	//	
	//	SliderTutorial gui = new SliderTutorial(gmap);
	//	//gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	
	//}
	
}