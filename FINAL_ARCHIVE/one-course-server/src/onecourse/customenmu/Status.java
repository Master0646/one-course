package onecourse.customenmu;

import org.json.JSONArray;

public class Status {

    // Common Status
    public static final BaseStatus OK = new BaseStatus(200, "Ok");
    public static final BaseStatus OK_WITH_CONTENT = new BaseStatus(201, "Ok");
    public static final BaseStatus NO_CONTENT = new BaseStatus(202, "No content");

    public static final BaseStatus METHOD_NOT_ALLOW = new BaseStatus(405, "Method not allowed");

    public static final BaseStatus DATABASE_ERROR = new BaseStatus(-99999, "Database error");
    public static final BaseStatus AUTH_REJECT = new BaseStatus(-80001, "Authorization reject");
    public static final BaseStatus AUTH_LOW = new BaseStatus(-80002, "Authorization is too low to do the operation");
    public static final BaseStatus MISSING_PARAM = new BaseStatus(-80003, "Require more fields");
    public static final BaseStatus INVALID_PARAM = new BaseStatus(-80004, "Invalid value of some fields");
    public static final BaseStatus INVALID_REQUEST_TYPE = new BaseStatus(-80005, "Invalid type of request");
    public static final BaseStatus AUTH_REQUIRED = new BaseStatus(-80006, "You need to log in first");

    // UserDAO Status
    public static final BaseStatus USER_EXIST = new BaseStatus(-10101, "User email exist");
    public static final BaseStatus USER_NO_EXIST = new BaseStatus(-10102, "User email does not exist");
    public static final BaseStatus WRONG_USER_OR_PASS = new BaseStatus(-10103, "Wrong user email or password");
    public static final BaseStatus INVALID_SIGN_UP_VALUE = new BaseStatus(-10104, "Invalid value of some parameter for signing up");
    public static final BaseStatus INVALID_EMAIL = new BaseStatus(-10105, "Invalid value of email, not a legal email format");
    public static final BaseStatus INVALID_PASSWORD = new BaseStatus(-10106, "Invalid value of password, it may be too short or contain space");
    public static final BaseStatus INVALID_ROLE = new BaseStatus(-10107, "Invalid value of role, it equals neither INSTRUCTOR nor STUDENT");

    // CourseDAO Status
    public static final BaseStatus SAME_COURSE_EXIST = new BaseStatus(-10201, "Same course exists");
    public static final BaseStatus COURSE_NO_EXIST = new BaseStatus(-10202, "Can not find the course by provided info");
    public static final BaseStatus INVALID_NAME = new BaseStatus(-10203, "Invalid value of name");
    public static final BaseStatus INVALID_SCHOOL = new BaseStatus(-10204, "Invalid value of school");
    public static final BaseStatus INVALID_YEAR = new BaseStatus(-10205, "Invalid value of year");
    public static final BaseStatus INVALID_SEMESTER = new BaseStatus(-10206, "Invalid value of semester");


    // CourseManagementDAO Status
    public static final BaseStatus MANAGEMENT_REJECT = new BaseStatus(-10301, "User is not one of the administrator of the course");
    public static final BaseStatus PARTICIPATE_REJECT = new BaseStatus(-10302, "User is not one of the student of the course");
    public static final BaseStatus ANY_REJECT = new BaseStatus(-10303, "User is not the member of the course");

    // CourseStudentDAO Status
    public static final BaseStatus JOIN_AS_INS = new BaseStatus(-10401, "Can not join a course as a instructor");
    public static final BaseStatus DUPLICATE_JOIN = new BaseStatus(-10402, "Can not join a course after you have joined it");


    // CoursePost Status
    public static final BaseStatus POST_NOT_EXIST = new BaseStatus(-10501, "Post does not exist by provided post id");
    public static final BaseStatus POST_NOT_BELONG_TO_COURSE = new BaseStatus(-10502, "Post does not belong to the course");


    // CourseHomework Status
    public static final BaseStatus INVALID_DEADLINE_FORMAT = new BaseStatus(-10601, "Invalid format of deadline");
    public static final BaseStatus INVALID_DEADLINE_DATE = new BaseStatus(-10602, "Invalid date of deadline, it's earlier than the current date");
    public static final BaseStatus NO_HOMEWORK_IN_COURSE = new BaseStatus(-10603, "The course has no homework, cheer up!");
    public static final BaseStatus HOMEWORK_NOT_BELONG_TO_COURSE = new BaseStatus(-10604, "Homework does not belong to the course");
    // File Status
    public static final BaseStatus FILE_UPLOAD_FAILURE = new BaseStatus(-10701, "File upload failure");
    public static final BaseStatus NO_FILE_IN_REQUEST = new BaseStatus(-10702, "No file in post of request");
    public static final BaseStatus INVALID_FILE_NAME = new BaseStatus(-10703, "Invalid filename");
    public static final BaseStatus NO_SUCH_FILE = new BaseStatus(-10704, "No such a file");
    public static final BaseStatus UNSUPPORTED_ENCODING = new BaseStatus(-10705, "Unsupported form encoding");



    public static BaseStatus okWithContent(JSONArray content) {
        return new BaseStatus(Status.OK_WITH_CONTENT, content);
    }

}
