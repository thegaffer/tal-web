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

package org.talframework.talui.template.render.apacheel;

import java.util.Map;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.apache.el.ExpressionFactoryImpl;
import org.apache.el.lang.FunctionMapperImpl;
import org.apache.el.lang.VariableMapperImpl;
import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.tal.aspects.annotations.TraceWarn;
import org.talframework.talui.template.RenderNode;
import org.talframework.talui.template.render.ExpressionEvaluator;

/**
 * This class implements the ExpressionEvaluator using the
 * Jasper EL implementation (part of Apache, as used inside
 * Tomcat). This is an optional dependency on the project
 * as you could provide your own implementation.
 * 
 * <p><b>Note: </b>This class should be constructed once for
 * each render model it is used within as it holds state 
 * between calls that if used across models would cause 
 * issues.</p>
 * 
 * @author Tom Spencer
 */
public class ApacheELExpressionEvaluator implements ExpressionEvaluator {
	
	private ExpressionFactory factory = new ExpressionFactoryImpl();
	/** Member holds the context which is created on first use */
	private ELContext context = null;
	/** Member holds the single node resolver set against the ELContext above */
	private RenderNodeELResolver nodeResolver = null;

	@Trace
	public Object evaluateExpression(Map<String, Object> model, RenderNode node, String expr, Class<?> expected) {
		if( context == null ) {
			nodeResolver = new RenderNodeELResolver();
			final CompositeELResolver resolver = new CompositeELResolver();
			resolver.add(nodeResolver);
			resolver.add(new RenderModelELResolver(model));
			resolver.add(new ArrayELResolver());
		    resolver.add(new ListELResolver());
		    resolver.add(new MapELResolver());
		    resolver.add(new BeanELResolver());
		    
		    final VariableMapper variableMapper = new VariableMapperImpl();
		    final FunctionMapper functionMapper = new FunctionMapperImpl();
			
			context = new ELContext() {
				@Override
				public ELResolver getELResolver() {
					return resolver;
				}
				
				@Override
				public FunctionMapper getFunctionMapper() {
					return functionMapper;
				}
				
				@Override
				public VariableMapper getVariableMapper() {
					return variableMapper;
				}
			};
		}
		
		nodeResolver.setCurrent(node);
		ValueExpression expression = factory.createValueExpression(context, expr, expected);
		try {
			return expression.getValue(context);
		}
		catch( Exception e ) {
		    expressionFail(expr, e);
			return null;
		}
	}

	/**
	 * Called when we fail to evaluate an expression so the
	 * trace aspects will log it.
	 */
	@TraceWarn
	private void expressionFail(String expr, Exception e) {
	    
	}
}
