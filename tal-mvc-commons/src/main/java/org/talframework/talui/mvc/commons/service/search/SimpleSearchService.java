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
 * This interface represents a simple search service and paging
 * service. It can be connected to a model via a simple search
 * resolver. 
 * 
 * @author Tom Spencer
 */
public interface SimpleSearchService {

	public SimpleSearchResults search(SimpleSearchRequest request);
}
