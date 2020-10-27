<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Profile"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<body>
<div>
    <form action="/controller" method="post">

        <h6 class="display-4" style="text-align: center; color: blue"><fmt:message
                key="teacher_jsp.h6.page_main_head"/></h6>

        <div class="content" style="border: #0f0f0f 1px;
    text-align: center;
	margin-right: 10px;
	padding: 2px; ">
            <table>
                <tr align="center">
                    <c:if test="${not empty sessionScope.user}">
                        <c:set var="teacherNames" value="${sessionScope.user}"/>
                    <ul class="bs-example border">
                        <%@ include file="/WEB-INF/jspf/common/show_teacher.jspf" %>
                    </ul>
                    </c:if>
                <tr>
                    <form style="float: left">
                        <c:choose>
                            <c:when test="${sessionScope.user.get('class').equals(teacher.getClass().simpleName)}">
                                <input type="hidden" name="command" value="editTeacher"/>
                            </c:when>
                            <c:when test="${sessionScope.user.get('class').equals(student.getClass().simpleName)}">
                                <input type="hidden" name="command" value="editStudent"/>
                            </c:when>
                        </c:choose>
                        <input type="submit" value="<fmt:message key="teacher_jsp.btn.edit"/>" class="btn-primary"
                               style="width:100px;height:40px"/>
                    </form>
                </tr>
            </table>
        </div>
    </form>
</div>
<br/><br/><br/>

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
