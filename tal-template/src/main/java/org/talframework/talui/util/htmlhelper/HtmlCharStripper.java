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

package org.talframework.talui.util.htmlhelper;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

/**
 * This utility class will strip any string of
 * HTML special characters. 
 * 
 * @author Tom Spencer
 */
public class HtmlCharStripper {
	
	private static Pattern htmlPattern = Pattern.compile(".*[&<>\\\\\"]+.*");

	/**
	 * Simple method that strips out HTML chars from a 
	 * string and returns the value.
	 * 
	 * @param src The source string
	 * @return The safe string
	 */
	public static String strip(String src) {
		if( src == null ) return src;
		
		StringBuilder buf = new StringBuilder(src.length());
		strip(src, buf);
		return buf.toString();
	}
	
	/**
	 * Helper to strip bad chars out out a string and
	 * add it directly to a writer.
	 * 
	 * @param src The source
	 * @param writer The writer to append to
	 * @throws IOException Any exceptions from writer
	 */
	public static void strip(String src, Writer writer) throws IOException {
		if( src == null ) return;
		
		// Ensure we need to perform the strip - our test shows this is faster!
		if( requiresStipping(src) ) {
			char[] arr = src.toCharArray();
			for( int i = 0 ; i < arr.length ; i++ ) {
				if( arr[i] == '&' ) writer.append("&amp;");
				else if( arr[i] == '<' ) writer.append("&lt;");
				else if( arr[i] == '>' ) writer.append("&gt;");
				else if( arr[i] == '\'' ) writer.append("&#39;");
				else if( arr[i] == '"' ) writer.append("&quot;");
				else writer.append(arr[i]);
			}
		}
		else {
			writer.append(src);
			return;
		}
	}
	
	/**
	 * Helper to strip bad chars and add the string to
	 * a StringBuilder.
	 * 
	 * @param src The source
	 * @param buf The StringBuilder to add to
	 */
	public static void strip(String src, StringBuilder buf) {
		if( src == null ) return;
		
		// Ensure we need to perform the strip - our test shows this is faster!
		if( requiresStipping(src) ) {
			char[] arr = src.toCharArray();
			for( int i = 0 ; i < arr.length ; i++ ) {
				if( arr[i] == '&' ) buf.append("&amp;");
				else if( arr[i] == '<' ) buf.append("&lt;");
				else if( arr[i] == '>' ) buf.append("&gt;");
				else if( arr[i] == '\'' ) buf.append("&#39;");
				else if( arr[i] == '"' ) buf.append("&quot;");
				else buf.append(arr[i]);
			}
		}
		else {
			buf.append(src);
			return;
		}
	}
	
	/**
	 * Internal helper to determine if stripping is necc.
	 * 
	 * @param src The src
	 * @return True if stripping is required
	 */
	private static boolean requiresStipping(String src) {
		return htmlPattern.matcher(src).matches();
		
		/*int index = src.indexOf('&');
		if( index < 0 ) index = src.indexOf('<'); else return true;
		if( index < 0 ) index = src.indexOf('>'); else return true;
		if( index < 0 ) index = src.indexOf('\''); else return true;
		if( index < 0 ) index = src.indexOf('"'); else return true;
		
		return index >= 0;*/
	}
}
