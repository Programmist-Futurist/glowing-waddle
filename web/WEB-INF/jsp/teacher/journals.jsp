<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Journals"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<body>
<div>
    <form action="/controller" method="post">

        <h6 class="display-4" align="center"><fmt:message key="journals_jsp.h6.page_name"/></h6>

        <a style="color: red; align: center">${requestScope.errorMessage}</a>
        <a style="color: limegreen; align: center">${requestScope.infoMessage}</a>

        <div align="center">
            <form action="/controller">
                <fmt:message key="journals_jsp.form_text.student"/>

                <select name="${requestPath.studentId}" style="margin: 3px">
                    <c:forEach var="student" items="${requestScope.students}">
                        <option value="${student.get('id')}">${student.get('fullName')}</option>
                    </c:forEach>
                </select>

                -------> <fmt:message key="journals_jsp.form_text.course"/>

                <select name="${requestPath.courseId}">
                    <c:forEach var="course" items="${requestScope.teacherCourses}">
                        <option value="${course.get('id')}">${course.get('name')}</option>
                    </c:forEach>
                </select>

                <input type="hidden" name="${requestPath.command}" value="teacherJournalsCommand"/>
                <input type="hidden" name="${requestPath.action}" value="enroll"/>
                <input type="submit" class="btn btn-primary"
                       value="<fmt:message key="journals_jsp.btn.enroll"/>"/>
            </form>
        </div>

        <div style="text-align: left; display: inline">
            <form action="/controller">
                <p><fmt:message key="journals_jsp.text.total_student_amount"/> ${requestScope.totalStudentAmount}</p>
                <select name="${requestPath.courseShowId}">
                    <c:forEach var="course" items="${requestScope.teacherCourses}">
                        <option value="${course.get('id')}">${course.get('name')}</option>
                    </c:forEach>
                </select>
                <input type="hidden" name="${requestPath.command}" value="teacherJournalsCommand"/>
                <input type="submit" value="<fmt:message key="journals_jsp.btn.show"/>" class="btn-success"/>
            </form>
        </div>
    </form>
</div>

<h6 align="center">${requestScope.currentJournal}</h6>


<c:if test="${requestScope.journal != null}">
    <div class="bs-example">
        <h6 align="center">${requestScope.course.get('name')}</h6>
        <table class="table table-bordered">
            <thead>
            <tr>
                <td><b><fmt:message key="journals_jsp.head_journal_text.name"/></b></td>
                <td><b><fmt:message key="journals_jsp.head_journal_text.mark"/></b></td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="entity" items="${requestScope.journal}">
                <tr>
                    <form action="/controller">
                        <td>${entity.get('studentName')}</td>
                        <td>
                            <input type="hidden" name="${requestPath.journalId}"
                                   value="${entity.get('id')}"/>
                            <input type="hidden" name="${requestPath.studentId}"
                                   value="${entity.get('studentId')}"/>
                            <input type="text" name="${requestPath.studentMark}"
                                   placeholder="${entity.get('mark')}"
                                   style="border: 8px white"/>
                        </td>
                        <td>
                            <input type="hidden" name="${requestPath.command}" value="teacherJournalsCommand"/>
                            <button type="submit" name="${requestPath.action}" value="Edit">
                                <fmt:message key="journals_jsp.journal_btn.edit"/></button>
                        </td>
                        <td>
                            <input type="hidden" name="${requestPath.command}" value="teacherJournalsCommand"/>
                            <button type="submit" name="${requestPath.action}" value="Delete"
                                    onclick="return confirmBox()" class="btn-warning">
                                <fmt:message key="journals_jsp.journal_btn.delete"/></button>
                        </td>
                    </form>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<br/><br/><br/>

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
