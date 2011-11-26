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

package org.talframework.talui.mvc.config;

/**
 * This interface represents a configuration item in a
 * Tal UI MVC Application.
 *
 * @author Tom Spencer
 */
public interface MVCConfig {

    /**
     * @return The name of the configuration item
     */
    public String getName();
    
    /**
     * @return The parent configuration item (if there is one)
     */
    public MVCConfig getParent();
    
    /**
     * @return The model for this configuration item (if there is one)
     */
    public ModelLayerConfig getModel();
}
