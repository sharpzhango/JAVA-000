package io.sharpzhango.jdbchicari;

import java.sql.*;

public class JDBCPreparedStatement {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/sang";
    private static final String USER = "root";
    private static final String PASSWORD = null;

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            String sql = "select * from sys_role where id = ?";
            statement = connection.prepareStatement(sql);
            // PreparedStatement 可以通过转义字符 \ 防止依赖注入， ,\ 的作用：标明 "hello \'1 " 中的 "'" 不是分割符而是字符串的一部分
            statement.setString(1, "1");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
//                String name = "1";

                System.out.printf("id %s name %s \n", id, name);
            }
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
