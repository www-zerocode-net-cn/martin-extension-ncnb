package com.java2e.martin.extension.ncnb.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion AbstractDBCommand
 * @since 1.0
 */
public abstract class AbstractDBCommand<T> {
    public static final String KEY_DRIVER_CLASS_NAME = "driver_class_name";
    public static final String KEY_URL = "url";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String driverClassName;
    protected String url;
    protected String username;
    protected String password;

    public AbstractDBCommand() {
    }

    public void init(Map<String, String> params) {
        this.driverClassName = (String) params.get("driverClassName");
        this.url = (String) params.get("url");
        this.username = (String) params.get("username");
        this.password = (String) params.get("password");
    }

    abstract T exec(Map<String, String> var1);
}

