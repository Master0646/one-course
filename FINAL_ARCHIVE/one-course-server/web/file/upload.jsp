<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.dao.CourseHomeworkUploadedFileDAO" %>
<%@ page import="onecourse.dao.CourseResourceDAO" %>
<%@ page import="onecourse.models.Course" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.FileHelper" %>
<%@ page import="onecourse.utils.JSONHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page import="java.io.File" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<%
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    if (request.getMethod().equalsIgnoreCase("post")) {
        // 登陆检测
        HttpSession session = AuthHelper.logCheck(request, application);
        if (session == null) {
            AuthHelper.killAllCookies(request, response);
            response.setContentType("text/json");
            out.print(Status.AUTH_REQUIRED.toJsonString());
            return;
        }

        User validUser = (User) session.getAttribute("userObj");
        String courseCode = request.getParameter("courseCode");
        String resType = request.getParameter("resType");
        if (!ParamValidHelper.isNotNull(courseCode, resType)) {
            response.setContentType("text/json");
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(courseCode, resType)) {
            response.setContentType("text/json");
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        // 检查权限
        CourseAuth authType = null;
        switch (resType) {
            case "resource":
                authType = CourseAuth.MANAGE;
                break;
            case "homework":
                authType = CourseAuth.PARTICIPATE;
                break;
        }
        if (authType == null) {
            response.setContentType("text/json");
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }
        CourseDAO courseController = new CourseDAO();
        BaseStatus authStatus = courseController.checkAuth(validUser, courseCode, authType);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            response.setContentType("text/json");
            out.print(authStatus.toJsonString());
            return;
        }
        BaseStatus parseStatus = null;
        BaseStatus saveStatus = null;
        BaseStatus dbStatus = null;
        Course validCourse = null;
        String validFilename = null;
        JSONObject formFields = new JSONObject();
        switch (resType) {
            case "resource":
                parseStatus = FileHelper.parseRequest(request);
                if (!parseStatus.equals(Status.OK_WITH_CONTENT)) {
                    out.print(parseStatus.toJsonString());
                    return;
                }

                formFields = JSONHelper.getSingleJSONObject(parseStatus.getJsonObject());
                String resName = formFields.getString("resName");
                String note = formFields.getString("note");

                if (!ParamValidHelper.isNotNull(resName, note)) {
                    out.print(Status.MISSING_PARAM.toJsonString());
                    return;
                } else if (!ParamValidHelper.isNotEmpty(resName)) {
                    out.print(Status.INVALID_PARAM.toJsonString());
                    return;
                }
                saveStatus = FileHelper.enjoyUploadResource((FileItem) formFields.get("fileItem"), application, courseCode, validUser);
                if (!saveStatus.equals(Status.OK_WITH_CONTENT)) {
                    out.print(saveStatus.toJsonString());
                    return;
                }
                validCourse = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");
                validFilename = JSONHelper.getSingleJSONObject(saveStatus.getJsonObject()).getString("filename");
                CourseResourceDAO crController = new CourseResourceDAO();
                dbStatus = crController.upload(validCourse, validFilename, resName, note);
                break;
            case "homework":
                parseStatus = FileHelper.parseRequest(request);
                if (!parseStatus.equals(Status.OK_WITH_CONTENT)) {
                    out.print(parseStatus.toJsonString());
                    return;
                }

                String hwIdStr = request.getParameter("hwId");
                if (!ParamValidHelper.isNotNull(hwIdStr)) {
                    out.print(Status.MISSING_PARAM.toJsonString());
                    return;
                } else if (!ParamValidHelper.isNotEmpty(hwIdStr)
                        || !ParamValidHelper.isPositiveIntNumberic(hwIdStr)) {
                    out.print(Status.INVALID_PARAM.toJsonString());
                    return;
                }
                int hwId = Integer.valueOf(hwIdStr);
                formFields = JSONHelper.getSingleJSONObject(parseStatus.getJsonObject());
                saveStatus = FileHelper.enjoyUploadHomework((FileItem) formFields.get("fileItem"), application, courseCode, validUser, hwId);
                if (!saveStatus.equals(Status.OK_WITH_CONTENT)) {
                    out.print(saveStatus.toJsonString());
                    return;
                }
                validCourse = (Course) JSONHelper.getSingleJSONObject(authStatus.getJsonObject()).get("courseObj");
                validFilename = JSONHelper.getSingleJSONObject(saveStatus.getJsonObject()).getString("filename");
                CourseHomeworkUploadedFileDAO chfController = new CourseHomeworkUploadedFileDAO();
                dbStatus = chfController.upload(validCourse, validUser, hwId, validFilename);
                break;
                default:
                    break;
        }

        if (parseStatus == null) {
            out.print(Status.INVALID_REQUEST_TYPE.toJsonString());
            return;
        }
        if (!dbStatus.equals(Status.OK)) {
            String filepath = JSONHelper.getSingleJSONObject(saveStatus.getJsonObject()).getString("filepath");
            File file = new File(filepath);
            if (file.exists())
                file.delete();
        }
        out.print(dbStatus.toJsonString());
        return;
    } else {
        response.setContentType("text/json");
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }
%>