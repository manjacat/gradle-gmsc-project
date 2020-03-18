package com.intergraph.dude.extensions;

public enum GMSCH2Metadata
{
	SIMPLEID,
	ID,
	NEW,
	CHANGED,
	DELETED,
	GEOMETRY;
	
	public final String	columnName;
	
	GMSCH2Metadata()
	{
		columnName = String.format("_%s_$$_GMSC_$$", this.name());
	}
}