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
import org.tpspencer.tal.util.htmlhelper.HtmlCharStripper;

import junit.framework.Assert;

public class TestHtmlCharStripper {

	@Test
	public void basic() {
		String tst = "this \"is\" a <bad> html 'string' & then some";
		String val = HtmlCharStripper.strip(tst);
		
		Assert.assertEquals("this &quot;is&quot; a &lt;bad&gt; html &#39;string&#39; &amp; then some", val);
	}

	@Test
	public void intoWriter() throws IOException {
		String tst = "this \"is\" a <bad> html 'string' & then some";
		Writer writer = new StringWriter();
		
		HtmlCharStripper.strip(tst, writer);
		Assert.assertEquals("this &quot;is&quot; a &lt;bad&gt; html &#39;string&#39; &amp; then some", writer.toString());
	}
	
	@Test
	public void comparePerf() throws IOException { 
		String tst = "normal string";
		Writer writer = new StringWriter();
		int per = 50;
		int runs = 200;
		
		// No escaping
		long st = System.nanoTime();
		for( int i = 0; i < runs ; i++ ) {
			for( int j = 0 ; j < per ; j++ ) {
				writer.write(tst);
			}
			writer = new StringWriter();
		}
		long normal = System.nanoTime() - st;
		System.out.println("Without HTML stripping takes: " + normal + "ns");
		
		// With escaping
		st = System.nanoTime();
		for( int i = 0; i < runs ; i++ ) {
			for( int j = 0 ; j < per ; j++ ) {
				HtmlCharStripper.strip(tst, writer);
			}
			writer = new StringWriter();
		}
		long strip = System.nanoTime() - st;
		System.out.println("With HTML stripping takes: " + strip + "ns");
		
		// Note: This is a performance test and could fail on lower
		// spec hardware or heavily used infrastructure. 
		Assert.assertTrue("HTML Stripping takes more than 1ms per " + per + " 'strips'", (strip / runs) < 1000000);
	}
}
