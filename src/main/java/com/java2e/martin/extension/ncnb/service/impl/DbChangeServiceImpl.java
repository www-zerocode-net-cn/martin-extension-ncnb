package com.java2e.martin.extension.ncnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.extension.ncnb.entity.DbChange;
import com.java2e.martin.extension.ncnb.entity.DbVersion;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.mapper.DbChangeMapper;
import com.java2e.martin.extension.ncnb.service.DbChangeService;
import com.java2e.martin.extension.ncnb.service.DbVersionService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 版本表 服务实现类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-28
 */
@Slf4j
@Service
public class DbChangeServiceImpl extends MartinServiceImpl<DbChangeMapper, DbChange> implements DbChangeService {
    @Autowired
    private DbVersionService dbVersionService;

    @Override
    public R loadHistory(Map map) {

        QueryWrapper<DbChange> wrapper = new QueryWrapper<>();
        wrapper.eq("project_id", map.get("projectId"));
        wrapper.eq("db_key", map.get("dbKey"));
        wrapper.orderBy(false, false, "version");
        List<DbChange> dbChanges = this.list(wrapper);
        return getHashMapsByDbChanges(dbChanges);
    }

    @Override
    public List<DbChange> loadHistoryVersion(String projectId,String dbKey) {
        QueryWrapper<DbChange> wrapper = new QueryWrapper<>();
        wrapper.eq("project_id", projectId);
        wrapper.eq("db_key", dbKey);
        wrapper.select("version", "version_desc", "version_date", "base_version");
        List<DbChange> dbChanges = this.list(wrapper);
        return dbChanges;
    }

    @Override
    public R getHashMapsByDbChanges(List<DbChange> dbChanges) {
        List<HashMap<Object, Object>> collect = dbChanges.stream().map(dv1 -> {
            HashMap<Object, Object> hashMap = new HashMap<>();
            try {
                hashMap.put("id", dv1.getId());
                hashMap.put("baseVersion", dv1.getBaseVersion());
                hashMap.put("changes", JsonUtil.parse(new String(dv1.getChanges()), List.class));
                hashMap.put("projectJSON", JsonUtil.parse(new String(dv1.getProjectJSON()), Map.class));
                hashMap.put("projectId", dv1.getProjectId());
                hashMap.put("dbKey", dv1.getDbKey());
                hashMap.put("version", dv1.getVersion());
                hashMap.put("versionDate", dv1.getVersionDate());
                hashMap.put("versionDesc", dv1.getVersionDesc());
            } catch (Exception e) {
                log.error("", e);
            }
            return hashMap;
        }).collect(Collectors.toList());
        return R.ok(collect);
    }

    @Override
    public R deleteHistory(String changeId) {
        DbChange dbChange = this.getById(changeId);
        if (dbChange != null) {
            QueryWrapper<DbVersion> wrapper = new QueryWrapper<>();
            wrapper.eq("project_id", dbChange.getProjectId());
            wrapper.eq("db_key", dbChange.getDbKey());
            wrapper.eq("db_version", dbChange.getVersion());
            dbVersionService.remove(wrapper);
            return R.ok(this.removeById(changeId));
        } else {
            return R.failed("删除失败，无效的版本");
        }
    }

    @Override
    public R deleteAllHistory(Map map) {
        QueryWrapper<DbChange> wrapper = new QueryWrapper<>();
        wrapper.eq("project_d", map.get("projectId"));
        wrapper.eq("db_key", map.get("dbKey"));
        return R.ok(this.remove(wrapper));
    }

    @Override
    protected void setEntity() {
        this.clz = DbChange.class;

    }
}
