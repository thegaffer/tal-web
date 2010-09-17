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

package org.talframework.talui.mvc.servlet.util;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;

/**
 * Some basic utilities to help logging/tracing http requests
 * 
 * @author Tom Spencer
 */
public class RequestLogUtils {

	/**
	 * Helper to return a parameter as a string when it might
	 * be a string[].
	 * 
	 * @param params The map of parameters
	 * @param name The name
	 * @return The value
	 */
	public static String getParameter(Map<String, String[]> params, String name) {
		Object v = params.get(name);
		if( v instanceof String[] ) v = ((String[])v)[0];
		return v != null ? v.toString() : null;
	}
	
	/**
	 * Helper to debug out the request parameters and cookies
	 */
	public static void debugRequestParameters(HttpServletRequest req, Log logger) {
		if( logger.isDebugEnabled() ) logger.debug(logRequestParameters(req));
	}
	
	/**
	 * Helper to trace out the request parameters and cookies
	 */
	public static void traceRequestParameters(HttpServletRequest req, Log logger) {
		if( logger.isTraceEnabled() ) logger.trace(logRequestParameters(req));
	}
	
	/**
	 * Helper to trace out the request parameters and cookies 
	 * at error level. This is done is a general exception is
	 * caught.
	 */
	public static void errorRequestParameters(HttpServletRequest req, Log logger) {
		logger.error(logRequestParameters(req));
	}
	
	@SuppressWarnings("unchecked")
	private static String logRequestParameters(HttpServletRequest req) {
		StringBuilder buf = new StringBuilder();
		buf.append("Request Parameters: ");
		buf.append("\n\t**** Request Parameters ****");
		Enumeration e = req.getParameterNames();
		while( e.hasMoreElements() ) {
			String k = (String)e.nextElement();
			buf.append("\n\t").append(k).append("=").append(req.getParameter(k));
		}
		
		Cookie[] cookies = req.getCookies();
		if( cookies != null && cookies.length > 0 ) {
			for( int i = 0 ; i < cookies.length ; i++ ) {
				buf.append("\n\tcookie[").append(i).append("] ");
				if( cookies[i].getPath() != null ) buf.append(cookies[i].getPath()).append('/');
				buf.append(cookies[i].getName()).append('=');
				buf.append(cookies[i].getValue());
			}
		}
		
		buf.append("\n\t**** End Contents ****");
		return buf.toString();
	}
	
	public static void traceRequestAttributes(HttpServletRequest req, Log logger) {
		if( logger.isTraceEnabled() ) logger.trace(logRequestAttributes(req));
	}
	
	public static void debugRequestAttributes(HttpServletRequest req, Log logger) {
		if( logger.isDebugEnabled() ) logger.debug(logRequestAttributes(req));
	}
	
	public static void errorRequestAttributes(HttpServletRequest req, Log logger) {
		logger.error(logRequestAttributes(req));
	}
	
	@SuppressWarnings("unchecked")
	private static String logRequestAttributes(HttpServletRequest req) {
		StringBuilder buf = new StringBuilder();
		buf.append("Request Attributes: ");
		buf.append("\n\t**** Request Attributes ****");
		Enumeration e = req.getAttributeNames();
		while( e.hasMoreElements() ) {
			String k = (String)e.nextElement();
			buf.append("\n\t").append(k).append("=").append(req.getAttribute(k));
		}
		buf.append("\n\t**** End Contents ****");
		return buf.toString();
	}
	
	public static void traceSessionAttributes(HttpServletRequest req, Log logger) {
		if( logger.isTraceEnabled() ) logger.trace(logSessionAttributes(req));
	}
	
	public static void debugSessionAttributes(HttpServletRequest req, Log logger) {
		if( logger.isDebugEnabled() ) logger.debug(logSessionAttributes(req));
	}
	
	public static void errorSessionAttributes(HttpServletRequest req, Log logger) {
		logger.error(logSessionAttributes(req));
	}
	
	@SuppressWarnings("unchecked")
	private static String logSessionAttributes(HttpServletRequest req) {
		StringBuilder buf = new StringBuilder();
		buf.append("Session Contents: ");
		buf.append("\n\t**** Session Contents ****");
		Enumeration e = req.getSession().getAttributeNames();
		while( e.hasMoreElements() ) {
			String k = (String)e.nextElement();
			buf.append("\n\t").append(k).append("=").append(req.getSession().getAttribute(k));
		}
		buf.append("\n\t**** End Contents ****");
		return buf.toString();
	}
	
	public static void traceContextAttributes(ServletContext context, Log logger) {
		if( logger.isTraceEnabled() ) logger.trace(logContextAttributes(context));
	}
	
	public static void debugContextAttributes(ServletContext context, Log logger) {
		if( logger.isDebugEnabled() ) logger.debug(logContextAttributes(context));
	}
	
	public static void errorContextAttributes(ServletContext context, Log logger) {
		logger.error(logContextAttributes(context));
	}
	
	@SuppressWarnings("unchecked")
	private static String logContextAttributes(ServletContext context) {
		StringBuilder buf = new StringBuilder();
		buf.append("Context Contents: ");
		buf.append("\n\t**** Context Contents ****");
		Enumeration e = context.getAttributeNames();
		while( e.hasMoreElements() ) {
			String k = (String)e.nextElement();
			buf.append("\n\t").append(k).append("=").append(context.getAttribute(k));
		}
		buf.append("\n\t**** End Contents ****");
		return buf.toString();
	}
	
	public static void traceRequestHeaders(HttpServletRequest req, Log logger) {
		if( logger.isTraceEnabled() ) logger.trace(logRequestHeaders(req));
	}
	
	public static void debugRequestHeaders(HttpServletRequest req, Log logger) {
		if( logger.isDebugEnabled() ) logger.debug(logRequestHeaders(req));
	}
	
	@SuppressWarnings("unchecked")
	private static String logRequestHeaders(HttpServletRequest req) {
		StringBuilder buf = new StringBuilder();
		buf.append("Request Headers: ");
		buf.append("\n\t**** Request Headers ****");
		Enumeration e = req.getHeaderNames();
		while( e.hasMoreElements() ) {
			String k = (String)e.nextElement();
			buf.append("\n\t").append(k).append("=").append(req.getHeader(k));
		}
		
		buf.append("\n\t**** End Contents ****");
		return buf.toString();
	}
}
