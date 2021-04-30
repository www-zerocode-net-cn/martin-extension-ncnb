package com.java2e.martin.extension.ncnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.entity.DbChange;
import com.java2e.martin.extension.ncnb.mapper.DbChangeMapper;
import com.java2e.martin.extension.ncnb.service.DbChangeService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
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
@Service
public class DbChangeServiceImpl extends ServiceImpl<DbChangeMapper, DbChange> implements DbChangeService {

    @Override
    public R loadHistory(String projectId) {
        QueryWrapper<DbChange> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId", projectId);
        wrapper.orderBy(false, false, "version");
        List<DbChange> dbChanges = this.list(wrapper);
        return getHashMapsByDbChanges(dbChanges);
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
                hashMap.put("version", dv1.getVersion());
                hashMap.put("versionDate", dv1.getVersionDate());
                hashMap.put("versionDesc", dv1.getVersionDesc());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hashMap;
        }).collect(Collectors.toList());
        return R.ok(collect);
    }

    @Override
    public R deleteHistory(String changeId) {
        return R.ok(this.removeById(changeId));
    }

    @Override
    public R deleteAllHistory(String projectId) {
        QueryWrapper<DbChange> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId", projectId);
        return R.ok(this.remove(wrapper));
    }
}
