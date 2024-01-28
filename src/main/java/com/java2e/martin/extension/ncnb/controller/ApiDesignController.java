package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.ApiDesign;
import com.java2e.martin.extension.ncnb.service.ApiDesignService;
import com.java2e.martin.extension.ncnb.util.Query;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.service.ApiDesignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;


/**
 * <p>
 * api设计表 前端控制器
 * </p>
 *
 * @author shishao
 * @version 1.0
 * @date 2020-12-10
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("apiDesign")
public class ApiDesignController {

    @Autowired
    private ApiDesignService apiDesignService;


    /**
     * 添加
     *
     * @param apiDesign ApiDesign
     * @return ExecResult
     */
    @PostMapping("/add")
    public R<Boolean> save(@Valid @RequestBody ApiDesign apiDesign) {
        return R.ok(apiDesignService.save(apiDesign));
    }

    /**
     * 删除
     *
     * @param apiDesign ApiDesign
     * @return ExecResult
     */
    @PostMapping("/delete")
    public R<Boolean> removeById(@Valid @RequestBody ApiDesign apiDesign) {
        return R.ok(apiDesignService.removeById(apiDesign.getId()));
    }

    /**
     * 编辑
     *
     * @param apiDesign ApiDesign
     * @return ExecResult
     */
    @PostMapping("/update")
    public R<Boolean> update(@Valid @RequestBody ApiDesign apiDesign) {
        return R.ok(apiDesignService.updateById(apiDesign));
    }

    /**
     * 通过ID查询
     *
     * @param id String
     * @return ApiDesign
     */
    @GetMapping("/get/{id}")
    public ApiDesign getById(@PathVariable String id) {
        return apiDesignService.getById(id);
    }

    /**
     * 分页查询
     *
     * @param params 分页以及查询参数
     * @return Page
     */
    @PostMapping("/page")
    public IPage getPage(@RequestBody Map params) {
        return apiDesignService.page(new Query<>(params));
    }


}

