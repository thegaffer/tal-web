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

package org.tpspencer.tal.util.htmlhelper;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Pattern;

/**
 * This class works like the HTMLCharStripper, but
 * instead of escaping HTML it escapes URL, which
 * is generally % and a character.
 * 
 * <p>Note the set in here is currently limited
 * and should include more chars outside the given
 * url range.</p>
 * 
 * @author Tom Spencer
 */
public class UrlCharStripper {
	
	private static Pattern urlPattern = Pattern.compile(".*[<> ,=]+.*");

	public static String strip(String src) {
		if( src == null ) return null;
		
		StringBuilder buf = new StringBuilder();
		strip(src, buf);
		return buf.toString();
	}
	
	public static void strip(String src, StringBuilder buf) {
		if( src == null ) return;
		
		if( requiresStripping(src) ) {
			char[] arr = src.toCharArray();
			for( int i = 0 ; i < arr.length ; i++ ) {
				if( arr[i] == '%' ) buf.append("%25");
				else if( arr[i] == '<' ) buf.append("%3C");
				else if( arr[i] == '>' ) buf.append("%3E");
				else if( arr[i] == ' ' ) buf.append("%20");
				else if( arr[i] == ',' ) buf.append("%2C");
				else if( arr[i] == '=' ) buf.append("%3D");
				else buf.append(arr[i]);
			}
		}
		else {
			buf.append(src);
		}
	}
	
	public static void strip(String src, Writer writer) throws IOException {
		if( src == null ) return;
		
		if( requiresStripping(src) ) {
			char[] arr = src.toCharArray();
			for( int i = 0 ; i < arr.length ; i++ ) {
				if( arr[i] == '%' ) writer.append("%25");
				else if( arr[i] == '<' ) writer.append("%3C");
				else if( arr[i] == '>' ) writer.append("%3E");
				else if( arr[i] == ' ' ) writer.append("%20");
				else if( arr[i] == ',' ) writer.append("%2C");
				else if( arr[i] == '=' ) writer.append("%3D");
				else writer.append(arr[i]);
			}
		}
		else {
			writer.append(src);
		}
	}
	
	/**
	 * Internal helper to test if we need to strip or not.
	 * 
	 * @param src
	 * @return
	 */
	private static boolean requiresStripping(String src) {
		return urlPattern.matcher(src).matches();
	}
}
