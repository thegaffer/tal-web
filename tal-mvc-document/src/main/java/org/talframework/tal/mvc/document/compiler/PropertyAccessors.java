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

package org.tpspencer.tal.mvc.document.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * This outer class is a wrapper for the various
 * standard {@link PropertyAccessor} classes and
 * containers other helpers they all use.
 * 
 * @author Tom Spencer
 */
public class PropertyAccessors {
	
	/**
	 * Obtains a property by using a getter from the property itself.
	 * 
	 * @author Tom Spencer
	 */
	public static class SimplePropertyAccessor implements PropertyAccessor {

		/** The name of the property to get */
		private final String prop;
		private final boolean format;
		
		/**
		 * Sets up a SimplePropertyCompiler
		 * 
		 * @param prop The property to get
		 */
		public SimplePropertyAccessor(String prop, boolean format) {
			this.prop = prop;
			this.format = format;
		}
		
		/**
		 * Simply gets the property from the object
		 */
		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			if( object == null ) return null;
			
			BeanWrapper wrapper = new BeanWrapperImpl(object);
			return PropertyAccessors.formatObject(wrapper.getPropertyValue(prop), format);
		}
	}
	
	/**
	 * Simply returns the name of the object held by its
	 * parent as the value.
	 * 
	 * @author Tom Spencer
	 */
	public static class ParentNamePropertyAccessor implements PropertyAccessor {

		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			return name;
		}
	}
	
	/**
	 * This class converts a boolean property into one of
	 * two values. Default is "yes" and "no".
	 * 
	 * @author Tom Spencer
	 */
	public static class BoolPropertyAccessor implements PropertyAccessor {

		private final String prop;
		private final String trueValue;
		private final String falseValue;
		
		public BoolPropertyAccessor(String prop) {
			this.prop = prop;
			this.trueValue = "yes";
			this.falseValue = "no";
		}
		
		public BoolPropertyAccessor(String prop, String trueValue, String falseValue) {
			this.prop = prop;
			this.trueValue = trueValue;
			this.falseValue = falseValue;
		}
		
		/**
		 * Simply gets the property from the object
		 */
		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			if( object == null ) return null;
			
			BeanWrapper wrapper = new BeanWrapperImpl(object);
			Object val = wrapper.getPropertyValue(prop);
			
			if( val instanceof Boolean ) {
				return ((Boolean)val).booleanValue() ? trueValue : falseValue;
			}
			
			return falseValue;
		}
	}
	
	/**
	 * This property accessor inspects boolean attributes and
	 * stops when at the first one that is true, against each
	 * property it has a value to use. If it gets to the end
	 * it sets the false value.
	 * 
	 * @author Tom Spencer
	 */
	public static class MultiBoolPropertyAccessor implements PropertyAccessor {

		/** Member holds the properties to inspect and the values if true */
		private final String[][] props;
		/** Member holds the value to use if all are false */
		private final String falseValue;
		
		public MultiBoolPropertyAccessor(String[][] props, String falseValue) {
			if( props == null ) throw new IllegalArgumentException("Properties cannot be null");
			
			this.props = props;
			this.falseValue = falseValue;
			
			for( int i = 0 ; i < props.length ; i++ ) {
				if( props[i] == null || props[i].length != 2 ) {
					throw new IllegalArgumentException("The boolean props to check do not have a prop and trueValue setting");
				}
			}
		}
		
		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			if( object == null ) return falseValue;
			
			String ret = falseValue;
			BeanWrapper wrapper = new BeanWrapperImpl(object);
			for( int i = 0 ; i < props.length ; i++ ) {
				Object val = wrapper.getPropertyValue(props[i][0]);
				if( val instanceof Boolean && ((Boolean)val).booleanValue() ) {
					ret = props[i][1];
					break;
				}
			}
			
			return ret;
		}
	}
	
	/**
	 * This class gets the ID of the bean in the Spring config
	 * 
	 * @author Tom Spencer
	 */
	public static class BeanIdPropertyAccessor implements PropertyAccessor {
		
		/** The optional property from current object to get bean id for */ 
		private final String prop;
		/** Determines if the value is formatted */
		private final boolean format;
		
		/**
		 * Constructs a {@link BeanIdPropertyAccessor} with no formatting
		 */
		public BeanIdPropertyAccessor() {
			this.prop = null;
			format = false;
		}
		
		/**
		 * Constructs a BeanIdPropertyAccessor with optional formatting
		 * 
		 * @param format True if the name should be formatted
		 */
		public BeanIdPropertyAccessor(boolean format) {
			this.prop = null;
			this.format = format;
		}
		
		/**
		 * Constructs a BeanIdPropertyAccessor with optional formatting
		 * and to obtain a sub-object first.
		 * 
		 * @param prop The property to get ID of
		 * @param format True if the name should be formatted
		 */
		public BeanIdPropertyAccessor(String prop, boolean format) {
			this.format = format;
			this.prop = prop;
		}

		/**
		 * Simply gets the ID of the object from the compiler
		 */
		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			Object obj = object;
			if( prop != null ) {
				BeanWrapper wrapper = new BeanWrapperImpl(object);
				obj = wrapper.getPropertyValue(prop);
			}
			
			return PropertyAccessors.formatObject(compiler.findId(obj), format);
		}
	}

	
	/**
	 * Property accessor using the objects class name or
	 * the class name of a attribute of current object
	 * 
	 * @author Tom Spencer
	 */
	public static class ClassNamePropertyAccessor implements PropertyAccessor {
		
		/** The optional property of current object to get class name of */
		private final String prop;
		/** Determines if the return the simple class name or the full class name */
		private final boolean simple;
		
		/**
		 * Constructs a {@link ClassNamePropertyAccessor} returning
		 * the full class name.
		 */
		public ClassNamePropertyAccessor() {
			this.prop = null;
			simple = false;
		}
		
		/**
		 * Constructs a {@link ClassNamePropertyAccessor} with
		 * flag to say whether its the full name or simple.
		 * 
		 * @param simple True if we want the simple class name returned
		 */
		public ClassNamePropertyAccessor(boolean simple) {
			this.prop = null;
			this.simple = simple;
		}
		
		/**
		 * Constructs a {@link ClassNamePropertyAccessor} with
		 * property to get the class name of and whether to use
		 * the simple class name or the full class name.
		 * 
		 * @param prop
		 * @param simple
		 */
		public ClassNamePropertyAccessor(String prop, boolean simple) {
			this.prop = prop;
			this.simple = simple;
		}

		/**
		 * Simple returns the classname of the object as either full
		 * name (with package) or simple.
		 */
		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			Object obj = object;
			
			if( this.prop != null ) {
				BeanWrapper wrapper = new BeanWrapperImpl(object);
				obj = wrapper.getPropertyValue(prop);
			}
			
			Class<?> cls = 
				obj instanceof Class<?> ? (Class<?>)obj : obj.getClass();
			
			return simple ? cls.getSimpleName() : cls.getName();
		}
	}
	
	/**
	 * This class gets the value of a particular annotation
	 * property. This target object is first determined if 
	 * its a method (if so it uses the method annotation) or
	 * otherwise just the annotation.
	 * 
	 * @author Tom Spencer
	 */
	public static class AnnotationParamPropertyAccessor implements PropertyAccessor {
		
		private final Class<? extends Annotation> annotation;
		private final String property;
		
		public AnnotationParamPropertyAccessor(Class<? extends Annotation> annotation, String prop) {
			this.annotation = annotation;
			this.property = prop;
		}

		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			if( object == null ) return null;
			
			Object annon = null;
			
			if( object instanceof Method ) {
				annon = ((Method)object).getAnnotation(annotation);
			}
			else {
				annon = object.getClass().getAnnotation(annotation);
			}
			
			if( annon != null ) {
				try {
					Method getter = annon.getClass().getMethod(property, (Class<?>[])null);
					Object val = getter.invoke(annon, (Object[])null);
					return val != null ? val.toString() : null;
				}
				catch( Exception e ) {
					throw new IllegalArgumentException("Cannot get annotation parameter", e);
				}
			}

			return null;
		}
	}

	/**
	 * A chained property accessor allows the creation of a
	 * strategy of property accessors, starting with the first
	 * until one property accessor has a non-null answer. 
	 * 
	 * @author Tom Spencer
	 *
	 */
	public static class ChainedPropertyAccessor implements PropertyAccessor {
		
		public final PropertyAccessor[] accessors;
		
		public ChainedPropertyAccessor(PropertyAccessor... accessors) {
			this.accessors = accessors;
		}
		
		public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name) {
			String ret = null;
			int i = 0;
			while( ret == null && i < accessors.length ) {
				ret = accessors[i].getProperty(compiler, type, object, name);
				i++;
			}
			
			return ret;
		}
	}


	/**
	 * Formats an object value into a string.
	 * 
	 * @param val The object value to format
	 * @param format If true, splits the name so "someValue" becomes "Some Value"
	 * @return The formatted string
	 */
	public static String formatObject(Object val, boolean format) {
		String ret = val != null ? val.toString() : null;
		if( ret != null && format ) {
			StringBuilder buf = new StringBuilder();
			char[] c = ret.toCharArray();
			for( int i = 0 ; i < c.length ; i++ ) {
				if( i == 0 ) buf.append(Character.toUpperCase(c[i]));
				else if( Character.isUpperCase(c[i]) ) buf.append(' ').append(c[i]);
				else buf.append(c[i]);
			}
			
			ret = buf.toString();
		}
		
		return ret;
	}
}
