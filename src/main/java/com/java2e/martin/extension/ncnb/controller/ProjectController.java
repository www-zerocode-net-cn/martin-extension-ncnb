package com.java2e.martin.extension.ncnb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import com.java2e.martin.extension.ncnb.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;


/**
 * <p>
 * PDMan全局配置表 前端控制器
 * </p>
 *
 * @author 狮少
 * @since 2020-10-26
 */
@Slf4j
@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 添加
     *
     * @param map Map
     * @return ExecResult
     */
    @PostMapping("/save")
    public R save(@RequestBody Map map) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        Object projectName = map.get("projectName");
        if (projectName == null) {
            return R.failed("projectName为空");
        }
        wrapper.eq("project_name", projectName);
        Project selectOne = projectService.getOne(wrapper);
        Project project = new Project();

        try {
            project.setProjectName(projectName.toString());
            project.setConfigJSON(JsonUtil.generate(map.get("configJSON")).getBytes());
            project.setProjectJSON(JsonUtil.generate(map.get("projectJSON")).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        if (selectOne == null) {
            return R.ok(projectService.save(project));
        } else {
            return R.ok(projectService.update(project, wrapper));
        }
    }

    @GetMapping("/info/{projectId}")
    public R projectService(@PathVariable String projectId) {
        return projectService.projectService(projectId);
    }


    /**
     * 删除
     *
     * @param project Project
     * @return ExecResult
     */
    @PostMapping("/delete")
    public R<Boolean> removeById(@RequestBody Project project) {
        return R.ok(projectService.removeById(project.getId()));
    }

    /**
     * 编辑
     *
     * @param project Project
     * @return ExecResult
     */
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody Project project) {
        project.setUpdatedTime(new Date());
        return R.ok(projectService.updateById(project));
    }

    /**
     * 通过ID查询
     *
     * @param id String
     * @return Project
     */
    @GetMapping("/get/{id}")
    public Project getById(@PathVariable String id) {
        return projectService.getById(id);
    }

    /**
     * 分页查询
     *
     * @param params 分页以及查询参数
     * @return Page
     */
    @PostMapping("/page")
    public IPage getPage(@RequestBody Map params) {
        return projectService.page(new Query<>(params));
    }


}

