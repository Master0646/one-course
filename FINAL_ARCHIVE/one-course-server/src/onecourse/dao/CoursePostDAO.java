package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.Course;
import onecourse.models.CoursePost;
import onecourse.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CoursePostDAO extends BaseDAO<CoursePost>{
    /* Model:
        private int id;
        private int courseId;
        private String authorName;
        private String userRole;
        private String title;
        private String content;
        private String updateTime;
     */
    /**
     * 发布一个post
     */
    public BaseStatus post(Course validCourse, User validUser, String title, String content) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        BaseStatus validStatus = isValid(title, content);
        if (!validStatus.equals(Status.OK)) return validStatus;
        CoursePost coursePostNew = new CoursePost(validCourse.getId(), validUser.getUniqueValue(), validUser.getRole(), title, content, timeFormat.format(new Date()));
        try {
            int id = add(coursePostNew);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("postId", id);
            jsonObject.put("courseId", coursePostNew.getCourseId());
            jsonObject.put("authorName", coursePostNew.getAuthorName());
            jsonObject.put("userRole", coursePostNew.getUserRole());
            jsonObject.put("title", coursePostNew.getTitle());
            jsonObject.put("content", coursePostNew.getContent());
            jsonObject.put("updateTime", coursePostNew.getUpdateTime());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }



    public BaseStatus getAllCoursePost(int validCourseId) {
        try {
            ArrayList<CoursePost> coursePostNews = findByCourseId(validCourseId);
            if (coursePostNews == null)    return Status.NO_CONTENT;
            JSONArray jsonArray = new JSONArray();
            for (CoursePost cp : coursePostNews) {
                JSONObject post = new JSONObject();
                post.put("postId", cp.getId());
                post.put("courseId", cp.getCourseId());
                post.put("authorName", cp.getAuthorName());
                post.put("userRole", cp.getUserRole());
                post.put("title", cp.getTitle());
                post.put("content", cp.getContent());
                post.put("updateTime", cp.getUpdateTime());
                jsonArray.put(post);
            }
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    public ArrayList<CoursePost> findByCourseId(int validCourseId) throws Exception{
        ArrayList<CoursePost> coursePostNews = select("courseId", validCourseId);
        if (coursePostNews.size() > 0) return coursePostNews;
        return null;
    }

    private BaseStatus isValid(String title, String content) {
        if (title == null || content == null)   return Status.MISSING_PARAM;
        if (title.length() == 0)    return Status.INVALID_PARAM;
        return Status.OK;
    }
}
