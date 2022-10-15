package com.java2e.martin.extension.ncnb.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.OssConstants;
import com.java2e.martin.common.oss.service.OssTemplate;
import com.java2e.martin.common.security.userdetail.MartinUser;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.service.WsService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import com.java2e.martin.extension.ncnb.util.Query;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.InsertProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private OssTemplate ossTemplate;

    /**
     * 添加
     *
     * @param map Map
     * @return ExecResult
     */
    @SneakyThrows
    @PostMapping("/add")
    public R add(@RequestBody Map map) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        Object projectName = map.get("projectName");
        Object description = map.get("description");
        if (projectName == null) {
            return R.failed("项目名为空");
        }
        if (description == null) {
            return R.failed("项目描述为空");
        }
        wrapper.eq("project_name", projectName);
        Project selectOne = projectService.getOne(wrapper);
        Project project = new Project();
        BeanUtil.copyProperties(map,project);

        project.setConfigJSON(JsonUtil.generate(map.get("configJSON")).getBytes());
        project.setProjectJSON(JsonUtil.generate(map.get("projectJSON")).getBytes());

        if (selectOne == null) {
            boolean save = projectService.save(project);
            return R.ok(save);
        } else {
            return R.failed("项目「" + projectName + "」已存在");
        }
    }

    /**
     * 保存
     *
     * @param map Map
     * @return ExecResult
     */
    @SneakyThrows
    @PostMapping("/save")
    public R save(@RequestBody Map map) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        String id = (String) map.get("id");
        if (StrUtil.isBlank(id)) {
            return R.failed("id为空");
        }
        wrapper.eq("id", id);
        Project selectOne = projectService.getOne(wrapper);
        Project project = new Project();
        BeanUtil.copyProperties(map,project);

        project.setConfigJSON(JsonUtil.generate(map.get("configJSON")).getBytes());
        project.setProjectJSON(JsonUtil.generate(map.get("projectJSON")).getBytes());

        boolean update = projectService.update(project, wrapper);
        return R.ok(update);
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
    @GetMapping("/page")
    public R page(@RequestParam Map params) {
        String order = params.get("order").toString();
        LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StrUtil.equals(order, "createTime")) {
            lambdaQueryWrapper.orderByDesc(Project::getCreateTime);
        } else if (StrUtil.equals(order, "updateTime")) {
            lambdaQueryWrapper.orderByDesc(Project::getUpdateTime);
        }
        MartinUser accessUser = SecurityContextUtil.getAccessUser();
        String userId = accessUser.getId();
        log.info("userId:{}", userId);
        String  projectName = (String) params.get("projectName");
        lambdaQueryWrapper.eq(Project::getCreator, userId);
        if (StrUtil.isNotBlank(projectName)){
            lambdaQueryWrapper.like(Project::getProjectName, projectName);
        }
        return R.ok(projectService.page(new Query<>(params), lambdaQueryWrapper));
    }

    @PostMapping("upload")
    public R uploadTest(@RequestParam("file") MultipartFile file) {
        return R.ok(ossTemplate.upload(OssConstants.DEFAULT_BUCKET, file, true));
    }

}

