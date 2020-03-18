package com.intergraph.dude.extensions;

import java.util.logging.Level;

import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.data.tree.IProjectTreeListener;
//import com.intergraph.web.core.data.tree.IProjectTreeListener.ProjectTreeUpdateEvent;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;

@Plugin(alias = "PluginTaskOnStartup Plugin", vendor = "Antaragrafik System Sdn Bhd")
public class PluginTaskOnStartup extends AbstractPlugin {
	public static String myTitle="";
	static String disclaimer = "This is TNB  information. Any modification, copying, reproduction, republication, uploading, \n"
			+ "posting, transmission, or distribution, in any manner, of the said information on the website,\n"
			+ "including text, graphics, code and/or software without permission in writing is prohibited.";
	static String testMessage = "Msg From New Java Class";
	
	@Override
	public void start() throws Exception
	{
		super.start();
		//new URL( "http://www.google.com" ).openConnection().setDefaultUseCaches( false ); //-->only needed to disable the java caching of urls
		myTitle = ApplicationContext.getDesktop().getMainFrame().getTitle();
		GUIToolkit.showInfo(testMessage);
		
		
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
	
	
	@Override
	public void loadOnStart() {
		try {
			
			//GUIToolkit.showInfo(disclaimer);

			//check if offline, if yes, prepare list of changes.
			 if (ApplicationContext.getInstance().isOffline())
			 {
				 final RendererFilter RF = new RendererFilter();
					
				 //call to Auto Render;
				 RF.AutoRenderFeat();
				 
				
				 ApplicationContext.getProject().addProjectTreeListener(new IProjectTreeListener()
			        {
			               @Override
			               public void treeChanged(ProjectTreeUpdateEvent event)
			               {
			            	   try {
				            	  // GUIToolkit.showInfo("treeChanged!");
				            	   if (event.getSource().isFeature() && event.getPropertyName().equals("active") && event.getSource().getFeature().getFeatureRight().toString() == "MODIFY")
				                   { 
				            		   RF.applyFilter(event.getSource().getFeature().getID().toString());
				                   }
				            	  
			            	   } catch (Exception e) {
				           			Log.getLogger().log(Level.SEVERE, "PluginTaskOnStartup (loadOnStart-treeChanged) : ERROR :: ", e);
				           	   }
			               }
			        });	


				
				 
				 // GUIToolkit.showInfo("End on plugin-startup!");
				 //} else {
				 //Online Prepare Symbology Metadata
				 // GUIToolkit.showInfo("End on plugin-startup!");
        	 
//			 } else {
//				 ApplicationContext.getProject().addProjectTreeListener(new IProjectTreeListener()
//			        {
//			               @Override
//			               public void treeChanged(ProjectTreeUpdateEvent event)
//			               {
//			            	   try {
//				            	 
//				            	   if (event.getSource().isStorageFeature())
//				            	   {
//				            		  Feature storageFeat = event.getSource().getFeature();
//				            		  //get bound and fit
//				            		  context.getBrowser().setMapBounds(storageFeat.getBounds());
//				            		 // GUIToolkit.showInfo("Tree Change " + event.getSource().getName());
//				            		   
//				            	   }
//			            	   } catch (Exception e) {
//				           			Log.getLogger().log(Level.SEVERE, "PluginTaskOnStartup (loadOnStart-treeChanged) : ERROR :: ", e);
//				           	   }
//			               }
//			        });	
				 
			
			 }
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, "loadOnStart: ERROR :: ", e);
		}
		
	}
}
	
	