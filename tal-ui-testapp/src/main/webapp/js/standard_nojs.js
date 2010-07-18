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

/**
 * standard_nojs.js
 * 
 * This file implements our required outer functions with noops
 * leaving us with no dynamic effects. Importing this file allow
 * a developer to test how the app works with no JavaScript.
 * 
 * This file is also useful to implement the required functions
 * in any new JS library.
 */

/**
 * Called to ensure something happens only after the page
 * has loaded. 
 * 
 * @param fn The function to call after load
 */
function dynamicOnLoad(fn) {
}

/**
 * Called to attach a dynamic reference to the element
 * 
 * @param elementName The name of the element (a style class it has)
 * @param roleName The role to attach reference to
 */
function dynamicTitleAttach(propertyName, roleName) {
}

/**
 * Called to attach a handler to any property
 * 
 * @param propertyName The name of the property (wrapper will have this class set)
 * @param roleName The role of the element inside wrapper (or null if to apply to wrapper)
 * @param eventName The event to attach
 * @param handlerName The handler to call
 */
function dynamicHandlerAttach(propertyName, roleName, eventName, handlerName) {	
}

/** 
 * Called to attach a dynamic basic input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_input(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic validating input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_text(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic memo input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_memo(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic number input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_number(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic currency input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_currency(wrapperType, roleName, attributes) {
	
}

/** 
 * Called to attach a dynamic number spinner input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_numberspinner(wrapperType, roleName, attributes) {
	
}

/** 
 * Called to attach a dynamic date input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_date(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic time input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_time(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic select widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_select(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic combobox widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_combo(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic button widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_button(wrapperType, roleName, attributes) {
}

/** 
 * Called to attach a dynamic checkbox widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_checkbox(wrapperType, roleName, attributes) {
}
