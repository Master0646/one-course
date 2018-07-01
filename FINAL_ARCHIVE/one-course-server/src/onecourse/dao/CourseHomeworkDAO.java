package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.Course;
import onecourse.models.CourseHomework;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseHomeworkDAO extends BaseDAO<CourseHomework>{
    /* Model:
    private int id;
    private int courseId;
    private String title;  // 作业名
    private String detail;  // 详细内容
    private String deadline; // 截止日期，格式为%yyyy-MM-dd
    */
    public BaseStatus publish(Course validCourse, String title, String detail, String deadline) {
        BaseStatus validStatus = isValid(deadline);
        if (!validStatus.equals(Status.OK)) return validStatus;
        CourseHomework courseHomework = new CourseHomework(validCourse.getId(), title, detail, deadline);
        try {
            int id = add(courseHomework);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("homeworkId", id);
            jsonObject.put("courseId", courseHomework.getCourseId());
            jsonObject.put("title", courseHomework.getTitle());
            jsonObject.put("detail", courseHomework.getDetail());
            jsonObject.put("deadline", courseHomework.getDeadline());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }


    public BaseStatus getAllCourseHomework(int validCourseId) {
        try {
            ArrayList<CourseHomework> courseHomework = findByCourseId(validCourseId);
            if (courseHomework == null)    return Status.NO_CONTENT;
            JSONArray jsonArray = new JSONArray();
            for (CourseHomework ch : courseHomework) {
                JSONObject post = new JSONObject();
                post.put("homeworkId", ch.getId());
                post.put("courseId", ch.getCourseId());
                post.put("title", ch.getTitle());
                post.put("detail", ch.getDetail());
                post.put("deadline", ch.getDeadline());
                jsonArray.put(post);
            }
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    ArrayList<CourseHomework> findByCourseId(int validCourseId) throws Exception {
        ArrayList<CourseHomework> chs = select("courseId", validCourseId);
        if (chs.size() > 0) return chs;
        return null;
    }

    private BaseStatus isValid(String deadline) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date validDate = timeFormat.parse(deadline, new ParsePosition(0));
        if (validDate == null)  return Status.INVALID_DEADLINE_FORMAT;
        Date now = new Date();
        if (now.after(validDate))   return Status.INVALID_DEADLINE_DATE;
        return Status.OK;
    }

}
