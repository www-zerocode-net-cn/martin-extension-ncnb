package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.Datatype;
import com.java2e.martin.extension.ncnb.service.DatatypeService;
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
 * 数据域 路由
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
public class DatatypeController{

    @Autowired
    private DatatypeService datatypeService;

    @ApiOperation(value = "数据域", nickname = "create", notes = "新增数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype", method = RequestMethod.POST)
    @MartinLog("添加数据域")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody Datatype datatype) {
        return R.ok(datatypeService.save(datatype));
    }

    @ApiOperation(value = "数据域", nickname = "delete", notes = "删除数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除数据域")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(datatypeService.removeById(id));
    }

    @ApiOperation(value = "数据域", nickname = "list", notes = "分页查询数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype", method = RequestMethod.GET)
    @MartinLog("分页查询数据域")
    @SneakyThrows
    public R list(@RequestParam Map<String,Object> map) {
        return R.ok(datatypeService.getPage(map));
    }

    @ApiOperation(value = "数据域", nickname = "multipleDelete", notes = "批量删除数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除数据域")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete datatype) {
        return R.ok(datatypeService.removeByIds(datatype.getKeys()));
    }

    @ApiOperation(value = "数据域", nickname = "partialUpdate", notes = "编辑数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑数据域")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody Datatype datatype) {
        return R.ok(datatypeService.updateById(datatype));
    }

    @ApiOperation(value = "数据域", nickname = "read", notes = "获取单个数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个数据域")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(datatypeService.getById(id));
    }

    @ApiOperation(value = "数据域", nickname = "tree", notes = "获取数据域树", tags = {"datatype",})
    @RequestMapping(value = "/datatype/tree", method = RequestMethod.GET)
    @MartinLog("获取数据域树")
    public R tree(@ApiParam(value = "", required = true) Datatype datatype) {
        return R.ok(datatypeService.tree(datatype));
    }

    @ApiOperation(value = "数据域", nickname = "update", notes = "修改数据域", tags = {"datatype",})
    @RequestMapping(value = "/datatype/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑数据域")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id")  String id,  @ApiParam(value = "", required = true) @Valid @RequestBody  Datatype datatype) {
        LambdaQueryWrapper<Datatype> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Datatype::getId, id);
        return R.ok(datatypeService.update(datatype,wrapper));
    }
}

