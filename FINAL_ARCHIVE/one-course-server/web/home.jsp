<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.customenmu.UserRole" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<%
    /**
     * 用户主页API，可获取用户所有课程信息
     * @accept-method GET
     * @auth-required 需要登录
     * @return OK_WITH_CONTENT([ ( courseId, courseCode, name, year, semester)]) | NO_CONTENT（用户没有加入任何课程）
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");
    if (request.getMethod().equalsIgnoreCase("get")) {
        CourseDAO courseController = new CourseDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }

        User validUser = (User) session.getAttribute("userObj");
        BaseStatus getAllCourseStatus = courseController.getAllCourseByUserId(validUser.getId(), UserRole.valueOf(validUser.getRole()));

        out.print(getAllCourseStatus.toJsonString());
        return;


    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>