package org.tpspencer.tal.mvc.commons.objex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.query.DefaultQueryRequest;
import org.talframework.objexj.query.QueryRequest;
import org.talframework.objexj.query.QueryResult;
import org.talframework.util.properties.PropertyUtil;
import org.tpspencer.tal.mvc.Model;

/**
 * This model resolver performs a search against using a query
 * on the given container.
 * 
 * TODO: Search parameters held in a single string
 * FUTURE: Search parameters held in a object?
 * TODO: Support handle (currentPage, nextPage?!?)
 * 
 * @author Tom Spencer
 */
public final class ObjexObjSearchResolver extends BaseObjexResolver {

	/** The name of the query to run on the container */
	private String queryName;
	/** The attribute holding the current offset (if null starts at first result) */
	private String offsetAttribute;
	/** The attribute holding the page size (if null defaults to 20) */
	private String sizeAttribute;
	/** The maximum results attribute (if null max results are not held) */
	private String maxResultsAttribute;
	/** The individual search attributes to pass into query (if they are set) */
	private String[] searchAttributes;
	/** Holds the map of searchAttribute names to their names in the query */
	private Map<String, String> searchParameters;
	/** Indicates if search is not performed if no parameters (this is the default) */
	private boolean ignoreIfNoParameters = true;
	/** The sort attributes (in order) */
	private String[] sortAttributes;
	
	public Object getModelAttribute(Model model, String name, Object param) {
		Container container = getContainer(model);
		
		QueryResult result = null;
		QueryRequest request = formRequest(model);
		if( request != null ) result = container.executeQuery(request);
		
		List<ObjexObj> ret = null;
		if( result != null ) {
		    if( maxResultsAttribute != null ) model.setAttribute(maxResultsAttribute, result.getMaxResults());
		    ret = result.getResults();
		}
		
		return ret;
	}
	
	/**
	 * Represents a DB operation so should not be nested
	 */
	public boolean canNestResolver() {
		return false;
	}
	
	/**
	 * Helper to form the search request.
	 * 
	 * @param model The model
	 * @return The search request (or null if search is invalid)
	 */
	private QueryRequest formRequest(Model model) {
	    int offset = PropertyUtil.getInt(offsetAttribute != null ? model.getAttribute(offsetAttribute) : null, 0);
        int pageSize = PropertyUtil.getInt(sizeAttribute != null ? model.getAttribute(sizeAttribute) : null, 20);
        boolean maxResultsRequired = maxResultsAttribute != null;
        
        Map<String, Object> params = formRequestParameters(model);
        if( params == null && ignoreIfNoParameters ) return null;
        
        return new DefaultQueryRequest(
                queryName, 
                offset, 
                pageSize, 
                params, 
                formRequestSort(model), 
                maxResultsRequired, 
                null);
	}
	
	/**
	 * Helper to form the request parameters
	 */
	private Map<String, Object> formRequestParameters(Model model) {
	    Map<String, Object> params = null;
        if( searchAttributes != null && searchAttributes.length > 0 ) {
            params = new HashMap<String, Object>();
            for( int i = 0 ; i < searchAttributes.length ; i++ ) {
                Object val = model.getAttribute(searchAttributes[i]);
                String name = searchAttributes[i];
                if( searchParameters != null && searchParameters.containsKey(name) ) name = searchParameters.get(name);
                if( val != null ) params.put(name, val);
            }
            if( params.size() == 0 ) params = null;
        }
        return params;
	}

	/**
	 * Helper to form the sort parameters
	 */
	private List<String> formRequestSort(Model model) {
	    List<String> ret = null;
	    
	    if( sortAttributes != null ) {
	        ret = new ArrayList<String>();
	        for( int i = 0 ; i < sortAttributes.length ; i++ ) {
	            Object val = model.getAttribute(sortAttributes[i]);
	            if( val != null ) ret.add(val.toString());
	        }
	        if( ret.size() == 0 ) return ret;
	    }
	    
	    return ret;
	}
	
	/////////////////////////////////////////////////////////
	// Getters & Setters

    /**
     * @return the queryName
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * Setter for the queryName field
     *
     * @param queryName the queryName to set
     */
    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    /**
     * @return the offsetAttribute
     */
    public String getOffsetAttribute() {
        return offsetAttribute;
    }

    /**
     * Setter for the offsetAttribute field
     *
     * @param offsetAttribute the offsetAttribute to set
     */
    public void setOffsetAttribute(String offsetAttribute) {
        this.offsetAttribute = offsetAttribute;
    }

    /**
     * @return the sizeAttribute
     */
    public String getSizeAttribute() {
        return sizeAttribute;
    }

    /**
     * Setter for the sizeAttribute field
     *
     * @param sizeAttribute the sizeAttribute to set
     */
    public void setSizeAttribute(String sizeAttribute) {
        this.sizeAttribute = sizeAttribute;
    }

    /**
     * @return the maxResultsAttribute
     */
    public String getMaxResultsAttribute() {
        return maxResultsAttribute;
    }

    /**
     * Setter for the maxResultsAttribute field
     *
     * @param maxResultsAttribute the maxResultsAttribute to set
     */
    public void setMaxResultsAttribute(String maxResultsAttribute) {
        this.maxResultsAttribute = maxResultsAttribute;
    }

    /**
     * @return the searchAttributes
     */
    public String[] getSearchAttributes() {
        return searchAttributes;
    }

    /**
     * Setter for the searchAttributes field
     *
     * @param searchAttributes the searchAttributes to set
     */
    public void setSearchAttributes(String[] searchAttributes) {
        this.searchAttributes = searchAttributes;
    }

    /**
     * @return the ignoreIfNoParameters
     */
    public boolean isIgnoreIfNoParameters() {
        return ignoreIfNoParameters;
    }

    /**
     * Setter for the ignoreIfNoParameters field
     *
     * @param ignoreIfNoParameters the ignoreIfNoParameters to set
     */
    public void setIgnoreIfNoParameters(boolean ignoreIfNoParameters) {
        this.ignoreIfNoParameters = ignoreIfNoParameters;
    }

    /**
     * @return the sortAttributes
     */
    public String[] getSortAttributes() {
        return sortAttributes;
    }

    /**
     * Setter for the sortAttributes field
     *
     * @param sortAttributes the sortAttributes to set
     */
    public void setSortAttributes(String[] sortAttributes) {
        this.sortAttributes = sortAttributes;
    }

    /**
     * @return the searchParameters
     */
    public Map<String, String> getSearchParameters() {
        return searchParameters;
    }

    /**
     * Setter for the searchParameters field
     *
     * @param searchParameters the searchParameters to set
     */
    public void setSearchParameters(Map<String, String> searchParameters) {
        this.searchParameters = searchParameters;
    }
    
}
