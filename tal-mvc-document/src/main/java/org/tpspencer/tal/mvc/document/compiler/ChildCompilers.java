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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.mvc.document.AppElement;

/**
 * This class holds all the standard {@link ChildCompiler}
 * instances and common helpers to those.
 * 
 * @author Tom Spencer
 */
public class ChildCompilers {
	
	/**
	 * Base class for most compilers
	 * 
	 * @author Tom Spencer
	 */
	public static abstract class BaseChildCompiler implements ChildCompiler {

		/** The compilers to use for the children */
		private final EntityCompiler[] childCompilers;
		
		/**
		 * Constructs a new child compiler
		 * 
		 * @param compilers The compiler(s) to use
		 */
		public BaseChildCompiler(EntityCompiler[] compilers) {
			this.childCompilers = compilers;
		}
		
		/**
		 * Constructs a new child compiler
		 * 
		 * @param compiler The compiler to use
		 */
		public BaseChildCompiler(EntityCompiler compiler) {
			this.childCompilers = new EntityCompiler[]{compiler};
		}
		
		/**
		 * Helper to compile the given object against the correct
		 * child compiler.
		 * 
		 * @param compiler The compiler
		 * @param app The contents
		 * @param parent The parent element
		 * @param obj The object in question
		 * @param name The name of the object
		 */
		protected void compileObject(AppCompiler compiler, AppElement app, AppElement parent, Object obj, String name) {
			if( obj == null ) return;
			if( childCompilers == null ) throw new IllegalArgumentException("Attempt to compile children with no child compilers!");
			
			for( int i = 0 ; i < childCompilers.length ; i++ ) {
				if( childCompilers[i].getExpected() == null ||
						childCompilers[i].getExpected().isAssignableFrom(obj.getClass()) ) {
					childCompilers[i].compile(compiler, app, parent, obj, name);
					break;
				}
			}
		}
	}

	/**
	 * This very simply child compiler simply gets a property
	 * of the current element and treats it as a map, list,
	 * array or a simple bean. If its a map, list or array it
	 * will iterate it and pass each member to the first 
	 * entity compiler that can deal with it. If its a bean
	 * it directly passes it to the child compiler. If the
	 * property is null then the child compiler will use the
	 * same undeylying element as the parent {@link AppElement}
	 * supplied at compile time.
	 * 
	 * @author Tom Spencer
	 */
	public static class SimpleChildCompiler extends BaseChildCompiler {
		
		/** The name of the child property */
		private final String childProperty; 
		
		/**
		 * Constructs a new child compiler
		 * 
		 * @param prop The property to get children from
		 * @param compilers The compiler(s) to use
		 */
		public SimpleChildCompiler(String prop, EntityCompiler[] compilers) {
			super(compilers);
			this.childProperty = prop;
		}
		
		/**
		 * Constructs a new child compiler with a single compiler
		 * 
		 * @param prop The property to get children from
		 * @param compiler The compiler to use
		 */
		public SimpleChildCompiler(String prop, EntityCompiler compiler) {
			super(compiler);
			this.childProperty = prop;
		}
		
		/**
		 * Compiles the given children using the child compilers.
		 * 
		 * @param compiler The app compiler
		 * @param app The contents
		 * @param parent The parent element
		 */
		@SuppressWarnings("unchecked")
		public void compile(AppCompiler compiler, AppElement app, AppElement parent) {
			Object children = null;
			if( childProperty != null ) {
				BeanWrapper wrapper = new BeanWrapperImpl(parent.getElement());
				children = wrapper.getPropertyValue(childProperty);
			}
			else {
				children = parent.getElement();
			}
			
			// Treat as a collection
			if( children instanceof Collection ) {
				Collection<Object> coll = (Collection<Object>)children;
				Iterator<Object> it = coll.iterator();
				while( it.hasNext() ) {
					compileObject(compiler, app, parent, it.next(), null);
				}
			}
			
			// Treat as a map
			else if( children instanceof Map ) {
				Map<Object, Object> map = (Map<Object, Object>)children;
				Iterator<Object> it = map.keySet().iterator();
				while( it.hasNext() ) {
					Object n = it.next();
					Object obj = map.get(n);
					compileObject(compiler, app, parent, obj, n.toString());
				}
			}
			
			// Treat as an array
			else if( children != null && children.getClass().isArray() ) {
				Object[] arr = (Object[])children;
				for( int i = 0 ; i < arr.length ; i++ ) {
					compileObject(compiler, app, parent, arr[i], null);
				}
			}
			
			// Treat as individual child
			else if( children != null ) {
				compileObject(compiler, app, parent, children, childProperty);
			}
		}
	}
	
