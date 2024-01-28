package com.java2e.martin.extension.ncnb.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion JdbcKit
 * @since 1.0
 */
@Slf4j
public abstract class JdbcKit {

    public JdbcKit() {
    }

    public static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception var2) {
            log.error("关闭出错", var2);
        }

    }

    public static Connection getConnection(String driverClassName, String url, String username, String password) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException var7) {
            log.error("", var7);
            throw new RuntimeException("驱动加载失败，驱动类不存在(ClassNotFoundException)！出错消息：" + var7.getMessage(), var7);
        }

        Connection conn = null;

        try {
            Properties props = new Properties();
            props.put("user", username);
            props.put("password", password);
            props.put("remarksReporting", "true");
            props.put("useInformationSchema", "true");
            conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (SQLException var6) {
            log.error("", var6);
            throw new RuntimeException("连接失败!出错消息：" + var6.getMessage(), var6);
        }
    }

    public static boolean isNumeric(int sqlType) {
        return -7 == sqlType || -5 == sqlType || 3 == sqlType || 8 == sqlType || 6 == sqlType || 4 == sqlType || 2 == sqlType || 7 == sqlType || 5 == sqlType || -6 == sqlType;
    }

    public static boolean isDate(int sqlType) {
        return 91 == sqlType || 92 == sqlType || 2013 == sqlType || 2014 == sqlType || 93 == sqlType;
    }

    public static boolean isShortString(int sqlType) {
        return 12 == sqlType || 1 == sqlType;
    }
}
