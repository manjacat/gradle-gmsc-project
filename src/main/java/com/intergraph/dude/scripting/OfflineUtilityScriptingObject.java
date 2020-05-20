package com.intergraph.dude.scripting;


/*
 * Created by KHAIRIL
 * Date 11/5/2019
 * */

import java.util.logging.Level;

import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.utils.disptach.ActionDispatcher;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.plugin.webbrowser.handler.AbstractScriptingObject;
import com.intergraph.web.plugin.webbrowser.handler.ScriptingObject;

@ScriptingObject(name = "OfflineUtility")
public class OfflineUtilityScriptingObject extends AbstractScriptingObject
{
	//kena create static class baru boleh kekalkan value
	//kalau tak static nanti value hilang
	public static ParamPass myParamPass = new ParamPass();	
	
	//to call this function in JavaScript, use OfflineUtility.redirectOffline
	public void redirectOffline(final String actionName, final String vSwid, final String vMoveType){
		
		try{
			//store value of SWID and MoveType
			myParamPass.set_swid(vSwid);
			myParamPass.set_moveType(vMoveType);
				
			//GUIToolkit.showInfo();
			
			GUIToolkit.showInfo("Copied " + vMoveType + "'s SWID: " + vSwid);
//			Log.getLogger().log(Level.FINE, "Redirect to " + actionName 
//					+ " SWID is " + myParamPass.get_swid()
//					+ " MoveType is " + myParamPass.get_moveType());

	    	ActionDispatcher.fireAction(actionName);
		}
		catch (Exception e) {
			GUIToolkit.showError(e.getMessage());
			Log.getLogger().log(Level.SEVERE, "OfflineUtility.redirectOffline: ERROR :" + e.toString());
		}
	}
	
	//to call this function in JavaScript, use OfflineUtility.GetSWID
	public String GetSWID(){
		try{
			String mySWID = myParamPass.get_swid();
			//GUIToolkit.showInfo("SWID value is " + mySWID);
			Log.getLogger().log(Level.FINE, "OfflineUtility.GetSWID: SWID is " + mySWID);
	    	return mySWID;
		}
		catch (Exception e) {
			GUIToolkit.showError(e.getMessage());
			Log.getLogger().log(Level.SEVERE, "OfflineUtility.GetSWID ERROR : " + e.toString());
			return "Error SWID";
		}
	}
	
	//to call this function in JavaScript, use OfflineUtility.GetMoveType
	public String GetMoveType(){
		try{
			String myMovetype = myParamPass.get_moveType();
			//GUIToolkit.showInfo("SWID value is " + myMovetype);
			Log.getLogger().log(Level.FINE, "redirectOffline.Movetype found. Movetype is ", myMovetype);
	    	return myMovetype;
		}
		catch (Exception e) {
			GUIToolkit.showError(e.getMessage());
			Log.getLogger().log(Level.SEVERE, "OfflineUtility.GetMoveType: ERROR: " + e.toString());
			return "Error Movetype";
		}
	}
	
	//to call this function in JavaScript, use OfflineUtility.GetTotalCount
	public String GetTotalCount(String vFeatureId, String vSwid)
	{
		try
		{
			String retString = "";
			Log.getLogger().log(Level.FINE, "OfflineUtility.GetTotalCount: vFeature is :" + vFeatureId + ", SwID is " + vSwid);		
			DynamicActiveJobScriptingObject dy = new DynamicActiveJobScriptingObject();
			retString = dy.getRowCountbySwid(vFeatureId, vSwid);
			Log.getLogger().log(Level.FINE, "OfflineUtility.GetTotalCount: RetString is :" + retString);
			return retString;			
		}
		catch(Exception e)
		{
			GUIToolkit.showError(e.getMessage());
			Log.getLogger().log(Level.SEVERE, "OfflineUtility.GetTotalCount: ERROR: " + e.toString());
			return "0";
		}		
	}
}

