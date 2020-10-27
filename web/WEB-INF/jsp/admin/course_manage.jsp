<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>


<html>

<c:set var="title" value="Course manage"/>
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
        <%@ include file="/WEB-INF/jspf/admin/courses/create_course.jspf" %>
    </c:when>
    <c:when test="${requestScope.pageNum == 2}">
        <%@ include file="/WEB-INF/jspf/admin/courses/update_course.jspf" %>
    </c:when>
    <c:when test="${requestScope.pageNum == 3}">
        <%@ include file="/WEB-INF/jspf/admin/courses/delete_course.jspf" %>
    </c:when>
</c:choose>


<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
