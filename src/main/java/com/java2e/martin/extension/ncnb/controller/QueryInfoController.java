package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.dynamic.annotation.Dynamic;
import com.java2e.martin.common.log.annotation.MartinLog;
import com.java2e.martin.extension.ncnb.command.DbSqlExecCommand;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.QueryInfo;
import com.java2e.martin.extension.ncnb.service.QueryInfoService;
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
 * sql信息表  路由
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping
public class QueryInfoController {

    @Autowired
    private QueryInfoService queryInfoService;

    @ApiOperation(value = "sql信息表 ", nickname = "create", notes = "新增sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo", method = RequestMethod.POST)
    @MartinLog("添加sql信息表 ")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody QueryInfo queryInfo) {
        return R.ok(queryInfoService.save(queryInfo));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "delete", notes = "删除sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除sql信息表 ")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(queryInfoService.removeById(id));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "list", notes = "分页查询sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo", method = RequestMethod.GET)
    @MartinLog("分页查询sql信息表 ")
    @SneakyThrows
    public R list(@RequestParam Map<String,Object> map) {
        return R.ok(queryInfoService.getPage(map));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "multipleDelete", notes = "批量删除sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除sql信息表 ")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete queryInfo) {
        return R.ok(queryInfoService.removeByIds(queryInfo.getKeys()));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "partialUpdate", notes = "编辑sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑sql信息表 ")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody QueryInfo queryInfo) {
        return R.ok(queryInfoService.updateById(queryInfo));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "read", notes = "获取单个sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个sql信息表 ")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(queryInfoService.getById(id));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "tree", notes = "获取sql信息表 树", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/tree", method = RequestMethod.GET)
    @MartinLog("获取sql信息表 树")
    public R tree(@ApiParam(value = "", required = true) QueryInfo queryInfo) {
        return R.ok(queryInfoService.tree(queryInfo));
    }

    @ApiOperation(value = "sql信息表 ", nickname = "update", notes = "修改sql信息表 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑sql信息表 ")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody QueryInfo queryInfo) {
        LambdaQueryWrapper<QueryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QueryInfo::getId, id);
        return R.ok(queryInfoService.update(queryInfo, wrapper));
    }


    @ApiOperation(value = "执行sql ", nickname = "exec", notes = "执行sql ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/exec", method = RequestMethod.POST)
    @MartinLog("执行sql ")
    @Dynamic
    public R exec(@RequestBody Map map) {
        return queryInfoService.exec(map);
    }


    @ApiOperation(value = "分析sql执行计划", nickname = "explain", notes = "分析sql执行计划 ", tags = {"queryInfo",})
    @RequestMapping(value = "/queryInfo/explain", method = RequestMethod.POST)
    @MartinLog("分析sql执行计划 ")
    @Dynamic
    public R explain(@RequestBody Map map) {
        return queryInfoService.explain(map);
    }
}

