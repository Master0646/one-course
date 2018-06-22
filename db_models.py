# One-course项目数据库模型，Python语言描述，使用Django框架模式

from django.db import models

ROLE_CHOICES = ['Student', 'Instructor']
SCHOOL_YEAR_CHOICES = [str(x) for x in range(2013, 2019)]
GRADUATE_YEAR_CHOICES = [str(x) for x in range(2013, 2030)]
PROGRAM_CHOICES = ['Undergraduate', 'Masters', 'PhD']
SEMESTER_CHOICES = ['Spring', 'Autumn', 'Other Semester']


class BaseUser(models.Model):
    email = models.CharField(max_length=255)
    password = models.CharField(max_length=255)
    role = models.CharField(max_length=16, choices=ROLE_CHOICES)
    school = models.CharField(max_length=64)  # 学院
    ps = models.TextField(max_length=1000, blank=True, null=True)  # 个人简介

    class Meta:
        db_table = 'user'


### User Models ###

class StudentUser(models.Model):
    baseuser = models.ForeignKey(BaseUser, on_delete=models.CASCADE)
    nickname = models.CharField(max_length=64)  # 昵称
    major = models.CharField(max_length=64)  # 专业
    program = models.CharField(max_length=16, choices=PROGRAM_CHOICES)  # 学位
    school_year = models.CharField(max_length=16, choices=SCHOOL_YEAR_CHOICES)  # 入学年份
    graduate_year = models.CharField(max_length=16, choices=GRADUATE_YEAR_CHOICES)  # 毕业年份

    class Meta:
        db_table = 'student_user'


class InstructorUser(models.Model):
    baseuser = models.ForeignKey(BaseUser, on_delete=models.CASCADE)
    fullname = models.CharField(max_length=64)  # 姓名

    class Meta:
        db_table = 'instructor_user'


### Course Models ###

class Course(models.Model):
    school = models.CharField(max_length=64)  # 开课学院
    name = models.CharField(max_length=64)  # 课程名
    year = models.CharField(max_length=16, choices=GRADUATE_YEAR_CHOICES)
    semester = models.CharField(max_length=16, choices=SEMESTER_CHOICES)
    created_by = models.ForeignKey(InstructorUser, on_delete=models.CASCADE)  # 创建者
    introduction = models.TextField

    class Meta:
        db_table = 'course'


# 记录课程管理员信息的表，每条记录是一门课的ID+一位管理员的ID
class CourseManagement(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    administrator = models.ForeignKey(InstructorUser, on_delete=models.CASCADE)

    class Meta:
        db_table = 'course_management'


# 参与课程的学生
class CourseStudent(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    student = models.ForeignKey(StudentUser, on_delete=models.CASCADE)

    class Meta:
        db_table = 'course_student'


# 课程Post所属的文件夹
class CoursePostFolder(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    name = models.CharField(max_length=16)

    class Meta:
        db_table = 'course_post_folder'


# 课程的Post
class CoursePost(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    title = models.CharField(max_length=64)
    content = models.TextField(max_length=5000)
    publisher_role = models.CharField(max_length=16, choices=ROLE_CHOICES)  # 发布者身份
    update_time = models.DateTimeField(auto_now=True)  # 最后一次修改的时间
    folder = models.ForeignKey(CoursePostFolder, on_delete=models.CASCADE)  # 所属文件夹

    class Meta:
        db_table = 'course_post'


# Post下的Discussion
class CoursePostDiscussion(models.Model):
    post = models.ForeignKey(CoursePost, on_delete=models.CASCADE)
    publisher = models.ForeignKey(BaseUser, on_delete=models.CASCADE)
    content = models.TextField(max_length=2000)

    class Meta:
        db_table = 'course_post_discussion'


# 课程资源
class CourseResource(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    res_name = models.CharField(max_length=64)  # 资源名称
    file_name = models.CharField(max_length=255)  # 资源文件名称
    note = models.CharField(max_length=255)  # 资源备注
    
    class Meta:
        db_table = 'course_resource'