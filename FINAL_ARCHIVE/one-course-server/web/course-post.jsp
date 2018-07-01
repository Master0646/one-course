<%@ page import="onecourse.models.Course" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.dao.*" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page session="false" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>

<%
    /**
     * 课程POST API
     * @param requestType 请求类型；
     *                    可接受的值："getall"     获取某课程的所有post
     *                               "post"       在某课程中发布一个post
     *
     * @param courseCode 课程代码
     * @if:param-requestType=post requestType == post时需要的参数
     *      @conditional-param title post的标题；必要
     *      @conditional-param content post的内容；必要，但可为空串
     * @authRequired 需要登录且登陆者是该课程的成员
     * @return INVALID_REQUEST_TYPE | ANY_REJECT | AUTH_REQUIRED | NO_CONTENT | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR |
     *         OK_WITH_CONTENT([(courseId, authorId, authorName, userRole, title, content, updateTime)])  - requestType == getall；多个post的信息
     *         OK_WITH_CONTENT(courseId, authorId, authorName, userRole, title, content, updateTime) - requestType == post；刚刚发布的post的信息
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
//    response.setHeader("Access-Control-Allow-Origin", "*");
//    response.setHeader("Access-Control-Allow-Origin", "http://172.18.157.157:8080");
    response.setHeader("Access-Control-Allow-Origin", "http://" + request.getRemoteAddr() + ":" + request.getRemotePort());
    response.setHeader("Access-Control-Allow-Credentials", "true");
    if (request.getMethod().equalsIgnoreCase("post")) {
        CourseDAO courseController = new CourseDAO();
        CoursePostDAO coursePostController = new CoursePostDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }

        User user = (User) session.getAttribute("userObj");
        String requestType = request.getParameter("requestType");
        String courseCode = request.getParameter("courseCode");
        BaseStatus status = null;

        if (!ParamValidHelper.isNotNull(requestType, courseCode)) {
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(requestType, courseCode)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        // 检查权限
        BaseStatus authStatus = courseController.checkAuth(user, courseCode, CourseAuth.ANY);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            out.print(authStatus.toJsonString());
            return;
        }
        Course course = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");

        switch (requestType) {
            case "getall":
                status = coursePostController.getAllCoursePost(course.getId());
                break;
            case "post":
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                if (!ParamValidHelper.isNotNull(title, content)) {
                    out.print(Status.MISSING_PARAM.toJsonString());
                    return;
                } else if (!ParamValidHelper.isNotEmpty(title)) {
                    out.print(Status.INVALID_PARAM.toJsonString());
                    return;
                }
                status = coursePostController.post(course, user, title, content);
                break;
        }

        if (status == null) status = Status.INVALID_REQUEST_TYPE;
        out.print(status.toJsonString());
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
