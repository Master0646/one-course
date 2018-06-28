var submit = document.getElementById("submit");
submit.onclick = function(){
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var identityArr = document.getElementsByName("identity");
	var identity;
	for (var i = identityArr.length - 1; i >= 0; i--) {
		if(identityArr[i].checked){
			identity = identityArr[i].value;
			break;
		}
	}

	var url = "http://172.18.157.244:55555/signup.jsp";
	var method = "POST";
	var xmlHttpRequest = new XMLHttpRequest();

	xmlHttpRequest.open(method, url, true);
	xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlHttpRequest.send("email="+email+"&pass="+password+"&role="+identity);

	xmlHttpRequest.onreadystatechange = function(){
		if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
			var returnData = JSON.parse(xmlHttpRequest.responseText);
			console.log(returnData.msg);      
        	if(returnData.msg == "Ok"){  
            	alert("注册成功！");
            	window.location.href='index.html';      
        	}else{      
            	alert("登录失败！");      
        	}
		}     
        	
    }
}