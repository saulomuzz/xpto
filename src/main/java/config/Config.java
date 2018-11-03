/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author SAULOD
 */
public class Config {
    private static String HOST_BD = "localhost";
    private static String PORTA_BD = "3306";
    private static String USER_BD = "root";
    private static String PASS_BD = "";
    private static String DATABASE_BD = "db_xpto";

    public static String getHOST_BD() {
        return HOST_BD;
    }

    public static String getPORTA_BD() {
        return PORTA_BD;
    }

    public static String getUSER_BD() {
        return USER_BD;
    }

    public static String getPASS_BD() {
        return PASS_BD;
    }

    public static String getDATABASE_BD() {
        return DATABASE_BD;
    }

    public static String getJDBC_BD() {
        return JDBC_BD;
    }

    public static String getDRIVER_BD() {
        return DRIVER_BD;
    }
    private static String JDBC_BD = "mysql";
    private static String DRIVER_BD = "com.mysql.jdbc.Driver";
    
    private static String UPLOAD_PATH = System.getProperty("user.dir");

    public static String getUPLOAD_PATH() {
        return UPLOAD_PATH;
    }
}
