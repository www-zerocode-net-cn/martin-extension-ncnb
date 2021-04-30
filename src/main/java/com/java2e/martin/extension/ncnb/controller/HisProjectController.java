package com.java2e.martin.extension.ncnb.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.entity.DbChange;
import com.java2e.martin.extension.ncnb.service.DbChangeService;
import com.java2e.martin.extension.ncnb.service.DbVersionService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/10/28
 * @describtion HisProjectController
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping
public class HisProjectController {
    @Autowired
    DbChangeService dbChangeService;

    @Autowired
    private DbVersionService dbVersionService;


    /**
     * 加载历史项目版本号
     *
     * @param projectId String
     * @return R
     */
    @GetMapping("/hisProject/load/{projectId}")
    public R loadHistory(@PathVariable String projectId) {
        return dbChangeService.loadHistory(projectId);

    }

    /**
     * 删除版本
     *
     * @param changeId String
     * @return R
     */
    @PostMapping("/hisProject/delete/{changeId}")
    public R deleteHistory(@PathVariable String changeId) {
        return dbChangeService.deleteHistory(changeId);

    }

    /**
     * 删除项目下所有版本版本
     *
     * @param projectId String
     * @return R
     */
    @PostMapping("/hisProject/deleteAll/{projectId}")
    public R deleteAllHistory(@PathVariable String projectId) {
        return dbChangeService.deleteAllHistory(projectId);

    }

    @PostMapping("/hisProject/save")
    public R save(@RequestBody Map map) {
        DbChange dbChange = new DbChange();
        dbChange.setBaseVersion((Boolean) map.get("baseVersion"));
        try {
            dbChange.setChanges(JsonUtil.generate(map.get("changes")).getBytes());
            dbChange.setProjectJSON(JsonUtil.generate(map.get("projectJSON")).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbChange.setId(RandomUtil.simpleUUID());
        dbChange.setProjectId((String) map.get("projectId"));
        dbChange.setVersion((String) map.get("version"));
        dbChange.setVersionDate((String) map.get("versionDate"));
        dbChange.setVersionDesc((String) map.get("versionDesc"));
        dbChangeService.save(dbChange);
        QueryWrapper<DbChange> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId", (String) map.get("projectId"));
        wrapper.orderBy(false, false, "version");
        List<DbChange> dbChanges = dbChangeService.list(wrapper);
        return dbChangeService.getHashMapsByDbChanges(dbChanges);
    }
}
