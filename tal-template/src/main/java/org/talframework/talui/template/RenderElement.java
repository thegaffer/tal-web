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

package org.talframework.talui.template;

import java.io.IOException;

/**
 * This interface represents a basic building block that at
 * render time will produce output. The render elements are
 * chained to one another during compilation so that when we
 * want to render it is simply a question of finding the root
 * render element and telling it to render.
 * 
 * <p>It should be noted that a render element should be 
 * quite fine grained so they can be re-used. It is the 
 * render templates that should do the more complex task
 * of creating and arranging the render elements together.</p>
 * 
 * @author Tom Spencer
 */
public interface RenderElement {

	/**
	 * Called at compile time to add another element to 
	 * this element as a child element.
	 * 
	 * <p>This element retains control over whether the
	 * element is added, or if it added whether it is
	 * used at render time. So if this element is 
	 * configured to not render out then neither will its
	 * child elements.</p>
	 * 
	 * @param element The element to add to this one.
	 */
	public void addElement(RenderElement element);
	
	/**
	 * Called at render time to instruct the element to
	 * render itself and its children with its 
	 * pre-configured instructions.
	 * 
	 * @param model The dynamic render model to use in run
	 * @throws The raw IOException given by the writer in the render model 
	 */
	public void render(RenderModel model) throws IOException;
}
