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

package org.tpspencer.tal.testapp.integrationtest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.selenium.SeleneseTestCase;

public class TestBooking extends SeleneseTestCase {

	public void setUp() throws Exception {
		setUp("http://127.0.0.1:8080/web-testapp/", "*chrome");
	}
	
	@SuppressWarnings("serial")
	public void testContact() throws Exception {
		selenium.open("/web-testapp/test/page/contactPage");
		// selenium.windowMaximize();
		selenium.captureEntirePageScreenshot("c:\\Dev\\contactPage.png", "background=#CCFFDD");
		
		Map<String, String> params = new HashMap<String, String>() {{
			put("firstName", "Selenium");
			put("lastName", "Test");
			put("address-address", "1 Selenium Road");
			put("address-town", "Bristol");
			put("address-country", "UK");
			put("address-postCode", "B45 TYX");
			put("company", "Selenium Test Co");
		}};
		
		SeleniumHelper.fillInForm(selenium, "con", "form", params);
		selenium.click("submit"); selenium.waitForPageToLoad("30000");
		
		// Ensure entered data is still present
		Iterator<String> it = params.keySet().iterator();
		while( it.hasNext() ) {
			selenium.isTextPresent(params.get(it.next()));
		}
	}
	
	@SuppressWarnings("serial")
	public void testBooking() throws Exception {
		selenium.open("/web-testapp/test/page/orderPage");
		selenium.windowMaximize();
		selenium.captureEntirePageScreenshot("c:\\Dev\\orderPage.png", null);
		
		selenium.click("ord-createOrder"); selenium.waitForPageToLoad("30000");
		
		SeleniumHelper.fillInForm(selenium, "ord", "newOrder", new HashMap<String, String>() {{
			put("collection-address", "1 Long Street");
			put("collection-town", "Smalltown");
			put("collection-postCode", "B45 TYX");
			put("delivery-town", "Paris");
			put("delivery-postCode", "France");
			put("goodsNumber", "4");
			put("goodsType", "label=Non-Documents");
			put("goodsWeight", "label=Less 1Kg");
			put("service", "Express");
		}});
		
		selenium.click("submit"); selenium.waitForPageToLoad("30000");
		
		// Ensure table shows the view
		selenium.isTextPresent("1 Long Street");
		selenium.isTextPresent("Smalltown");
		selenium.isTextPresent("B45 TYX");
		selenium.isTextPresent("Express");
	}
}
