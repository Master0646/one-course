<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.dao.CourseHomeworkDAO" %>
<%@ page import="onecourse.models.Course" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%
    /**
     * 课程作业 API
     * @param requestType 请求类型；
     *                    可接受的值："getall"     获取某课程的所有homework
     *                               "publish"       在某课程中发布一个homework
     *
     * @param courseCode 课程代码
     * @if:param-requestType=publish requestType == publish时需要的参数
     *      @conditional-param title homework的标题；必要
     *      @conditional-param detail homework的内容；必要，但可为空串
     *      @conditional-param deadline homework的截止日期；必要，只接受"yyyy-MM-dd"格式，而且必须至少是当前日期的后一天
     *                                                           应当在前端使用下拉框方式控制输入
     *
     * @authRequired 需要登录且登陆者是该课程的老师
     * @return INVALID_REQUEST_TYPE | ANY_REJECT | AUTH_REQUIRED | NO_CONTENT | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR |
     *         INVALID_DEADLINE_FORMAT | INVALID_DEADLINE_DATE |
     *         NO_FILE_IN_REQUEST | FILE_UPLOAD_FAILURE | INVALID_FILE_NAME
     *         OK_WITH_CONTENT([(homeworkId, courseId, title, detail, deadline)])  - requestType == getall；多个homework的信息
     *         OK_WITH_CONTENT(homeworkId, courseId, title, detail, deadline) - requestType == publish；刚刚发布的homework的信息
     *         OK - requestType == upload
     */
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");

    if (request.getMethod().equalsIgnoreCase("post")) {
        CourseDAO courseController = new CourseDAO();
        CourseHomeworkDAO courseHomeworkController = new CourseHomeworkDAO();

        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }

        User validUser = (User) session.getAttribute("userObj");
        String requestType = request.getParameter("requestType");
        String courseCode = request.getParameter("courseCode");
        BaseStatus status = null;

        if (!ParamValidHelper.isNotEmpty(requestType, courseCode)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        // 检查权限
        CourseAuth authType = CourseAuth.ANY;
        switch (requestType) {
            case "getall":
                break;
            case "publish":
                authType = CourseAuth.MANAGE;
                break;
        }
        BaseStatus authStatus = courseController.checkAuth(validUser, courseCode, authType);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            out.print(authStatus.toJsonString());
            return;
        }

        Course validCourse = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");

        switch (requestType) {
            case "getall":
                status = courseHomeworkController.getAllCourseHomework(validCourse.getId());
                break;
            case "publish":
                String title = request.getParameter("title");
                String detail = request.getParameter("detail");
                String deadline = request.getParameter("deadline");
                if (!ParamValidHelper.isNotNull(title, detail, deadline)) {
                    out.print(Status.MISSING_PARAM.toJsonString());
                    return;
                } else if (!ParamValidHelper.isNotEmpty(title, deadline)) {
                    out.print(Status.INVALID_PARAM.toJsonString());
                    return;
                }
                status = courseHomeworkController.publish(validCourse, title, detail, deadline);
                break;
        }

        if (status == null) status = Status.INVALID_REQUEST_TYPE;
        out.print(status.toJsonString());
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
