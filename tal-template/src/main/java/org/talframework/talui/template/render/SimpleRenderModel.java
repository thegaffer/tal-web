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

package org.talframework.talui.template.render;

import java.io.Writer;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.RenderNode;
import org.talframework.talui.util.htmlhelper.AttributeAdaptor;
import org.talframework.talui.util.htmlhelper.GenericElement;

/**
 * This class is a basic implementation of the render model
 * interface that is perfectly ok for most purposes. It supports
 * the basic functionality directly with the following exceptions.
 * 
 * <p><b>Expressions</b> - In order to support expressions you
 * must plug in an instance of the ExpressionEvaluator interface.
 * Any calls to evaluate an expression are passed to this. An
 * implementation of this interface using Apache's EL 
 * implementation is provided, but the dependencies are optional.
 * See the ApacheELExpressionEvaluator class.</p>
 * 
 * <p><b>Render Nodes</b> - Because the render node also supports
 * dynamically getting property values from the object the node
 * represents in the model this is functionality is delegated
 * to another class to support rather than having any direct
 * dependency on a library/framework to accomplish this. There
 * is a default implementation of this interface based on the
 * spring framework, but the dependencies are option. See the
 * SpringRenderNodeFactory class.</p> 
 * 
 * <p>Sample Usage:<br /><br />
 * <code>SimpleRenderModel model  = new SimpleRenderModel(writer, urlGenerator);<br />
 * model.setNamespace(someNamespace);<br />
 * model.setUser(someUser);<br />
 * model.setLocale(someLocale);<br />
 * model.setBundle(someBundle);<br />
 * model.setModel(someMap);<br />
 * model.setExpressionEvaluator(new SomeExpressionEvaluator());<br />
 * model.setNotFactory(someNodeFactory);<br />
 * // use the model.<br /></code><br />
 * Except the constructor args the rest is entirely optional.
 * </p>
 * 
 * @author Tom Spencer
 */
public class SimpleRenderModel implements RenderModel, AttributeAdaptor {
	private static final Log logger = LogFactory.getLog(SimpleRenderModel.class);
	
	private final Writer writer;
	private final UrlGenerator urlGenerator;
	private final GenericElement genericElement;
	private String namespace = null;
	private Principal user = null;
	private Locale locale = null;
	private ResourceBundle bundle = null;
	private Map<String, Object> model = null;
	private RenderNode currentNode = null;
	
	/** Holds the optional error checker to determine if there are errors */
	private RenderModelErrorChecker errorChecker = null; 
	
	private ExpressionEvaluator evaluator = null;
	private RenderNodeFactory nodeFactory = null;
	
	private StringBuilder tempBuffer = null;
	private StringBuilder internalBuffer = null;
	
	/**
	 * Simple constructor. This version constructs an instance
	 * that uses a url generator to implement the attribute
	 * adaptor interface.
	 * 
	 * @param writer The writer to write into
	 * @param generator A class that can generator urls
	 */
	public SimpleRenderModel(Writer writer, UrlGenerator generator) {
		this.writer = writer;
		this.urlGenerator = generator;
		genericElement = new GenericElement(this);
	}
	
	/**
	 * This version constructs an instance that directly uses the
	 * given GenericElement. Any adaption of elements are performed
	 * by this class.
	 * 
	 * @param writer The writer to write into
	 * @param element The generic element to use
	 */
	public SimpleRenderModel(Writer writer, GenericElement element) {
		this.writer = writer;
		this.urlGenerator = null;
		this.genericElement = element;
	}

	/**
	 * Simply returns the configured writer
	 */
	public Writer getWriter() {
		return writer;
	}
	
	/**
	 * Returns the generic element either constructed internally
	 * if the class was provided with a urlGenerator or the
	 * externally provided instance.
	 */
	public GenericElement getGenericElement() {
		return genericElement;
	}
	
	/**
	 * Simply returns the configured namespace if there is one
	 */
	public String getNamespace() {
		return namespace;
	}
	
	/**
	 * @param namespace The namespace to use
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	/**
	 * Returns the user object if set
	 */
	public Principal getUser() {
		return user;
	}
	
