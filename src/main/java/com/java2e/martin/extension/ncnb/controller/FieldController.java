package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.Field;
import com.java2e.martin.extension.ncnb.service.FieldService;
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
 * 字段  路由
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
public class FieldController{

    @Autowired
    private FieldService fieldService;

    @ApiOperation(value = "字段 ", nickname = "create", notes = "新增字段 ", tags = {"field",})
    @RequestMapping(value = "/field", method = RequestMethod.POST)
    @MartinLog("添加字段 ")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody Field field) {
        return R.ok(fieldService.save(field));
    }

    @ApiOperation(value = "字段 ", nickname = "delete", notes = "删除字段 ", tags = {"field",})
    @RequestMapping(value = "/field/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除字段 ")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(fieldService.removeById(id));
    }

    @ApiOperation(value = "字段 ", nickname = "list", notes = "分页查询字段 ", tags = {"field",})
    @RequestMapping(value = "/field", method = RequestMethod.GET)
    @MartinLog("分页查询字段 ")
    @SneakyThrows
    public R list(@RequestParam Map<String,Object> map) {
        return R.ok(fieldService.getPage(map));
    }

    @ApiOperation(value = "字段 ", nickname = "multipleDelete", notes = "批量删除字段 ", tags = {"field",})
    @RequestMapping(value = "/field/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除字段 ")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete field) {
        return R.ok(fieldService.removeByIds(field.getKeys()));
    }

    @ApiOperation(value = "字段 ", nickname = "partialUpdate", notes = "编辑字段 ", tags = {"field",})
    @RequestMapping(value = "/field/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑字段 ")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody Field field) {
        return R.ok(fieldService.updateById(field));
    }

    @ApiOperation(value = "字段 ", nickname = "read", notes = "获取单个字段 ", tags = {"field",})
    @RequestMapping(value = "/field/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个字段 ")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(fieldService.getById(id));
    }

    @ApiOperation(value = "字段 ", nickname = "tree", notes = "获取字段 树", tags = {"field",})
    @RequestMapping(value = "/field/tree", method = RequestMethod.GET)
    @MartinLog("获取字段 树")
    public R tree(@ApiParam(value = "", required = true) Field field) {
        return R.ok(fieldService.tree(field));
    }

    @ApiOperation(value = "字段 ", nickname = "update", notes = "修改字段 ", tags = {"field",})
    @RequestMapping(value = "/field/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑字段 ")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id")  String id,  @ApiParam(value = "", required = true) @Valid @RequestBody  Field field) {
        LambdaQueryWrapper<Field> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Field::getId, id);
        return R.ok(fieldService.update(field,wrapper));
    }
}

