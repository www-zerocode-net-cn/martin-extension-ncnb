package com.java2e.martin.extension.ncnb.codegen.mybatis;

import com.java2e.martin.extension.ncnb.entity.Code;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class MybatisPlusCodegenTest {

    @Test
    public void generateService() {
        String path = "/Users/masterliang/idea_project/martin/martin-extension/martin-extension-ncnb/src/test/java/com/java2e/martin/extension/ncnb/codegen/mybatis";
        MybatisPlusCodegen mybatisPlusCodegen = new MybatisPlusCodegen();
        Code code = new Code();
        code.setAuthor("ncnb");
        code.setDbDriverName("com.mysql.cj.jdbc.Driver");
        code.setDbPassword("root");
        code.setDbUrl("jdbc:mysql://martin-mysql:3306/erd?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
        code.setDbUsername("root");
        code.setModuleName("");
        code.setParent("com.java2e.ncnb");
        code.setTableName("project_role");
        mybatisPlusCodegen.generateService(code, path);
    }
}