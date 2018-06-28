var email = getcookie("email");
var username_on_bar = document.getElementById("username-on-bar");
username_on_bar.innerHTML=email;


function getcookie(objname){//获取指定名称的cookie的值
	var arrstr = document.cookie.split("; ");
	for(var i = 0;i < arrstr.length;i ++){
		var temp = arrstr[i].split("=");
		if(temp[0] == objname) return unescape(temp[1]);
	}
}