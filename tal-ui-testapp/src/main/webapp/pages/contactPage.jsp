<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.tpspencer.org/mvctags" prefix="mvc"%>

<%--
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
--%>

<jsp:include page="common/header.jsp"><jsp:param name="pageTitle" value="Contact Page" /></jsp:include>

<h1>Contact Page</h1>

<!-- LHS -->
<div style="display: inline-block; width: 25%; vertical-align: top; overflow: hidden">
	<mvc:window window="menuWindow" header="true" style="silent-window" />
	<mvc:window window="callerLookupWindow" header="true" outerStyle="tool" initialAsync="true" />
	<mvc:window window="accountLookupWindow" header="true" outerStyle="tool" initialAsync="true" />
</div>

<!-- RHS -->
<div style="display: inline-block; width: 74%; vertical-align: top">
	<mvc:window window="contactWindow" header="true" />
</div>

<jsp:include page="common/footer.jsp" />