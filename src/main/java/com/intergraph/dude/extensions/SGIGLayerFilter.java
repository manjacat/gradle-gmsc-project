package com.intergraph.dude.extensions;


import java.util.UUID;
import java.util.logging.Level;

import com.intergraph.tools.utils.log.Log;
//import com.intergraph.sgi.offline.plugin.db.metadata.RPIFeature;
//import com.intergraph.sgi.offline.plugin.model.RPIFeatureModel;
//import com.intergraph.sgi.offline.plugin.model.RPISymbologyDisplayRule;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.viewer.data.GAction;
import com.intergraph.web.viewer.data.GDefaultDataLayer;
import com.intergraph.web.viewer.data.GLayerFilter;
import com.intergraph.web.viewer.data.GPrimitive;
import com.intergraph.web.viewer.map.style.GFeatureTypeStyleManager;

public class SGIGLayerFilter extends GLayerFilter 
{
	public String featureId = null;
	//private MyH2Database myH2DB = new MyH2Database();
	UUID GreenDot_Style = UUID.fromString("1b1d0aa0-9a3c-4868-9504-d32d71436b2d");
	
	
	public SGIGLayerFilter( String featureId ){
		this.featureId = featureId;
	}
	
	
    @Override
    protected FilterType process(GDefaultDataLayer layer, FilteredObject filteredObject) {
    	
    	FilterType rtn = FilterType.Original;
    	GPrimitive copiedPrimitive = null;
    	
    	try {
	    	//Log.getLogger().log(Level.INFO, "SGIGLayerFilter -FilterType  (process) --- featureId [" + featureId + "]");
			Feature fea = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);
	    	int nFeas = fea.getRowCount();
	    	if ( nFeas > 0 ){
		    	try {
			    	
		    		//Cath: Always get the default StyleID first
			    	UUID styleId = DefaultFeatureSymbologies.valueOf("FEATURE_" + featureId.trim()).toUUID();
	
			    	
			    	if ( layer != null && filteredObject != null ){
			    		GPrimitive toFilter = filteredObject.getOriginal();
			
			    		if ( rtn != null ){
			    			if ( rtn == FilterType.Changed ){
			    				toFilter = filteredObject.filtered;
			    			}
			    			//rtn =  FilterType.Original;
			    		}
			    		UUID id = toFilter.getStyle().getUniqueId();
			    		
			    		//check if style is green dot or green line or green area? then change to purple 
			    		//if ( !id.equals( styleId ) && myH2DB.HasChanges(featureId, toFilter.getID())){
			    		if (id.equals(GreenDot_Style)) {
			    			//Log.getLogger().log(Level.INFO, "SGIGLayerFilter  (process) --- change the style to " + styleId);
				    		GAction action = filteredObject.getOriginal().getAction().clone();
			    			copiedPrimitive = toFilter.copy(action,GFeatureTypeStyleManager.getInstance().getStyle(styleId), 0.0, 0.0);
			    	        filteredObject.original = copiedPrimitive;
			    		}
			    		
			    	} 
			          
			    } catch (Exception e) {
			    	Log.getLogger().log(Level.SEVERE, "SGIGLayerFilter (FilterType)- process : ERROR :: total record: " + nFeas + ", Feature : "+ featureId, e);
			    }
			}
    	} catch (Exception e) {
    		Log.getLogger().log(Level.SEVERE, "SGIGLayerFilter (FilterType)- process : ERROR :: ", e);
    	}
       // if(copiedPrimitive!=null){ 
       // 	rtn = GLayerFilter.FilterType.Original;
       // }
       return rtn;
    }
    
    
	
    @Override
    public GLayerFilter copy() {
          // TODO Auto-generated method stub
    	
    	GLayerFilter filter = new SGIGLayerFilter(featureId);
    	try {
	    	if ( getChildFilter() != null ){
	    		if ( ! (getChildFilter() instanceof SGIGLayerFilter )){
	    			filter.addChildFilter( getChildFilter().copy() );
	    		}
	    	}
    	} catch (Exception e){
    		Log.getLogger().log(Level.SEVERE, "SGIGLayerFilter (GLayerFilter- copy) : ERROR :: ", e);
    	}
    	
    	return filter;
    	
    }
    
    @Override
    protected void clear() {
          // TODO Auto-generated method stub
          
    }
}