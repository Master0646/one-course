<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.UserDAO" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page import="org.json.JSONObject" %>
<%@ page session="false" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>

<%
    /**
     * 用户登陆API
     * @param email
     * @param pass
     * @return OK_WITH_CONTENT(userId) | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR | WRONG_USER_OR_PASS
     */


    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");


    if (request.getMethod().equalsIgnoreCase("post") || request.getMethod().equalsIgnoreCase("get")) {
        UserDAO userController = new UserDAO();

        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        if (!ParamValidHelper.isNotEmpty(email, password)) {
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotNull(email, password)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session != null) {
            User validUser = (User) session.getAttribute("userObj");
            if (email.equals(validUser.getEmail()) && password.equals(validUser.getPassword())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", validUser.getId());
                jsonObject.put("username", validUser.getUniqueValue());
                jsonObject.put("userRole", validUser.getRole());
                out.print(Status.okWithContent(JSONHelper.getJSONArray(jsonObject)).toJsonString());  // OK with userObj as the content
                return;
            }
        }

        BaseStatus status = userController.signIn(email, password);
        if (!status.equals(Status.OK_WITH_CONTENT)) {
            out.print(status.toJsonString());
            return;
        }
        User validUser = (User) JSONHelper.getSingleJSONObject(status.getJsonObject()).get("userObj");

        // 保存登陆状态
        session = request.getSession();
        session.setAttribute("userObj", validUser);
        Cookie sessionCookie = new Cookie("session", session.getId());  // 默认时间为当前session的持续
        Cookie roleCookie = new Cookie("role", validUser.getRole());
        Cookie emailCookie = new Cookie("email", validUser.getEmail());
        response.addCookie(sessionCookie);
        response.addCookie(roleCookie);
        response.addCookie(emailCookie);

        application.setAttribute(session.getId(), session);  // 将session保存在服务器上

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", validUser.getId());
        jsonObject.put("username", validUser.getUniqueValue());
        jsonObject.put("userRole", validUser.getRole());
        out.print(Status.okWithContent(JSONHelper.getJSONArray(jsonObject)).toJsonString());  // OK with userObj as the content
        return;
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
