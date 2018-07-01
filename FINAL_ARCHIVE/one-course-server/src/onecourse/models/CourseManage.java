package onecourse.models;

/**
 * 课程的管理员，老师/TA
 */
public class CourseManage {
    private int id;
    private int courseId;  // 课程ID
    private int adminId;   // 管理员ID，应当为老师/TA

    public CourseManage() {
    }

    public CourseManage(int courseId, int adminId) {
        this.courseId = courseId;
        this.adminId = adminId;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getAdminId() {
        return adminId;
    }

}
