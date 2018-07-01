function handleStatus() {

    if (invalidStatusHandler(-80006, "您还未进行登录，点击确认后将跳转到登录界面", "/"))
        return;
    if (invalidStatusHandler(-10301, "您不是该课程的教师，点击确认后将跳转到个人主页", "/user/instructor/home.html"))
        return;
    if (invalidStatusHandler(-10303, "您不是该课程的教师，点击确认后将跳转到个人主页", "/user/instructor/home.html"))
        return;

    if (!(window.localjson.code === 200 || window.localjson.code === 201 || window.localjson.code === 202)) {
        alert("状态码：" + window.localjson.code + "\n服务器开小差了，请稍后重试！");
        window.location.href = getPagePrefix() + "/user/instructor/home.html";
    }
}

function setCourseAdminLink(coursecode) {
    var code = document.getElementById("course-code");
    var qa = document.getElementById("course-qa");
    var hw = document.getElementById("course-homework");
    // var res = document.getElementById("course-resource");
    // var manage = document.getElementById("course-manage");

    code.setAttribute("href", getPagePrefix() + "/class/instructor/?courseCode=" + coursecode);
    qa.setAttribute("href", getPagePrefix() + "/class/instructor/qa.html?courseCode=" + coursecode);
    hw.setAttribute("href", getPagePrefix() + "/class/instructor/homework.html?courseCode=" + coursecode);
    // res.setAttribute("href", "/class/instructor/resource.html?courseCode=" + coursecode);
    // manage.setAttribute("href", "/class/instructor/manage.html?courseCode=" + coursecode);
}

function getPagePrefix() {
    return "/15336036_v9";
}