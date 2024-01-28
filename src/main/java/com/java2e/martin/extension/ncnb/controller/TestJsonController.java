package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.mybatis.config.UpdateJsonWrapper;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.TestJson;
import com.java2e.martin.extension.ncnb.service.TestJsonService;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



/**
 * <p>
 *  路由
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-02-26
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping
public class TestJsonController{

    @Autowired
    private TestJsonService testJsonService;

    @ApiOperation(value = "", nickname = "create", notes = "新增", tags = {"testJson",})
    @RequestMapping(value = "/testJson", method = RequestMethod.POST)
    @MartinLog("添加")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody TestJson testJson) {
        return R.ok(testJsonService.save(testJson));
    }

    @ApiOperation(value = "", nickname = "delete", notes = "删除", tags = {"testJson",})
    @RequestMapping(value = "/testJson/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(testJsonService.removeById(id));
    }

    @ApiOperation(value = "", nickname = "list", notes = "分页查询", tags = {"testJson",})
    @RequestMapping(value = "/testJson", method = RequestMethod.GET)
    @MartinLog("分页查询")
    @SneakyThrows
    public R list(@RequestParam Map<String,Object> map) {
        return R.ok(testJsonService.getPage(map));
    }

    @ApiOperation(value = "", nickname = "multipleDelete", notes = "批量删除", tags = {"testJson",})
    @RequestMapping(value = "/testJson/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete testJson) {
        return R.ok(testJsonService.removeByIds(testJson.getKeys()));
    }

    @ApiOperation(value = "", nickname = "partialUpdate", notes = "编辑", tags = {"testJson",})
    @RequestMapping(value = "/testJson/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody TestJson testJson) {
        return R.ok(testJsonService.updateById(testJson));
    }

    @ApiOperation(value = "", nickname = "read", notes = "获取单个", tags = {"testJson",})
    @RequestMapping(value = "/testJson/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(testJsonService.getById(id));
    }

    @ApiOperation(value = "", nickname = "tree", notes = "获取树", tags = {"testJson",})
    @RequestMapping(value = "/testJson/tree", method = RequestMethod.GET)
    @MartinLog("获取树")
    public R tree(@ApiParam(value = "", required = true) TestJson testJson) {
        return R.ok(testJsonService.tree(testJson));
    }

    @ApiOperation(value = "", nickname = "update", notes = "修改", tags = {"testJson",})
    @RequestMapping(value = "/testJson/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id")  String id,  @ApiParam(value = "", required = true) @Valid @RequestBody  TestJson testJson) {
        UpdateJsonWrapper<TestJson> wrapper = new UpdateJsonWrapper();
        wrapper.set("test", "$.a",testJson.getTest());
        wrapper.eq("id", "1");
        return R.ok(testJsonService.update(wrapper));
    }
}

