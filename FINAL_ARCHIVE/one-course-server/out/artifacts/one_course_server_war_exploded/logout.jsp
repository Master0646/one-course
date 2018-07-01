<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<%
    /**
     * 用户登出API
     * @accept-method GET
     * @auth-required 需要登录
     * @return OK | AUTH_REQUIRED
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "http://" + request.getRemoteAddr() + ":" + request.getRemotePort());
    response.setHeader("Access-Control-Allow-Credentials", "true");
    if (request.getMethod().equalsIgnoreCase("get")) {
        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }
        // 清除session
        application.removeAttribute(session.getId());
        if (session != null) session.invalidate();

        // 清除cookie
        AuthHelper.killAllCookies(request, response);
        out.print(Status.OK.toJsonString());
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>