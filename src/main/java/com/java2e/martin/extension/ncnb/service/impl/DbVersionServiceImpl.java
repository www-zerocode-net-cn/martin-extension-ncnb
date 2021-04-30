package com.java2e.martin.extension.ncnb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java2e.martin.extension.ncnb.entity.DbVersion;
import com.java2e.martin.extension.ncnb.mapper.DbVersionMapper;
import com.java2e.martin.extension.ncnb.service.DbVersionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-29
 */
@Service
public class DbVersionServiceImpl extends ServiceImpl<DbVersionMapper, DbVersion> implements DbVersionService {

    @Override
    public String dbversion(String projectId) {
        return baseMapper.dbversion(projectId);
    }

    @Override
    public Integer rebaseline(String projectId) {
        return baseMapper.rebaseline(projectId);
    }

    @Override
    public Boolean saveDbVersion(Map map) {
        DbVersion dbVersion = new DbVersion();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        dbVersion.setId(id);
        dbVersion.setDbVersion((String) map.get("version"));
        dbVersion.setVersionDesc((String) map.get("versionDesc"));
        dbVersion.setProjectId((String) map.get("projectId"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = LocalDateTime.now().format(formatter);
        dbVersion.setCreatedTime(createTime);
        return this.save(dbVersion);
    }
}
