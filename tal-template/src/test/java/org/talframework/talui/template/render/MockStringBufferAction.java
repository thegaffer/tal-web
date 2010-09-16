/*
 * Copyright 2008 Thomas Spencer
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

package org.tpspencer.tal.template.render;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

/**
 * Simple helper class that returns a new StringBuilder
 * instance. Useful to mock out the render model when
 * testing.
 * 
 * @author Tom Spencer
 */
public class MockStringBufferAction implements Action {
	
	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	public void describeTo(Description description) {
		 description.appendText("returns a new string builder each time");

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jmock.api.Invokable#invoke(org.jmock.api.Invocation)
	 */
	public Object invoke(Invocation arg0) throws Throwable {
		return new StringBuilder();
	}
}
