<%@ page import="onecourse.customenmu.BaseStatus" %>
<%@ page import="onecourse.customenmu.CourseAuth" %>
<%@ page import="onecourse.customenmu.Status" %>
<%@ page import="onecourse.dao.CourseDAO" %>
<%@ page import="onecourse.models.User" %>
<%@ page import="onecourse.utils.AuthHelper" %>
<%@ page import="onecourse.utils.FileHelper" %>
<%@ page import="onecourse.utils.ParamValidHelper" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" %>
<%@ page session="false" %>
<%

    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setHeader("Access-Control-Allow-Origin", "*");

    if (request.getMethod().equalsIgnoreCase("post") || request.getMethod().equalsIgnoreCase("get")) {
        CourseDAO courseController = new CourseDAO();

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
        String filename = request.getParameter("fileName");
        if (!ParamValidHelper.isNotNull(resType, filename)) {
            response.setContentType("text/json");
            out.print(Status.MISSING_PARAM.toJsonString());
            return;
        } else if (!ParamValidHelper.isNotEmpty(resType, filename)) {
            response.setContentType("text/json");
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }

        // 检查权限
        CourseAuth authType = null;
        switch (resType) {
            case "resource":
                authType = CourseAuth.ANY;
                break;
            case "homework":
                authType = CourseAuth.MANAGE;
                break;
        }
        if (authType == null) {
            response.setContentType("text/json");
            out.print(Status.INVALID_PARAM.toJsonString());
            return;
        }
        BaseStatus authStatus = courseController.checkAuth(validUser, courseCode, authType);
        if (!authStatus.equals(Status.OK_WITH_CONTENT)) {
            response.setContentType("text/json");
            out.print(authStatus.toJsonString());
            return;
        }

        String filepath = null;
        File file;
        switch (resType) {
            case "resource":
                filepath = FileHelper.getResourceSavePath(application, courseCode) + "\\" + filename;
                break;
            case "homework":
                String hwIdStr = request.getParameter("hwId");
                if (!ParamValidHelper.isNotNull(hwIdStr)) {
                    response.setContentType("text/json");
                    out.print(Status.MISSING_PARAM.toJsonString());
                    return;
                } else if (!ParamValidHelper.isNotEmpty(hwIdStr) || !ParamValidHelper.isPositiveIntNumberic(hwIdStr)) {
                    response.setContentType("text/json");
                    out.print(Status.INVALID_PARAM.toJsonString());
                    return;
                }
                int hwId = Integer.valueOf(hwIdStr);
                filepath = FileHelper.getHomeworkSavePath(application, courseCode, hwId) + "\\" + filename;
                break;
        }
        if (filepath == null) {
            response.setContentType("text/json");
            out.print(Status.INVALID_REQUEST_TYPE.toJsonString());
            return;
        }
        file = new File(filepath);
        if (!file.exists()) {
            response.setContentType("text/json");
            out.print(Status.NO_SUCH_FILE.toJsonString());
            return;
        }
        //设置响应头，控制浏览器下载该文件
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(filepath);
        //创建输出流
        OutputStream outStream = response.getOutputStream();
        //创建缓冲区
        byte buffer[] = new byte[1024];
        int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0) {
            //输出缓冲区的内容到浏览器，实现文件下载
            outStream.write(buffer, 0, len);
        }
        //关闭文件输入流
        in.close();
        //关闭输出流
        outStream.close();
    } else {
        response.setContentType("text/json");
        out.print(Status.METHOD_NOT_ALLOW.toJsonString());
    }

%>