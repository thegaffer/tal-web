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

package org.talframework.talui.template.core.xml;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.core.SimpleTemplate;
import org.talframework.talui.template.core.TemplateProp;
import org.talframework.talui.template.core.groups.ActionGroup;
import org.talframework.talui.template.core.groups.CompositeGroup;
import org.talframework.talui.template.core.groups.FormGroup;
import org.talframework.talui.template.core.groups.GridGroup;
import org.talframework.talui.template.core.groups.ImageGroup;
import org.talframework.talui.template.core.groups.SimpleGroup;
import org.talframework.talui.template.core.memberprops.MemberProperty;
import org.talframework.talui.template.core.props.BooleanProperty;
import org.talframework.talui.template.core.props.ChoiceProperty;
import org.talframework.talui.template.core.props.CommandProperty;
import org.talframework.talui.template.core.props.DateProperty;
import org.talframework.talui.template.core.props.MemoProperty;
import org.talframework.talui.template.core.props.NumberProperty;
import org.talframework.talui.template.core.props.SimpleChoiceProperty;
import org.talframework.talui.template.core.props.SimpleProperty;
import org.talframework.talui.template.core.props.StringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class will read an XML file containing
 * template definitions and turn them into a map
 * of templates, which can then be compiled as
 * appropriate.
 * 
 * <p>There is a singleton XmlTemplateReader instance.
 * This comes pre-configured with all the default
 * template and template element classes (they are
 * mapped to the XML element name). Additional
 * registrations can be added or this class can be
 * constructed separately and the mappings must be
 * added manually.
 * 
 * @author Tom Spencer
 */
public class XmlTemplateReader {
	private static final Log logger = LogFactory.getLog(XmlTemplateReader.class);
	
	/** The singleton instance that is used by default */
	private static final XmlTemplateReader XML_READER = new XmlTemplateReader();
	
	/** The template class to use */
	private Class<? extends Template> templateClass = null;
	/** The XML name to TemplateElement mappings */
	private Map<String, Class<? extends TemplateElement>> mappings = null;
	
	public static Map<Class<?>, ParamConverter> converters = null;
	
	static {
		converters = new HashMap<Class<?>, ParamConverter>();
		converters.put(String.class, new StringConverter());
		converters.put(boolean.class, new BooleanConverter());
		converters.put(Boolean.class, new BooleanConverter());
		converters.put(String[].class, new StringArrayConverter());
		converters.put(Class.class, new ClassConverter());
		converters.put(Date.class, new DateConverter());
		converters.put(Number.class, new NumberConverter());
		converters.put(Double.class, new NumberConverter());
		converters.put(Float.class, new NumberConverter());
		converters.put(Long.class, new NumberConverter());
		converters.put(Integer.class, new NumberConverter());
		converters.put(Short.class, new NumberConverter());
		converters.put(double.class, new NumberConverter());
		converters.put(float.class, new NumberConverter());
		converters.put(long.class, new NumberConverter());
		converters.put(int.class, new NumberConverter());
		converters.put(short.class, new NumberConverter());
	}

	/**
	 * Constructs an XmlTemplateReader with the default
	 * (in-built) XML name to TemplateElement class mappings.
	 */
	public XmlTemplateReader() {
		this.templateClass = SimpleTemplate.class;
		this.mappings = new HashMap<String, Class<? extends TemplateElement>>();

		this.mappings.put("prop", SimpleProperty.class);
		this.mappings.put("string-prop", StringProperty.class);
		this.mappings.put("memo-prop", MemoProperty.class);
		this.mappings.put("boolean-prop", BooleanProperty.class);
		this.mappings.put("date-prop", DateProperty.class);
		this.mappings.put("number-prop", NumberProperty.class);
		this.mappings.put("choice-prop", ChoiceProperty.class);
		this.mappings.put("simplechoice-prop", SimpleChoiceProperty.class);
		this.mappings.put("command-prop", CommandProperty.class);
		
		this.mappings.put("member-prop", MemberProperty.class);
		
		this.mappings.put("group", SimpleGroup.class);
		this.mappings.put("composite-group", CompositeGroup.class);
		this.mappings.put("action-group", ActionGroup.class);
		this.mappings.put("image-group", ImageGroup.class);
		this.mappings.put("form-group", FormGroup.class);
		this.mappings.put("grid-group", GridGroup.class);
		
		this.mappings.put("template-prop", TemplateProp.class);
	}
	
