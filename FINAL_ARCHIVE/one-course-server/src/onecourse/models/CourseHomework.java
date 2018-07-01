package onecourse.models;

/**
 * 课程的作业
 */
public class CourseHomework {
    private int id;
    private int courseId;
    private String title;  // 作业名
    private String detail;  // 详细内容
    private String deadline; // 截止日期，格式为%YYYY-MM-DD

    public CourseHomework() {
    }

    public CourseHomework(int courseId, String title, String detail, String deadline) {
        this.courseId = courseId;
        this.title = title;
        this.detail = detail;
        this.deadline = deadline;
    }

    public CourseHomework(CourseHomework ch, int id) {
        this.courseId = ch.courseId;
        this.title = ch.title;
        this.detail = ch.detail;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
