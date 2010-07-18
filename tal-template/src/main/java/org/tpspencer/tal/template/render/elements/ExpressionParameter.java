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

package org.tpspencer.tal.template.render.elements;

import org.tpspencer.tal.template.RenderModel;

/**
 * This class implements the the {@link RenderParameter} interface 
 * to resolve the parameter's value using the EL Expression 
 * evaluator in the render model.
 * 
 * @author Tom Spencer
 */
public final class ExpressionParameter implements RenderParameter {

	private final String expression;
	private final Class<?> expected;
	
	/**
	 * Constructs an ExpressionParameter with the given expression
	 * that expects to be a string
	 * 
	 * @param expression The expression
	 */
	public ExpressionParameter(String expression) {
		this.expression = expression;
		this.expected = String.class;
	}
	
	/**
	 * This helper method can be used to create either a Expression 
	 * parameter or a Simple parameter based on whether the parameter
	 * starts with a $. This is useful if you have a possible parameter
	 * at compilation time that might be an expr, but don't want to 
	 * create an ExpressionParameter unless it is.
	 * 
	 * @param param The parameter
	 * @return The render parameter
	 */
	public static RenderParameter createSimpleOrExpression(String param) {
		if( param.charAt(0) == '$' ) return new ExpressionParameter(param);
		else return new SimpleParameter(param);
	}
	
	/**
	 * Constructs an ExpressionParameter with the given expression
	 * and the expected class.
	 * 
	 * @param expression
	 * @param expected
	 */
	public ExpressionParameter(String expression, Class<?> expected) {
		this.expression = expression;
		this.expected = expected;
	}
	
	/**
	 * @return The expression
	 */
	public String getExpression() {
		return expression;
	}
	
	/**
	 * @return The expected type of the parameter
	 */
	public Class<?> getExpected() {
		return expected;
	}
	
	/**
	 * Simply evaluates the expression
	 */
	public String getValue(RenderModel model) {
		Object o = expected != null ? 
				model.evaluateExpression(expression, expected) : 
					model.evaluateExpression(expression);
				
		return o != null ? o.toString() : null;
	}
	
	@Override
	public String toString() {
		return "ExpressionParameter: expr=" + getExpression();
	}
}
