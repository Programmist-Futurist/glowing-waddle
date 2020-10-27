<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<%-- set the locale --%>
<fmt:setLocale value="${param.locale}" scope="session"/>

<%-- load the bundle (by locale) --%>
<fmt:setBundle basename="ua.nure.levchenko.SummaryTask4.resources"/>

<%-- set current locale to session --%>
<c:set var="currentLocale" value="${param.locale}" scope="session"/>

<%-- goto back to the settings--%>
<c:redirect url="controller?${requestPath.command}=languageSettings"/>

