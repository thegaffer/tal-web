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

package org.talframework.talui.mvc.servlet.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.process.ModelLayerAttributesResolver;

/**
 * This class implements the ModelLayerResolver interface
 * for the servlet by simply storing all layers in the
 * session as attributes.
 * 
 * @author Tom Spencer
 */
public class SessionModelAttributeResolver implements ModelLayerAttributesResolver {
	/** Determines if simple attributes should be ignored as well as flash */
	private boolean ignoreSimple = false;
	/** Determines if we are saving changes (renders do not save changes) */
	private boolean saveMode = true;
	/** The request */
	private final HttpServletRequest request;
	
	public SessionModelAttributeResolver(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * Simply gets the layer from the session as required
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getModelAttributes(ModelConfiguration model) {
		Map<String, Object> ret = (Map<String, Object>)request.getAttribute(model.getName());
		if( ret != null ) return ret;
		
		// Otherwise see if on session
		HttpSession session = request.getSession(false);
		if( session != null ) ret = (Map<String, Object>)session.getAttribute(model.getName());
		
		return ret;
	}
	
	/**
	 * Simply saves the attributes in the session
	 */
	public void saveModelAttributes(ModelConfiguration model, Map<String, Object> attrs) {
		// Save all on this request for future access in same request
		request.setAttribute(model.getName(), attrs);
		
		if( saveMode ) {
			// Strip out any flash attributes
			Map<String, Object> saveAttrs = null;
			if( attrs != null ) {
				Iterator<String> it = attrs.keySet().iterator();
				while( it.hasNext() ) {
					String k = it.next();
					Object v = attrs.get(k);
					if( v != null ) {
						ModelAttribute attr = model.getAttribute(k);
						if( !attr.isFlash() && (!ignoreSimple || !attr.isSimple()) ) {
							if( saveAttrs == null ) saveAttrs = new HashMap<String, Object>();
							saveAttrs.put(k, v);
						}
					}
				}
			}
		
			HttpSession session = request.getSession(saveAttrs != null);
			if( saveAttrs != null ) session.setAttribute(model.getName(), saveAttrs);
			else if( session != null ) session.removeAttribute(model.getName());
		}
	}
	
	/**
	 * Removes from session and the request
	 */
	public void removeModel(ModelConfiguration model) {
		HttpSession session = request.getSession(false);
		if( session != null ) session.removeAttribute(model.getName());
		
		request.removeAttribute(model.getName());
	}
	
	/**
	 * Sets the save mode. If saving is off no attributes are
	 * saved on the session, but are stored in request for future
	 * retreival in same request cycle.
	 */
	public void setSaveMode(boolean on) {
		saveMode = on;
	}
	
	/**
	 * @return True if we are saving changes, false otherwise
	 */
	public boolean isSaveMode() {
		return saveMode;
	}
	
	/**
	 * @param ignoreSimple True if this class should also ignore simple attrs
	 */
	public void setIgnoreSimple(boolean ignoreSimple) {
		this.ignoreSimple = ignoreSimple;
	}
}
