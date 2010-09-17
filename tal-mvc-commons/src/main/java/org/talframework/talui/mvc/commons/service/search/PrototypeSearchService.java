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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.talframework.talui.mvc.commons.repository.RepositoryHolder;

/**
 * This class acts a simple search service for prototyping
 * purposes. It interacts with a repository to get all 
 * objects and then dynamically compares any search fields
 * for matches and only return the matches. The service
 * also supports paging the results.
 * 
 * <p>This class is purely for prototyping and is 
 * inefficient. A warning is put out in the logs when used
 * at construction time.</p>
 * 
 * @author Tom Spencer
 */
public class PrototypeSearchService extends RepositoryHolder implements SimpleSearchService {
	private static final Log logger = LogFactory.getLog(PrototypeSearchService.class);
	
	/** Member holds the mapping of search criteria to bean proeprty name */
	private Map<String, String> criteriaNameMapping = null;
	/** Member holds settings that determine how fields are compared */
	private Map<String, String> searchTypes = null;
	
	/**
	 * Default constructor - warns on usage
	 */
	public PrototypeSearchService() {
		logger.warn("*** Prototype Search Results in use - ensure this is replaced in Production Environment");
	}

	public SimpleSearchResults search(SimpleSearchRequest request) {
		if( request.getPage() > 0 && request.getPageSize() <= 0 ) throw new IllegalArgumentException("Cannot ask for search with a page number without providing page size");
		
		Object[] obj = getRepository().findAll(Object.class);
		
		SimpleSearchResults ret = null;
		if( obj == null ) {
			ret = new PrototypeSearchResults(1, request.getPageSize(), -1, null);
		}
		else {
			List<SearchCriteria> criteria = null;
			if( request.getSearchCriteria() != null ) {
				Map<String, Object> searchCriteria = request.getSearchCriteria();
				Iterator<String> it = searchCriteria.keySet().iterator();
				while( it.hasNext() ) {
					String name = it.next();
					Object comp = searchCriteria.get(name);
					if( comp == null ) continue;
					
					name = criteriaNameMapping != null && criteriaNameMapping.containsKey(name) ? criteriaNameMapping.get(name) : name;
					
					if( criteria == null ) criteria = new ArrayList<SearchCriteria>();
					criteria.add(new StringCriteria(name, comp.toString()));
					// TODO: Other types of criteria!!
				}
			}
			
			// TODO: Sort Comparator
			Set<Object> possibleResults = new HashSet<Object>();
			for( int i = 0 ; i < obj.length ; i++ ) {
				BeanWrapper bean = new BeanWrapperImpl(obj[i]);
			
				boolean match = criteria == null;
				if( criteria != null ) {
					Iterator<SearchCriteria> it = criteria.iterator();
					while( it.hasNext() ) {
						match = it.next().test(bean);
						if( !match ) break;
					}
				}
				
				if( match) possibleResults.add(bean.getWrappedInstance());
			}
			Object[] allResults = possibleResults.toArray();
			
			if( request.getPage() > 0 ) {
				int first = (request.getPage() - 1) & request.getPageSize();
				if( first < 0 || first >= allResults.length ) first = 0;
				
				int amount = request.getPageSize();
				if( first + amount >= allResults.length ) amount = allResults.length - first;
				
				Object[] res = new Object[amount];
				if( amount > 0 ) System.arraycopy(allResults, first, res, 0, amount);
				
				ret = new PrototypeSearchResults((first / request.getPageSize()) + 1, amount, allResults.length, res);
			}
			
			// Return all results
			else {
				ret = new PrototypeSearchResults(1, -1, allResults.length, allResults);
			}
		}
		
		return ret;
	}
	
	/**
	 * @return the criteriaNameMapping
	 */
	public Map<String, String> getCriteriaNameMapping() {
		return criteriaNameMapping;
	}

	/**
	 * @param criteriaNameMapping the criteriaNameMapping to set
	 */
	public void setCriteriaNameMapping(Map<String, String> criteriaNameMapping) {
		this.criteriaNameMapping = criteriaNameMapping;
	}

	/**
	 * Interface represents each search criteria
	 * 
	 * @author Tom Spencer
	 */
	public interface SearchCriteria {
		public boolean test(BeanWrapper bean);
	}
	
	/**
	 * Inner search criteria to test a string for equality
	 * 
	 * @author Tom Spencer
	 */
	public class StringCriteria implements SearchCriteria {
		private final String property;
		private final String match;
		
		public StringCriteria(String property, String match) {
			this.property = property;
			this.match = match;
		}
		
		public boolean test(BeanWrapper bean) {
			Object val = bean.getPropertyValue(property);
			if( val == null ) return false;
			return match.equals(val.toString());
		}
	}
	
	/**
	 * Implementation of the results interface
	 * 
	 * @author Tom Spencer
	 */
	public class PrototypeSearchResults implements SimpleSearchResults {
		private final int page;
		private final int pageSize;
		private final int totalResults;
		private final Object[] results;
		
		public PrototypeSearchResults(int page, int pageSize, int total, Object[] results) {
			this.page = page;
			this.pageSize = pageSize;
			this.totalResults = total;
			this.results = results;
		}

		/* (non-Javadoc)
		 * @see org.talframework.talui.mvc.commons.service.search.SimpleSearchResults#getPage()
		 */
		public int getPage() {
			return page;
		}

		/* (non-Javadoc)
		 * @see org.talframework.talui.mvc.commons.service.search.SimpleSearchResults#getPageSize()
		 */
		public int getPageSize() {
			return pageSize;
		}
		
		/* (non-Javadoc)
		 * @see org.talframework.talui.mvc.commons.service.search.SimpleSearchResults#isEor()
		 */
		public boolean isEor() {
			if( pageSize < 0 ) return true;
			else if( this.results != null && this.results.length < this.pageSize ) return true;
			else return false; 
		}
		
		/* (non-Javadoc)
		 * @see org.talframework.talui.mvc.commons.service.search.SimpleSearchResults#getTotalResults()
		 */
		public int getTotalResults() {
			return totalResults;
		}
		
		/* (non-Javadoc)
		 * @see org.talframework.talui.mvc.commons.service.search.SimpleSearchResults#getMaxPage()
		 */
		public int getMaxPage() {
			if( this.pageSize < 0 || this.totalResults < 0 ) return -1;
			
			return totalResults / pageSize;
		}

		/* (non-Javadoc)
		 * @see org.talframework.talui.mvc.commons.service.search.SimpleSearchResults#getResults()
		 */
		public Object[] getResults() {
			return results;
		}
	}
}
