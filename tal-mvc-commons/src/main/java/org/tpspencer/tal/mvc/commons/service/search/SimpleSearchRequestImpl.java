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

package org.tpspencer.tal.mvc.commons.service.search;

import java.util.Map;

/**
 * Basic class implementing the SimpleSearchReques interface
 * 
 * @author Tom Spencer
 */
public class SimpleSearchRequestImpl implements SimpleSearchRequest {

	private int page = -1;
	private int pageSize = -1;
	private Map<String, Object> criteria;
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the criteria
	 */
	public Map<String, Object> getSearchCriteria() {
		return criteria;
	}
	/**
	 * @param criteria the criteria to set
	 */
	public void setSearchCriteria(Map<String, Object> criteria) {
		this.criteria = criteria;
	}
}
