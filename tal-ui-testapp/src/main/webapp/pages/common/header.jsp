<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!--
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
-->

<html>
<head>
	<title>${param.pageTitle}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <%--<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" > --%>
    
    <link rel="stylesheet" href="<%= request.getContextPath()%>/css/app-site.css" type="text/css"></link>
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/mvc.js"></script>
    
    <%-- No JS 
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/standard_nojs.js"></script>
    --%>
    
    <%-- DOJO   
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/dojo/dojo.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/standard_dojo.js"></script>
    --%>
    
    <%-- JQuery --%>  
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery/jquery-1.4.1.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery/jquery.bgiframe.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery/jquery.cluetip.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery/jquery.hoverIntent.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/js/standard_jquery.js"></script>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/js/jquery/jquery.css" type="text/css"></link>
</head>

<body>
