package com.java2e.martin.extension.ncnb.command;

import cn.hutool.core.util.StrUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.util.JdbcKit;

import java.sql.Connection;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/29
 * @describtion DbSqlExecCommand
 * @since 1.0
 */
public class DbSqlExecCommand extends AbstractDBCommand<R> {
    @Override
    public R exec(Map<String, String> params) {
        super.init(params);
        return execSqls(params);
    }

    private R execSqls(Map<String, String> params) {
        Connection conn = JdbcKit.getConnection(this.driverClassName, this.url, this.username, this.password);
        String separator = params.get("separator");
        String sql = params.get("sql");
        String[] sqls = StrUtil.split(sql, separator);
        return Common.execSqls(params, conn, sqls);
    }
}
