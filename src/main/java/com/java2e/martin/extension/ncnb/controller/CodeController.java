package com.java2e.martin.extension.ncnb.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.common.core.api.ApiErrorCode;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import com.java2e.martin.extension.ncnb.entity.Code;
import com.java2e.martin.extension.ncnb.service.CodeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统代码生成表 前端控制器
 * </p>
 *
 * @author 狮少
 * @version 1.0
 * @date 2020-09-14
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/code")
@Api(value = "Code 控制器", tags = "系统代码生成表")
public class CodeController {

    @Autowired
    private CodeService codeService;


    /**
     * 添加
     *
     * @param code Code
     * @return R
     */
    @MartinLog("添加系统代码生成表")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('sys_code_add')")
    public R save(@Valid @RequestBody Code code) {
        return R.ok(codeService.save(code));
    }

    /**
     * 删除
     *
     * @param code Code
     * @return R
     */
    @MartinLog("删除系统代码生成表")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys_code_del')")
    public R removeById(@Valid @RequestBody Code code) {
        return R.ok(codeService.removeById(code.getId()));
    }

    /**
     * 编辑
     *
     * @param code Code
     * @return R
     */
    @MartinLog("编辑系统代码生成表")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys_code_edit')")
    public R update(@Valid @RequestBody Code code) {
        return R.ok(codeService.updateById(code));
    }

    /**
     * 通过ID查询
     *
     * @param code Code
     * @return R
     */
    @MartinLog("单个查询系统代码生成表")
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('sys_code_get')")
    public R getById(@RequestBody Code code) {
        return R.ok(codeService.getById(code.getId()));
    }

    /**
     * 分页查询
     *
     * @param params 分页以及查询参数
     * @return R
     */
    @MartinLog("分页查询系统代码生成表")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('sys_code_page')")
    public R<IPage> getPage(@RequestBody Map params) {
        try {
            return R.ok(codeService.getPage(params));
        } catch (IllegalAccessException e) {
            log.error("", e);
            return R.failed(ApiErrorCode.FAIL);
        } catch (InstantiationException e) {
            log.error("", e);
            return R.failed(ApiErrorCode.FAIL);
        }
    }

    @MartinLog("批量删除系统代码生成表")
    @PostMapping("/deleteBatch")
    @PreAuthorize("hasAuthority('sys_code_deleteBatch')")
    public R removeBatch(@RequestBody String ids) {
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        if (CollUtil.isEmpty(idList)) {
            return R.failed("id 不能为空");
        }
        return R.ok(codeService.removeByIds(idList));
    }

    @MartinLog("生成前后端代码")
    @PostMapping("/generateCode")
    public R generateCode(@RequestBody Code code, HttpServletResponse response) {
        return codeService.generateCode(code, response);
    }

}

