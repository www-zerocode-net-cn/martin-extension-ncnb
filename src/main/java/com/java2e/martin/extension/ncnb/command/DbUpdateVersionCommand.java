package com.java2e.martin.extension.ncnb.command;

import cn.hutool.core.util.ArrayUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.util.JdbcKit;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/29
 * @describtion DbUpdateVersionCommand
 * @since 1.0
 */
public class DbUpdateVersionCommand extends AbstractDBCommand<R> {
    @Override
    public R exec(Map<String, String> params) {
        super.init(params);
        return execSqls(params);
    }

    private R execSqls(Map<String, String> params) {
        super.init(params);
        Connection conn = JdbcKit.getConnection(this.driverClassName, this.url, this.username, this.password);
        String initVersion = Common.getInsertVersionSql(params);
        ArrayList<String> sqlList = new ArrayList<>();
        sqlList.add(initVersion);
        return Common.execSqls(params, conn, ArrayUtil.toArray(sqlList.iterator(), String.class));
    }
}
