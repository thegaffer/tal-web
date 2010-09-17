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

package org.talframework.talui.template.behaviour;

/**
 * This interface represents a template element that is simply
 * not a dynamic property, command or member property. Often a
 * group holds other elements (where it gets its name), but it
 * may also be simply a static element that should not be 
 * treated as a property (such an element typically also 
 * exhibits the ResourceProperty behaviour).
 * 
 * <p><b>Note: </b>This is a primary behaviour, while 
 * the element can implement other supporting or property
 * behaviours it cannot implement any of the other 
 * primary behaviours.</p>
 * 
 * @author Tom Spencer
 */
public interface GroupElement {

}
