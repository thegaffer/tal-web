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

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This servlet serves up resources from JAR files directly.
 * This is useful because the web util project encourages you
 * to build UI components independantly of the final application
 * so that could intermix the same UI in multiple wars.
 * 
 * FUTURE: If failure, client is caching this and not re-requesting
 * 
 * @author Tom Spencer
 */
public class ResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(ResourceServlet.class);
	
	/** Member holds valid file extensions for resources we will serve up */
	private String[] validExtensions = new String[]{".css", ".js", ".properties", ".xml", ".png", ".gif", ".jpg"};
	/** Member holds the startup time of the servlet, used if cannot get last modified from resource */
	private long startupTime = 0L;
	
	/**
	 * Overridden to set the startup time which we use to stamp resources with
	 */
	public void init() throws ServletException {
		this.startupTime = System.currentTimeMillis();
		super.init();
	}

	/**
	 * Overridden to serve up the resource 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String path = request.getPathInfo();
	    if( logger.isTraceEnabled() ) logger.trace(">>> Starting resource request: " + path);
        
        /* failure conditions */
        if( path == null ) {
        	if( logger.isWarnEnabled() ) logger.warn("!!! No resource request");
        	response.sendError(406, "no path");
        	return;
        }
        else if( !isValidExtension(path) ) {
        	if( logger.isWarnEnabled() ) logger.warn("!!! Resource not allowed: " + path);
            response.sendError(403, path + " denied");
            return;
        }
        
        /* find the resource */
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path.substring(1));
        if (resource == null) {
        	if( logger.isWarnEnabled() ) logger.warn("!!! Resource not found: " + path);
            response.sendError(404, path + " not found on classpath");
            return;
        } 
        else {
            /* check modification date */
            URLConnection connection = resource.openConnection();
            if( Boolean.getBoolean("dev.env") ) connection.setUseCaches(false);
            // NOTE: Not checking last modified servlet is reloaded if we change
            
            /* write to response */
            response.setContentType(getServletContext().getMimeType(path));
            OutputStream out = new BufferedOutputStream(response.getOutputStream(), 512);
            InputStream in = new BufferedInputStream(resource.openStream(), 512);
            try {
                int len;
                byte[] data = new byte[512];
                while( (len = in.read(data)) != -1 ) {
                    out.write(data, 0, len);
                }
            } 
            finally {
                out.close();
                in.close();
                if (connection.getInputStream() != null) {
                    connection.getInputStream().close();
                }
            }
        }
        
        if( logger.isDebugEnabled() ) logger.debug("*** Served up resource: " + path);
        if( logger.isTraceEnabled() ) logger.trace("<<< Ending resource request: " + path);
	}
	
	/**
	 * Overridden to determine if neccessary to get the resource
	 */
	protected long getLastModified(HttpServletRequest req) {
		return this.startupTime;
	}
	
	/**
	 * Determines if the path is an allowed resource to serve up
	 * 
	 * @param path
	 * @return
	 */
	protected boolean isValidExtension(String path) {
		boolean ret = false;
		
		for( int i = 0 ; i < validExtensions.length ; i++ ) {
			if( path.endsWith(validExtensions[i]) ) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
}
