/*
 * Copyright 2010 Thomas Spencer
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
package org.talframework.talui.mvc.annotations.test.samplepage;

import org.talframework.talui.mvc.annotations.view.View;

/**
 * Demonstrates the use of the View annotation in a simple view.
 * This simple view is just an interface because it delegates down
 * to a template straight away. It does not need to contain
 * any code. It even extract a couple of attributes and adds them
 * to the render model.
 *
 * @author Tom Spencer
 */
@View(templateName="/jsp/simple.jsp", renderAttributes={"attr1=someValue", "attr2"})
public interface SampleView {
}
