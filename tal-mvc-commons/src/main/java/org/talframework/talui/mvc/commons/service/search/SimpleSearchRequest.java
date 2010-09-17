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

import java.util.Map;

/**
 * This interface represents a request for the simple
 * search controller. The input consists of the search
 * fields, and optional page and page size.
 * 
 * @author Tom Spencer
 */
public interface SimpleSearchRequest {

	/**
	 * @return The page that is being request, if -1 all results are requested
	 */
	public int getPage();
	
	/**
	 * @param page Set the page request (-1 for all results)
	 */
	public void setPage(int page);
	
	/**
	 * @return The size of each page requested
	 */
	public int getPageSize();
	
	/**
	 * @param size The size of each page request (ignored if not requesting specific page)
	 */
	public void setPageSize(int size);

	/**
	 * @return The search criteria in a name, value map
	 */
	public Map<String, Object> getSearchCriteria();
	
	/**
	 * @param criteria Set the search criteria in a name/value map
	 */
	public void setSearchCriteria(Map<String, Object> criteria);
}
