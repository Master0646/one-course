<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.models.Course" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%
    /**
     * 加入课程API
     * @param courseCode 课程号；
     * @authRequired 需要登录&&登陆者身份为学生&&他没有加入该课程
     * @return OK | AUTH_REQUIRED | JOIN_AS_INS | DUPLICATE_JOIN | COURSE_NO_EXIST | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");

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

        String courseCode = request.getParameter("courseCode");

        if (!ParamValidHelper.isNotNull(courseCode)) {
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(courseCode)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }
        // 检查权限
        BaseStatus authStatus = courseController.checkAuth(validUser, courseCode, CourseAuth.PARTICIPATE);
        if (authStatus.equals(Status.DATABASE_ERROR) || authStatus.equals(Status.COURSE_NO_EXIST)) {
            out.print(authStatus.toJsonString());
            return;
        }
        if (!authStatus.equals(Status.PARTICIPATE_REJECT)) {
            out.print(Status.DUPLICATE_JOIN.toJsonString());
            return;
        }
        Course validCourse;
        try {
            validCourse = courseController.findCourseByCode(courseCode);
        } catch (Exception e) {
            e.printStackTrace();
            out.print(Status.DATABASE_ERROR.toJsonString());
            return;
        }
        if (validCourse == null) {
            out.print(Status.COURSE_NO_EXIST.toJsonString());
            return;
        }
        BaseStatus joinStatus = courseController.join(validCourse, validUser);
        out.print(joinStatus.toJsonString());

    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
