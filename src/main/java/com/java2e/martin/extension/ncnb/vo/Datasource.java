package com.java2e.martin.extension.ncnb.vo;

import lombok.Data;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/26
 * @describtion Datasource
 * @since 1.0
 */
@Data
public class Datasource {
    private String driverClassName;
    private String password;
    private String projectId;
    private String url;
    private String username;
    private String flag;
}
