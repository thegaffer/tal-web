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

package org.tpspencer.tal.template;

import java.io.Writer;
import java.security.Principal;
import java.util.Locale;

import org.tpspencer.tal.template.render.UrlGenerator;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

/**
 * This interface represents the model that is
 * provided to the render elements at render time.
 * It provides access to the dynamic elements that
 * can be used by the render elements (or even the
 * template elements). Most notably this includes
 * the object(s) we are rendering, the users 
 * locale, the users role/security levels etc.
 * 
 * @author Tom Spencer
 */
public interface RenderModel {

	/**
	 * @return The writer to write into
	 */
	public Writer getWriter();
	
	/**
	 * This method allows access to a temporary {@link StringBuilder}.
	 * This use of this needs to be atomic as it may be accessed by
	 * any {@link RenderElement}, by any {@link TemplateElement} and
	 * can also be used in the {@link RenderNode} implementation.
	 * A safe implementation of the resulting buffer would be to
	 * obtain all the component elements that make up the string and
	 * then get the temp buffer and form the string. Even within the
	 * same method if there are multiple uses of the temp buffer the
	 * recommended pattern is to get it again. For instance:
	 * 
	 * <p><code><pre>
	 * String str1 = model.getCurrentNode.getProperty(...);
	 * Object obj1 = element.getValue(model);
	 * int i1 = someProperty;
	 * 
	 * String output = getTempBuffer().append(str1).append(obj1).append(i1).toString();
	 * 
	 * ...
	 * 
	 * String str1 = model.getCurrentNode.getProperty(...);
	 * String output2 = getTempBuffer().append(str1).append("hello");
	 * </pre></code></p>
	 * 
	 * @return The temporary builder
	 */
	public StringBuilder getTempBuffer();
	
	/**
	 * @return A generic element that can be used to form html/xml a bit more efficiently
	 */
	public GenericElement getGenericElement();
	
	/**
	 * @return Call to get the URL generator
	 */
	public UrlGenerator getUrlGenerator();
	
	/**
	 * @return The namespace (if any) to prefix ids with
	 */
	public String getNamespace();
	
	/**
	 * @return The current user principle object (if there is one)
	 */
	public Principal getUser();
	
	/**
	 * @return The locale (typically of the user) to use
	 */
	public Locale getLocale();
	
	/**
	 * Helper to get the given resource bundle message
	 * without throwing an exception if is does not
	 * exist, instead the returning the default value.
	 * 
	 * @param key The key to obtain
	 * @param def The default value to use
	 * @return The message (or default value)
	 */
	public String getMessage(String key, String def);
	
	/**
	 * Helper to get the given resource bundle message
	 * without throwing an exception if is does not
	 * exist, instead the returning the default value.
	 * This version also formats the message with the
	 * given arguments
	 * 
	 * @param key The key to obtain
	 * @param def The default value to use
	 * @param args Any arguments in the formatted message
	 * @return The formatted message (or default value)
	 */
	public String getMessage(String key, String def, Object[] args);
	
	/**
	 * Call to get an arbitrary object from the render
	 * model set of objects. 
	 * 
	 * <p><b>Note: </b>This does not support levels of
	 * indirection, if you are for some.object then 
	 * there needs to be an object under the label 
	 * "some.object" to get it. If there is an object
	 * called "some" that contains another called
	 * "object" you don't get it. Access to the 'current'
	 * object can be obtained through the current node.</p> 
	 * 
	 * @param name The name of the object to get
	 * @return The object itself
	 */
	public Object getObject(String name);
	
	/**
	 * Call to add, set or remove a render model object.
	 * 
	 * <p><b>Note: </b>As above this does not support
	 * indirection, i.e. "some.object" is set under
	 * the key "some.object", it will not change the
	 * value of the property "object" held by the
	 * object under the key "some" in the model.</p>
	 * 
	 * @param name
	 * @param value
	 */
	public void setObject(String name, Object value);
	
	/**
	 * Call to evaluate an expression and return the response.
	 * This is only supported if the implementing class supports
	 * evaluating expressions.
	 * 
	 * @param expr The expression to evaluate
	 * @return The expressions value against the model
	 */
	public Object evaluateExpression(String expr);
	
	/**
	 * As evaluateExpression above, but is templated to expect
	 * the response to be a particular type.
	 * 
	 * @param <T>
	 * @param expr The expression
	 * @param expected The expected value
	 * @return The expression
	 */
	public <T> T evaluateExpression(String expr, Class<T> expected);
	
	/**
	 * Call to get the current node. This instance should be used
	 * immediately. Any future calls to getCurrentNode may 
	 * invalidate or change the instance returned. Specifically
	 * render elements should not hold this over calls to
	 * child render elements.
	 * 
	 * @return The current render node
	 */
	public RenderNode getCurrentNode();
	
	/**
	 * Call to push a new embedded object member as
	 * the new node. All property access will now be
	 * using this node.
	 * 
	 * @param name The name, key or index of the node (relative to current node)
	 * @return The new render node
	 */
	public RenderNode pushNode(String name, int index);
	
	/**
	 * Call to pop the last node off the stack. So
	 * if the current node is "some.object.member"
	 * and you call popNode it will now be 
	 * "some.object". 
	 */
	public void popNode();
	
	/**
	 * Call to determine if the given property on the given
	 * node has any errors against it. If so returns true
	 * 
	 * @param node The node to check property from (if null uses current)
	 * @param property The property on the node (if null checks for errors against the node only)
	 * @return Any errors
	 */
	public boolean isError(RenderNode node, String property);
}
