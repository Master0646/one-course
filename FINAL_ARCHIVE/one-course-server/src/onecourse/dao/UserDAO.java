package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.customenmu.UserRole;
import onecourse.models.Course;
import onecourse.models.CourseManage;
import onecourse.models.CourseStudent;
import onecourse.models.User;
import onecourse.utils.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDAO extends BaseDAO<User> {

    private CourseManageDAO courseManageController = new CourseManageDAO();
    private CourseStudentDAO courseStudentController = new CourseStudentDAO();

    public UserDAO() {
    }

    /***
     * 用户登陆接口
     * @return OK_WITH_CONTENT(userObj) | DATABASE_ERROR | WRONG_USER_OR_PASS
     */
    public BaseStatus signIn(String email, String password) {
        BaseStatus authStatus = checkAuth(email, password);
        if (authStatus.equals(Status.DATABASE_ERROR))
            return Status.DATABASE_ERROR;
        if (!authStatus.equals(Status.OK_WITH_CONTENT))
            return Status.WRONG_USER_OR_PASS;
        return authStatus;
    }

    /***
     * 用户注册接口
     * @return 状态码及信息
     */
    public BaseStatus signUp(String email, String password, String role) {
        BaseStatus authStatus = checkAuth(email, password);
        if (authStatus.equals(Status.DATABASE_ERROR))
            return Status.DATABASE_ERROR;
        if (!authStatus.equals(Status.USER_NO_EXIST))
            return Status.USER_EXIST;
        BaseStatus validStatus = isValid(email, password, role);
        if (!validStatus.equals(Status.OK))
            return validStatus;
        User user = new User(email, password, role);
        try {
            int id = add(user);
            return Status.okWithContent(JSONHelper.getJSONArray("userObj", new User(user, id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    public BaseStatus setInfo(User validUser, String fieldName, String fieldValue) {
        switch (fieldName) {
            case "email":
                validUser.setEmail(fieldValue);
                break;
            case "password":
                validUser.setPassword(fieldValue);
                break;
        }
        try {
            update(validUser, "id", validUser.getId());
            return Status.OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }


    public BaseStatus getAllUserByCourseId(int validCourseId, UserRole role) {
        try {
            ArrayList<User> users;
            ArrayList<Integer> userId = new ArrayList<>();
            JSONArray jsonArray = new JSONArray();
            switch (role) {
                case INSTRUCTOR:
                    ArrayList<CourseManage> cms = courseManageController.select("courseId", validCourseId);
                    if (cms.size() == 0) return Status.NO_CONTENT;
                    for (CourseManage cm : cms) {
                        userId.add(cm.getAdminId());
                    }
                    break;
                case STUDENT:
                    ArrayList<CourseStudent> css = courseStudentController.select("courseId", validCourseId);
                    if (css.size() == 0) return Status.NO_CONTENT;
                    for (CourseStudent cs : css) {
                        userId.add(cs.getStuId());
                    }
                    break;
            }

            users = selectMulti("id", userId);
            for (User u : users) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", u.getId());
                jsonObject.put("userName", u.getUniqueValue());
                jsonArray.put(jsonObject);
            }
            return Status.okWithContent(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }
    /**
     * 根据用户名和密码来查询数据库内的User
     *
     * @return Status.OK_WITH_CONTENT    用户名与密码匹配，content内携带userObj
     * Status.AUTH_REJECT        用户名存在但密码不匹配
     * Status.USER_NO_EXIST      用户名不存在
     * Status.DATABASE_ERROR     数据库错误
     */
    public BaseStatus checkAuth(String email, String password) {
        try {
            User user = findByEmail(email);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    return Status.okWithContent(JSONHelper.getJSONArray("userObj", user));
                } else return Status.AUTH_REJECT;
            } else {
                return Status.USER_NO_EXIST;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Status.DATABASE_ERROR;
    }

    /***
     * 通过邮箱查找用户
     * @return 如果数据库中存在该邮箱，则返回对应的User对象，否则返回null
     */
    private User findByEmail(String email) throws Exception {
        ArrayList<User> users = select("email", email);
        if (users.size() > 0) return users.get(0);
        return null;
    }

    private BaseStatus isValid(String email, String password, String role) {
        if (email.length() < 5 || !email.contains("@") || !email.contains("."))
            return Status.INVALID_EMAIL;
        if (password.length() < 6 || password.contains(" "))
            return Status.INVALID_PASSWORD;
        if (!role.equals(UserRole.INSTRUCTOR.toString()) && !role.equals(UserRole.STUDENT.toString()))
            return Status.INVALID_ROLE;
        return Status.OK;
    }
}
