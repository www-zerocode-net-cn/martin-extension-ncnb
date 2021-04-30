package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.common.core.api.ApiErrorCode;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.command.DBReverseParseCommand;
import com.java2e.martin.extension.ncnb.command.DbSqlExecCommand;
import com.java2e.martin.extension.ncnb.command.DbSyncCommand;
import com.java2e.martin.extension.ncnb.command.PingDBCommand;
import com.java2e.martin.extension.ncnb.entity.DbVersion;
import com.java2e.martin.extension.ncnb.service.DbChangeService;
import com.java2e.martin.extension.ncnb.service.DbVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;


/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/26
 * @describtion ConnectorController
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("connector")
public class ConnectorController {
    @Autowired
    DbChangeService dbChangeService;

    @Autowired
    private DbVersionService dbVersionService;

    @PostMapping("ping")
    public R ping(@RequestBody Map map) {
        PingDBCommand pingDBCommand = new PingDBCommand();
        return pingDBCommand.exec(map);
    }

    @PostMapping("dbReverseParse")
    public R dbReverseParse(@RequestBody Map map) {
        DBReverseParseCommand dbReverseParseCommand = new DBReverseParseCommand();
        return dbReverseParseCommand.exec(map);
    }

    @PostMapping("dbversion")
    public R dbversion(@RequestBody Map map) {
        String version = dbVersionService.dbversion((String) map.get("projectId"));
        if (null == version) {
            DbVersion dbVersion = new DbVersion();
            dbVersion.setProjectId((String) map.get("projectId"));
            dbVersion.setDbVersion("v0.0.0");
            dbVersion.setVersionDesc("基线版本，新建版本时请勿低于该版本");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createTime = LocalDateTime.now().format(formatter);
            dbVersion.setCreatedTime(createTime);
            dbVersionService.save(dbVersion);
            version = "v0.0.0";
        }
        return R.ok(version);

    }


    @PostMapping("rebaseline")
    public R rebaseline(@RequestBody Map map) {
        return R.ok(dbVersionService.rebaseline((String) map.get("projectId")));
    }

    @PostMapping("dbsync")
    public R dbsync(@RequestBody Map map) {
        DbSyncCommand dbSyncCommand = new DbSyncCommand();
        R result = dbSyncCommand.exec(map);
        if (ApiErrorCode.OK.equals(result.getCode())) {
            dbVersionService.saveDbVersion(map);
        }
        return result;
    }


    @PostMapping("sqlexec")
    public R sqlexec(@RequestBody Map map) {
        DbSqlExecCommand dbSqlExecCommand = new DbSqlExecCommand();
        return dbSqlExecCommand.exec(map);
    }

    @PostMapping("updateVersion")
    public R updateVersion(@RequestBody Map<String, Object> params) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String version = (String) params.get("version");
        String versionDesc = (String) params.get("versionDesc");
        String projectId = (String) params.get("projectId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = LocalDateTime.now().format(formatter);
        DbVersion dbVersion = new DbVersion();
        dbVersion.setId(id);
        dbVersion.setDbVersion(version);
        dbVersion.setVersionDesc(versionDesc);
        dbVersion.setProjectId(projectId);
        dbVersion.setCreatedTime(createTime);
        return R.ok(dbVersionService.save(dbVersion));
    }


}
