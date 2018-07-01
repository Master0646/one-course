function getcookie(objname) {
    var arrstr = document.cookie.split("; ");
    for (var i = 0; i < arrstr.length; i++) {
        var tmp = arrstr[i].split("=");
        if (tmp[0] === objname) return unescape(tmp[1]);
    }
}

function getCurrentUrlParam(name) {
    var pattern = new RegExp("[?&]" + name + "\=([^&]+)", "g");
    var matcher = pattern.exec(window.location.href);
    var items = null;
    if (null != matcher) {
        try {
            items = decodeURIComponent(decodeURIComponent(matcher[1]));
        } catch (e) {
            try {
                items = decodeURIComponent(matcher[1]);
            } catch (e) {
                items = matcher[1];
            }
        }
    }
    return items;
}

function invalidStatusHandler(code, alertInfo, redirect) {
    if (window.localjson.code === code) {
        alert(alertInfo);
        window.location.href = getPagePrefix() + redirect;
        return true;
    }
    return false;
}

function getJsonFromServer(serverPageName, paramstr, callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4) {
            if (xmlhttp.status >= 200 && xmlhttp.status < 300 ||
                xmlhttp.status === 304) {
                window.localjson = JSON.parse(xmlhttp.responseText);
                callback();
            }
        } else {
            if (xmlhttp.status === 404) {
                alert("服务器上不存在'" + serverPageName + "'");
            }
        }
    };
    var method = paramstr === null ? "get" : "post";
    xmlhttp.open(method, getPagePrefix() + serverPageName, true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlhttp.send(paramstr);
}

function initNav() {
    var brandlink = document.getElementById("navbar-brand-link");
    var username = document.getElementById("username");
    var role = getcookie("role");
    username.innerText = getcookie("email");
    if (role === "INSTRUCTOR") {
        username.href = getPagePrefix() + "/user/instructor/home.html";
        brandlink.href = getPagePrefix() + "/user/instructor/home.html";
    }
    else {
        username.href = getPagePrefix() +"/user/student/home.html";
        brandlink.href = getPagePrefix() + "/user/student/home.html";
    }
}

function getJsonFromServerWithFile(serverPageName, paramstr, callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4) {
            if (xmlhttp.status >= 200 && xmlhttp.status < 300 ||
                xmlhttp.status === 304) {
                window.localjson = JSON.parse(xmlhttp.responseText);
                callback();
            }
        } else {
            if (xmlhttp.status === 404) {
                alert("服务器上不存在'" + serverPageName + "'");
            }
        }
    };
    var method = paramstr === null ? "get" : "post";
    xmlhttp.open(method, getPagePrefix() + serverPageName, true);
    xmlhttp.send(paramstr);
}

function getPagePrefix() {
    return "/15336036_v9";
}