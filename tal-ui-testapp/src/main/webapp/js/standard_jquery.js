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
 * standard_jquery.js
 * 
 * This file implements our required outer functions using JQuery. 
 */

$(document).ready(function() {
	/* Attach refresh icon to all div window-refresh and auto initiate AJAX windows */
	$("span.window-refresh").each( function(item) {
		var id = $(this).attr("id");
		var context = $("#" + id + "-content").first();
		
		$(this).show();
		$(this).click(function() {
			$("#" + id + "-form").first().submit();
		});
		
		jquery_attachRefresh("#" + id + "-form", "#" + id + "-content");
	});
	
	// Automatically load any async panels
	$(".async-panel").each( function(item) {
		var context = $(this).parent();
		var id = $(this).attr("id");
		
		$.ajax({ 
			url: context.attr("refreshWindow"),
			dataType: "html",
			context: context, 
			success: function(data, status, request){
	        	$(this).html(data);
	        	
	        	// Attach the submit form
	        	jquery_attachRefresh("#" + id + "-form", "#" + id + "-content");
	      	}
		});
	});
});

/**
 * Helper to attach an AJAX submit to a windows hidden
 * model form.
 * 
 * @param formId The formId
 * @param contextId The contextId for the content
 * @return
 */
function jquery_attachRefresh(formId, contextId) {
	var frm = $(formId).first();
	var context = $(contextId).first();
	
	frm.submit(function() {
		var frmId = formId;
		var ctxId = contextId;
		
		$.ajax({ 
			url: frm.attr("action"),
			cache: false,
			data: frm.serialize(),
			dataType: "html",
			context: context, 
			success: function(data, status, request){
	        	$(this).html(data);
	        	
	        	// Re-attach this handler
	        	jquery_attachRefresh(frmId, ctxId);
	      	}
		});
		
		return false;
	});
}

/**
 * Called to ensure something happens only after the page
 * has loaded. 
 * 
 * @param fn The function to call after load
 */
function dynamicOnLoad(fn) {
	$(document).ready(fn);
}

function standardJquerySelector(propertyName, roleName) {
	var selector = "." + propertyName;
	if( roleName != null ) return "." + propertyName + " ." + roleName;
	else return "." + propertyName;
}

/**
 * Called to attach a dynamic reference to the element
 * 
 * @param elementName The name of the element (a style class it has)
 * @param roleName The role to attach reference to
 */
function dynamicTitleAttach(propertyName, roleName) {
	var selector = standardJquerySelector(propertyName, roleName);
	$(selector).cluetip({showTitle: false, positionBy: 'mouse', clickThrough : true});
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
	var selector = standardJquerySelector(propertyName, roleName);
	
	if( eventName == "onclick" ) $(selector).click(handerName);
	// TODO: Others
}

