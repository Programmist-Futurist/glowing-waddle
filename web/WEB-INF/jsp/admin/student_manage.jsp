<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Student manage"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<body>
<div align="center">
    <form>
        <a style="color: limegreen">${requestScope.infoMessage}</a><br/>
        <form action="/controller">
            <fmt:message key="student_manage_jsp.text.unblocked_students"/>
            <select name="${requestPath.studentId}" style="margin: 3px">
                <c:forEach var="student" items="${requestScope.unblockedStudents}">
                    <option value="${student.get('id')}">${student.get('fullName')}</option>
                </c:forEach>
            </select>
            <input type="hidden" name="${requestPath.command}" value="manageStudent"/>
            <input type="hidden" name="${requestPath.action}" value="Block"/>
            <input type="submit" name="button" class="btn btn-warning"
                   value="<fmt:message key="student_manage_jsp.btn.block"/>"/>
        </form>

        <form>
            <fmt:message key="student_manage_jsp.text.blocked_students"/>
            <select name="${requestPath.studentId}" style="margin: 3px">
                <c:forEach var="student" items="${requestScope.blockedStudents}">
                    <option value="${student.get('id')}">${student.get('fullName')}</option>
                </c:forEach>
            </select>
            <input type="hidden" name="${requestPath.command}" value="manageStudent"/>
            <input type="hidden" name="${requestPath.action}" value="Unblock"/>
            <input type="submit" name="button" class="btn btn-primary"
                   value="<fmt:message key="student_manage_jsp.btn.unblock"/>"/>
        </form>
    </form>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
