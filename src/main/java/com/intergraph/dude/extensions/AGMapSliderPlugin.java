package com.intergraph.dude.extensions;

import javax.swing.JFrame;

import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.ActionLocation;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;

@Plugin(alias = "AGMapInteractionSlider", vendor = "RedPlanet Corp.")
public class AGMapSliderPlugin extends AbstractPlugin {
	
	static String testMessage = "Tokuchi calling the Slider";	
	
	@Action(icon = "interface-circle.svg", actionLocation = ActionLocation.FAVORITES)
	public void TokuchiTest()
	{
		GUIToolkit.showInfo(testMessage);
		SliderTutorial gui = new SliderTutorial();
		//gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(300,100);		
		gui.setVisible(true);
		gui.setTitle("Slider Program");	
		
	}
	
}