/** 
 * Called to attach a dynamic basic input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_input(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic validating input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_text(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic memo input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_memo(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic number input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_number(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic currency input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_currency(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic number spinner input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_numberspinner(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic date input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_date(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic time input widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_time(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic select widget to a type of field
 * 
 * <p>Attributes: ...</p>
 * <ul>
 * <li>showId - If true then the ID is also shown (and searched on)
 * </ul>
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_select(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	$(selector).each(function(item) {
		var sId = $(this).attr("id");
		var sName = $(this).attr("name");

		// Options for the autocomplete
		var data = null;
		var mustMatch = false;
		var extraParams = null;
		var showId = (attributes && attributes.showId) || false;
		
		// Treat as select
		if( $(this).attr("tagName").toLowerCase() == "select" ) {
			var sVal = $(this).val();
			var sText = $("#" + sId + " option:selected").text();
			
			data = jquery_findSelectData($(this));
			mustMatch = true;
			
			// Replace with 2 text boxes, hidden val, visible input
			$(this).after("<button type='button' id='" + sId + "_click' class='ac_dropdown'></button>");
			$(this).after("<input type='text' id='" + sId + "_visible' name='" + sName + "_visible' value='" + sText + "' class='ac_value " + $(this).attr("class") + "'/>");
			$(this).replaceWith("<input type='hidden' id='" + sId + "' name='" + sName + "' value='" + sVal + "'/>");
		}
	
		// Treat as input showing val (with a hidden _visible)
		else {
			data = attributes.searchUrl;
			extraParams = attributes.extraParams || { type: attributes.type };  
			mustMatch = true;
			
			$(this).hide();
			$("#" + sId + "_visible").each(function (i) {
				$(this).after("<button type='button' id='" + sId + "_click' class='ac_dropdown'></button>");
				$(this).replaceWith("<input type='text' id='" + sId + "_visible' name='" + sName + "_visible' value='" + $(this).attr("value") + "' class='ac_value " + $(this).attr("class") + "'/>");
			});
		}
		
		// Click event for the dropdown link
		$("#" + sId + "_click").click( function() {
			$("#" + sId + "_visible").each( function(i) {
				$(this).focus();
				$(this).trigger('click');
			});
		});
		
		// Set autocomplete
		var isUrl = typeof data == "string";
		$("#" + sId + "_visible").each(function(i) {
			$(this).autocomplete(data, { 
				minChars: (attributes && attributes.minChars) || (isUrl ? 1 : 0),
				mustMatch: mustMatch, 
				matchContains: true,
				delay: (attributes && attributes.searchDelay) || 400,
				extraParams: extraParams,
				formatItem: function(data, i, max) {
					if( showId ) return data.label + " (" + data.id + ")";
					return data.label;
				},
				formatMatch: function(data, i, max) {
					if( showId ) return data.label + " " + data.id;
					else return data.label;
				},
				formatResult: function(data) {
					return data.label;
				},
				dataType: 'json', /* Only used if its a url request */
				parse: function(data) {
					if( attributes && attributes.searchHandler ) return attributes.searchHandler(data);
					else {
						var rows = new Array();
						data = data.results;
						for(var i=0; i<data.length; i++){
							rows[i] = { data:data[i], value:data[i].label, result:data[i].label };
						}
						return rows;
					}
				}
			});
			
			// Event handler to update real field when selected
			$(this).result(function(event, data, formatted) {
				if( data != null ) {
					$("#" + sId).val(data.id);
				}
				else {
					$("#" + sId).val("");
				}
			});
		});
	});
}

/**
 * Helepr function to populate the autocomplete data based
 * on existing select option statements
 */
function jquery_findSelectData(elem) {
	var data = [];
	elem.children("option").each( function(i) {
		var v = $(this).attr("value"); 
		if( v != null && v != "" ) {
			data[i] = { id : v, label : $(this).text() };
		}
	});
	return data;
}

/** 
 * Called to attach a dynamic combobox widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_combo(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	$(selector).each( function(item) {
		var sId = $(this).attr("id");
		$(this).after("<button type='button' id='" + sId + "_click' class='ac_dropsearch'></button>");
		$(this).addClass("ac_value");
		
		var showId = attributes.showId || false;
		
		// Click event for the dropdown link
		$("#" + sId + "_click").click( function() {
			$("#" + sId).each( function(i) {
				$(this).focus();
				$(this).trigger('click');
			});
		});
		
		$(this).autocomplete(attributes.searchUrl, {
			minChars: attributes.minChars || 3, 
			noAutoShowOnChange: true, /* Ensures search is only when drop-down for down key pressed */
			matchContains: true,
			delay: attributes.searchDelay || 400,
			extraParams: attributes.extraParams || { type: attributes.type },
			formatItem: function(data, i, max) {
				if( showId ) return data.label + " (" + data.id + ")";
				return data.label;
			},
			formatMatch: function(data, i, max) {
				if( showId ) return data.label + " " + data.id;
				else return data.label;
			},
			formatResult: function(data) {
				return data.label;
			},
			dataType: 'json', /* Only used if its a url request */
			parse: function(data) {
				if( attributes.searchHandler ) return attributes.searchHandler(data);
				else {
					var rows = new Array();
					data = data.results;
					for(var i=0; i<data.length; i++){
						rows[i] = { data:data[i], value:data[i].label, result:data[i].label };
					}
					return rows;
				}
			}
		});
	});
}

/** 
 * Called to attach a dynamic button widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_button(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}

/** 
 * Called to attach a dynamic checkbox widget to a type of field
 * 
 * @param wrapperType A class that the wrapper for this field will have
 * @param roleName Again a class that indicates the role of the actual field in wrapper
 * @param attributes The additional dynamic attributes to apply (if any)
 */
function dynamicFieldAttach_checkbox(wrapperType, roleName, attributes) {
	var selector = standardJquerySelector(wrapperType, roleName);
	
	// TODO: Add effects
}
