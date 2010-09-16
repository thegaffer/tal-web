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

package org.tpspencer.tal.mvc.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;
import org.tpspencer.tal.mvc.process.SimpleModelAttributeResolver;

import junit.framework.Assert;

/**
 * Simple tests for the {@link SimpleModelAttributeResolver}
 * 
 * @author Tom Spencer
 */
public class TestSimpleModelAttributeResolver {
	
	@Test
	public void basic() {
		SimpleModelAttributeResolver resolver = new SimpleModelAttributeResolver();
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		attrs.add(new SimpleModelAttribute("test"));
		ModelConfiguration config1 = new ModelConfiguration("test1", attrs);
		ModelConfiguration config2 = new ModelConfiguration("test2", attrs);
		
		Assert.assertNull(resolver.getModelAttributes(config1));
		Assert.assertNull(resolver.getModelAttributes(config2));
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("val1", "val1");
		model.put("val2", "val2");
		
		resolver.saveModelAttributes(config1, model);
		Assert.assertEquals(model, resolver.getModelAttributes(config1));
		Assert.assertNull(resolver.getModelAttributes(config2));
		
		resolver.saveModelAttributes(config2, model);
		Assert.assertEquals(model, resolver.getModelAttributes(config1));
		Assert.assertEquals(model, resolver.getModelAttributes(config2));

		// Remove the model
		resolver.removeModel(config2);
		Assert.assertNull(resolver.getModelAttributes(config2));
	}
}