	/**
	 * Constructs an XmlTemplateReader with custom XML
	 * name to TemplateElement class mappings.
	 * 
	 * @param mappings The XML to TemplateElement class mappings
	 */
	public XmlTemplateReader(Map<String, Class<? extends TemplateElement>> mappings) {
		this.templateClass = SimpleTemplate.class;
		this.mappings = mappings;
	}
	
	/**
	 * @return The default single XmlTemplateReader
	 */
	public static XmlTemplateReader getStdReader() {
		return XML_READER;
	}
	
	/**
	 * Helper to add a new XML element name to 
	 * TemplateElement class mapping.
	 * 
	 * @param elementName The name of the XML element
	 * @param elementClass The class to 
	 */
	public void addMapping(String elementName, Class<? extends TemplateElement> elementClass) {
		mappings.put(elementName, elementClass);
	}

	/**
	 * @return the templateClass
	 */
	public Class<?> getTemplateClass() {
		return templateClass;
	}

	/**
	 * @return the mappings
	 */
	public Map<String, Class<? extends TemplateElement>> getMappings() {
		return mappings;
	}

	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(Map<String, Class<? extends TemplateElement>> mappings) {
		this.mappings = mappings;
	}

	/**
	 * @param templateClass the templateClass to set
	 */
	public void setTemplateClass(Class<? extends Template> templateClass) {
		this.templateClass = templateClass;
	}
	
	/**
	 * The main method - this loads an XML file, reads each
	 * template and constructs it using its internal mapping.
	 * The resource is treated first as a file (if it exists), 
	 * then as a classpath resource (if it begins /), and 
	 * failing that as a uri.
	 * 
	 * @param resource The XML file/resource
	 * @return The list of templates found
	 * @throws IllegalArgumentException Is thrown if we come across errors in the XML file/mapping
	 */
	public List<Template> loadTemplates(String resource) {
		if( resource == null || resource.length() == 0 ) throw new IllegalArgumentException("Cannot load a XML Doc with no resource");
		
		Document doc = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// a. Try as a classpath resource first
			if( resource.charAt(0) == '/' ) {
				doc = db.parse(this.getClass().getResourceAsStream(resource) );
			}
			else {
				File f = new File(resource);
				if( f.exists() ) doc = db.parse(f);
				else doc = db.parse(resource);
			}
		}
		catch( Exception e ) {
			if( logger.isDebugEnabled() ) e.printStackTrace();
			logger.error("!!! Failure reading Template XML File: " + e.getMessage());
			throw new IllegalArgumentException("Unable to load the XML template file it appears to be invalid: " + e.getMessage());
		}
		
