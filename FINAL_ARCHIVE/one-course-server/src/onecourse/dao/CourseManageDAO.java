package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.CourseManage;

import java.util.ArrayList;

public class CourseManageDAO extends BaseDAO<CourseManage> {
    /* Model:
    private int id;
    private int courseId;  // 课程ID
    private int adminId;   // 管理员ID，应当为老师/TA
    */


    public BaseStatus contains(int validCourseId, int validUserId) throws Exception{
        ArrayList<CourseManage> courseManages = findByCourseId(validCourseId);
        if (courseManages == null)  return Status.MANAGEMENT_REJECT;
        for (CourseManage cm : courseManages) {
            if (cm.getAdminId() == validUserId)
                return Status.OK;
        }
        return Status.MANAGEMENT_REJECT;
    }

    private ArrayList<CourseManage> findByCourseId(int courseId) throws Exception{
        ArrayList<CourseManage> courseManages = select("courseId", courseId);
        if (courseManages.size() == 0)   return null;
        return courseManages;
    }

    private ArrayList<CourseManage> findByUserId(int userId) throws Exception {
        ArrayList<CourseManage> courseManages = select("adminId", userId);
        if (courseManages.size() == 0)   return null;
        return courseManages;
    }
}
