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

/**
 * Interface representing the results of a search
 * 
 * @author Tom Spencer
 */
public interface SimpleSearchResults {

	/**
	 * @return The page returned (or -1 if all results)
	 */
	public int getPage();
	
	/**
	 * @return The page size
	 */
	public int getPageSize();
	
	/**
	 * @return If true then there are no more results
	 */
	public boolean isEor();
	
	/**
	 * @return The total amount of results
	 */
	public int getTotalResults();
	
	/**
	 * @return The highest page possible
	 */
	public int getMaxPage();
	
	/**
	 * @return The results
	 */
	public Object[] getResults();
}
