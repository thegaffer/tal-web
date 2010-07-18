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
 * standard_dojo.js
 * 
 * This file implements our required outer functions using the
 * DOJO library. Our templates are unaware of the use of DOJO
 * allowing us to replace in the future.
 */

/* Require the various DOJO bits */
dojo.require("dojo.parser");
dojo.require("dojox.widget.DynamicTooltip");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.DateTextBox");
dojo.require("dijit.form.TimeTextBox");
dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.form.NumberTextBox");
dojo.require("dijit.form.Form");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.CheckBox");
//dojo.require("dijit.form.RadioButton");

// Add the DOJO style sheet dynamically - no CSS, no DOJO!
addStyleSheet('/js/dojo/resources/dojo.css');
addStyleSheet('/js/dijit/themes/tundra/tundra.css');
dojo.addOnLoad(function(){dojo.addClass(document.documentElement, "tundra")});

/** Internal helper to add a style sheet to the page */
function addStyleSheet(ref) {
	var cssNode = document.createElement('link');
	cssNode.type = 'text/css';
	cssNode.rel = 'stylesheet';
	// TODO: The fixed ref to the app needs to be dynamic!!
	cssNode.href = '/web-testapp' + ref;
	cssNode.media = 'screen';
	dojo.query("head")[0].appendChild(cssNode);
}

/** DOJO Implementation of the instruction to run on load ending */
function dynamicOnLoad(fn) {
	dojo.addOnLoad(fn);
}

/**
 * Called to attach a dynamic reference to the element
 * 
 * @param elementName The name of the element (a style class it has)
 * @param roleName The role to attach reference to
 * @param pageUrl The URL
 */
function dynamicTitleAttach(propertyName, roleName) {
	var wrappers = dojo.query("." + propertyName);
	if( wrappers == null ) return;
	
	for( var i = 0 ; i < wrappers.length ; i++ ) {
		if( roleName != null ) {
			var fields = dojo.query("." + roleName, wrappers[i]);
			if( fields != null ) {
				for( var j = 0 ; j < fields.length ; j++ ) {
					var tt = new dojox.widget.DynamicTooltip();
					tt.href = dojo.attr(fields[j], "referenceUrl");
					tt.addTarget(fields[j].id);
				}
			}
		}
		else {
			var tt = new dojox.widget.DynamicTooltip();
			tt.href = dojo.attr(wrappers[i], "referenceUrl");
			tt.addTarget(wrappers[i].id);
		}
	}
}


/** Attaches a handler to some element */
function dynamicHandlerAttach(propertyName, roleName, eventName, handlerName) {
	var wrappers = dojo.query("." + propertyName);
	if( wrappers == null ) return;
	for( var i = 0 ; i < wrappers.length ; i++ ) {
		if( roleName != null ) {
			var fields = dojo.query("." + roleName, wrappers[i]);
			console.log("*** Pre-add: " + handlerName + " fields: " + fields);
			if( fields != null ) {
				for( var j = 0 ; j < fields.length ; j++ ) {
					// TODO: Check that field directly belongs to wrapper of property!
					dojo.connect(fields[j], eventName, null, handlerName);
				}
			}
		}
		else {
			dojo.connect(wrappers[i], eventName, null, handlerName);
		}
	}
}

/** Private function that attaches the dynamic dijit to a field */
function dynamicFieldAttach(wrapperType, roleName, type, attributes) {
	var wrappers = dojo.query("." + wrapperType);
	if( wrappers == null ) return;
	for( var i = 0 ; i < wrappers.length ; i++ ) {
		var fields = dojo.query("." + roleName, wrappers[i]);
		if( fields != null ) {
			for( var j = 0 ; j < fields.length ; j++ ) {
				dojo.attr(fields[j], "dojoType", type);
				if( attributes != null ) dojo.attr(fields[j], attributes);
			}
			
			// Make dojo aware of these dijit's
			dojo.parser.parse(wrappers[i]);
		}
	}
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_input(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.TextBox", attributes);
}

/** Attaches standard dynamic behaviour to a validating text box */
function dynamicFieldAttach_text(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.ValidationTextBox", attributes);
}

/** Attaches standard dynamic behaviour to a memo field */
function dynamicFieldAttach_memo(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.Textarea", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_number(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.NumberTextBox", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_currency(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.CurrencyTextBox", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_numberspinner(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.NumberSpinnerTextBox", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_date(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.DateTextBox", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_time(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.TimeTextBox", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_select(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.FilteringSelect", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_combo(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.ComboBox", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_button(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.Button", attributes);
}

/** Attaches standard dynamic behaviour to a text box */
function dynamicFieldAttach_checkbox(wrapperType, roleName, attributes) {
	dynamicFieldAttach(wrapperType, roleName, "dijit.form.CheckBox", attributes);
}

