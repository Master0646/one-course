//初始化用户名
var email = getcookie("email");
var username_on_bar = document.getElementById("username-on-bar");
username_on_bar.innerHTML = email;
var username_on_title = document.getElementById("username-on-title");
username_on_title.innerHTML = email;
var username_on_brief_info = document.getElementById("username-on-brief-info");
username_on_brief_info.innerHTML = email;
var username_on_profile_display= document.getElementById("username-on-profile-display");
username_on_profile_display.innerHTML = email;
//初始化身份
var role = getcookie("role");
var userrole_on_brief_info = document.getElementById("userrole-on-brief-info");
userrole_on_brief_info.innerHTML = role;

function getcookie(objname){//获取指定名称的cookie的值
	var arrstr = document.cookie.split("; ");
	for(var i = 0;i < arrstr.length;i ++){
		var temp = arrstr[i].split("=");
		if(temp[0] == objname) return unescape(temp[1]);
	}
	
	return "Unknown";
}

if(role == "STUDENT"){
	var class_display_item =document.getElementById("class-display-item");
	class_display_item.style.display = "none";

	var url = "http://localhost:8080/one-course-server_war/home.jsp";
    var method = "GET";
    var xmlHttpRequest = new XMLHttpRequest();

    xmlHttpRequest.open(method, url, true);
    xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xmlHttpRequest.send(null);

    xmlHttpRequest.onreadystatechange = function(){
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
            var returnData = JSON.parse(xmlHttpRequest.responseText);     
            if(returnData.msg == "Ok"){  
                var content = returnData.content;
                var classes_choosed = document.getElementById("classes-choosed");
                for(var i = 0; i < content.length; i++){
                    var class_name = content[i].name;
                    var class_code = content[i].courseCode;
                    var li = document.createElement("li");
                    li.innerHTML = class_name + " [" + class_code + "]";
                    classes_choosed.appendChild(li);
                }
            }else if(returnData.code == "202"){
                alert("没有已选课程!");
            }else{      
                alert("获取课程失败！");      
            }
        }     
    }
}else if(role == "INSTRUCTOR"){
	var class_choose_item =document.getElementById("class-choose-item");
	class_choose_item.style.display = "none";

	var detailed_info_2 =document.getElementById("detailed-info-2");
	detailed_info_2.style.display = "none";
}


//创建几个样例课程
// var url = "http://localhost:8080/one-course-server_war/create-class.jsp";
// var method = "POST";
// for(var i = 0; i < 10; i++){
// 	var xmlHttpRequest = new XMLHttpRequest();

//     xmlHttpRequest.open(method, url, true);
//     xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
//     xmlHttpRequest.send("name=test"+i+"&school=sdcs&year=2018&semester=OTH");

//     xmlHttpRequest.onreadystatechange = function(){
//         if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
//             var returnData = JSON.parse(xmlHttpRequest.responseText);     
//             if(returnData.msg == "Ok"){  
//                 alert("课程创建成功："+i);
//             }else if(returnData.code == "202"){
//                 alert(returnData.content);
//             }else{      
//                 alert("获取课程失败！");      
//             }
//         }      
//     }
// }
