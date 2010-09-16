/*
 * Copyright 2010 Thomas Spencer
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

package org.tpspencer.tal.mvc.document.boq;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.boq.BillOfQuantityWriter;
import org.tpspencer.tal.mvc.document.compiler.AppCompiler;

/**
 * Tests we can produce the bill of quantity document.
 * This is an integration test, not a unit test.
 * 
 * @author Tom Spencer
 */
public class TestBillOfQuantity {

	@Test
	public void basic() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext*.xml");
		AppElement app = AppCompiler.compileApp("test", ctx);
		
		BillOfQuantityWriter boq = new BillOfQuantityWriter("target/BillOfQuantity.pdf");
		boq.createDocument(app);
	}
}
