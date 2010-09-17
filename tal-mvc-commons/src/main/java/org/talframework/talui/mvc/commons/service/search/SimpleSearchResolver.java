/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.talui.mvc.commons.service.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.model.ModelResolver;

/**
 * This class implements the ModelResolver interface to allow
 * a search results attribute to be added to the model. The
 * resolver is configured with other (simple) model attributes
 * that are the potential search criteria (these are only 
 * added if they are non-null).
 * 
 * TODO: Settings errors ???
 * 
 * @author Tom Spencer
 */
public class SimpleSearchResolver implements ModelResolver {
	
	/** Holds the model attribute that holds the page */
	private String pageModelAttribute = null;
	/** Holds the model attribute that holds the page size */
	private String pageSizeModelAttribute = null;
	/** Holds the model attribute that holds the max results */
	private String maxResultsAttribute = null;
	/** Name of outer search bean in model instead of getting attributes directly */
	private String searchBean = null;
	/** Holds the model attributes that if set are search criteria */
	private List<String> searchCriteriaAttributes = null;
	
	/** Holds the search service */
	private SimpleSearchService searchService = null;
	
	public Object getModelAttribute(Model model, String name, Object param) {
		int page = pageModelAttribute != null ? getIntValue(model.getAttribute(pageModelAttribute), -1) : -1;
		int pageSize = pageSizeModelAttribute != null ? getIntValue(model.getAttribute(pageSizeModelAttribute), -1) : -1;
		
		// Setup the criteria
		Map<String, Object> criteria = null;
		if( searchCriteriaAttributes != null && searchCriteriaAttributes.size() > 0 ) {
			BeanWrapper criteriaWrapper = null;
			if( searchBean != null ) {
				criteriaWrapper = new BeanWrapperImpl(model.getAttribute(searchBean));
			}
			
			Iterator<String> it = searchCriteriaAttributes.iterator();
			while( it.hasNext() ) {
				String n = it.next();
				Object val = criteriaWrapper != null ? criteriaWrapper.getPropertyValue(n) : model.getAttribute(n);
				
				// Clear down strings if empty
				if( val instanceof String ) {
					val = val.toString().trim();
					if( val.toString().length() == 0 ) val = null;
				}
				
				if( val != null ) {
					if( criteria == null ) criteria = new HashMap<String, Object>();
					criteria.put(n, val);
				}
			}
		}
		
		SimpleSearchRequest req = new SimpleSearchRequestImpl();
		req.setPage(page);
		req.setPageSize(pageSize);
		if( criteria != null ) req.setSearchCriteria(criteria);
		
		SimpleSearchResults results = searchService.search(req);
		if( results != null ) {
			if( pageModelAttribute != null ) model.setAttribute(pageModelAttribute, Integer.toString(results.getPage()));
			if( maxResultsAttribute != null ) model.setAttribute(maxResultsAttribute, Integer.toString(results.getTotalResults()));
			return results.getResults();
		}
		else {
			return null;
		}
	}
	
	/**
	 * No nesting as this represents a remote call
	 */
	public boolean canNestResolver() {
		return false;
	}
	
	/**
	 * Helper to get an int value from an object
	 * 
	 * @param val The value to check
	 * @param def The default if null/not a number
	 * @return The value
	 */
	private int getIntValue(Object val, int def) {
		if( val == null ) return def;
		if( val instanceof Number ) return ((Number)val).intValue();
		
		try {
			return Integer.valueOf(val.toString());
		}
		catch( NumberFormatException e ) {
			return def;
		}
	}

	/**
	 * @return the pageModelAttribute
	 */
	public String getPageModelAttribute() {
		return pageModelAttribute;
	}

	/**
	 * @param pageModelAttribute the pageModelAttribute to set
	 */
	public void setPageModelAttribute(String pageModelAttribute) {
		this.pageModelAttribute = pageModelAttribute;
	}

	/**
	 * @return the pageSizeModelAttribute
	 */
	public String getPageSizeModelAttribute() {
		return pageSizeModelAttribute;
	}

	/**
	 * @param pageSizeModelAttribute the pageSizeModelAttribute to set
	 */
	public void setPageSizeModelAttribute(String pageSizeModelAttribute) {
		this.pageSizeModelAttribute = pageSizeModelAttribute;
	}

	/**
	 * @return the searchCriteriaAttributes
	 */
	public List<String> getSearchCriteriaAttributes() {
		return searchCriteriaAttributes;
	}

	/**
	 * @param searchCriteriaAttributes the searchCriteriaAttributes to set
	 */
	public void setSearchCriteriaAttributes(List<String> searchCriteriaAttributes) {
		this.searchCriteriaAttributes = searchCriteriaAttributes;
	}

	/**
	 * @return the searchService
	 */
	public SimpleSearchService getSearchService() {
		return searchService;
	}

	/**
	 * @param searchService the searchService to set
	 */
	public void setSearchService(SimpleSearchService searchService) {
		this.searchService = searchService;
	}

	/**
	 * @return the searchBean
	 */
	public String getSearchBean() {
		return searchBean;
	}

	/**
	 * @param searchBean the searchBean to set
	 */
	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
	}

	/**
	 * @return the maxResultsAttribute
	 */
	public String getMaxResultsAttribute() {
		return maxResultsAttribute;
	}

	/**
	 * @param maxResultsAttribute the maxResultsAttribute to set
	 */
	public void setMaxResultsAttribute(String maxResultsAttribute) {
		this.maxResultsAttribute = maxResultsAttribute;
	}
}
