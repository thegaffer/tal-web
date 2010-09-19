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

package org.talframework.talui.template.compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.CommandElement;
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.behaviour.GroupElement;
import org.talframework.talui.template.behaviour.InnerTemplateElement;
import org.talframework.talui.template.behaviour.MemberProperty;
import org.talframework.talui.template.render.elements.special.EmptyElement;


/**
 * This class implements the TemplateRenderMold interface.
 * It does not generate any output itself, but it holds
 * in turn a series of {@link TemplateElementRenderMold}
 * instances which it delegates for each template element
 * in the template. The molds it holds are held by:
 * 
 * <ul>
 * <li>Named molds - molds which are matched to the
 * template elements by name (actually the name is in
 * the form of "templateName.elementName"</li>
 * <li>Style molds - molds which are matched to the
 * type of the template element and the 'styles' that
 * the compiler contains.</li>
 * <li>A default mold - that is used if no direct
 * match can be found.</li>
 * </ul>
 * 
 * <p>There is no need to derive from this class although
 * it can be overridden. The main purpose for this would
 * be to add or remove compiler styles on entry/exit to
 * a particular type of element.</p>
 * 
 * <p>Additionally there is a helper method to configure
 * the styles molds through the use of a 
 * {@link TemplateElementRenderMoldFactory}.</p>
 * 
 * @author Tom Spencer
 */
public class BasicTemplateRenderMold implements TemplateRenderMold {
	
	/** Member holds the default render template to use */
	private TemplateElementRenderMold defaultMold = null;
	/** Member holds the element templates mapped to an element name and applicable styles */
	private List<NamedMold> namedMolds = null;
	/** Member holds the element templates mapped to types and applicable styles */
	private List<TypedMold> styledMolds = null;
	/** Member holds the element templates mapped to behaviours and applicable styles */
	private List<BehaviourMold> behaviourMolds = null;
	
	/**
	 * Configures the mold given a mold factory.
	 * 
	 * @param factory The factory to use
	 * @param includeForms If true forms support is added on
	 */
	public void configureMold(TemplateElementRenderMoldFactory factory, boolean includeForms) {
		if( factory.getDefaultMold() == null ) throw new IllegalArgumentException("The template element render mold factory has no default mold set, cannot proceed: " + factory);
		setDefaultMold(factory.getDefaultMold());
		
		addBehaviourMold(InnerTemplateElement.class, null, factory.getInnerTemplateMold());
		addTypedMold("message-group", null, factory.getMessagesMold());
		
		// Default
		addBehaviourMold(DynamicProperty.class, null, factory.getDefaultPropMold());
		addBehaviourMold(GroupElement.class, null, factory.getDefaultGroupMold());
		addBehaviourMold(MemberProperty.class, null, factory.getDefaultMemberMold());
		addBehaviourMold(CommandElement.class, null, factory.getDefaultCommandMold());
		
		// Table
		addTypedMold("grid-group", null, factory.getTableGroupMold());
		addBehaviourMold(MemberProperty.class, "table,table-member", factory.getTableMemberMold());
		addBehaviourMold(DynamicProperty.class, "table", factory.getTableInnerPropMold());
		addBehaviourMold(GroupElement.class, "table", factory.getTableInnerGroupMold());
		addBehaviourMold(MemberProperty.class, "table", factory.getTableInnerMemberMold());
		addBehaviourMold(CommandElement.class, "table", factory.getTableInnerCommandMold());
		
		// Forms
		if( includeForms ) {
			addTypedMold("form-group", null, factory.getFormGroupMold());
			addBehaviourMold(DynamicProperty.class, "form", factory.getFormInnerPropMold());
			addBehaviourMold(GroupElement.class, "form", factory.getFormInnerGroupMold());
			addBehaviourMold(MemberProperty.class, "form", factory.getFormInnerMemberMold());
			addBehaviourMold(CommandElement.class, "form", factory.getFormInnerCommandMold());
		}
	}
	
	/**
	 * @return the defaultTemplate
	 */
	public TemplateElementRenderMold getDefaultMold() {
		return defaultMold;
	}

	/**
	 * @param defaultMold the defaultTemplate to set
	 */
	public void setDefaultMold(TemplateElementRenderMold defaultMold) {
		this.defaultMold = defaultMold;
	}
	
	/**
	 * Setter to add named molds only be name. The molds will not be
	 * added with any style selectors.
	 * 
	 * @param namedMolds The named molds to add
	 */
	public void setNamedMolds(Map<String, TemplateElementRenderMold> namedMolds) {
		this.namedMolds = null;
		
		if( namedMolds == null ) return;
		
		Iterator<String> it = namedMolds.keySet().iterator();
		while( it.hasNext() ) {
			String name = it.next();
			addNamedMold(name, null, namedMolds.get(name));
		}
	}
	
