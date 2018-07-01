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
<%@ page import="onecourse.dao.UserDAO" %>
<%@ page import="onecourse.customenmu.UserRole" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%

    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");

    if (request.getMethod().equalsIgnoreCase("post")) {
        CourseDAO courseController = new CourseDAO();
        UserDAO userController = new UserDAO();
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

        BaseStatus submited = chuf.getAllByHwId(validCourse.getId(), hwId);
        if (!submited.equals(Status.OK_WITH_CONTENT) && !submited.equals(Status.NO_CONTENT)) {
            out.print(submited.toJsonString());
            return;
        }
        BaseStatus allstu = userController.getAllUserByCourseId(validCourse.getId(), UserRole.STUDENT);
        if (!allstu.equals(Status.OK_WITH_CONTENT)) {
            out.print(allstu.toJsonString());
            return;
        }
        JSONArray submitedArr = submited.equals(Status.NO_CONTENT) ? new JSONArray() : JSONHelper.getStatusContent(submited);
        JSONArray allstuArr = JSONHelper.getStatusContent(allstu);
        JSONArray finalCheck = new JSONArray();
        for (int i = 0; i < allstuArr.length(); i++) {
            JSONObject submitStatus = new JSONObject();
            String email = allstuArr.getJSONObject(i).getString("userName");
            submitStatus.put("email", email);
            boolean isSubmitted = false;
            String filename = null;
            for (int j = 0; j < submitedArr.length(); j++) {
                if (submitedArr.getJSONObject(j).getString("stuName").equals(email)) {
                    isSubmitted = true;
                    filename = submitedArr.getJSONObject(j).getString("fileName");
                    break;
                }
            }
            if (isSubmitted) {
                submitStatus.put("status", 1);
                submitStatus.put("filename", filename);
            }
            else {
                submitStatus.put("status", 0);
            }
            finalCheck.put(submitStatus);
        }
        out.print(Status.okWithContent(finalCheck).toJsonString());
        return;
    } else {
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>
