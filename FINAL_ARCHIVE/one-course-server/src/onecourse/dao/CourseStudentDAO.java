package onecourse.dao;

import onecourse.customenmu.BaseStatus;
import onecourse.customenmu.Status;
import onecourse.models.CourseStudent;

import java.util.ArrayList;

public class CourseStudentDAO extends BaseDAO<CourseStudent>{
    /* Model:
    private int id;
    private int courseId;  // 课程ID
    private int stuId;  // 学生用户ID
    */

    public BaseStatus contains(int validCourseId, int validUserId) throws Exception{
        ArrayList<CourseStudent> courseStudents = findByCourseId(validCourseId);
        if (courseStudents == null)  return Status.PARTICIPATE_REJECT;
        for (CourseStudent cs : courseStudents) {
            if (cs.getStuId() == validUserId)
                return Status.OK;
        }
        return Status.PARTICIPATE_REJECT;
    }

    private ArrayList<CourseStudent> findByCourseId(int courseId) throws Exception{
        ArrayList<CourseStudent> courseStudents = select("courseId", courseId);
        if (courseStudents.size() == 0)   return null;
        return courseStudents;
    }

    private ArrayList<CourseStudent> findByUserId(int userId) throws Exception {
        ArrayList<CourseStudent> courseStudents = select("stuId", userId);
        if (courseStudents.size() == 0)   return null;
        return courseStudents;
    }


}
