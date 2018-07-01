package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.Course;
import onecourse.models.CourseHomework;
import onecourse.models.CourseHomeworkUploadedFile;
import onecourse.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseHomeworkUploadedFileDAO extends BaseDAO<CourseHomeworkUploadedFile> {
    /* Model:
    private int id;
    private int hwId;
    private String stuName;
    private String uploadTime;
    private String fileName;
    */

    private CourseHomeworkDAO chController = new CourseHomeworkDAO();

    public CourseHomeworkUploadedFileDAO() {
    }

    public BaseStatus upload(Course validCourse, User validUser, int hwId, String filename) {
        try {
            ArrayList<CourseHomework> chs = chController.findByCourseId(validCourse.getId());
            if (chs == null) return Status.NO_HOMEWORK_IN_COURSE;
            if (!contains(chs, hwId)) return Status.HOMEWORK_NOT_BELONG_TO_COURSE;
            CourseHomeworkUploadedFile chf = new CourseHomeworkUploadedFile(
                    hwId,
                    validUser.getUniqueValue(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                    filename);
            this.add(chf);
            return Status.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;

    }

    public BaseStatus getAllByHwId(int validCourseId, int hwId) {
        try {
            ArrayList<CourseHomework> chs = chController.findByCourseId(validCourseId);
            if (chs == null) return Status.NO_HOMEWORK_IN_COURSE;
            if (!contains(chs, hwId)) return Status.HOMEWORK_NOT_BELONG_TO_COURSE;
            ArrayList<CourseHomeworkUploadedFile> chufs = select("hwId", hwId);
            if (chufs.size() == 0)    return Status.NO_CONTENT;
            JSONArray jsonArray = new JSONArray();
            for (CourseHomeworkUploadedFile chuf : chufs) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("stuName", chuf.getStuName());
                jsonObject.put("fileName", chuf.getFileName());
                jsonObject.put("uploadTime", chuf.getUploadTime());
                jsonArray.put(jsonObject);
            }
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;

}

    private boolean contains(ArrayList<CourseHomework> courseHomeworks, int hwId) {
        for (CourseHomework ch : courseHomeworks) {
            if (ch.getId() == hwId) return true;
        }
        return false;
    }

}
