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
package org.talframework.talui.mvc.annotations.test;

import org.talframework.talui.mvc.annotations.App;
import org.talframework.talui.mvc.annotations.test.model.SampleAppModel;

/**
 * This demonstrates the use of the App annotation. In
 * this case we are just showing the app has a model. We
 * could have some controllers on the app just like a
 * page, but this is not shown.
 *
 * @author Tom Spencer
 */
@App(model=SampleAppModel.class)
public interface SampleApp {
}