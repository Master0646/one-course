var submit = document.getElementById("submit");
submit.onclick = function () {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var url = getPagePrefix() + "/login.jsp";
    var method = "POST";
    var xmlHttpRequest = new XMLHttpRequest();

    xmlHttpRequest.open(method, url, true);
    xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttpRequest.send("email=" + email + "&pass=" + password);

    xmlHttpRequest.onreadystatechange = function () {
        if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200) {
            var returnData = JSON.parse(xmlHttpRequest.responseText);
            console.log(returnData.msg);
            if (returnData.code === 200 || returnData.code === 201) {
                // alert("登录成功！");
                // document.cookie = "email=" + email;
                // document.cookie = "role=" + returnData.content[0].userRole;
                if (returnData.content[0].userRole === "INSTRUCTOR") {
                    window.location.href = getPagePrefix() + "/user/instructor/home.html";
                }
                else {
                    window.location.href = getPagePrefix() + "/user/student/home.html";
                }
            } else if (returnData.code === -10103){
                alert("用户名或密码错误！");
            }
            else
            {
                alert("状态码："+returnData.code + "\n服务器开小差啦，请稍后再试");
            }
        }

    }
};

function getPagePrefix() {
    return "/15336036_v9";
}