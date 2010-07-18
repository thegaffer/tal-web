dynamicOnLoad(function() {
	var input = {
		propertyName : "template-element",
		eventName : "onblur",
		handlerName : "blur"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		propertyName : "template-element",
		eventName : "onfocus",
		handlerName : "focus"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

