package onecourse.models;

/**
 * 课程资源
 */
public class CourseResource {
    private int id;
    private int courseId;  // 课程ID
    private String resName;  // 要在网页上展示的资源名称
    private String fileName;  // 存储在服务器上的文件名称
    private String note;  // 备注

    public CourseResource() {
    }

    public CourseResource(int courseId, String resName, String fileName, String note) {
        this.courseId = courseId;
        this.resName = resName;
        this.fileName = fileName;
        this.note = note;
    }

    public CourseResource(CourseResource cr, int id) {
        this.courseId = cr.courseId;
        this.resName = cr.resName;
        this.fileName = cr.fileName;
        this.note = cr.note;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getResName() {
        return resName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getNote() {
        return note;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
