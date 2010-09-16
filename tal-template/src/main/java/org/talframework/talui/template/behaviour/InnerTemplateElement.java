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

package org.tpspencer.tal.template.behaviour;

import org.tpspencer.tal.template.TemplateElement;

/**
 * This interface is a special behaviour interface that 
 * means the element should be ignored, but the inner
 * template should be referred to as is part of the
 * current template.
 * 
 * <p><b>Note: </b>This is a primary behaviour, while 
 * the element can implement other supporting or property
 * behaviours it cannot implement any of the other 
 * primary behaviours.</p>
 * 
 * @author Tom Spencer
 */
public interface InnerTemplateElement extends TemplateElement {

	/**
	 * @return The name of the inner template
	 */
	public String getTemplate();
}