		Element root = doc.getDocumentElement();
		return loadTemplates(root);
	}
	
	private List<Template> loadTemplates(Element root) {
		List<Template> ret = new ArrayList<Template>();
	
		NodeList children = root.getChildNodes();
		if( children != null ) {
			for( int i = 0 ; i < children.getLength() ; i++ ) {
				Node n = children.item(i);
				if( n.getNodeType() == Node.ELEMENT_NODE &&
						"template".equals(n.getNodeName()) ) {
					Template t = createTemplate();
					if( logger.isTraceEnabled() ) logger.trace(">>> Starting new template (details at end)");
					
					mapElement((Element)n, t);
					List<TemplateElement> nextChildren = loadTemplateElements(t, (Element)n);
					t.init(nextChildren);
					
					if( logger.isDebugEnabled() ) logger.debug("<<< Loaded template: " + t);
					ret.add(t);
				}
			}
		}
		
		return ret;
	}
	
	private List<TemplateElement> loadTemplateElements(Template template, Element parent) {
		List<TemplateElement> ret = new ArrayList<TemplateElement>();
		
		NodeList children = parent.getChildNodes();
		if( children != null ) {
			for( int i = 0 ; i < children.getLength() ; i++ ) {
				Node n = children.item(i);
				if( n.getNodeType() == Node.ELEMENT_NODE ) {
					TemplateElement e = createTemplateElement(n.getNodeName());
					mapElement((Element)n, e);
					
					List<TemplateElement> nextChildren = loadTemplateElements(template, (Element)n);
					e.init(template, nextChildren);
					if( logger.isTraceEnabled() ) logger.trace("\tFound template element: " + e);
					ret.add(e);
				}
			}
		}
		
		return ret.size() > 0 ? ret : null;
	}
	
	private Template createTemplate() {
		try {
			return templateClass.newInstance();
		}
		catch( IllegalAccessException e ) {
			if( logger.isDebugEnabled() ) e.printStackTrace();
			logger.error("!!! Failed to create template class [" + templateClass + "]: " + e.getMessage()); 
			throw new IllegalArgumentException("Failed to load specified file: " + e.getMessage());
		}
		catch( InstantiationException e ) {
			if( logger.isDebugEnabled() ) e.printStackTrace();
			logger.error("!!! Failed to create template class [" + templateClass + "]: " + e.getMessage());
			throw new IllegalArgumentException("Failed to load specified file: " + e.getMessage());
		}
	}
	
	private TemplateElement createTemplateElement(String name) {
		Class<? extends TemplateElement> cls = mappings.get(name);
		if( cls == null ) throw new IllegalArgumentException("No mapping for the element: " + name);
		
		try {
			return cls.newInstance();
		}
		catch( IllegalAccessException e ) {
			if( logger.isDebugEnabled() ) e.printStackTrace();
			logger.error("!!! Failed to create template class [" + templateClass + "]: " + e.getMessage());
			throw new IllegalArgumentException("Failed to load specified file: " + e.getMessage());
		}
		catch( InstantiationException e ) {
			if( logger.isDebugEnabled() ) e.printStackTrace();
			logger.error("!!! Failed to create template class [" + templateClass + "]: " + e.getMessage());
			throw new IllegalArgumentException("Failed to load specified file: " + e.getMessage());
		}
	}

	/**
	 * This internal helper does most of the magic. It simply
	 * maps each and every public property of the template
	 * element class to an attribute and if the attribute is
	 * set (and not empty!) it will set it.
	 * 
	 * @param xmlElement
	 * @param templateElement
	 */
	private void mapElement(Element xmlElement, Object templateElement) {
		// a. Create map of all attributes
		Map<String, String> properties = new HashMap<String, String>();
		NamedNodeMap attrs = xmlElement.getAttributes();
		if( attrs != null ) {
			int ln = attrs.getLength();
			for( int i = 0 ; i < ln ; i++ ) {
				Node a = attrs.item(i);
				if( a.getNodeType() == Node.ATTRIBUTE_NODE ) {
					properties.put(a.getNodeName(), a.getNodeValue());
				}
			}
		}
		
		// b. Add in all props we match specifically (remove from list if found)
		try {
			Method propsSetter = null;
			BeanInfo info = Introspector.getBeanInfo(templateElement.getClass());
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for( int i = 0 ; i < props.length ; i++ ) {
				if( xmlElement.hasAttribute(props[i].getName()) ) {
					if( props[i].getWriteMethod() == null ) throw new IllegalArgumentException("There is no setter for the attribute [" + props[i].getName() + "] on the template element class: " + templateElement.getClass());
					
					String v = xmlElement.getAttribute(props[i].getName());
					Object val = convertXmlAttribute(v, props[i]);
					props[i].getWriteMethod().invoke(templateElement, val);
					
					// Remove from properties
					properties.remove(props[i].getName());
				}
				
				if( props[i].getName().equals("properties") ) {
					propsSetter = props[i].getWriteMethod();
				}
			}
			
			// c. Add the properties in
			if( properties.size() > 0 && propsSetter != null ) {
				propsSetter.invoke(templateElement, properties);
			}
		}
		catch( RuntimeException e ) {
			throw e;
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Unable to load XML template file: " + e.getMessage());
		}
	}
	
	/**
	 * This method performs simple 
	 * 
	 * @param attr
	 * @param prop
	 * @return
	 */
	private Object convertXmlAttribute(String attr, PropertyDescriptor prop) {
		Object ret = null;
		Class<?> param = prop.getPropertyType();
		
		ParamConverter converter = converters.get(param);
		if( converter != null ) {
			ret = converter.convert(param, attr);
		}
		else {
			throw new IllegalArgumentException("Cannot load XML template file because attribute [" + prop.getName() + "] type is not supported: " + param);
		}
		
		return ret;
	}
	
	/**
	 * Simple interface for an object that converts a value
	 * into a object appropriately.
	 */
	private interface ParamConverter {
		/**
		 * Converts the value to the correct object.
		 * 
		 * @param expected The actual expected type
		 * @param val The string value
		 * @return The object to set
		 */
		public Object convert(Class<?> expected, String val);
	}
	
	/**
	 * Converts a string into a, erm, string
	 */
	private static class StringConverter implements ParamConverter {
		public Object convert(Class<?> expected, String val) {
			return val;
		}
	}
	
	/**
	 * Converts the string into a boolean value
	 */
	private static class BooleanConverter implements ParamConverter {
		public Object convert(Class<?> expected, String val) {
			return Boolean.valueOf(val);
		}
	}
	
	/**
	 * Converts the string into a class
	 */
	private static class ClassConverter implements ParamConverter {
		public Object convert(Class<?> expected, String val) {
			try {
				return Class.forName(val);
			}
			catch( ClassNotFoundException e ) {
				throw new IllegalArgumentException("Cannot load XML template because attribute is not a valid class: " + val);
			}
		}
	}
	
	/**
	 * Converts a string into a string array by splitting on
	 * either ',' or ' ' (space). If neither is found then
	 * creates a string array with a single index of value
	 */
	private static class StringArrayConverter implements ParamConverter {
		public Object convert(Class<?> expected, String val) {
			Object ret = null;
			if( val.indexOf(',') >= 0 ) {
				ret = val.split(",");
			}
			else if( val.indexOf(' ') >= 0 ) {
				ret = val.split(" ");
			}
			else {
				ret = new String[]{val};
			}
			return ret;
		}
	}
	
	/**
	 * Converts strings into numbers as appropriate
	 */
	private static class NumberConverter implements ParamConverter {
		public Object convert(Class<?> expected, String val) {
			if( expected.equals(Double.class) || expected.equals(double.class) ) {
				return new Double(val);
			}
			else if( expected.equals(Float.class) || expected.equals(float.class) ) {
				return new Float(val);
			}
			else if( expected.equals(Long.class) || expected.equals(long.class) ) {
				return new Long(val);
			}
			else if( expected.equals(Short.class) || expected.equals(short.class) ) {
				return new Short(val);
			}
			else if( expected.equals(Integer.class) || expected.equals(int.class) || expected.equals(Number.class) ) {
				return new Integer(val);
			}
			
			throw new IllegalArgumentException("Cannot load XML template because attribute is not a valid number: " + val);
		}
	}
	
	/**
	 * Converts strings to date using the default date formatter
	 */
	private static class DateConverter implements ParamConverter {
		public Object convert(Class<?> expected, String val) {
			try {
				return DateFormat.getDateInstance().parse(val);
			}
			catch( ParseException e ) {
				throw new IllegalArgumentException("Cannot load XML template because attribute is not a valid date: " + val);
			}
		}
	}
}
