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

package org.tpspencer.tal.template.render;

import java.util.Iterator;
import java.util.Map;

/**
 * This class implements the URL Generator with a simple 
 * string and is useful for test purposes.
 * 
 * @author Tom Spencer
 */
public class TestUrlGenerator implements UrlGenerator {
	
	private final String app;
	private final String page;
	private final String window;
	
	private final StringBuilder temp;
	
	public TestUrlGenerator() {
		this.app = "testApp";
		this.page = "testPage";
		this.window = "testWindow";
		temp = new StringBuilder();
	}
	
	public TestUrlGenerator(String app, String page, String window) {
		this.app = app;
		this.page = page;
		this.window = window;
		temp = new StringBuilder();
	}
	
	private StringBuilder getTempBuffer() {
		temp.setLength(0);
		return temp;
	}

	public String generateResourceUrl(String resource) {
		return getTempBuffer().append("/").append(resource).toString();
	}
	
	public String generateResourceUrl(String resource,
			Map<String, String> params) {
		StringBuilder buf = getTempBuffer();
		buf.append("/").append(resource);
		addParams(buf, params);
		return buf.toString();
	}
	
	public String generateActionUrl(String action, Map<String, String> params) {
		StringBuilder buf = getTempBuffer();
		buf.append("/").append(app);
		buf.append("/").append(page);
		buf.append("/").append(window);
		buf.append("/").append(action);
		addParams(buf, params);
		return buf.toString();
	}
	
	public String generateTemplateUrl(String templateName, String renderType) {
		StringBuilder buf = new StringBuilder();
		buf.append("/template/").append(templateName).append(".").append(renderType);
		return buf.toString();
	}
	
	public String generatePageUrl(String page, String target, String action, Map<String, String> params) {
		StringBuilder buf = getTempBuffer();
		buf.append("/").append(app);
		buf.append("/").append(page);
		if( target != null ) {
			buf.append("/").append(target);
			buf.append("/").append(action);
		}
		addParams(buf, params);
		return buf.toString();
	}
	
	private void addParams(StringBuilder buf, Map<String, String> params) {
		if( params == null ) return;
		
		boolean first = true;
		Iterator<String> it = params.keySet().iterator();
		while( it.hasNext() ) {
			String k = it.next();
			if( first ) buf.append("?");
			else buf.append(",");
			buf.append(k).append("=").append(params.get(k));
			first = false;
		}
	}
}
