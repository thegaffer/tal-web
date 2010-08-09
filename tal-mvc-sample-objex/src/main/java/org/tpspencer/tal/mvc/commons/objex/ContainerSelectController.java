package org.tpspencer.tal.mvc.commons.objex;

import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.input.InputModel;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexObj;

/**
 * Standard controller selects an objex object and then puts
 * it in the model. Care should be taken with this controller
 * because it potentially involves a remote lookup for the
 * container and object.
 * 
 * @author Tom Spencer
 */
public class ContainerSelectController implements Controller {

	/** Holds the name of the parameter that becomes the ID */
	private String idParameter = "id";
	/** Holds the name of the attribute to add object to (if not null) */
	private String modelAttribute = null;
	/** Holds the result */
	private String result = "selected";
	
	private ContainerLocator locator;
	
	public String performAction(Model model, InputModel input) {
		String id = input.getParameter(idParameter);
		
		if( id != null ) {
			Container container = locator.getContainer(model);
			ObjexObj obj = container.getObject(id);
			model.setAttribute(modelAttribute, obj);
		}
		else {
			model.removeAttribute(modelAttribute);
		}
		
		return result;
	}
}
