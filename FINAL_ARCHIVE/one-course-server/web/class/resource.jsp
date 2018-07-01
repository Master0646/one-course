<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.dao.CourseResourceDAO" %>
<%@ page import="onecourse.models.Course" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%

    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");

    if (request.getMethod().equalsIgnoreCase("post")) {
        CourseDAO courseController = new CourseDAO();
        CourseResourceDAO crController = new CourseResourceDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }

        User validUser = (User) session.getAttribute("userObj");
        String courseCode = request.getParameter("courseCode");

        if (!ParamValidHelper.isNotEmpty(courseCode)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        BaseStatus authStatus = courseController.checkAuth(validUser, courseCode, CourseAuth.ANY);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            out.print(authStatus.toJsonString());
            return;
        }

        Course validCourse = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");

        BaseStatus getAllStatus = crController.getAllByCourseId(validCourse.getId());
        out.print(getAllStatus.toJsonString());
        return;
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
