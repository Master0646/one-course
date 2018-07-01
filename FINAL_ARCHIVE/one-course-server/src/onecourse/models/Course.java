package onecourse.models;

/**
 * 课程
 */
public class Course {
    private int id;
    private String code;
    private String name;
    private String school;
    private String year;
    private String semester;
    private String intro;

    public Course() {
    }

    public Course(String name, String school, String year, String semester, String code) {
        this.name = name;
        this.school = school;
        this.year = year;
        this.semester = semester;
        this.code = code;
    }

    public Course(Course c, int id) {
        this.name = c.name;
        this.school = c.school;
        this.year = c.year;
        this.semester = c.semester;
        this.code = c.code;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
