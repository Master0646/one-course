<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.dao.CourseHomeworkUploadedFileDAO" %>
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
        CourseHomeworkUploadedFileDAO chuf = new CourseHomeworkUploadedFileDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }

        // 权限检测&部分参数检测
        User validUser = (User) session.getAttribute("userObj");
        String courseCode = request.getParameter("courseCode");
        String hwIdStr = request.getParameter("hwId");
        if (!ParamValidHelper.isNotEmpty(courseCode) || !ParamValidHelper.isPositiveIntNumberic(hwIdStr)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }
        int hwId = Integer.valueOf(hwIdStr);

        BaseStatus authStatus = courseController.checkAuth(validUser, courseCode, CourseAuth.MANAGE);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            out.print(authStatus.toJsonString());
            return;
        }

        Course validCourse = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");

        BaseStatus getAllStatus = chuf.getAllByHwId(validCourse.getId(), hwId);
        out.print(getAllStatus.toJsonString());
        return;
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