	/**
	 * Helper to add a mold against a specific element
	 * name (and styles) 
	 * 
	 * @param name The name to check against (exact match only)
	 * @param styles The styles that should apply
	 * @param mold The mold to use (if null nothing happens) 
	 */
	public void addNamedMold(String name, String styles, TemplateElementRenderMold mold) {
		if( mold == null ) return;
		if( namedMolds == null ) namedMolds = new ArrayList<NamedMold>();
		namedMolds.add(new NamedMold(name, styles, mold));
	}
	
	/**
	 * Helper to add a mold against a specific element
	 * type (and styles) 
	 * 
	 * @param type The type to match against
	 * @param styles The styles that must apply (comma separated)
	 * @param mold The mold to use (if null nothing happens) 
	 */
	public void addTypedMold(String type, String styles, TemplateElementRenderMold mold) {
		if( mold == null ) return;
		if( styledMolds == null ) styledMolds = new ArrayList<TypedMold>();
		styledMolds.add(new TypedMold(type, styles, mold));
	}
	
	/**
	 * Helper to add a mold against a specific behvaiour
	 * class (and styles) 
	 * 
	 * @param behaviour The behaviour to check against
	 * @param styles The styles that must also apply (comma separated)
	 * @param mold The mold to use (if null nothing happens) 
	 */
	public void addBehaviourMold(Class<?> behaviour, String styles, TemplateElementRenderMold mold) {
		if( mold == null ) return;
		if( behaviourMolds == null ) behaviourMolds = new ArrayList<BehaviourMold>();
		behaviourMolds.add(new BehaviourMold(behaviour, styles, mold));
	}
	
	/**
	 * Default version of this class creates an empty render
	 * element and puts all children inside.
	 */
	public RenderElement compile(GenericCompiler compiler, Template template) {
		EmptyElement ret = new EmptyElement();
		evaluateChildren(compiler, template, ret);
		return ret;
	}

	/**
	 * Attempts to find a render template using the following
	 * order:
	 * <ul>
	 * <li>First by the name of the element against 
	 * namedTemplates</li>
	 * <li>Second by group and type of the element against 
	 * typedTamplates</li>
	 * <li>Third by dropping segments from group until left 
	 * with just the elements type against typedTemplates.</li>
	 * </ul>
	 */
	@Trace
	public RenderElement compileChild(GenericCompiler compiler, Template template, TemplateElement child) {
		TemplateElementRenderMold mold = getCorrectMold(compiler, child, namedMolds);
		if( mold == null ) mold = getCorrectMold(compiler, child, styledMolds);
		if( mold == null ) mold = getCorrectMold(compiler, child, behaviourMolds);
		
		// If all else fails use the default
		if( mold == null ) mold = defaultMold;
		
		if( mold == null ) {
			throw new IllegalArgumentException("No render template for given model element: " + child);
		}
		
		return mold.compile(compiler, this, template, child);
	}
	
	/**
	 * Helper goes through a list of MatchedMolds to find best match.
	 * This runs through all the possible molds to find the best 
	 * match.
	 * 
	 * @return The mold if one is found
	 */
	@Trace
    private TemplateElementRenderMold getCorrectMold(GenericCompiler compiler, TemplateElement element, List<? extends MatchedMold> possibleMolds) {
		if( possibleMolds == null ) return null;
		
		MatchResult result = new MatchResult();
		Iterator<? extends MatchedMold> it = possibleMolds.iterator();
		while( it.hasNext() ) {
			MatchedMold style = it.next();
			style.match(compiler, element, result);
		}
		
		return result.getMold();
	}
	
	/**
	 * Helper to evaluate the children of the container
	 * element (usually the template we are processing).
	 * The render element used is this one, so all requests
	 * for element render templates will come through this
	 * class.
	 * 
	 * @param compiler
	 * @param containerElement
	 * @param group
	 * @param baseElement
	 */
	@Trace
    protected void evaluateChildren(GenericCompiler compiler, Template template, RenderElement baseElement) {
		List<TemplateElement> elements = template.getElements();
		if( elements == null || elements.size() == 0 ) return;
		
		Iterator<TemplateElement> it = elements.iterator();
		while( it.hasNext() ) {
			TemplateElement elem = it.next();
			
			RenderElement child = compileChild(compiler, template, elem);
			
			if( child != null ) baseElement.addElement(child);
		}
	}
	
	/**
	 * Private base class used to match a mold to the given
	 * element. The basis for determining the match is done
	 * by the derived class, it could be name, type or
	 * behaviour. This base class contains all the logic for
	 * finding the best match given the styles set in the
	 * compiler.
	 * 
	 * @author Tom Spencer
	 */
	private abstract class MatchedMold {
		/** The styles that should apply for this match */
		private final String[] styles;
		/** The molds to use if a match */
		private final TemplateElementRenderMold mold;
		
