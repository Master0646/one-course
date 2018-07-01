var submit = document.getElementById("submit");
submit.onclick = function () {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var identityArr = document.getElementsByName("identity");
    var identity = null;
    for (var i = identityArr.length - 1; i >= 0; i--) {
        if (identityArr[i].checked) {
            identity = identityArr[i].value;
            break;
        }
    }
    if (identity === null) {
        alert("请选择要注册的账户类型！");
    }

    var url = getPagePrefix() + "/signup.jsp";
    var method = "POST";
    var xmlHttpRequest = new XMLHttpRequest();

    xmlHttpRequest.open(method, url, true);
    xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttpRequest.send("email=" + email + "&pass=" + password + "&role=" + identity);

    xmlHttpRequest.onreadystatechange = function () {
        if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200) {
            var returnData = JSON.parse(xmlHttpRequest.responseText);
            console.log(returnData.msg);
            if (returnData.code === 201 || returnData.code === 200) {
                alert("注册成功！");
                // document.cookie = "email=" + email;
                // document.cookie = "role=" + returnData.content[0].userRole;
                window.location.href = getPagePrefix() + "/";
            } else if (returnData.code === -10101) {
                alert("用户名已存在，换个用户名试试吧~");
            }
            else if (returnData.code === -10105) {
                alert("用户名非法，请确认输入了正确的邮箱格式，例如：example@domain.com");
            }
            else if (returnData.code === -10106) {
                alert("密码非法，请检查密码中是否含有空格，或长度是否小于6位！");
            }
            else {
                alert("状态码："+returnData.code + "\n服务器开小差啦，请稍后再试");
            }
        }

    }
};

function getPagePrefix() {
    return "/15336036_v9";
}