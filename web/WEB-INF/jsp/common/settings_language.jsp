<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>
<c:set var="title" value="Language settings" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<body>
<form action="controller" method="post" style="text-align: center">
    <fmt:message key="settings_jsp.label.set_locale"/>:
    <select name="locale">
        <c:forEach items="${applicationScope.locales}" var="locale">
            <c:set var="selected" value="${locale.key == currentLocale ? 'selected' : '' }"/>
            <option value="${locale.key}" ${selected}>${locale.value}</option>
        </c:forEach>
    </select>
    <input type="hidden" name="command" value="setLanguage">
    <input type="submit" value="<fmt:message key='settings_jsp.form.submit_save_locale'/>" class="btn-primary">

</form>
</body>
</html>
