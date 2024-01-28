package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.QueryHistory;
import com.java2e.martin.extension.ncnb.service.QueryHistoryService;
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
 * sql执行记录表  路由
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-03
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping
public class QueryHistoryController{

    @Autowired
    private QueryHistoryService queryHistoryService;

    @ApiOperation(value = "sql执行记录表 ", nickname = "create", notes = "新增sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory", method = RequestMethod.POST)
    @MartinLog("添加sql执行记录表 ")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody QueryHistory queryHistory) {
        return R.ok(queryHistoryService.save(queryHistory));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "delete", notes = "删除sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除sql执行记录表 ")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(queryHistoryService.removeById(id));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "list", notes = "分页查询sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory", method = RequestMethod.GET)
    @MartinLog("分页查询sql执行记录表 ")
    @SneakyThrows
    public R list(@RequestParam Map<String,Object> map) {
        return R.ok(queryHistoryService.getPage(map));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "multipleDelete", notes = "批量删除sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除sql执行记录表 ")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete queryHistory) {
        return R.ok(queryHistoryService.removeByIds(queryHistory.getKeys()));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "partialUpdate", notes = "编辑sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑sql执行记录表 ")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody QueryHistory queryHistory) {
        return R.ok(queryHistoryService.updateById(queryHistory));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "read", notes = "获取单个sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个sql执行记录表 ")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(queryHistoryService.getById(id));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "tree", notes = "获取sql执行记录表 树", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory/tree", method = RequestMethod.GET)
    @MartinLog("获取sql执行记录表 树")
    public R tree(@ApiParam(value = "", required = true) QueryHistory queryHistory) {
        return R.ok(queryHistoryService.tree(queryHistory));
    }

    @ApiOperation(value = "sql执行记录表 ", nickname = "update", notes = "修改sql执行记录表 ", tags = {"queryHistory",})
    @RequestMapping(value = "/queryHistory/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑sql执行记录表 ")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id")  String id,  @ApiParam(value = "", required = true) @Valid @RequestBody  QueryHistory queryHistory) {
        LambdaQueryWrapper<QueryHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QueryHistory::getId, id);
        return R.ok(queryHistoryService.update(queryHistory,wrapper));
    }
}

