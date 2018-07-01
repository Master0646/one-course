<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.dao.CoursePostDiscussionDAO" %>
<%@ page import="onecourse.models.Course" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%
    /**
     * 课程POST-DISCUSSION API
     * @param requestType 请求类型；
     *                    可接受的值："getall"        获取某post的所有discussion
     *                               "publish"       在某post下发表一个discussion
     *
     * @param courseCode 课程代码
     * @param postId post的ID；必要
     * @if:param-requestType=publish requestType == publish时需要的参数
     *       @conditional-param content discussion的内容；必要，不可为空串
     * @auth-required 需要登录且登陆者是该课程的成员
     * @return INVALID_REQUEST_TYPE | ANY_REJECT | AUTH_REQUIRED | NO_CONTENT | MISSING_PARAM | INVALID_PARAM | DATABASE_ERROR |
     *         OK_WITH_CONTENT([(discussionId, postId, authorId, authorName, content)])  - requestType == getall；多个DISCUSSION的信息
     *         OK_WITH_CONTENT(postId, authorId, content) - requestType == publish；刚刚发布的DISCUSSION的信息
     */

    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "http://" + request.getRemoteAddr() + ":" + request.getRemotePort());
    response.setHeader("Access-Control-Allow-Credentials", "true");

    if (request.getMethod().equalsIgnoreCase("post")) {
        CourseDAO courseController = new CourseDAO();
        CoursePostDiscussionDAO coursePostDiscussionController = new CoursePostDiscussionDAO();

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
        String postIdStr = request.getParameter("postId");
        BaseStatus status = null;

        if (!ParamValidHelper.isNotNull(requestType, courseCode, postIdStr)) {
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(requestType, courseCode)
                || !ParamValidHelper.isPositiveIntNumberic(postIdStr)) {
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        int postId = Integer.valueOf(postIdStr);

        // 检查权限
        BaseStatus authStatus = courseController.checkAuth(user, courseCode, CourseAuth.ANY);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            out.print(authStatus.toJsonString());
            return;
        }
        Course course = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");

        switch (requestType) {
            case "getall":
                status = coursePostDiscussionController.getAllCoursePostDiscussion(postId);
                break;
            case "publish":
                String content = request.getParameter("content");
                if (!ParamValidHelper.isNotNull(content)) {
                    out.print(Status.MISSING_PARAM.toJsonString());
                    return;
                } else if (!ParamValidHelper.isNotEmpty(content)) {
                    out.print(Status.INVALID_PARAM.toJsonString());
                    return;
                }
                status = coursePostDiscussionController.publish(course, user, postId, content);
                break;
        }

        if (status == null) status = Status.INVALID_REQUEST_TYPE;
        out.print(status.toJsonString());
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
