<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%
    /**
     * 创建课程API
     * @param name 课程名；
     * @param school 开课院系；
     * @param year 开课年份；可接受的格式 yyyy
     * @param semester 开课学期；可接受的值："SPR" | "ATU" | "OTH" ，分别代表春季学期、秋季学期、其他学期
     * @authRequired 需要登录且登陆者身份为教师
     * @return OK_WITH_CONTENT(courseId, courseCode, userId) | AUTH_REQUIRED | AUTH_LOW | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "http://" + request.getRemoteAddr() + ":" + request.getRemotePort());
    response.setHeader("Access-Control-Allow-Credentials", "true");
    if (request.getMethod().equalsIgnoreCase("get")) {
        response.sendRedirect("create-class.html");
        return;
    }
    if (request.getMethod().equalsIgnoreCase("post")) {
        CourseDAO courseController = new CourseDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }
        User validUser = (User) session.getAttribute("userObj");

        String name = request.getParameter("name");
        String school = request.getParameter("school");
        String year = request.getParameter("year");
        String semester = request.getParameter("semester");

        if (!ParamValidHelper.isNotNull(name, school, year, semester)) {
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(name, school, year, semester)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        BaseStatus createStatus = courseController.create(name, school, year, semester, validUser);
        out.print(createStatus.toJsonString());
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>