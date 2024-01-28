package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.ProjectConstants;
import com.java2e.martin.common.data.mybatis.bean.JsonBase;
import com.java2e.martin.common.data.mybatis.config.UpdateJsonWrapper;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.mapper.JsonBaseMapper;
import com.java2e.martin.extension.ncnb.service.JsonBaseService;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/3/5 11:47
 * @describtion: JsonBaseServiceImpl
 */
@Slf4j
@Service
public class JsonBaseServiceImpl extends MartinServiceImpl<JsonBaseMapper, JsonBase> implements JsonBaseService {

    @Autowired
    private ProjectService projectService;

    @Override
    protected void setEntity() {
        this.clz = JsonBase.class;
    }

    @Override
    public R getJsonByName(String id, String name, String jsonPath) {
        R result = this.getPathByName(id, name, jsonPath);
        if (result.invalid()) {
            return result;
        }
        String path = (String) result.getData();
        JsonBase jsonBase = new JsonBase();
        jsonBase.setPath(path);
        jsonBase.setId(id);
        jsonBase.setName(name);
        QueryChainWrapper<JsonBase> wrapper = new QueryChainWrapper<>(baseMapper);
        wrapper.select("projectJSON ->> '" + path + "' as json");
        wrapper.eq(ProjectConstants.PROJECT_ID, id);
        JsonBase one = wrapper.one();
        if (one != null) {
            jsonBase.setJson(one.getJson());
        }
        log.info("id: {},name: {},jsonPath: {}", id, name, jsonPath);
        return R.ok(jsonBase);
    }

    @Override
    public R<String> getPathByName(String id, String name, String jsonPath) {
        List<String> paths = baseMapper.getPathByName(id, name, jsonPath);
        if (CollUtil.isEmpty(paths)) {
            return R.failed("该项目找不到「" + name + "」");
        }
        String path = paths.get(0);
        log.info("path: {}", path);
        if (path == null) {
            return R.failed("该项目找不到「" + name + "」");
        }
        path = StrUtil.subBefore(path, ".", true);
        path = StrUtil.removeAll(path, "\"");
        return R.ok(path);
    }

    @Override
    public R updateJson(String id, String name, String path, JSONObject json, String jsonPath, String jsonSchema) {
        boolean flag = baseMapper.jsonSchemaValid(jsonSchema, json.toJSONString());
        if (!flag) {
            return R.failed("json schema 验证未通过");
        }
        UpdateJsonWrapper<Project> wrapper = new UpdateJsonWrapper();
        if (StrUtil.isBlank(path)) {
            R<String> result = this.getPathByName(id, name, jsonPath);
            if (result.invalid()) {
                return result;
            }
            path = result.getData();
        }
        log.info("id: {},name: {},path: {},json: {},jsonPath: {},jsonSchema: {}", id, name, path, "", jsonPath, jsonSchema);
        wrapper.setJson(ProjectConstants.PROJECT_JSON, path, json);
        wrapper.eq(ProjectConstants.PROJECT_ID, id);
        return R.ok(projectService.update(wrapper));
    }

    @Override
    public R updateValue(String id, JsonBase jsonBase, String jsonPath) {
//        UpdateJsonWrapper<Project> wrapper = new UpdateJsonWrapper();
//        String path = jsonBase.getPath();
//        if (StrUtil.isBlank(path)) {
//            R<String> result = this.getPathByName(id, jsonBase.getName(), jsonPath);
//            if (result.invalid()) {
//                return result;
//            }
//            path = result.getData();
//        }
//        wrapper.set(ProjectConstants.PROJECT_JSON, path, jsonBase.getValue());
//        wrapper.eq(ProjectConstants.PROJECT_ID, id);
//        return R.ok(projectService.update(wrapper));
        return R.ok("success");
    }

    @Override
    public R removeJson(String id, String name, String path, String jsonPath) {
        if (StrUtil.isBlank(path)) {
            R<String> result = this.getPathByName(id, name, jsonPath);
            if (result.invalid()) {
                return result;
            }
            path = result.getData();
        }
        log.info("id: {},name: {},path: {},jsonPath: {}", id, name, path, jsonPath);
        return R.ok(baseMapper.removeJson(id, path));
    }

    @Override
    public R insertJson(String id, JSONObject json, String jsonPath, String jsonSchema) {
        if (json == null) {
            return R.failed("不能添加空json");
        }
        boolean flag = baseMapper.jsonSchemaValid(jsonSchema, json.toJSONString());
        if (!flag) {
            return R.failed("json schema 验证未通过");
        }
        log.info("id: {},json: {},jsonPath: {},jsonSchema: {}", id, "", jsonPath, jsonSchema);
        return R.ok(baseMapper.insertJson(id, jsonPath, json.toJSONString()));
    }
}
