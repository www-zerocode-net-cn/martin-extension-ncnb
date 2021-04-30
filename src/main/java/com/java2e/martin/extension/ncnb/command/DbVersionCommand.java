package com.java2e.martin.extension.ncnb.command;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.util.JdbcKit;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/29
 * @describtion DbVersionCommand
 * @since 1.0
 */
@Slf4j
public class DbVersionCommand extends AbstractDBCommand<R> {
    @Override
    public R exec(Map<String, String> params) {
        super.init(params);
        Connection conn = null;
        String dbType = null;

        try {
            conn = JdbcKit.getConnection(this.driverClassName, this.url, this.username, this.password);
            Statement statement = conn.createStatement();
            String sql = "SELECT DB_VERSION FROM DB_VERSION WHERE DB_VERSION=(SELECT max(DB_VERSION) FROM DB_VERSION)";
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            String version = "";
            while (resultSet.next()) {
                version += resultSet.getString(1);
            }
            return R.ok(version);
        } catch (Exception var6) {
            log.error("", var6);
            return R.failed(var6.getMessage());
        } finally {
            JdbcKit.close(conn);
        }


    }


}
