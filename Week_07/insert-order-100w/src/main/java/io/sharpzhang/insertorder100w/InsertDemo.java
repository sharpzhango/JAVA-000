package io.sharpzhang.insertorder100w;


import com.github.javafaker.Faker;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class InsertDemo {
    public static void main(String[] args) {
        String configFilePath = InsertDemo.class.getClassLoader().getResource("application.properties").getPath();
        HikariDataSource ds = new HikariDataSource(new HikariConfig(configFilePath));


        Connection con = null;
        PreparedStatement pst = null;
        int[] rs = null;
        Faker faker = new Faker();
        try {
            long start = System.currentTimeMillis();
            con = ds.getConnection();
//            String sql = "insert into  tb_user (id,password,username) values (?,?,?)";
            String sql = "insert into tb_user (id,create_time,name,age,phone,address,status,nickname,certificate_no) values(?,now(),?,21,12392323,'ddd',0,'ad','2323')";
            pst = con.prepareStatement(sql);
            for (int i = 0; i < 1000; i++) {
                pst.setInt(1,i+4);
                pst.setString(2, faker.name().firstName());
                pst.addBatch();

            }
            rs = pst.executeBatch();
            int number = rs.length;
            long end = System.currentTimeMillis();
            System.out.printf("insert %d rows,total millis %d ms %n",number,(end-start));
//            while (rs.next()){
//                int id = rs.getInt(1);
//                String name = rs.getString(2);
//                System.out.printf("id %s name %s \n",id,name);
//                System.out.println("say:\"hello sharpzhang\" ");
//            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
//                if (rs != null){
//                    rs.close();
//                }
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
