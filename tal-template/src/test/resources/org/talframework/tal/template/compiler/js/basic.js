dynamicOnLoad(function() {
	var input = {
		propertyName : "SimpleBeanA-amount",
		eventName : "onblur",
		handlerName : "onAmountBlur"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		propertyName : "SimpleBeanA-amount",
		eventName : "onfocus",
		handlerName : "onAmountFocus"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		propertyName : "SimpleBeanB-postCode",
		roleName : "role-value",
		eventName : "ondblclick",
		handlerName : "onPostCodeDblClick"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanB-address1",
		roleName : "role-field"
	};
	dynamicFieldAttach_input(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanB-postCode",
		roleName : "role-field"
	};
	dynamicFieldAttach_input(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanB-country",
		roleName : "role-field"
	};
	dynamicFieldAttach_select(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanB-address2",
		roleName : "role-field",
		attributes : {
			required : "false" }
	};
	dynamicFieldAttach_text(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		propertyName : "results-resultTable",
		eventName : "onfocus",
		handlerName : "onResultsFocus"
	};
	dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanA-amount",
		roleName : "role-field"
	};
	dynamicFieldAttach_input(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanA-currency",
		roleName : "role-field",
		attributes : {
			onChange : "onCurrencyChange" }
	};
	dynamicFieldAttach_input(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanA-date",
		roleName : "role-field",
		attributes : {
			formatLength : "short" }
	};
	dynamicFieldAttach_date(input.wrapperType, input.roleName, input.attributes);
});

dynamicOnLoad(function() {
	var input = {
		wrapperType : "SimpleBeanA-active",
		roleName : "role-field"
	};
	dynamicFieldAttach_checkbox(input.wrapperType, input.roleName, input.attributes);
});

