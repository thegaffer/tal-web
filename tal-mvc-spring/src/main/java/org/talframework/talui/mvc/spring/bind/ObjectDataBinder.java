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

package org.talframework.talui.mvc.spring.bind;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.DataBinder;
import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.controller.ObjectCreator;

/**
 * With Spring there is a DataBinder that will bind from an input
 * map of property values to a command object. This works well,
 * except when the command object contains embedded members. In 
 * this case the DataBinder will not create new instances of these 
 * members unless there is an explicit property set. In modern apps 
 * where some logic is in Javascript and new members could be 
 * created this is a limitation. An ObjectDataBinder wraps a 
 * databinder to pre-parse the input to create all the member 
 * objects as appropriate. It then passes control to the real data 
 * binder.
 * 
 * @author Tom Spencer
 */
public class ObjectDataBinder extends DataBinder {
	
	/** Member holds the prefix for hidden fields, used to indicate existence of field that might not be submitted */
	private String fieldMarkerPrefix = "_";
	/** Map holds the name of any fields to combined together with the combined field name */
	private Map<String, FieldCombiner> combinedFields = null;
	
	public ObjectDataBinder(Object target) {
		super(target);
		
	}
	
	public ObjectDataBinder(Object target, String objectName) {
		super(target, objectName);
	}
	
	/**
	 * Overridden doBind method will hunt out any embedded properties 
	 * and ensure they exist on the command object
	 */
	@Trace
	protected void doBind(MutablePropertyValues mpvs) {
	    checkFieldMarkers(mpvs);
        checkMembersExist(mpvs);
        combineFields(mpvs);
                
        super.doBind(mpvs);
	}
	
