package com.intergraph.dude.extensions;

import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.ActionLocation;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;


@Plugin(alias="OfflineDBSummary", vendor="Antaragrafik")
public class OfflineDBSummaryPlugin extends AbstractPlugin {
	
	@Action(icon="viewdtl.png",actionLocation=ActionLocation.MENUBARFAVORITS)
	public void startDisplay(){
		
		//kalau dalam Offline Mode, tunjuk cath punya plugin
		if (ApplicationContext.getInstance().isOffline()){
			MyDialog dialog = new MyDialog();

			dialog.setBounds(20, 30, 400, 300);
			
			dialog.setTitle("Offline DB Summary ");
			dialog.setVisible(true);
			
			//do nothing if dialog click cancel
			if (dialog.wasCanceled()) {
				//nothing
			}
		}
		//kalau ada Oracle Connection, tunjuk offline/online TOTAL
		else{
			MyDialogNew dialog = new MyDialogNew();

			dialog.setBounds(20, 30, 400, 300);
			
			dialog.setTitle("Sync Balance Check - DB Summary ");
			dialog.setVisible(true);
			
			//do nothing if dialog click cancel
			if (dialog.wasCanceled()) {
				//nothing
			}
		}		
	}
	

//	@Action(icon="application.png",actionLocation=ActionLocation.MENUBARFAVORITS)
//	public void startDisplay2(){
		
//		COPSelectionDialog dialog = new COPSelectionDialog(null, null, null);

		//dialog.setBounds(20, 30, 400, 300);
		
//		dialog.setTitle("My Multiple selection Summary ");
//		dialog.setVisible(true);
		
		//do nothing if dialog click cancel
//		if (dialog.wasCanceled()) {
			//nothing
//		}
//	}
	
	
}
