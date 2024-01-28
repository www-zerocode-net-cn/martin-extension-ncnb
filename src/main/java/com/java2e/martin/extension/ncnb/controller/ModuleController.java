package com.java2e.martin.extension.ncnb.controller;

import com.alibaba.fastjson.JSONObject;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.ProjectConstants;
import com.java2e.martin.common.log.annotation.MartinLog;
import com.java2e.martin.extension.ncnb.service.JsonBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import com.java2e.martin.extension.ncnb.entity.Module;
import com.java2e.martin.extension.ncnb.service.ModuleService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * <p>
 * 模块 路由
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-04
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping
@Api(value = "Module 控制器", tags = "元数据分组API")
public class ModuleController {
    @Autowired
    private JsonBaseService jsonBaseService;

    @ApiOperation(value = "新增模块", nickname = "create", notes = "新增模块", tags = {"module",})
    @RequestMapping(value = "/project/{id}/module", method = RequestMethod.POST)
    @MartinLog("新增模块")
    public R create(@ApiParam(value = "id", required = true) @PathVariable("id") String id,
                    @ApiParam(value = "json", required = true) @Valid @RequestBody JSONObject json) {
        return jsonBaseService.insertJson(id, json, ProjectConstants.PROJECT_MODULE_PATH,ProjectConstants.PROJECT_MODULE_SCHEMA);
    }

    @ApiOperation(value = "删除模块", nickname = "delete", notes = "删除模块", tags = {"module",})
    @RequestMapping(value = "/project/{id}/module", method = RequestMethod.DELETE)
    @MartinLog("删除模块")
    public R delete(@ApiParam(value = "id", required = true) @PathVariable("id") String id,
                    @ApiParam(value = "name", required = true) @RequestParam("name") String name,
                    @ApiParam(value = "path", required = false) @RequestParam(value = "path",required = false) String path) {
        return jsonBaseService.removeJson(id, name, path, ProjectConstants.PROJECT_MODULE_NAME_PATH);
    }

    @ApiOperation(value = "根据模块名称获取json path", nickname = "read", notes = "根据模块名称获取json path", tags = {"module",})
    @RequestMapping(value = "/project/{id}/module/path", method = RequestMethod.GET)
    @MartinLog("根据模块名称获取json path")
    public R getPathByName(@ApiParam(value = "id", required = true) @PathVariable("id") String id,
                           @ApiParam(value = "name", required = true) @RequestParam("name") String name) {
        return jsonBaseService.getPathByName(id, name, ProjectConstants.PROJECT_MODULE_NAME_PATH);
    }

    @ApiOperation(value = "根据模块名称获取单个模块json", nickname = "read", notes = "根据模块名称获取单个模块json", tags = {"module",})
    @RequestMapping(value = "/project/{id}/module/json", method = RequestMethod.GET)
    @MartinLog("根据模块名称获取单个模块json")
    public R getModuleByName(@ApiParam(value = "id", required = true) @PathVariable("id") String id,
                             @ApiParam(value = "name", required = true) @RequestParam("name") String name) {
        return jsonBaseService.getJsonByName(id, name, ProjectConstants.PROJECT_MODULE_NAME_PATH);
    }

    @ApiOperation(value = "修改模块Json", nickname = "update", notes = "修改模块Json", tags = {"module",})
    @RequestMapping(value = "/project/{id}/module", method = RequestMethod.PUT)
    @MartinLog("编辑模块Json")
    public R updateJson(@ApiParam(value = "id", required = true) @PathVariable("id") String id,
                        @ApiParam(value = "name", required = true) @RequestParam("name") String name,
                        @ApiParam(value = "json", required = true) @RequestBody JSONObject json,
                        @ApiParam(value = "path", required = false) @RequestParam(value = "path",required = false) String path) {
        return jsonBaseService.updateJson(id, name, path, json, ProjectConstants.PROJECT_MODULE_NAME_PATH,ProjectConstants.PROJECT_MODULE_SCHEMA);
    }

//    @ApiOperation(value = "模块", nickname = "update", notes = "修改模块属性", tags = {"module",})
//    @RequestMapping(value = "/project/{id}/module/value", method = RequestMethod.PUT)
//    @MartinLog("编辑模块属性")
//    public R update(@ApiParam(value = "id", required = true) @PathVariable("id") String id, @ApiParam(value = "module", required = true) @Valid @RequestBody Module module) {
//        return R.ok(jsonBaseService.updateValue(id, module, ProjectConstants.PROJECT_MODULE_NAME_PATH));
//    }
}

