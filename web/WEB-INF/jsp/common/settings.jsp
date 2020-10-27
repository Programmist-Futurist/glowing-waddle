<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Settings" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>


<body>
<table id="main-container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div class="input-group-text">
        <form action="/controller" method="post" style="margin-left: 20%">
            <c:choose>
                <c:when test="${sessionScope.user.get('class').equals(teacher.getClass().simpleName)}">
                    <%@ include file="/WEB-INF/jspf/settings/settings_teacher.jspf" %>
                    <input type="hidden" name="${requestPath.command}" value="editTeacher"/>
                </c:when>

                <c:when test="${sessionScope.user.get('class').equals(student.getClass().simpleName)}">
                    <%@ include file="/WEB-INF/jspf/settings/settings_student.jspf" %>
                    <input type="hidden" name="${requestPath.command}" value="editStudent"/>
                </c:when>
            </c:choose>
        </form>
        <div>
            <a style="color: red">${requestScope.errorMessage}</a>
            <a style="color: limegreen">${requestScope.infoMessage}</a>
        </div>
    </div>
</table>
<br/><br/><br/>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>