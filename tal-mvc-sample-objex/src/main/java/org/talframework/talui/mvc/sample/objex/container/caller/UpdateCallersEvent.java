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

package org.talframework.talui.mvc.sample.objex.container.caller;

import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.Event;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.query.DefaultQueryRequest;
import org.talframework.objexj.query.QueryResult;
import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.sample.model.contact.Caller;

public class UpdateCallersEvent implements EventHandler {
    
    private ContainerFactory factory;

    @Trace
    public void execute(Container container, Event event) {
        String firstName = event.getPayload("firstName");
        String lastName = event.getPayload("lastName");
        String account = event.getPayload("account");
        
        // Don't do anything if no caller details
        if( firstName == null && lastName == null ) return;
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        if( firstName != null ) parameters.put("firstName", firstName);
        if( lastName != null ) parameters.put("lastName", lastName);
        if( account != null ) parameters.put("account", account);
        
        QueryResult result = container.executeQuery(new DefaultQueryRequest("caller", 0, 1, parameters, null, false, null));
        
        // Add new contact if not found
        if( result.getResults() == null || result.getResults().size() == 0 ) {
            Container c = factory.open(container.getId());
            Caller caller = null; // c.newObject("Caller", c.getRootObject()).getBehaviour(Caller.class);
            throw new IllegalArgumentException("TODO: Creation of object in anon parent!");
            /*caller.setFirstName(firstName);
            caller.setLastName(lastName);
            caller.setAccount(account);
            c.saveContainer();*/
        }
    }

    /**
     * @return the factory
     */
    public ContainerFactory getFactory() {
        return factory;
    }

    /**
     * Setter for the factory field
     *
     * @param factory the factory to set
     */
    public void setFactory(ContainerFactory factory) {
        this.factory = factory;
    }
}
