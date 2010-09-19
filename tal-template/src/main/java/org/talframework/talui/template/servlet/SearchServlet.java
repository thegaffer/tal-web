/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.tal.aspects.annotations.HttpTrace;
import org.talframework.talui.template.render.codes.CodeType;
import org.talframework.talui.template.render.codes.CodeTypeFactoryLocator;

/**
 * This standard servlet serves up code types dynamically given
 * the parameters submitted.
 * 
 * @author Tom Spencer
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	@HttpTrace
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Parameters
		String prefix = req.getParameter("q");
		String typeName = req.getPathInfo();
		if( typeName.charAt(0) == '/' ) typeName = typeName.substring(1);
		
		Map<String, String> params = null;
		Enumeration e = req.getParameterNames();
		while(e.hasMoreElements()) {
			String param = (String)e.nextElement();
			if( !"q".equals(param) && !"type".equals(param) &&
					!"timestamp".equals(param) && !"limit".equals(param) ) {
				if( params == null ) params = new HashMap<String, String>();
				params.put(param, req.getParameter(param));
			}
		}
		
		StringBuilder buf = new StringBuilder();
		buf.append("{ \"results\" : [");
		CodeType type = CodeTypeFactoryLocator.getCodeType(typeName, req.getLocale(), req.getUserPrincipal(), params);
		if( type != null ) {
			String[] ids = type.getCodes();
			boolean first = true;
			if( ids != null ) {
				for( int i = 0 ; i < ids.length ; i++ ) {
					String desc = type.getCodeDescription(ids[i]);
					if( ids[i].toLowerCase().contains(prefix) || desc.toLowerCase().contains(prefix) ) {
						if( !first ) buf.append(", ");
						buf.append("{ \"id\": \"").append(ids[i]).append("\", \"label\": \"").append(desc).append("\" }");
						first = false;
					}
				}
			}
		}
		buf.append("]}");
		
		resp.setContentType("application/json");
		resp.getWriter().write(buf.toString());
	}
}
