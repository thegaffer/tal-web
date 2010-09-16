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

import java.util.Map;

import org.tpspencer.tal.template.RenderNode;

/**
 * This interface represents a service that can evaluate
 * expressions found in render elements. If an instance
 * of this interface is not set on the SimpleRenderModel
 * then it can not perform expression evaluation.
 * 
 * @author Tom Spencer
 */
public interface ExpressionEvaluator {

	public Object evaluateExpression(Map<String, Object> model, RenderNode node, String expr, Class<?> expected);	
}
