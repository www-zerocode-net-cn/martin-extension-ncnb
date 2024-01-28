package com.java2e.martin.extension.ncnb.command;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.util.JdbcKit;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/29
 * @describtion DbSyncCommand
 * @since 1.0
 */
@Slf4j
public class DbSyncCommand extends AbstractDBCommand<R> {
    @Override
    public R exec(Map<String, String> params) {
        super.init(params);
        Connection conn = JdbcKit.getConnection(this.driverClassName, this.url, this.username, this.password);
        String separator = params.get("separator");
        String sql = params.get("sql");
        String[] sqls = StrUtil.split(sql, separator);
        List<String> sqlList = new ArrayList<>();
        for (String s : sqls) {
            if (StrUtil.isNotBlank(s)) {
                log.info("=========sql===========", s);
                sqlList.add(s.replace(StrUtil.CRLF, "").replace("/n", ""));
            }
        }
        return Common.execSqls(params, conn, ArrayUtil.toArray(sqlList.iterator(), String.class));
    }
}