	/**
	 * Compiles all methods of the target object
	 * 
	 * @author Tom Spencer
	 */
	public static class MethodChildCompiler extends BaseChildCompiler {
		
		private final Class<? extends Annotation> annotation;
		
		/**
		 * Constructs a new child compiler
		 * 
		 * @param compilers The compiler(s) to use
		 */
		public MethodChildCompiler(Class<? extends Annotation> annotation, EntityCompiler[] compilers) {
			super(compilers);
			this.annotation = annotation;
		}
		
		/**
		 * Constructs a new child compiler
		 * 
		 * @param compiler The compiler to use
		 */
		public MethodChildCompiler(Class<? extends Annotation> annotation, EntityCompiler compiler) {
			super(compiler);
			this.annotation = annotation;
		}

		/**
		 * Gets all methods and compiles then. Only does so if
		 * method has given annotation.
		 */
		public void compile(AppCompiler compiler, AppElement app, AppElement parent) {
			Object obj = parent.getElement();
			if( obj == null ) return;
			
			Method[] methods = obj.getClass().getMethods();
			for( int i = 0 ; i < methods.length ; i++ ) {
				if( annotation == null || methods[i].getAnnotation(annotation) != null ) {
					compileObject(compiler, app, parent, methods[i], methods[i].getName());
				}
			}
		}
	}
	
	/**
	 * Creates a child for each 'property' of class hold in element.
	 * If element directly holds a class then great, otherwise it
	 * takes the class of the object held.
	 * 
	 * @author Tom Spencer
	 */
	public static class BeanPropertyChildCompiler extends BaseChildCompiler {
		
		public BeanPropertyChildCompiler(EntityCompiler compiler) {
			super(compiler);
		}
		
		public BeanPropertyChildCompiler(EntityCompiler[] compilers) {
			super(compilers);
		}

		/**
		 * Simply takes the class of the parent element and creates
		 * an {@link AppElement} for each property that is not 'class'.
		 */
		public void compile(AppCompiler compiler, AppElement app, AppElement parent) {
			try {
				Class<?> cls = 
					parent.getElement() instanceof Class<?> ?
							(Class<?>)parent.getElement() :
							parent.getElement().getClass();
				
				BeanInfo info = Introspector.getBeanInfo(cls);
				PropertyDescriptor[] props = info.getPropertyDescriptors();
				for( int i = 0 ; i < props.length ; i++ ) {
					if( !"class".equals(props[i].getName()) ) {
						compileObject(compiler, app, parent, props[i], props[i].getName());
					}
				}
			}
			catch( IntrospectionException e ) {
				throw new IllegalArgumentException("Cannot compile due to introspection error", e);
			}
		}
	}
	
	/**
	 * If the target object is a method, this will attempt to invoke
	 * the compilation on every parameter that exposes a certain 
	 * annotation.
	 * 
	 * @author Tom Spencer
	 */
	public static class ParamAnnotationTypeChildCompiler extends BaseChildCompiler {
		
		private final Class<? extends Annotation> annotation;

		public ParamAnnotationTypeChildCompiler(Class<? extends Annotation> annotation, EntityCompiler compiler) {
			super(compiler);
			this.annotation = annotation;
		}
		
		public ParamAnnotationTypeChildCompiler(Class<? extends Annotation> annotation, EntityCompiler[] compilers) {
			super(compilers);
			this.annotation = annotation;
		}
		
		public void compile(AppCompiler compiler, AppElement app, AppElement parent) {
			Object obj = parent.getElement();
			if( obj instanceof Method ) {
				Method method = (Method)obj;
				
				Annotation[][] paramAnnotations = method.getParameterAnnotations();
				if( paramAnnotations == null ) return;
				for( int j = 0 ; j < paramAnnotations.length ; j++ ) {
					for( int k = 0 ; k < paramAnnotations[j].length ; k++ ) {
						if( paramAnnotations[j][k].annotationType().equals(annotation) ) {
							compileObject(compiler, app, parent, method.getParameterTypes()[j], null);
						}
					}
				}
			}
		}
	}
}
