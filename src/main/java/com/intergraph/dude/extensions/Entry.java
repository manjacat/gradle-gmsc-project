package com.intergraph.dude.extensions;


public class Entry
{
	public final String	id;
	
	public final String	name;
	
	public Entry(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[id: %s, name: %s]", getClass().getSimpleName(), id, name);
	}
}