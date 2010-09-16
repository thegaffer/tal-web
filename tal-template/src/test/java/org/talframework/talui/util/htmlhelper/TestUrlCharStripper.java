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

package org.tpspencer.tal.util.htmlhelper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;
import org.tpspencer.tal.util.htmlhelper.UrlCharStripper;

import junit.framework.Assert;

public class TestUrlCharStripper {

	@Test
	public void basic() {
		String tst = "a <bad>, val=param";
		String val = UrlCharStripper.strip(tst);
		
		Assert.assertEquals("a%20%3Cbad%3E%2C%20val%3Dparam", val);
	}

	@Test
	public void intoWriter() throws IOException {
		String tst = "a <bad>, val=param";
		Writer writer = new StringWriter();
		
		UrlCharStripper.strip(tst, writer);
		Assert.assertEquals("a%20%3Cbad%3E%2C%20val%3Dparam", writer.toString());
	}
	
	@Test
	public void comparePerf() throws IOException { 
		String tst = "normal string";
		StringBuilder buf = new StringBuilder();
		int per = 10;
		int runs = 1000;
		
		// No escaping
		long st = System.nanoTime();
		for( int i = 0; i < runs ; i++ ) {
			for( int j = 0 ; j < per ; j++ ) {
				buf.append(tst);
			}
			buf = new StringBuilder();
		}
		long normal = System.nanoTime() - st;
		System.out.println("Without URL stripping takes: " + normal + "ns");
		
		// With escaping
		st = System.nanoTime();
		for( int i = 0; i < runs ; i++ ) {
			for( int j = 0 ; j < per ; j++ ) {
				UrlCharStripper.strip(tst, buf);
			}
			buf = new StringBuilder();
		}
		long strip = System.nanoTime() - st;
		System.out.println("With URL stripping takes: " + strip + "ns");
		
		// Note: This is a performance test and could fail on lower
		// spec hardware or heavily used infrastructure. 
		Assert.assertTrue("URL Stripping takes more than 1ms per " + per + " 'strips'", (strip / runs) < 1000000);
	}
}
