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
 * mvc.js
 * This file contains various Javascript for the Web MVC platform.
 * The Javascript in here is based on Dojo and provides facilities 
 * to:
 * 
 * - Refresh a MVC Window
 * - Invokve an action and refresh all affected windows (or the page)
 */

/**
 * Refreshes a Window in its current view. The div holding
 * the window should have a refresh window attribute on it
 * otherwise this will not work - an alert is created, but
 * the window is unchanged.
 * 
 * param id The ID of the div holding window. 
 * @return The raw AJAX response is returned
 */
function refreshMVCWindow(id) {
	dojo.xhrGet({
		url : dojo.attr(id, "refreshWindow"),
		load : function(response, ioArgs) {
			dojo.byId(id).innerHTML = response;
			return response;
		},
		error : function(response, ioArgs) {
			alert("Failed to refresh window: " + response);
			return response;
		}
	});
}

/**
 * Call to perform an action asynchronously. All affected
 * windows are refreshed and if a full page url is returned
 * the browser re-directs
 * 
 * <p>See also the post action based on a form submit</p>
 * 
 * TODO: Update windows and redirect
 * 
 * @param actionUrl The url to perform
 * @param params The parameters
 * @return The raw AJAX response is returned
 */
function performGetAction(actionUrl, params) {
	dojo.xhrGet({
		url : actionUrl,
		handleAs : "json",
		content: params,
		load : function(response, ioArgs) {
			if( response != null && response["windows"] != null ) {
				for( var i = 0 ; i < response["windows"].length ; i++ ) {
					log("Refreshing window: " + response["windows"][i]);
					refreshMVCWindow(response["windows"][i]);
				}
			}

			// TODO: Redirect

			return response;
		},
		error : function(response, ioArgs) {
			log("Failed to call async action: " + response);
			alert("Failed to call async action: " + response);
			return response;
		}
	});
}


function log(msg) {
	if( window.console != null ) {
		console.log(msg);
	}
}