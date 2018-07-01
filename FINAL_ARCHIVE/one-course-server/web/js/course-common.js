function initNavAndHead() {
    var title = document.getElementById("course-title");
    var name = document.getElementById("course-name");
    var code = document.getElementById("course-code");
    var uname = document.getElementById("email");
    title.innerText = getTitle();
    name.innerText = window.localjson.content[0].cname;
    code.innerText = window.coursecode;
    // uname.innerText = getcookie("username");
}

function getTitle() {
    var school = window.localjson.content[0].school;
    var year = window.localjson.content[0].year;
    var semester = window.localjson.content[0].semester;
    if (semester === "SPR") semester = "春季学期";
    else if (semester === "ATU") semester = "秋季学期";
    else semester = "其他学期";
    return school + " - " + year + " " + semester;
}

function getPagePrefix() {
    return "/15336036_v9";
}

