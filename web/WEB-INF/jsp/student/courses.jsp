<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="My Courses"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<style>
    .bs-example {
        margin: 20px;
    }

    ul.hr {
        margin: 5% 20%;
        border: 1px solid #000; /* Рамка вокруг текста */
        padding: 3px; /* Поля вокруг текста */
    }

    ul.hr li {
        display: block; /* Отображать как вертикальный элемент */
    }

</style>

<body>

<div style="text-align: center;">

    <div align="center">
        <a style="color: red">${requestScope.errorMessage}</a>
        <a style="color: limegreen">${requestScope.infoMessage}</a>
        <ul class="hr">
            <li><h4><fmt:message key="courses_jsp.filter.filter_name"/></h4></li>
            <br/>

            <li>
                <form action="/controller">
                    <input type="hidden" name="${requestPath.command}" value="filterCourses"/>
                    <input type="hidden" name="${requestPath.action}" value="All"/>
                    <button type="submit" name="button" value="Show All courses"
                            class="btn btn-success"><fmt:message
                            key="courses_jsp.filter.btn.show_all_courses"/></button>
                    <br/>
                </form>

                <form action="/controller" method="post">
                    <input type="hidden" name="${requestPath.command}" value="filterCourses"/>

                    <a>Filter by status</a>
                    <select name="status" style="margin: 3px">
                        <c:forEach var="status" items="${requestScope.statuses}">
                            <option value="${status.get('id')}">${status.get('name')}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name="${requestPath.action}" value="status"/>
                    <button type="submit" name="button" value="Apply"
                            class="btn btn-outline-secondary"><fmt:message
                            key="courses_jsp.filter.btn.apply"/></button>
                    <br/>
                </form>

                <form action="/controller" method="post">
                    <input type="hidden" name="${requestPath.command}" value="filterCourses"/>

                    <a>Filter by topic</a>
                    <select name="topic" style="margin: 3px">
                        <c:forEach var="topic" items="${requestScope.topics}">
                            <option value="${topic.get('id')}">${topic.get('name')}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name="${requestPath.action}" value="topic"/>
                    <button type="submit" name="button" value="Apply"
                            class="btn btn-outline-secondary"><fmt:message
                            key="courses_jsp.filter.btn.apply"/></button>
                    <br/>
                </form>

                <form action="/controller" method="post">
                    <input type="hidden" name="${requestPath.command}" value="filterCourses"/>

                    <a>Show courses of particular Teacher: </a>
                    <select name="teacherId" style="margin: 3px">
                        <c:forEach var="teacher" items="${requestScope.teachers}">
                            <option value="${teacher.get('id')}">${teacher.get('fullName')}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name="${requestPath.action}" value="teacherSort"/>
                    <button type="submit" name="button" value="Apply"
                            class="btn btn-outline-secondary"><fmt:message
                            key="courses_jsp.filter.btn.apply"/></button>
                </form>

                <form action="/controller" method="post">
                    <input type="hidden" name="${requestPath.command}" value="filterCourses"/>

                    <a>Sort: </a>
                    <select name="sort" style="margin: 3px">
                        <option value="a-z"><fmt:message key="courses_jsp.filter.sort.a-z"/></option>
                        <option value="z-a"><fmt:message key="courses_jsp.filter.sort.z-a"/></option>
                        <option value="duration"><fmt:message key="courses_jsp.filter.sort.duration"/></option>
                        <option value="amount"><fmt:message key="courses_jsp.filter.sort.student_amount"/></option>
                    </select>
                    <input type="hidden" name="${requestPath.action}" value="sort"/>
                    <button type="submit" name="button" value="Sort"
                            class="btn btn-outline-secondary"><fmt:message
                            key="courses_jsp.filter.btn.sort"/></button>
                    <br/>
                </form>
            </li>
            <br/>
        </ul>
    </div>

    <h6>${requestScope.filterApply}</h6>

    <c:if test="${not empty requestScope.studentCourses}">
        <c:forEach var="courseNames" items="${requestScope.studentCourses}">
            <%@ include file="/WEB-INF/jspf/common/show_course.jspf" %>
        </c:forEach>
    </c:if>

    <c:if test="${empty requestScope.studentCourses}">
        <h4><fmt:message key="courses_jsp.message.no_courses_found"/></h4>
    </c:if>
</div>
<br/><br/><br/>
<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
