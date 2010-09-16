dynamicOnLoad(function() {
	var input = {
		propertyName : "template-element",
		roleName : "role-value",
		eventName : "onkeydown",
		handlerName : "keyDown"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		propertyName : "template-element",
		roleName : "role-value",
		eventName : "onkeyup",
		handlerName : "keyUp"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		propertyName : "template-element",
		roleName : "role-value",
		eventName : "onkeypress",
		handlerName : "keyPress"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

