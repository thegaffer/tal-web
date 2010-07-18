<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.tpspencer.org/mvctags" prefix="mvc"%>

<jsp:include page="common/header.jsp">
	<jsp:param name="pageTitle" value="View Caller" />
</jsp:include>

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

<mvc:window window="viewCallerWindow" header="true" style="silent-window" />

<jsp:include page="common/footer.jsp" />