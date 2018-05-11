package com.supplier.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class K3DbTools {
    private final static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//连接mySql数据库的驱动
    private final static String url = "jdbc:sqlserver://192.168.1.254:1433;DatabaseName=AIScloud";//1433是端口号
    private final static String user = "sa";
    private final static String password = "KLxs8888";

    public  void test()

    {

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url,user,password);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
