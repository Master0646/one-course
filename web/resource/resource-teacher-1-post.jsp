<%request.setCharacterEncoding("utf-8"); 
	String coursename = request.getParameter("coursename");
	String descripition = request.getParameter("description");  
	String inform = request.getParameter("inform");  
	System.out.println(description + "" + inform);  
	request.setCharacterEncoding("utf-8");  
	String connectString = "jdbc:mysql://172.18.187.233:53306/teaching"  
		+ "?autoReconnect=true&useUnicode=true"  
		+ "&characterEncoding=UTF-8";   
		StringBuilder res_str=new StringBuilder("");  
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(connectString,   
			"user", "123");  
		Statement stmt=con.createStatement();  
		String sql=String.format("Update XXX Set description='%s' inform='%s' Where coursename=%s", description, inform, coursename);   
		if(stmt.execute(sql)){  
			response.getWriter().write("success");  
			System.out.print("success");  
		};  
	} catch(Exception e){  
		e.printStackTrace();  
		}  
%>  
