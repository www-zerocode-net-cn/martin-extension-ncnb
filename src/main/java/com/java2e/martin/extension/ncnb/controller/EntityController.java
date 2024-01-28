package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.Entity;
import com.java2e.martin.extension.ncnb.service.EntityService;
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
 * 元数据 路由
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
public class EntityController{

    @Autowired
    private EntityService entityService;

    @ApiOperation(value = "元数据", nickname = "create", notes = "新增元数据", tags = {"entity",})
    @RequestMapping(value = "/entity", method = RequestMethod.POST)
    @MartinLog("添加元数据")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody Entity entity) {
        return R.ok(entityService.save(entity));
    }

    @ApiOperation(value = "元数据", nickname = "delete", notes = "删除元数据", tags = {"entity",})
    @RequestMapping(value = "/entity/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除元数据")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(entityService.removeById(id));
    }

    @ApiOperation(value = "元数据", nickname = "list", notes = "分页查询元数据", tags = {"entity",})
    @RequestMapping(value = "/entity", method = RequestMethod.GET)
    @MartinLog("分页查询元数据")
    @SneakyThrows
    public R list(@RequestParam Map<String,Object> map) {
        return R.ok(entityService.getPage(map));
    }

    @ApiOperation(value = "元数据", nickname = "multipleDelete", notes = "批量删除元数据", tags = {"entity",})
    @RequestMapping(value = "/entity/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除元数据")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete entity) {
        return R.ok(entityService.removeByIds(entity.getKeys()));
    }

    @ApiOperation(value = "元数据", nickname = "partialUpdate", notes = "编辑元数据", tags = {"entity",})
    @RequestMapping(value = "/entity/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑元数据")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody Entity entity) {
        return R.ok(entityService.updateById(entity));
    }

    @ApiOperation(value = "元数据", nickname = "read", notes = "获取单个元数据", tags = {"entity",})
    @RequestMapping(value = "/entity/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个元数据")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(entityService.getById(id));
    }

    @ApiOperation(value = "元数据", nickname = "tree", notes = "获取元数据树", tags = {"entity",})
    @RequestMapping(value = "/entity/tree", method = RequestMethod.GET)
    @MartinLog("获取元数据树")
    public R tree(@ApiParam(value = "", required = true) Entity entity) {
        return R.ok(entityService.tree(entity));
    }

    @ApiOperation(value = "元数据", nickname = "update", notes = "修改元数据", tags = {"entity",})
    @RequestMapping(value = "/entity/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑元数据")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id")  String id,  @ApiParam(value = "", required = true) @Valid @RequestBody  Entity entity) {
        LambdaQueryWrapper<Entity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Entity::getId, id);
        return R.ok(entityService.update(entity,wrapper));
    }
}

