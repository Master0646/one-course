package onecourse.models;

public class CourseHomeworkUploadedFile {
    private int id;
    private int hwId;
    private String stuName;
    private String uploadTime;
    private String fileName;

    public CourseHomeworkUploadedFile() {
    }

    public CourseHomeworkUploadedFile(int hwId, String stuName, String uploadTime, String fileName) {
        this.hwId = hwId;
        this.stuName = stuName;
        this.uploadTime = uploadTime;
        this.fileName = fileName;
    }

    public CourseHomeworkUploadedFile(CourseHomeworkUploadedFile chf, int id) {
        this.hwId = chf.hwId;
        this.stuName = chf.stuName;
        this.uploadTime = chf.uploadTime;
        this.fileName = chf.fileName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getHwId() {
        return hwId;
    }

    public String getStuName() {
        return stuName;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getFileName() {
        return fileName;
    }
}
