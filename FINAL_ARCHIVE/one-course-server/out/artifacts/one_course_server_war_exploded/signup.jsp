<%@ page language="java" contentType="text/json;charset=UTF-8" %>
<%@ page session="false" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.dao.UserDAO" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.UserRole" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page import="org.json.JSONObject" %>
<%

    /**
     * 用户注册API
     * @param email
     * @param pass
     * @param role 账号身份；可接受的值："INSTRUCTOR" | "STUDENT"
     * @return OK_WITH_CONTENT(userId) | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR | USER_EXIST
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "http://" + request.getRemoteAddr() + ":" + request.getRemotePort());
    response.setHeader("Access-Control-Allow-Credentials", "true");
    if (request.getMethod().equalsIgnoreCase("get")) {
        response.sendRedirect("login.html");
        return;
    }

    if (request.getMethod().equalsIgnoreCase("post")) {
        UserDAO userController = new UserDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session != null) {
            User validUser = (User) session.getAttribute("userObj");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", validUser.getId());
            jsonObject.put("username", validUser.getUniqueValue());
            out.print(Status.okWithContent(JSONHelper.getJSONArray(jsonObject)).toJsonString());  // OK with userObj as the content
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        String role = request.getParameter("role");
        if (!ParamValidHelper.isNotNull(email, password, role)) {
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(email, password, role)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        BaseStatus status = userController.signUp(email, password, role);
        if (!status.equals(Status.OK_WITH_CONTENT)) {
            out.print(status.toJsonString());
            return;
        }

        User validUser = (User) JSONHelper.getSingleJSONObject(status.getJsonObject()).get("userObj");
        session = request.getSession();
        session.setAttribute("userObj", validUser);
        Cookie sessionCookie = new Cookie("session", session.getId());  // 默认时间为当前session的持续
        Cookie roleCookie = new Cookie("role", validUser.getRole());
        Cookie emailCookie = new Cookie("email", validUser.getEmail());
        response.addCookie(sessionCookie);
        response.addCookie(roleCookie);
        response.addCookie(emailCookie);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", validUser.getId());
        jsonObject.put("username", validUser.getUniqueValue());
        application.setAttribute(session.getId(), session);  // 将session保存在服务器上
        out.print(Status.okWithContent(JSONHelper.getJSONArray(jsonObject)).toJsonString());  // OK with userObj as the content
        return;
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }

%>
