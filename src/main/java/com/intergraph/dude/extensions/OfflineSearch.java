package com.intergraph.dude.extensions;

import com.intergraph.web.core.data.feature.FeatureID;
import com.intergraph.web.extension.search.ISearch;
import com.intergraph.web.extension.search.MissingSearchSelectionException;
import com.intergraph.web.extension.search.SearchException;
import com.intergraph.web.extension.search.SearchResult;

public class OfflineSearch implements ISearch
{
    private final FeatureID featureID;
    
    private final SearchResult searchResult;
    
    private final String TabSearchName;
    
    public OfflineSearch(FeatureID featureID, SearchResult searchResult, String TabSearchName)
    {
        this.searchResult = searchResult;
        this.featureID = featureID;
        this.TabSearchName = TabSearchName;
    }

    /**
     * @see com.intergraph.web.extension.search.ISearch#getId()
     */
    @Override
    public String getId()
    {
        return "07301dca-e59e-4088-8979-b4410c176fb5";
    }

    /**
     * @see com.intergraph.web.extension.search.ISearch#getName()
     */
    @Override
    public String getName()
    {
        return TabSearchName;
    }

    /**
     * @see com.intergraph.web.extension.search.ISearch#getDescription()
     */
    @Override
    public String getDescription()
    {
        return TabSearchName;
    }

    
    /**
     * @see com.intergraph.web.extension.search.ISearch#getSourceFeatureId()
     */
    @Override
    public FeatureID getSourceFeatureId()
    {
        return featureID;
    }

    /**
     * @see com.intergraph.web.extension.search.ISearch#getResultFeatureId()
     */
    @Override
    public FeatureID getResultFeatureId()
    {
        return featureID;
    }

    /**
     * @see com.intergraph.web.extension.search.ISearch#execute(java.util.Map, java.lang.Object[])
     */
    @Override
    public SearchResult execute(java.util.Map<String, String> searchCriterias, Object[] selection) throws SearchException, MissingSearchSelectionException
    {
        return searchResult;
    }

    /**
     * @see com.intergraph.web.extension.search.ISearch#getQueryType()
     */
    @Override
    public QueryType getQueryType()
    {
        return QueryType.CUSTOM;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((featureID == null) ? 0 : featureID.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OfflineSearch other = (OfflineSearch) obj;
        if (featureID == null)
        {
            if (other.featureID != null)
                return false;
        }
        else if (!featureID.equals(other.featureID))
            return false;
        return true;
    }
}
