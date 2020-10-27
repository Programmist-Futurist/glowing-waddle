<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Login"/>
<%@ include file="WEB-INF/jspf/head.jspf" %>

<body>

<div style="text-align: center; position: absolute;
top: 40%;
left: 50%;
margin: 0 -50% 0 0;
transform: translate(-50%, -50%)">
    <form action="/controller" method="post">
        <input type="hidden" name="${requestPath.command}" value="login"/>
        <h1><fmt:message key="login_jsp.page_name.login"/></h1>
        <table align="center">
            <tr>
                <td><input type="text" name="${requestPath.login}"
                           placeholder="<fmt:message key="login_jsp.placeholder.login"/>"></td>
            </tr>
            <tr>
                <td><input type="password" name="${requestPath.password}"
                           placeholder="<fmt:message key="login_jsp.placeholder.password"/>"></td>
            </tr>

            <form action="/controller" method="post">
                <tr>
                    <td colspan="2" align="right">
                        <input type="hidden" name="${requestPath.command}" value="login"/>
                        <input type="hidden" name="${requestPath.action}" value="login"/>
                        <input type="submit" value="<fmt:message key="login_jsp.btn.login"/>" class="btn-primary">
                    </td>
                </tr>
            </form>
            <form action="/controller" method="post">
                <tr>
                    <td><fmt:message key="login_jsp.text.do_you_have_an_account"/></td>
                    <td colspan="2" align="right">
                        <input type="hidden" name="${requestPath.command}" value="login"/>
                        <input type="hidden" name="${requestPath.action}" value="singIn"/>
                        <input type="submit" value="<fmt:message key="login_jsp.btn.sign_up"/>" class="btn-primary">
                    </td>
                </tr>
            </form>
            <tr align="center">
                <a style="color: red">${requestScope.errorMessage}</a>
            </tr>
        </table>
    </form>
</div>
<%@include file="WEB-INF/jspf/footer.jspf" %>
</body>
</html>