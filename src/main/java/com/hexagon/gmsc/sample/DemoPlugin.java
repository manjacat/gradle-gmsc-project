package com.hexagon.gmsc.sample;
import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;
import com.intergraph.tools.utils.log.Log;
import java.util.logging.Level;

@Plugin(alias = "Demo Plugin", vendor = "Hexagon Geospatial")
public class DemoPlugin extends AbstractPlugin {
	//Hello World!
	static String myTitle="";
	static String disclaimer = "This is TNB  information. Any modification, copying, reproduction, republication, uploading, \n"
			+ "posting, transmission, or distribution, in any manner, of the said information on the website,\n"
			+ "including text, graphics, code and/or software without permission in writing is prohibited.";
	
	@Override
	public void start() throws Exception
	{
		super.start();
		//new URL( "http://www.google.com" ).openConnection().setDefaultUseCaches( false ); //-->only needed to disable the java caching of urls
		myTitle = ApplicationContext.getDesktop().getMainFrame().getTitle();
		GUIToolkit.showInfo(disclaimer);
		
	}
	
	@Override
	public void stop() 
	{
		try { 
			//super.stop();
			//GUIToolkit.showInfo("Something TODO on plugin-shutdown!");
			//GUIToolkit.showYesNoCancelConfirmDialog("testing yes no cancel1");
			Log.getLogger().log(Level.INFO, "Application Exit Normally");
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, "PluginTaskOnStartup-stop(): ERROR :: ", e);
		}
	}	
	
}