		public MatchedMold(String styles, TemplateElementRenderMold mold) {
			this.mold = mold;
			
			if( styles != null && styles.indexOf(',') > 0 ) {
				this.styles = styles.split(",");
			}
			else {
				this.styles = styles != null ? new String[]{styles} : null;
			}
		}
		
		/**
		 * Determines if this mold is suitable given type and 
		 * styles. Updates the result if it is.
		 * 
		 * @param compiler The compiler
		 * @param type The type to match on
		 * @param result The result to update
		 */
		public void match(GenericCompiler compiler, TemplateElement element, MatchResult result) {
			boolean positiveMatch = isExactMatch(element);
			int inexactMatch = getInexactMatch(element);
			if( !positiveMatch && inexactMatch <= 0 ) return;
			
			// Now work out how many styles we match on
			// Obviously return 0 if a given style is not set
			int nosStyles = 0;
			if( styles != null ) {
				for( int i = 0 ; i < styles.length ; i++ ) {
					if( !compiler.isStyle(styles[i]) ) return;
				}
				
				nosStyles = styles.length;
			}
			
			result.update(positiveMatch, inexactMatch, nosStyles, mold);
		}
		
		/**
		 * Called during match to determine if this mold is an
		 * exact match for the element
		 * 
		 * @param element The element to test
		 * @return True if it is an inexact match, false otherwise
		 */
		protected abstract boolean isExactMatch(TemplateElement element);
		
		/**
		 * Called during match to get the dimensions of the 
		 * inexact match (typically the size of the regular
		 * expression that is used).
		 * 
		 * @param element The element to test
		 * @return Size of the inexact match
		 */
		protected abstract int getInexactMatch(TemplateElement element);
	}
	
	/**
	 * A typed mold is one that is matched based on the elements
	 * type.
	 * 
	 * @author Tom Spencer
	 */
	private final class TypedMold extends MatchedMold {
		private final String type;
		
		public TypedMold(String type, String styles, TemplateElementRenderMold mold) {
			super(styles, mold);
			this.type = type;
		}
		
		@Override
		protected boolean isExactMatch(TemplateElement element) {
			return this.type.equals(element.getType());
		}
		
		@Override
		public int getInexactMatch(TemplateElement element) {
			if( Pattern.matches(type, element.getType()) ) {
				return type.length();
			}
			
			return 0;
		}
	}
	
	/**
	 * A named mold is one that is matched purely on the name
	 * 
	 * @author Tom Spencer
	 */
	private final class NamedMold extends MatchedMold {
		private final String name;
		
		public NamedMold(String name, String styles, TemplateElementRenderMold mold) {
			super(styles, mold);
			this.name = name;
		}
		
		@Override
		protected boolean isExactMatch(TemplateElement element) {
			return this.name.equals(element.getName());
		}
		
		@Override
		protected int getInexactMatch(TemplateElement element) {
			return 0;
		}
	}
	
	/**
	 * A behaviour mold is one that is setup to match based
	 * on whether the element implements the given behaviour
	 * interface. 
	 * 
	 * @author Tom Spencer
	 */
	private final class BehaviourMold extends MatchedMold {
		private Class<?> behaviour = null;
		
		public BehaviourMold(Class<?> behaviour, String styles, TemplateElementRenderMold mold) {
			super(styles, mold);
			this.behaviour = behaviour;
		}
		
		@Override
		protected boolean isExactMatch(TemplateElement element) {
			return element.getBehaviour(behaviour) != null;
		}
		
		@Override
		protected int getInexactMatch(TemplateElement element) {
			return 0;
		}
	}
	
	private class MatchResult {
		private boolean exactMatch = false;
		private int inexactMatch = 0;
		private int stylesMatched = 0;
		private TemplateElementRenderMold mold = null;
		
		public void update(boolean exact, int nameLength, int styles, TemplateElementRenderMold mold) {
			if( exact ) {
				// Current has to be exact and match on more styles
				if( this.exactMatch && (styles < this.stylesMatched) ) return;
				
				this.exactMatch = true;
				this.inexactMatch = 0;
				this.stylesMatched = styles;
				this.mold = mold;
			}
			else {
				// If current is exact, or has more inexact chars or has more styles, ignore
				if( this.exactMatch ) return;
				else if( this.stylesMatched > styles ) return;
				else if( this.inexactMatch >= nameLength) return;
				
				this.inexactMatch = nameLength;
				this.stylesMatched = styles;
				this.mold = mold;
			}
		}
		
		/**
		 * @return the mold
		 */
		public TemplateElementRenderMold getMold() {
			return mold;
		}
	}
}
