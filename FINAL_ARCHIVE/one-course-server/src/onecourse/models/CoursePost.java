package onecourse.models;

public class CoursePost {
    private int id;
    private int courseId;
    private String authorName;
    private String userRole;
    private String title;
    private String content;
    private String updateTime;


    public CoursePost() {
    }

    public CoursePost(int courseId, String authorName, String userRole, String title, String content, String updateTime) {
        this.courseId = courseId;
        this.authorName = authorName;
        this.userRole = userRole;
        this.title = title;
        this.content = content;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
