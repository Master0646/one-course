package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.Course;
import onecourse.models.CourseResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseResourceDAO extends BaseDAO<CourseResource>{
    /* Model:
    private int id;
    private int courseId;  // 课程ID
    private String resName;  // 要在网页上展示的资源名称
    private String fileName;  // 存储在服务器上的文件名称
    private String note;  // 备注
    */

    public BaseStatus upload(Course validCourse, String validFilename, String resName, String note) {
        try {
            CourseResource chf = new CourseResource(validCourse.getId(), resName, validFilename, note);
            int id = add(chf);
            return Status.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    public BaseStatus getAllByCourseId(int validCourseId) {
        try {
            ArrayList<CourseResource> crs = select("courseId", validCourseId);
            if (crs.size() == 0)    return Status.NO_CONTENT;
            JSONArray content = new JSONArray();
            for (CourseResource cr : crs) {
                JSONObject jsonObject =  new JSONObject();
                jsonObject.put("resName", cr.getResName());
                jsonObject.put("fileName", cr.getFileName());
                jsonObject.put("note", cr.getNote());
                content.put(jsonObject);
            }
            return Status.okWithContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

}

