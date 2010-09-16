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

package org.tpspencer.tal.mvc.servlet.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;

/**
 * This class implements the ModelLayerResolver interface
 * for the servlet by storing all simple attributes as cookies.
 * Each cookie is given a name based on the model name and 
 * the attribute name. If there are any other model attributes
 * then they are stored in the session
 * 
 * @author Tom Spencer
 */
public class CookieModelAttributeResolver extends SessionModelAttributeResolver {
	private final static Log logger = LogFactory.getLog(CookieModelAttributeResolver.class);
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	public CookieModelAttributeResolver(HttpServletRequest request, HttpServletResponse response) {
		super(request);
		setIgnoreSimple(true);
		this.request = request;
		this.response = response;
	}
	
	/**
	 * Simply gets the layer from the session as required
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getModelAttributes(ModelConfiguration model) {
		// See if layer is in request already
		Map<String, Object> ret = (Map<String, Object>)request.getAttribute(model.getName());
		if( ret != null ) return ret;
		
		// Get any from session (non-simple)
		ret = super.getModelAttributes(model);
		
		// Find cookie for model if it exists
		Cookie cookie = getModelCookie(model);
		if( cookie != null && cookie.getValue() != null ) {
			String[] vals = cookie.getValue().split(",");
			if( vals != null && vals.length > 0 ) {
				for( int i = 0 ; i < vals.length ; i++ ) {
					// TODO: Clean this, firebug!!
					String val = vals[i];
					val = val.replaceAll("%3D", "=");
					val = val.replaceAll("%22", "");
					
					int index = val.indexOf('=');
					if( index > 0 && index < (val.length() - 1) ) {
						if( ret == null ) ret = new HashMap<String, Object>();
					
						String k = val.substring(0, index).trim();
						String v = val.substring(index +1).trim();
						ret.put(k, v);
					}
				}
			}
		}
		
		// Save model attrs away in case asked for in same request
		request.setAttribute(model.getName(), ret);
		
		return ret;
	}
	
	/**
	 * Internal helper to get the cookie for a particular model
	 */
	private Cookie getModelCookie(ModelConfiguration model) {
		Cookie ret = null;
		
		Cookie[] cookies = request.getCookies();
		if( cookies != null && cookies.length > 0 ) {
			for( int i = 0 ; i < cookies.length ; i++ ) {
				if( cookies[i].getName().equals(model.getName()) ) {
					ret = cookies[i];
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Simply saves the attributes in the session
	 */
	public void saveModelAttributes(ModelConfiguration model, Map<String, Object> attrs) {
		super.saveModelAttributes(model, attrs);
		
		// Save cookies and session if set
		if( isSaveMode() ) {
			StringBuilder buf = null;
			
			if( attrs != null ) {
				Iterator<String> it = attrs.keySet().iterator();
				while( it.hasNext() ) {
					String name = it.next();
					ModelAttribute attr = model.getAttribute(name);
					
					if( attr != null && !attr.isFlash() && attr.isSimple() ) {
						Object val = attrs.get(name);
						if( val != null ) {
							if( buf == null ) buf = new StringBuilder();
							else buf.append(", ");
							
							buf.append(name).append('=').append(val.toString());
						}
					}
				}
			}
			
			// Save the cookie away
			Cookie cookie = new Cookie(model.getName(), buf != null ? buf.toString() : "");
			cookie.setPath(request.getContextPath() + request.getServletPath());
			if( logger.isTraceEnabled() ) logger.trace("\t Adding cookie [" + cookie.getPath() + "/" + cookie.getName() + "] = " + cookie.getValue());
			response.addCookie(cookie);
		}
	}
	
	/**
	 * Removes the cookie if, and only if, already present
	 */
	public void removeModel(ModelConfiguration model) {
		super.removeModel(model);
		
		Cookie[] cookies = request.getCookies();
		if( cookies != null ) {
			for( int i = 0 ; i < cookies.length ; i++ ) {
				if( cookies[i].getName().equals(model.getName()) ) {
					Cookie cookie = new Cookie(model.getName(), "");
					cookie.setPath(request.getContextPath() + request.getServletPath());
					response.addCookie(cookie);
					break;
				}
			}
		}
	}
}
