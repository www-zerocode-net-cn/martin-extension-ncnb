package com.java2e.martin.extension.ncnb.command;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import com.java2e.martin.common.core.api.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/11/2
 * @describtion Common
 * @since 1.0
 */
@Slf4j
public class Common {
    public static String getInsertVersionSql(Map<String, String> params) {
        String initVersion = "insert into db_version(id,db_version,version_desc,created_time,project_id) values ('%s','%s','%s','%s','%s');";
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String version = params.get("version");
        String versionDesc = params.get("versionDesc");
        String projectId = params.get("projectId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = LocalDateTime.now().format(formatter);
        initVersion = String.format(initVersion, id, version, versionDesc, createTime, projectId);
        log.info("=========updateVersion===========", initVersion);
        return initVersion;
    }

    @SneakyThrows
    public static R execSqls(Map<String, String> params, Connection conn, String[] sqls) {
        Statement statement = conn.createStatement();
        try {
            conn.setAutoCommit(false);
            List<String> execSqls = Arrays.stream(sqls).filter(f -> StrUtil.isNotBlank(f)).collect(Collectors.toList());
            if (CollUtil.isEmpty(execSqls)) {
                return R.failed("不能执行空的sql");
            }
            for (String s : sqls) {

                if (StrUtil.isNotBlank(s)) {
                    log.info("=========sql==========={}", s);
                    statement.addBatch(s);
                }
            }
            statement.executeBatch();
            //提交事务
            log.info("提交sql");
            conn.commit();
            return R.ok(params.get("sql"));
        } catch (Exception var6) {
            try {
                // 若出现异常，对数据库中所有已完成的操作全部撤销，则回滚到事务开始状态
                log.error("回滚sql", var6);
                conn.rollback();
            } catch (Exception e) {
                log.error("回滚sql失败", var6);
                return R.failed(e.getMessage());
            }
            Throwable causedBy = ExceptionUtil.getCausedBy(var6, SQLException.class);
            return R.failed(causedBy.getMessage());
        } finally {
            log.error("关闭所有sql连接");
            closeConnection(conn, statement, null);
        }
    }


    /**
     * 按照连接方式倒序关闭所有连接
     *
     * @param conn
     * @param stmt
     * @param resultSet
     */
    private static void closeConnection(Connection conn, Statement stmt, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            log.error("关闭数据库连接失败", e);
        }

    }
}