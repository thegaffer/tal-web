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

import java.util.Map;

import org.talframework.talui.mvc.annotations.test.model.SampleTransactionModel;
import org.talframework.talui.mvc.annotations.view.View;

/**
 * Demonstrates the creation of a custom view. Here we have an
 * actual class whose prepare method is called during the 
 * rendering phase. It can optionally take in the model of the
 * view. It should return a map of objects that the template 
 * will have available.
 * 
 * <p>When using a DI wrapper of course this class will be created
 * by your IoC framework and injected with dependencies as 
 * required.</p>
 *
 * @author Tom Spencer
 */
@View(model=SampleTransactionModel.class, templateName="/jsp/custom.jsp")
public class CustomView {
    
    public Map<String, Object> prepare(SampleTransactionModel model) {
        // Set objects that will be part of the output
        return null;
    }
}