	/**
	 * Simply returns the configured resource bundle
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	/**
	 * Simply gets the key from the bundle if it exists
	 */
	public String getMessage(String key, String def) {
		if( bundle == null && def == null ) throw new IllegalArgumentException("Cannot get a message when there is no bundle set to use");
		
		String ret = def;
		if( bundle != null ) {
			try {
				ret = bundle.getString(key);
			}
			catch( MissingResourceException e ) {
				logger.warn("!!! Missing resource [" + key + "] in bundle: " + bundle.toString());
				ret = def;
			}
		}
		
		return ret;
	}

	/**
	 * Uses MessageFormat to format the message with the given
	 * arguments
	 */
	public String getMessage(String key, String def, Object[] args) {
		String msg = getMessage(key, def);
		
		// Add on args if we are left with the default
		if( msg.equals(def) && args != null && args.length > 0 ) {
			StringBuilder buf = getInternalBuffer().append(msg);
			for( int i = 0 ; i < args.length ; i++ ) {
				if( i == 0 ) buf.append(": ");
				else buf.append(", ");
				buf.append("{").append(i).append("}");
			}
			msg = buf.toString();
		}
		
		// Extension code - if arg is a string starting #, replace it with a resource lookup
		if( args != null && args.length > 0 ) {
			Object[] newArgs = null;
			for( int i = 0 ; i < args.length ; i++ ) {
				String val = args[i] != null ? args[i].toString() : null;
				if( val != null && val.length() > 1 && val.charAt(0) == '#' ) {
					if( newArgs == null ) {
						newArgs = new Object[args.length];
						System.arraycopy(args, 0, newArgs, 0, args.length);
					}
					newArgs[i] = getMessage(val.substring(1), val);
				}
			}
			if( newArgs != null ) args = newArgs;
		}
		
		if( args != null ) msg = MessageFormat.format(msg, args);
		return msg;
	}
	
	/**
	 * Simply returns an object from the model
	 */
	public Object getObject(String name) {
		return model != null ? model.get(name) : null;
	}
	
	/**
	 * Simply adds val to the model unless val is
	 * null in which case it is removed.
	 */
	public void setObject(String name, Object val) {
		if( val == null && model != null ) model.remove(name);
		else if( val != null ) {
			if( model == null ) model = new HashMap<String, Object>();
			model.put(name, val);
		}
	}
	
	/**
	 * Uses the expression evaluator to evaluate the expression.
	 * If there isn't an evaluator an exception is thrown
	 */
	public Object evaluateExpression(String expr) {
		if( evaluator == null ) throw new UnsupportedOperationException("Expressions not supported in simple render model without an expression evaluator");
		
		return evaluator.evaluateExpression(model, currentNode, expr, Object.class);
	}
	
	/**
	 * Uses the expression evaluator to evaluate the expression.
	 * If there isn't an evaluator an exception is thrown
	 */
	public <T> T evaluateExpression(String expr, Class<T> expected) {
		if( evaluator == null ) throw new UnsupportedOperationException("Expressions not supported in simple render model without an expression evaluator");
		
		Object ret = evaluator.evaluateExpression(model, currentNode, expr, expected);
		return expected.cast(ret);
	}
	
	/**
	 * Simply returns the current node
	 */
	public RenderNode getCurrentNode() {
		return currentNode;
	}
	
	/**
	 * Simply uses the node factory to get the new node.
	 * If there is no nodeFactory an unsupported exception
	 * is thrown.
	 */
	public RenderNode pushNode(String name, int index) {
		if( nodeFactory == null ) throw new UnsupportedOperationException("Cannot push a node because there is no node factory set of the Simple Render Model");
		
		currentNode = nodeFactory.getNode(this, currentNode, name, index);
		return currentNode;
	}
	
	/**
	 * Gets the parent to the current node or throws an exception
	 * if there isn't a current node.
	 */
	public void popNode() {
		if( currentNode != null ) currentNode = currentNode.getParentNode();
		else throw new IllegalArgumentException("Cannot pop the node if there isn't a current node");
	}
	
