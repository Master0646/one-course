package onecourse.utils;

import java.sql.*;

public class DBHelper {
    private static final String LIB_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://172.18.187.234:53306/onecourse_15336036"
            + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&&useSSL=false";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "123";
    private static Connection con;

    // 获取数据库连接对象
    public static Connection getConnection() {

        if (con == null) {
            try {
                Class.forName(LIB_NAME);
                DriverManager.setLoginTimeout(2);
                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            return con;
        }
        return con;
    }

    public static PreparedStatement getPreparedStatement(String sql)
            throws SQLException {
        Connection con = getConnection();
        if (con == null)    throw new SQLException();
        return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public static PreparedStatement setPreparedStatementParam(PreparedStatement statement, Object obj[])
            throws SQLException {
        for (int i = 0; i < obj.length; i++) {
            statement.setObject(i + 1, obj[i]);
        }

        return statement;
    }

    // 释放资源
    public static void release(PreparedStatement ps, ResultSet rs) {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
