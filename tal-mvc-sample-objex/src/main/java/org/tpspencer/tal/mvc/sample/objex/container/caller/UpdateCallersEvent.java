package org.tpspencer.tal.mvc.sample.objex.container.caller;

import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.mvc.sample.model.contact.Caller;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.events.Event;
import org.tpspencer.tal.objexj.events.EventHandler;
import org.tpspencer.tal.objexj.locator.ContainerFactory;
import org.tpspencer.tal.objexj.query.DefaultQueryRequest;
import org.tpspencer.tal.objexj.query.QueryResult;
import org.tpspencer.tal.util.aspects.annotations.Trace;

@Trace
public class UpdateCallersEvent implements EventHandler {
    
    private ContainerFactory factory;

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
            EditableContainer c = factory.open(container.getId());
            Caller caller = c.newObject("Caller", c.getRootObject()).getBehaviour(Caller.class);
            caller.setFirstName(firstName);
            caller.setLastName(lastName);
            caller.setAccount(account);
            c.saveContainer();
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
