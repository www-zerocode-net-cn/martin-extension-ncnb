package com.java2e.martin.extension.ncnb.command;


import cn.hutool.core.util.ArrayUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.util.JdbcKit;
import com.java2e.martin.extension.ncnb.util.StringKit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/29
 * @describtion DbRebaseLineCommand
 * @since 1.0
 */
@Slf4j
public class DbRebaseLineCommand extends AbstractDBCommand<R> {
    @SneakyThrows
    @Override
    public R exec(Map<String, String> params) {
        super.init(params);
        Connection conn = JdbcKit.getConnection(this.driverClassName, this.url, this.username, this.password);
        DatabaseMetaData dbmd = conn.getMetaData();
        String dbType = StringKit.nvl(conn.getMetaData().getDatabaseProductName(), "MYSQL").toUpperCase();
        String schemaPattern = null;
        if ("ORACLE".equalsIgnoreCase(dbType) || "DB2".equalsIgnoreCase(dbType) || "DM DBMS".equalsIgnoreCase(dbType)) {
            schemaPattern = conn.getMetaData().getUserName().toUpperCase();
        }
        ResultSet resultSet = dbmd.getTables(null, schemaPattern, "db_version", null);
        ArrayList<String> sqlList = new ArrayList<>();
        if (!resultSet.next()) {
            StringBuilder createVersion = new StringBuilder();
            createVersion.append("create table db_version                          ");
            createVersion.append("(                                                ");
            createVersion.append("    id           varchar(64)   not null,         ");
            createVersion.append("    db_version   varchar(256)  null,             ");
            createVersion.append("    version_desc varchar(1024) null,             ");
            createVersion.append("    created_time varchar(32)   null,             ");
            createVersion.append("    project_id   varchar(64)   not null          ");
            createVersion.append(");                                               ");
            sqlList.add(createVersion.toString());
            //更新数据库版本信息
            String initVersion = Common.getInsertVersionSql(params);
            sqlList.add(initVersion);
        } else {
            //重建版本信息
            String rebaseVersion = "delete from db_version where db_version!='v0.0.0' ;";
            sqlList.add(rebaseVersion);
            log.info("=========rebaseVersion===========", rebaseVersion);
        }
        return Common.execSqls(params, conn, ArrayUtil.toArray(sqlList.iterator(), String.class));
    }
}
