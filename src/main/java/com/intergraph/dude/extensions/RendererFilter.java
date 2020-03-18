package com.intergraph.dude.extensions;

import java.util.Iterator;
import java.util.logging.Level;

import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.viewer.data.GLayerFilter;
 
public class RendererFilter {
	
	public RendererFilter(){
		
	}
	
	public void AutoRenderFeat() {
		
		try {
		
		// This is offline function
		//1.0 check if DB have any changes or new data
		MyH2Database myh2Db = new MyH2Database();
		Iterator <String> myFeatlist = myh2Db.getH2TableListHasChanges().iterator();
		
		//1.1 if found changes or new then create new legend "offline changes"
		while (myFeatlist.hasNext()) {
			//get Feature ID
			String featName = myFeatlist.next();
			//Log.getLogger().log(Level.INFO, "RendererFilter -- AutoRenderFeat feature id : " + featName);

			//1.2 Apply rendering agent
			String FeatID = featName.substring(featName.lastIndexOf('_') + 1);
			applyFilter(FeatID);
		}
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, "RendererFilter -- AutoRenderFeat  : ERROR :: ", e);
		}
	}
	
//	public void RenderFeatByID(String FeatID) {
//		applyFilter(FeatID);
		
//	}

	public void applyFilter( String featureId ){
	
		try {
			//String msg= "";
			//Log.getLogger().log(Level.INFO, "RendererFilter -- applyFilter : [ " + featureId + "]");

			//get feature info
			Feature feature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId); 
			boolean add = true;
			
			if ( feature != null ){	    
		    	GLayerFilter filter = feature.getFilter(); 
		    	while ( add && filter != null ){
		    		if ( filter instanceof SGIGLayerFilter ){
		    			add = false;
		    			//msg += " Filter not in SGIGLayerFilter";
		    		}
		   			filter = filter.getChildFilter();
		    	}//while
				
			    if ( add ){
			    	GLayerFilter defaultFilter = feature.getFilter();
			    	GLayerFilter offlineLayerFilter = new SGIGLayerFilter( featureId );
			    	
				    if(defaultFilter == null){
				           feature.addFilter(offlineLayerFilter);
				           feature.reload();
				          // msg +="|| Add to Filter";
		
				    } else{
				           defaultFilter.addChildFilter(offlineLayerFilter);
				         //  msg +="|| Add to child Filter";
					} //defaultfilter
				//} else {
					//msg +="|| No add";
				}
				
				//Log.getLogger().log(Level.INFO, "RendererFilter -- applyFilter  :  " + msg);
			}// if feature not null
		
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, "RendererFilter -- applyFilter  : ERROR :: ", e);
		}
		
	}//applyFilter

	
}
