package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.Course;
import onecourse.models.CoursePost;
import onecourse.models.CoursePostDiscussion;
import onecourse.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoursePostDiscussionDAO extends BaseDAO<CoursePostDiscussion>{
    /* Model:
    private int id;
    private int postId;  // 所属的Post
    private int authorId;  // 作者
    private String content;   // 内容
    */

    private CoursePostDAO coursePostController = new CoursePostDAO();

    public BaseStatus publish(Course validCourse, User validUser, int postId, String content) {
        // 需要检查postId是否存在，以及是否属于该课程
        try {
            ArrayList<CoursePost> coursePostNews = coursePostController.findByCourseId(validCourse.getId());
            if (coursePostNews == null)    return Status.POST_NOT_EXIST;
            if (!contains(coursePostNews, postId)) return Status.POST_NOT_BELONG_TO_COURSE;
            CoursePostDiscussion cpd = new CoursePostDiscussion(postId, validUser.getUniqueValue(), content);
            int id = add(cpd);
            cpd = new CoursePostDiscussion(cpd, id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("discussionId", id);
            jsonObject.put("postId", postId);
            jsonObject.put("authorName", cpd.getAuthorName());
            jsonObject.put("content", cpd.getContent());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }


    public BaseStatus getAllCoursePostDiscussion(int validPostId) {
        try {
            ArrayList<CoursePostDiscussion> cpds = findByPostId(validPostId);
            if (cpds == null)    return Status.NO_CONTENT;
            JSONArray jsonArray = new JSONArray();
            for (CoursePostDiscussion cpd : cpds) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("discussionId", cpd.getId());
                jsonObject.put("postId", cpd.getId());
                jsonObject.put("authorName", cpd.getAuthorName());
                jsonObject.put("content", cpd.getContent());
                jsonArray.put(jsonObject);
            }
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    public ArrayList<CoursePostDiscussion> findByPostId(int validPostId) throws Exception{
        ArrayList<CoursePostDiscussion> cpds = select("postId", validPostId);
        if (cpds.size() > 0) return cpds;
        return null;
    }

    private boolean contains(ArrayList<CoursePost> posts, int postId) {
        for (CoursePost cp : posts) {
            if (cp.getId() == postId)   return true;
        }
        return false;
    }
}
