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

import org.talframework.talui.mvc.annotations.PerformWhen;
import org.talframework.talui.mvc.annotations.ShowWhen;
import org.talframework.talui.mvc.annotations.Window;
import org.talframework.talui.mvc.annotations.test.model.SampleWindowModel;

/**
 * Demonstrates the use of the Window annotation
 *
 * @author Tom Spencer
 */
@Window(model=SampleWindowModel.class)
public interface SampleWindow {

    /**
     * This tells the framework that there is a view on this
     * window called sampleView. We also in this case know its
     * an instance of SampleView. It should be shown when the
     * result of processing a controller is "someAction" or
     * "someOtherAction".
     * 
     * @return The window1 window instance
     */
    @ShowWhen({"someAction", "someOtherAction"})
    public SampleView getSampleView();
    
    /**
     * This tells the framework that there is a view on this
     * window called customView. We also in this case know
     * its an instance of CustomView. This is the default
     * view and should be shown when the result of processing
     * a controller is "action".
     * 
     * @return The window1 window instance
     */
    @ShowWhen(value="action", defaultView=true)
    public CustomView getCustomView();
    
    /**
     * This tells the framework that there is a controller
     * called onSubmitForm controller that should be fired
     * when /submitForm is seen. In this case we are 
     * actually stating the type of controller, a 
     * SampleController.
     *  
     * @return The controller to fire when the user submits the form
     */
    @PerformWhen("/submitForm")
    public SampleController getOnSubmitForm();
}
