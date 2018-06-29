var role = getcookie("role");
//页面跳转
var jump_classes_edit = document.getElementById("btn-classes-edit");
var jump_classes_choose = document.getElementById("main-bar-2");
var jump_profile_display = document.getElementById("main-bar-1");

var class_choose_area = document.getElementById("class-choose-area");
var profile_display_area = document.getElementById("profile-display-area");
var profile_edit_area = document.getElementById("profile-edit-area");
var classes_edit_area = document.getElementById("classes-edit-area");

jump_classes_edit.onclick = function(){
    jump_classes_choose.style.color = "black";
    jump_profile_display.style.color = 'black';

    class_choose_area.style.display ="none";
    profile_display_area.style.display ="none";
    classes_edit_area.style.display ="block";

    var classes_edit_list = document.getElementById("classes-edit-list");
    //初始化编辑列表
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
                var classes_edit_list = document.getElementById("classes-edit-list");
                classes_edit_list.innerHTML = "";
                for(var i = 0; i < content.length; i++){
                    var class_name = content[i].name;
                    var class_code = content[i].courseCode;
                    var input = document.createElement("input");
                    input.type="radio";
                    input.style="margin:20px;";
                    input.value=class_code;
                    input.name="class-to-edit";
                    classes_edit_list.appendChild(input);

                    var tag_name = document.createElement("b");
                    tag_name.style="margin:20px; color:#696969;";
                    tag_name.innerHTML="CLASS "+i+" : " +class_name+" Code: "+class_code +"<br>";
                    classes_edit_list.appendChild(tag_name);


                }
            }else if(returnData.code == "202"){
                alert("没有已选课程!");
            }else{      
                alert("获取课程失败！");      
            }
        }     
    }
}

jump_classes_choose.onclick = function(){
    jump_classes_choose.style.color = "#428bca";
    jump_profile_display.style.color = 'black';

    class_choose_area.style.display ="block";
    profile_display_area.style.display ="none";
    classes_edit_area.style.display ="none";

    //初始化课程列表
    if (role == "INSTRUCTOR"){
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
                    var classes_to_choose = document.getElementById("classes-to-display");
                    for(var i = 0; i < content.length; i++){
                        var class_name = content[i].name;
                        var class_code = content[i].courseCode;
                        var option = document.createElement("option");
                        option.text = class_name + " [" + class_code + "]";
                        option.value = class_code;
                        classes_to_choose.options.add(option, 1);
                    }
                }else if(returnData.code == "202"){
                    alert("没有可选课程!");
                }else{      
                    alert("获取课程失败！");      
                }
            }     
            
        }
    }
    
}

jump_profile_display.onclick = function(){
    jump_classes_choose.style.color = "black";
    jump_profile_display.style.color = '#428bca';

    class_choose_area.style.display ="none";
    profile_display_area.style.display ="block";
    classes_edit_area.style.display ="none";
}

var btn_class_add = document.getElementById("btn-class-add");
btn_class_add.onclick = function(){
    var course_code = document.getElementById("class-to-choose").value;
    var url = "http://localhost:8080/one-course-server_war/join-class.jsp";
    var method = "POST";
    var xmlHttpRequest = new XMLHttpRequest();

    xmlHttpRequest.open(method, url, true);
    xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xmlHttpRequest.send("courseCode="+course_code);

    xmlHttpRequest.onreadystatechange = function(){
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
            var returnData = JSON.parse(xmlHttpRequest.responseText);     
            if(returnData.msg == "Ok"){  
                alert("添加成功！")
            }else{      
                alert("添加失败！");      
            }
        }     
            
    }
}

var class_cancel = document.getElementById("class-cancel");
var class_detail = document.getElementById("class-detail");
class_cancel.onclick = function(){
    var classes_edit_list = document.getElementsByName("class-to-edit");
    var course_code = "";
    for (var i = classes_edit_list.length - 1; i >= 0; i--) {
        if(classes_edit_list[i].checked){
            course_code = classes_edit_list[i].value;
            break;
        }
    }

    if(course_code != ""){
        var url = "http://localhost:8080/one-course-server_war/join-class.jsp";
        var method = "POST";
        var xmlHttpRequest = new XMLHttpRequest();

        xmlHttpRequest.open(method, url, true);
        xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttpRequest.send("courseCode="+course_code);

        xmlHttpRequest.onreadystatechange = function(){
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
                var returnData = JSON.parse(xmlHttpRequest.responseText);     
                if(returnData.msg == "Ok"){  
                    alert("退选成功！")
                }else{      
                    alert("退选失败！");      
                }
            }     
        }
    }else{
        alert("请选择课程")
    }
}

class_detail.onclick = function(){
    var classes_edit_list = document.getElementsByName("class-to-edit");
    var course_code = "";
    for (var i = classes_edit_list.length - 1; i >= 0; i--) {
        if(classes_edit_list[i].checked){
            course_code = classes_edit_list[i].value;
            break;
        }
    }
    if (course_code != "") {
        window.location.href='../class/student/index.html?courseCode='+course_code;
    }else{
        alert("请选择课程");
    }
}
