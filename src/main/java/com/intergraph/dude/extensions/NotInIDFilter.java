package com.intergraph.dude.extensions;

import com.intergraph.tools.utils.Str;
import com.intergraph.web.viewer.data.GDefaultDataLayer;
import com.intergraph.web.viewer.data.GLayerFilter;

/**
 * Filters all elements which are not in the given ID-list.
 * 
 * @author tanzinge
 * 
 */
public class NotInIDFilter extends GLayerFilter
{
	String[]	ids;
	
	public NotInIDFilter(String[] ids)
	{
		this.ids = ids;
	}
	
	/**
	 * @see com.intergraph.web.viewer.data.GLayerFilter#process(com.intergraph.web.viewer.data.GDefaultDataLayer,
	 *      com.intergraph.web.viewer.data.GLayerFilter.FilteredObject)
	 */
	@Override
	protected FilterType process(GDefaultDataLayer l, FilteredObject f)
	{
		String primid = f.getOriginal().getID();
		
		// not interrested in nonactive primitives
		if (primid == null)
			return FilterType.Original;
		
		// primitive found in id-list --> keep it
		if (Str.find(ids, primid) > -1)
			return FilterType.Original;
		
		return FilterType.Drop;
	}
	
	/**
	 * @see com.intergraph.web.viewer.data.GLayerFilter#clear()
	 */
	@Override
	protected void clear()
	{
	}
	
	/**
	 * @see com.intergraph.web.viewer.data.GLayerFilter#copy()
	 */
	@Override
	public GLayerFilter copy()
	{
		NotInIDFilter copy = new NotInIDFilter(this.ids);
		if (childFilter != null)
			copy.addChildFilter(childFilter.copy());
		
		return copy;
	}
}
