package onecourse.dao;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import onecourse.customenmu.*;
import onecourse.models.Course;
import onecourse.models.CourseManage;
import onecourse.models.CourseStudent;
import onecourse.models.User;
import onecourse.utils.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class CourseDAO extends BaseDAO<Course> {
    /* Model:
        private int id;
        private String code;
        private String name;
        private String school;
        private String year;
        private String semester;
        private String intro;
     */

    private CourseManageDAO courseManageController = new CourseManageDAO();
    private CourseStudentDAO courseStudentController = new CourseStudentDAO();

    public CourseDAO() {
    }

    /**
     * 用于修改课程信息时检查用户是否具有权限
     *
     * @param code      课程代码
     * @param validUser 通过UserDAO.checkAuth得到的合法User对象
     * @param authType  目标权限类型
     * @return 如果用户权限符合，返回OK；否则返回AUTH_REJECT
     */
    public BaseStatus checkAuth(User validUser, String code, CourseAuth authType) {
        try {
            Course validCourse = findCourseByCode(code);
            if (validCourse == null) return Status.COURSE_NO_EXIST;
            switch (authType) {
                case MANAGE:
                    if (!validUser.getRole().equals(UserRole.INSTRUCTOR.toString()))
                        return Status.MANAGEMENT_REJECT;
                    if (!courseManageController.contains(validCourse.getId(), validUser.getId()
                    ).equals(Status.OK))
                        return Status.MANAGEMENT_REJECT;
                    break;
                case PARTICIPATE:
                    if (!courseStudentController.contains(validCourse.getId(), validUser.getId()).equals(Status.OK))
                        return Status.PARTICIPATE_REJECT;
                    break;
                case ANY:
                    if (!courseManageController.contains(validCourse.getId(), validUser.getId()).equals(Status.OK)
                            && !courseStudentController.contains(validCourse.getId(), validUser.getId()).equals(Status.OK))
                        return Status.ANY_REJECT;

            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("courseObj", validCourse);
            return Status.okWithContent(JSONHelper.getJSONArray(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    /**
     * 创建课程接口
     *
     * @param validUser 具有ID的用户对象，意味着创建之前需要使用USer.checkAuth来获得一个合法的User对象
     */
    public BaseStatus create(String name, String school, String year, String semester, User validUser) {
        BaseStatus codeStatus = createCode(name, school, year, semester);
        if (!codeStatus.equals(Status.OK_WITH_CONTENT))
            return codeStatus;
        if (!validUser.getRole().equals(UserRole.INSTRUCTOR.toString()))
            return Status.AUTH_LOW;
        JSONObject statusContent = JSONHelper.getSingleJSONObject(codeStatus.getJsonObject());
        Course course = new Course(name, school, year, semester, statusContent.getString("code"));
        try {
            int id = add(course);  // 添加课程
            course = new Course(course, id);
            courseManageController.add(new CourseManage(course.getId(), validUser.getId()));  // 添加课程管理
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("courseId", course.getId());
            jsonObject.put("courseCode", course.getCode());
            return Status.okWithContent(JSONHelper.getJSONArray(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    public BaseStatus join(Course validCourse, User validUser) {
        if (!validUser.getRole().equals(UserRole.STUDENT.toString())) return Status.JOIN_AS_INS;
        // 不是该课程的学生的用户才能加入该课程

        CourseStudent courseStudent = new CourseStudent(validCourse.getId(), validUser.getId());
        try {
            courseStudentController.add(courseStudent);
            return Status.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    public BaseStatus getAllCourseByUserId(int validUserId, UserRole role) {
        try {
            ArrayList<Course> courses;
            ArrayList<Integer> courseId = new ArrayList<>();
            JSONArray jsonArray = new JSONArray();
            switch (role) {
                case INSTRUCTOR:
                    ArrayList<CourseManage> cms = courseManageController.select("adminId", validUserId);
                    if (cms.size() == 0) return Status.NO_CONTENT;

                    for (CourseManage cm : cms) {
                        courseId.add(cm.getCourseId());
                    }
                    break;
                case STUDENT:
                    ArrayList<CourseStudent> css = courseStudentController.select("stuId", validUserId);
                    if (css.size() == 0) return Status.NO_CONTENT;
                    for (CourseStudent cs : css) {
                        courseId.add(cs.getCourseId());
                    }
                    break;
            }

            courses = selectMulti("id", courseId);
            for (Course c : courses) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("courseId", c.getId());
                jsonObject.put("courseCode", c.getCode());
                jsonObject.put("name", c.getName());
                jsonObject.put("school", c.getSchool());
                jsonObject.put("semester", c.getSemester());
                jsonObject.put("year", c.getYear());
                jsonArray.put(jsonObject);
            }
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }



    /**
     * 修改课程信息
     *
     * @param validUser  通过UserDAO.checkAuth得到的合法用户
     * @param code       课程代码
     * @param fieldName  要修改的信息
     * @param fieldValue 要修改的值
     * @return Status
     */
    public BaseStatus setInfo(User validUser, String code, String fieldName, String fieldValue) {
        BaseStatus authStatus = checkAuth(validUser, code, CourseAuth.MANAGE);
        if (authStatus.equals(Status.DATABASE_ERROR))
            return Status.DATABASE_ERROR;
        if (authStatus.equals(Status.MANAGEMENT_REJECT))
            return authStatus;
        Course validCourse = (Course) authStatus.getJsonObject().get("courseObj");
        switch (fieldName) {
            case "name":
                validCourse.setName(fieldValue);
                break;
            case "school":
                validCourse.setSchool(fieldValue);
                break;
            case "year":
                validCourse.setYear(fieldValue);
                break;
            case "semester":
                validCourse.setIntro(fieldValue);
                break;
            case "intro":
                break;
        }
        try {
            update(validCourse, "id", validCourse.getId());
            return Status.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    /**
     * @param code 课程代码，唯一
     * @return 与代码匹配的Course对象
     * @throws Exception 数据库异常
     */
    public Course findCourseByCode(String code) throws Exception {
        ArrayList<Course> courses = select("code", code);
        if (courses.size() > 0) return courses.get(0);
        return null;
    }

    /**
     * 验证给定的课程信息是否valid
     *
     * @return 如果所有字段都valid，则返回OK_WITH_CONTENT(name, pure_name, school, pure_school, semester, year)
     * 否则返回INVALID_SOME_FIELD
     */
    private BaseStatus isValid(String name, String school, String year, String semester) {
        // check name
        int MAX_COURSE_FIELD_LENGTH = 64;
        if (name.length() > MAX_COURSE_FIELD_LENGTH) return Status.INVALID_NAME;
        // 去掉所有标点、空格、数字
        String pure_name = name.replaceAll("[\\pP\\p{Punct}]", "")
                .replaceAll(" +", "")
                .replaceAll("//d+", "");
        if (name.equals("")) return Status.INVALID_NAME;

        // check school
        if (school.length() > MAX_COURSE_FIELD_LENGTH) return Status.INVALID_SCHOOL;
        // 去掉所有标点、空格、数字
        String pure_school = school.replaceAll("[\\pP\\p{Punct}]", "")
                .replaceAll(" +", "")
                .replaceAll("//d+", "");
        if (school.equals("")) return Status.INVALID_SCHOOL;

        // check year
        int num_year;
        try {
            num_year = Integer.valueOf(year);
        } catch (NumberFormatException e) {
            return Status.INVALID_YEAR;
        }
        if (num_year < 0) return Status.INVALID_YEAR;

        // check semester
        // Options: SPR for Spring, ATU for Autumn, OTH for Other
        if (!(semester.equals(CourseSemester.SPR.toString())
                || semester.equals(CourseSemester.ATU.toString())
                || semester.equals(CourseSemester.OTH.toString())))
            return Status.INVALID_SEMESTER;

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("name", name);
        jsonObject.putOpt("pure_name", pure_name);
        jsonObject.putOpt("school", school);
        jsonObject.putOpt("pure_school", pure_school);
        jsonObject.putOpt("year", year);
        jsonObject.putOpt("semester", semester);

        return Status.okWithContent(JSONHelper.getJSONArray(jsonObject));
    }

    /**
     * 为课程生成课程代码，将name和school转成英文字母后（中文转拼音）分别取首字母
     * +学期缩写（SPR/ATU/OTH）+创建年月（%YYMM）+两个随机字母
     *
     * @return 课程代码
     */
    private BaseStatus createCode(String name, String school, String year, String semester) {
        BaseStatus status = isValid(name, school, year, semester);
        if (!status.equals(Status.OK_WITH_CONTENT))
            return status;
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        StringBuilder code = new StringBuilder();
        String pure_name = ((String) JSONHelper.getSingleJSONObject(status.getJsonObject()).get("pure_name")).toUpperCase();
        String pure_school = ((String) JSONHelper.getSingleJSONObject(status.getJsonObject()).get("pure_school")).toUpperCase();
        String first = null, second = null;
        try {
            String[] firstArr = PinyinHelper.toHanyuPinyinStringArray(pure_name.charAt(0), format);
            String[] secondArr = PinyinHelper.toHanyuPinyinStringArray(pure_school.charAt(0), format);
            if (firstArr != null) first = firstArr[0];
            if (secondArr != null) second = secondArr[0];
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            return Status.INVALID_PARAM;
        }
        if (first == null) first = pure_name.substring(0, 1);
        if (second == null) second = pure_school.substring(0, 1);

        Random random = new Random();
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        code.append(first.charAt(0)).append(second.charAt(0)).append("-").append(semester)
                .append(new SimpleDateFormat("YY-MM").format(new Date()))
                .append(ALPHABET.charAt(random.nextInt(ALPHABET.length())))
                .append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        return Status.okWithContent(JSONHelper.getJSONArray("code", code.toString()));
    }
}
