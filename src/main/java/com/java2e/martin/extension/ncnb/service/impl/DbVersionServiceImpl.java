package com.java2e.martin.extension.ncnb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.extension.ncnb.entity.DbChange;
import com.java2e.martin.extension.ncnb.entity.DbVersion;
import com.java2e.martin.extension.ncnb.mapper.DbVersionMapper;
import com.java2e.martin.extension.ncnb.service.DbVersionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
public class DbVersionServiceImpl extends MartinServiceImpl<DbVersionMapper, DbVersion> implements DbVersionService {

    @Override
    public String dbversion(Map map) {
        return baseMapper.dbversion(map);
    }

    @Override
    public List<String> checkdbversion(Map map) {
        return baseMapper.checkdbversion(map);
    }

    @Override
    public Integer rebaseline(Map map) {
        return baseMapper.rebaseline(map);
    }

    @Override
    public Boolean saveDbVersion(Map map) {
        DbVersion dbVersion = new DbVersion();
        dbVersion.setDbVersion((String) map.get("version"));
        dbVersion.setVersionDesc((String) map.get("versionDesc"));
        dbVersion.setProjectId((String) map.get("projectId"));
        dbVersion.setDbKey((String) map.get("dbKey"));
        return this.save(dbVersion);
    }

    @Override
    protected void setEntity() {
        this.clz = DbVersion.class;

    }
}
