package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.OssConstants;
import com.java2e.martin.common.oss.service.OssTemplate;
import com.java2e.martin.common.vip.annotation.VIP;
import com.java2e.martin.common.vip.enums.VIPLevelEnum;
import com.java2e.martin.common.vip.enums.VIPModuleEnum;
import com.java2e.martin.extension.ncnb.dto.ProjectDto;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.vip.rights.PersonProjectCountRight;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * <p>
 * 个人项目控制器
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
    private OssTemplate minioOssTemplate;

    /**
     * 添加
     *
     * @param projectDto
     * @return R
     */
    @PostMapping("/add")
    @VIP(module = VIPModuleEnum.ERD, vipLevel = {VIPLevelEnum.NONE, VIPLevelEnum.PRO}, rights = {PersonProjectCountRight.class}, reset = true)
    public R add(@RequestBody ProjectDto projectDto) {
        return projectService.initPersonProject(projectDto);
    }

    /**
     * 保存
     *
     * @param projectDto
     * @return R
     */
    @SneakyThrows
    @PostMapping("/save")
    @VIP(module = VIPModuleEnum.ERD, vipLevel = {VIPLevelEnum.NONE, VIPLevelEnum.PRO}, rights = {PersonProjectCountRight.class}, reset = false)
    public R save(@RequestBody ProjectDto projectDto) {
        return projectService.saveProject(projectDto);
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
    public R<Boolean> removeById(@RequestBody ProjectDto project) {
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
    public R getById(@PathVariable String id) {
        return R.ok(projectService.getById(id));
    }

    /**
     * 分页查询
     *
     * @param params 分页以及查询参数
     * @return Page
     */
    @GetMapping("/page")
    public R page(@RequestParam Map params) {
        return projectService.personProjectPage(params);
    }

    /**
     * 分页查询
     *
     * @param params 分页以及查询参数
     * @return Page
     */
    @GetMapping("/recent")
    public R recent(@RequestParam Map params) {
        return projectService.recentProjectPage(params);
    }

    @PostMapping("upload")
    public R uploadTest(@RequestParam("file") MultipartFile file) {
        return R.ok(minioOssTemplate.upload(OssConstants.DEFAULT_BUCKET, file, true));
    }


    /**
     * 查询统计信息
     *
     * @return Project
     */
    @GetMapping("/statistic")
    public R statistic() {
        return projectService.statistic();
    }
}

