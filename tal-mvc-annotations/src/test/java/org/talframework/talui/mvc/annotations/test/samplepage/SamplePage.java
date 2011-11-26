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

import org.talframework.talui.mvc.annotations.Page;
import org.talframework.talui.mvc.annotations.PageWindow;
import org.talframework.talui.mvc.annotations.PerformWhen;
import org.talframework.talui.mvc.annotations.test.model.SamplePageModel;

/**
 * Demonstrates the use of the Page annotation.
 * 
 * <p>The Page annotation tells the framework that this
 * represents a page and the model for this page is
 * SamplePageModel.
 *
 * @author Tom Spencer
 */
@Page(model=SamplePageModel.class)
public interface SamplePage {

    /**
     * This tells the framework that there is a window on the
     * page called window1 and it is an instance of SampleWindow.
     * 
     * @return The window1 window instance
     */
    @PageWindow(shortName="win1")
    public SampleWindow getWindow1();
    
    /**
     * This tells the framework that there is another window 
     * on the page called window2 and it is also an instance of
     * SampleWindow. It's ok to have two windows of the same
     * type on the page.
     * 
     * @return The window1 window instance
     */
    @PageWindow(shortName="win1")
    public SampleWindow getWindow2();
    
    /**
     * This tells the framework to use the sample controller when the
     * action "submitForm" is received (and it not handled by the view
     * or the window first).
     * 
     * @return The onSubmitForm controller instance
     */
    @PerformWhen
    public SampleController getSubmitForm();
}
