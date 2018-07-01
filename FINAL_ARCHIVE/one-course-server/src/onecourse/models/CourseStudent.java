package onecourse.models;

/**
 * 参与课程的学生
 */
public class CourseStudent {
    private int id;
    private int courseId;  // 课程ID
    private int stuId;  // 学生用户ID

    public CourseStudent() {
    }

    public CourseStudent(int courseId, int stuId) {
        this.courseId = courseId;
        this.stuId = stuId;
    }

    public CourseStudent(CourseStudent courseStudent, int id) {
        this.courseId = courseStudent.courseId;
        this.stuId = courseStudent.stuId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getStuId() {
        return stuId;
    }

}
