package io.sharpzhango.jdbchicari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariDemo {

    public static void main(String[] args) {
        String configFilePath = HikariDemo.class.getClassLoader().getResource("application.properties").getPath();
        HikariDataSource ds = new HikariDataSource(new HikariConfig(configFilePath));

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;


        try {
            con = ds.getConnection();
            pst = con.prepareStatement("SELECT * FROM sys_user");
            rs = pst.executeQuery();
            while (rs.next()) {
                System.out.format("%d %s %n", rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally{
            try {
                if (rs != null){
                    rs.close();
                }
                if (con != null){
                    con.close();
                }
                if (pst != null){
                    pst.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ds.close();
        }

    }
}