	/**
	 * Uses the error checker to determine if there are errors or
	 * determines an expression to work it out assuming errors is
	 * a map.
	 * 
	 * TODO: I am not totally happy with this and the error checker?!?!
	 */
	public boolean isError(RenderNode node, String property) {
		if( !model.containsKey("errors") ) return false;
		
		if( node == null ) node = getCurrentNode();
		String fullId = null;
		
		if( node != null && property != null ) fullId = getInternalBuffer().append(node.getName()).append(".").append(property).toString();
		else if( node != null ) fullId = node.getName(); 
		else if( property != null ) fullId = property;
		else return false;
		
		if( errorChecker != null ) return errorChecker.isErrors(fullId);
		else {
			if( evaluator == null ) throw new IllegalArgumentException("Cannot check for field errors if there is no error checker or expression evaluator in render model");
			StringBuilder buf = getInternalBuffer();
			buf.append("${! empty errors[\"").append(fullId).append("\"]}");
			
			if( evaluateExpression(buf.toString(), Boolean.class) ) return true;
		}
		
		return false;
	}
	
	/**
	 * Simply returns the empty temporary buffer
	 */
	public StringBuilder getTempBuffer() {
		if( tempBuffer == null ) tempBuffer = new StringBuilder();
		else tempBuffer.setLength(0);
		return tempBuffer;
	}
	
	/**
	 * @return An internal buffer that the render elements are not using
	 */
	protected StringBuilder getInternalBuffer() {
		if( internalBuffer == null ) internalBuffer = new StringBuilder();
		else internalBuffer.setLength(0);
		return internalBuffer;
	}
	
	/////////////////////////////////////////
	// Attribute Adaptor
	
	public String adaptId(String in) {
		StringBuilder buf = getInternalBuffer();
		if( namespace != null ) buf.append(namespace).append("-");
		if( currentNode != null ) buf.append(currentNode.getId()).append("-");
		buf.append(in);
		return buf.toString();
	}
	
	public String adaptName(String name) {
		StringBuilder buf = getInternalBuffer();
		if( currentNode != null ) buf.append(currentNode.getName()).append(".");
		buf.append(name);
		return buf.toString();
	}
	
	public String adaptFunction(String func) {
		return namespace != null ? namespace + func : func;
	}
	
	public String adaptResource(String res) {
		if( urlGenerator == null ) throw new IllegalArgumentException("Attempted to use the attribute adaptor of the base render model with no url generator: " + res);
		
		return urlGenerator.generateResourceUrl(res);
	}
	
	public String adaptActionUrl(String action, Map<String, String> params) {
		if( urlGenerator == null ) throw new IllegalArgumentException("Attempted to use the attribute adaptor of the base render model with no url generator: " + action);
		
		return urlGenerator.generateActionUrl(action, params);
	}
	
	//////////////////////////////////////////////
	// Getters/Setters

	/**
	 * @return the model
	 */
	public Map<String, Object> getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	/**
	 * @return the evaluator
	 */
	public ExpressionEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * @param evaluator the evaluator to set
	 */
	public void setEvaluator(ExpressionEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * @return the nodeFactory
	 */
	public RenderNodeFactory getNodeFactory() {
		return nodeFactory;
	}

	/**
	 * @param nodeFactory the nodeFactory to set
	 */
	public void setNodeFactory(RenderNodeFactory nodeFactory) {
		this.nodeFactory = nodeFactory;
	}

	/**
	 * @return the urlGenerator
	 */
	public UrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Principal user) {
		this.user = user;
	}

	/**
	 * @param currentNode the currentNode to set
	 */
	public void setCurrentNode(RenderNode currentNode) {
		this.currentNode = currentNode;
	}
	/**
	 * @param bundle The bundle to use when rendering
	 */
	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}
	
	/**
	 * Call to set the bundle given its base name. If it
	 * does not exist it will use a default one.
	 * 
	 * @param name The name of the bundle
	 */
	public void setBundle(String name) {
		this.bundle = locale != null ? ResourceBundle.getBundle(name, locale) : ResourceBundle.getBundle(name);
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the errorChecker
	 */
	public RenderModelErrorChecker getErrorChecker() {
		return errorChecker;
	}

	/**
	 * @param errorChecker the errorChecker to set
	 */
	public void setErrorChecker(RenderModelErrorChecker errorChecker) {
		this.errorChecker = errorChecker;
	}
}
