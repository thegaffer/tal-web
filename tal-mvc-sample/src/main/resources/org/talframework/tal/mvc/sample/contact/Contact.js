/*
 * contact.js
 * 
 * JS for the contact window
 * Assumes JQuery is used
 */

$(document).ready( function() {
	// Update callers if first name changes
	$("#con-form-firstName").blur(function() {
		var lookup = $("#clrs-form-firstName").first();
		if( lookup.val() != $(this).val() ) {
			lookup.val($(this).val());
			$("#clrs-form").submit();
		}
	});
	
	// Update callers if last name changes
	$("#con-form-lastName").blur(function() {
		var lookup = $("#clrs-form-lastName").first();
		if( lookup.val() != $(this).val() ) {
			lookup.val($(this).val());
			$("#clrs-form").submit();
		}
	});
});