	/**
	 * Internal method to check for any non-submitted fields.
	 * Checkboxes and other button fields are not submitted unless
	 * they are checked. The idea is that a hidden property of the
	 * same named with a underscore prefix is added. Then this 
	 * method tries to find a submitted property of the same name
	 * without the underscore. If one is not found then a default
	 * value is added to the list of properties before binding.
	 * 
	 * <p><b>Note: </b>This is taken from Spring MVC. It is copied
	 * here so that we don't need to include Spring MVC libs</p>
	 * 
	 * TODO: This currently only works for root props, if accessed (something._marker) it will not work!!!
	 */
	protected void checkFieldMarkers(MutablePropertyValues mpvs) {
		if (getFieldMarkerPrefix() != null) {
			String fieldMarkerPrefix = getFieldMarkerPrefix();
			PropertyValue[] pvArray = mpvs.getPropertyValues();
			for (int i = 0; i < pvArray.length; i++) {
				PropertyValue pv = pvArray[i];
				if (pv.getName().startsWith(fieldMarkerPrefix)) {
					String field = pv.getName().substring(fieldMarkerPrefix.length());
					if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
						Class<?> fieldType = getPropertyAccessor().getPropertyType(field);
						mpvs.addPropertyValue(field, getEmptyValue(field, fieldType));
					}
					mpvs.removePropertyValue(pv);
				}
			}
		}
	}

	/**
	 * Determines the default value for a non-submitted property.
	 * Booleans are returned as false, arrays as empty arrays and
	 * others as null.
	 */
	private Object getEmptyValue(String field, Class<?> fieldType) {
		if (fieldType != null && boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
			// Special handling of boolean property.
			return Boolean.FALSE;
		}
		else if (fieldType != null && fieldType.isArray()) {
			// Special handling of array property.
			return Array.newInstance(fieldType.getComponentType(), 0);
		}
		else {
			// Default value: try null.
			return null;
		}
	}
	
	/**
	 * This internal helper will combine any fields into new 
	 * fields as specified in the configuration. The most 
	 * common use case is where a date/time field is two (or
	 * more) submitted fields, but should be combined into a 
	 * single field for matching with the input.
	 */
	private void combineFields(MutablePropertyValues mpvs) {
		if( combinedFields == null ) return;
		
		Map<String, Object> newMembers = new HashMap<String, Object>();
		
		PropertyValue[] pvArray = mpvs.getPropertyValues();
		for( int i = 0 ; i < pvArray.length ; i++ ) {
			PropertyValue pv = pvArray[i];
			String param = pv.getName();
			int lastDot = param.lastIndexOf('.');
			String attr = (lastDot >= 0) ? param.substring(lastDot + 1) : param;
			String prefix = (lastDot >= 0) ? param.substring(0, lastDot + 1) : null;
			
			FieldCombiner combiner = (FieldCombiner)combinedFields.get(attr);
			if( combiner != null ) {
				String newAttr = combiner.getFieldName(pv, attr);
				if( newAttr != null ) {
					newAttr = prefix != null ? prefix + newAttr : newAttr;
					Object val = combiner.getCombinedField(pv, attr, newMembers.get(newAttr));
					if( val != null ) newMembers.put(newAttr, val);
				}
			}
		}
		
		// Now add in any new members
		Iterator<String> it = newMembers.keySet().iterator();
		while( it.hasNext() ) {
			String attr = it.next();
			Object val = newMembers.get(attr);
			if( val != null ) {
				mpvs.addPropertyValue(attr, val.toString());
			}
		}
	}
	
	/**
	 * Internal function to modify the incoming property values 
	 * with appropriate members if there are any member properties.
	 * 
	 * @param mpvs
	 */
	private void checkMembersExist(MutablePropertyValues mpvs) {
		Map<String, Object> uniqueMembers = findUniqueEmbeddedMembers(mpvs);	// Holds any member prop with dims as map against it
		if( uniqueMembers == null ) return;
		
		StringBuilder tempBuf = null;
		
		Iterator<String> it = uniqueMembers.keySet().iterator();
		while( it.hasNext() ) {
			String member = it.next();
			
			// See if itself is an embedded member
			if( member.indexOf('.') >= 0 ) {
				if( tempBuf == null ) tempBuf = new StringBuilder();
				else tempBuf.setLength(0);
				
				String[] elements = member.split(".");
				for( int i = 0 ; i < elements.length ; i++ ) {
					if( i > 0 ) tempBuf.append('.');
					tempBuf.append(elements[i]);
					
					// Create if this member does not yet exist
					if( getPropertyAccessor().getPropertyValue(tempBuf.toString()) == null ) {
						createMember(member, elements[i], uniqueMembers.get(tempBuf.toString()));
					}
				}
			}
			else {
				if( getPropertyAccessor().getPropertyValue(member) == null ) {
					createMember(member, member, uniqueMembers.get(member));
				}
			}
		}
	}
	
	/**
	 * Helper to create an embedded member object.
	 * 
	 * @param member The full name of the attribute from root
	 * @param def The definition to the object directly holding this member
	 * @param prop The name of the property to create (tip of member)
	 * @param indexes Any indexes found
	 */
	private void createMember(String member, String prop, Object indexes) {
		PropertyAccessor accessor = getPropertyAccessor();
		Class<?> expectedClass = accessor.getPropertyType(member); 
		
		// Array
		if( expectedClass.isArray() ) {
			int dims = 1;
			if( indexes != null ) dims = ((Number)indexes).intValue() + 1;
			
			Object[] arr = (Object[])Array.newInstance(expectedClass.getComponentType(), dims);
			for( int i = 0 ; i < dims ; i++ ) {
				arr[i] = ObjectCreator.createObject(expectedClass.getComponentType());
			}
			accessor.setPropertyValue(member, arr);
		}
		
		// List
		else if( List.class.isAssignableFrom(expectedClass) ) {
			// Create a list and populate it with type params??
		}
		
		// Map
		else if( Map.class.isAssignableFrom(expectedClass) ) {
			// Add this in, index will be Map of indexes!
		}
		
		// Normal member
		else {
			accessor.setPropertyValue(member, ObjectCreator.createObject(expectedClass));
		}
	}
	
	/**
	 * Helper method returns unique embedded members in the list of 
	 * properties. These are properties with the . notation. So for
	 * instance "member.name" would result in 'member' being in the 
	 * returned map.
	 * 
	 * <p>This will take into account indexed properties []. It will 
	 * store these in the map with a positive dimensions. Normal 
	 * members (those without the []) will have a dimension of -1</p>
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> findUniqueEmbeddedMembers(MutablePropertyValues mpvs) {
		Map<String, Object> uniqueMembers = new HashMap<String, Object>();
		
		PropertyValue[] pvArray = mpvs.getPropertyValues();
		for (int i = 0; i < pvArray.length; i++) {
			PropertyValue pv = pvArray[i];
			int index = pv.getName().indexOf('.');
			if( index > 0 ) {
				String dim = null;
				String member = pv.getName().substring(0, index);
				
				// Remove any index
				if( member.charAt(member.length() - 1) == ']' ) {
					// Determine index and update dimension
					int i2 = member.lastIndexOf('[');
					if( i2 > 0 ) {
						dim = member.substring(i2 + 1, member.length() - 1);
						member = member.substring(0, i2);
					}
				}
			
				// Add the member in
				if( dim != null ) {
					try {
						int d = Integer.parseInt(dim);
						Integer cur = (Integer)uniqueMembers.get(member);
						if( cur == null ) cur = new Integer(d);
						else if( cur.intValue() < d ) cur = new Integer(d);
						uniqueMembers.put(member, cur);
					}
					catch(Exception e) {
						// Not a number
						Set<String> m = (Set<String>)uniqueMembers.get(member);
						if( m == null ) m = new TreeSet<String>();
						m.add(dim);
						uniqueMembers.put(member, m);
					}
				}
				else {
					uniqueMembers.put(member, null);
				}
			}
		}
		
		return uniqueMembers;
	}
	
	/////////////////////////////////////////////////////
	// Getters and Setters
	
	/**
	 * @return the fieldMarkerPrefix
	 */
	public String getFieldMarkerPrefix() {
		return fieldMarkerPrefix;
	}

	/**
	 * @param fieldMarkerPrefix the fieldMarkerPrefix to set
	 */
	public void setFieldMarkerPrefix(String fieldMarkerPrefix) {
		this.fieldMarkerPrefix = fieldMarkerPrefix;
	}

	/**
	 * @return the combinedFields
	 */
	public Map<String, FieldCombiner> getCombinedFields() {
		return combinedFields;
	}

	/**
	 * @param combinedFields the combinedFields to set
	 */
	public void setCombinedFields(Map<String, FieldCombiner> combinedFields) {
		this.combinedFields = combinedFields;
	}
}
