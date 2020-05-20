package com.intergraph.dude.scripting;

public class ParamPass
{
	//this class is to pass SWID and MOVETYPE to Redline PE/LVDB
	private static String swid;
	private static String moveType;
	
	//constructor
	public ParamPass()
	{
		if(swid == null){
			set_swid("0");
		}
		if(moveType == null){
			set_moveType("NONE");
		}				
	}

	public String get_swid()
	{
		return swid;
	}

	public void set_swid(String _swid)
	{
		ParamPass.swid = _swid;
	}

	public String get_moveType()
	{
		return moveType;
	}

	public void set_moveType(String _moveType)
	{
		ParamPass.moveType = _moveType;
	}
}
