/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class DBContext {

    Connection connection;

//    public DBContext() {
//        try {
//            String username = "sa";
//            String password = "sa";
//            String url = "jdbc:sqlserver://localhost:1433;databaseName=OnlineShop2";
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    public DBContext() {
//        try {
//            String username = "se1617-g1";
//            String password = "passmon123!@";
//            String url = "jdbc:sqlserver://10.0.2.25:1433;databaseName=OnlineShop2";
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    
    
     public DBContext() {
         try {
             String username = "se1617-g1";
             String password = "passmon123!@";
             String url = "jdbc:sqlserver://103.9.158.241:1433;databaseName=OnlineShop2";
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             connection = DriverManager.getConnection(url, username, password);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
             Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
         }
     }


//    public DBContext() {
//        try {
//            String user = "se1610";
//            String pass = "123456";
//            String url = "jdbc:sqlserver://QUANG:1433;databaseName=OnlineShop8";
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            connection = DriverManager.getConnection(url, user, pass);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public static void main(String[] args) {
        DBContext bContext = new DBContext();
        System.out.println(bContext.connection.toString());
    }
}
