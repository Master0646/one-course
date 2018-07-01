package onecourse.utils;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class FileHelper {
    private static final String UPLOAD_PATH = "/WEB-INF/upload";
    private static final String HOMEWORK_PATH = UPLOAD_PATH + "/homework";
    private static final String RESOURCE_PATH = UPLOAD_PATH + "/resource";

    @SuppressWarnings("unchecked")
    public static BaseStatus parseRequest(HttpServletRequest request) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            //按照传统方式获取数据
            return Status.NO_FILE_IN_REQUEST;
        }

        try {
            JSONObject fields = new JSONObject();
            //创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            List<FileItem> fileItemList = upload.parseRequest(request);
            FileItem fileItem = null;
            for (FileItem item : fileItemList) {
                if (!item.isFormField()) {
                    fileItem = item;
                } else {
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                    fields.put(name, value);
                }
            }
            if (fileItem == null) return Status.NO_FILE_IN_REQUEST;
            fields.put("fileItem", fileItem);
            return Status.okWithContent(JSONHelper.getJSONArray(fields));
        } catch (FileUploadException e) {
            e.printStackTrace();
            return Status.FILE_UPLOAD_FAILURE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Status.UNSUPPORTED_ENCODING;
        }
    }


    public static BaseStatus save(FileItem file, String savePath, String filename) {
        try {
            //获取item中的上传文件的输入流
            InputStream in = file.getInputStream();
            //创建一个文件输出流
            FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
            //创建一个缓冲区
            byte buffer[] = new byte[1024];
            //判断输入流中的数据是否已经读完的标识
            int len = 0;
            //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while ((len = in.read(buffer)) > 0) {
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                out.write(buffer, 0, len);
            }
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();
            //删除处理文件上传时生成的临时文件
            file.delete();
            return Status.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return Status.FILE_UPLOAD_FAILURE;
        }
    }

    public static String getHomeworkSavePath(ServletContext application, String validCourseCode, int hwId) {
        String savePath = application.getRealPath(HOMEWORK_PATH + "/" + validCourseCode + "/" + hwId);
        File dir = new File(savePath);
        boolean result = false;
        boolean createdBefore = true;
        if (!dir.exists() && !dir.isDirectory()) {
            result = dir.mkdirs();
            createdBefore = false;
        }
        if (createdBefore) return savePath;
        if (result) return savePath;
        return null;
    }

    public static String getResourceSavePath(ServletContext application, String validCourseCode) {
        String savePath = application.getRealPath(RESOURCE_PATH + "/" + validCourseCode);
        File dir = new File(savePath);
        boolean result = false;
        boolean createdBefore = true;
        if (!dir.exists() && !dir.isDirectory()) {
            result = dir.mkdirs();
            createdBefore = false;
        }
        if (createdBefore) return savePath;
        if (result) return savePath;
        return null;
    }

    /**
     * 上传作业只需要一个函数
     */
    public static BaseStatus enjoyUploadHomework(FileItem fileItem,
                                                 ServletContext application,
                                                 String validCourseCode,
                                                 User validUser,
                                                 int hwId) {
        String filename = fileItem.getName();
        if (filename == null || filename.trim().equals("")) {
            return Status.INVALID_FILE_NAME;
        }
        String pure_filename = filename.substring(filename.lastIndexOf("\\") + 1);
        String extension = pure_filename.substring(pure_filename.lastIndexOf("."));
        pure_filename = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + String.format("_uid_%06d_", validUser.getId()) + extension;
        String savePath = getHomeworkSavePath(application, validCourseCode, hwId);
        if (savePath == null) return Status.FILE_UPLOAD_FAILURE;
        BaseStatus saveStatus = FileHelper.save(fileItem, savePath, pure_filename);
        if (!saveStatus.equals(Status.OK)) return saveStatus;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filename", pure_filename);
        jsonObject.put("filepath", savePath + "\\" + pure_filename);
        return Status.okWithContent(JSONHelper.getJSONArray(jsonObject));
    }

    /**
     * 上传资源只需要一个函数
     */
    public static BaseStatus enjoyUploadResource(FileItem fileItem,
                                                 ServletContext application,
                                                 String validCourseCode,
                                                 User validUser) {
        String filename = fileItem.getName();
        if (filename == null || filename.trim().equals("")) {
            return Status.INVALID_FILE_NAME;
        }
        String pure_filename = filename.substring(filename.lastIndexOf("\\") + 1);
        String extension = pure_filename.substring(pure_filename.lastIndexOf("."));
        pure_filename = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + String.format("_uid_%06d_", validUser.getId()) + extension;
        String savePath = getResourceSavePath(application, validCourseCode);
        if (savePath == null) return Status.FILE_UPLOAD_FAILURE;
        BaseStatus saveStatus = FileHelper.save(fileItem, savePath, pure_filename);
        if (!saveStatus.equals(Status.OK)) return saveStatus;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filename", pure_filename);
        jsonObject.put("filepath", savePath + "\\" + pure_filename);
        return Status.okWithContent(JSONHelper.getJSONArray(jsonObject));
    }
}
