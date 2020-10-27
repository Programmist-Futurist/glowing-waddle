<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Teacher manage"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<style>
    .bs-example {
        margin: 20px;
    }

    ul.hr {
        margin: 5%;
        border: 1px solid #000; /* Рамка вокруг текста */
        padding: 10px; /* Поля вокруг текста */
    }

    ul.hr li {
        display: block; /* Отображать как вертикальный элемент */
    }
</style>

<body>
<p align="center" style="color: red">${requestScope.errorMessage}</p>
<p align="center" style="color: limegreen">${requestScope.infoMessage}</p>
<c:choose>
    <c:when test="${requestScope.pageNum == 1}">
        <%@include file="/WEB-INF/jspf/admin/teacher/create_teacher.jspf" %>
    </c:when>
    <c:when test="${requestScope.pageNum == 2}">
        <%@include file="/WEB-INF/jspf/admin/teacher/teacher_course_connection.jspf" %>
    </c:when>
</c:choose>
<br/><br/><br/>
<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
