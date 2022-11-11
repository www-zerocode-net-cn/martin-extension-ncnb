package com.java2e.martin.extension.ncnb.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.api.dto.ProjectUserDto;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.common.api.dto.UserDto;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.OssConstants;
import com.java2e.martin.common.oss.service.OssTemplate;
import com.java2e.martin.common.security.userdetail.MartinUser;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.dto.ProjectDto;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.Query;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * 团队项目控制器
 * </p>
 *
 * @author 狮少
 * @since 2022-11-11
 */
@Slf4j
@RestController
@RequestMapping("/project/group")
public class GroupProjectController {

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
    @PostMapping("/add")
    public R add(@RequestBody ProjectDto projectDto) {
        return projectService.initGroupProject(projectDto);
    }

    /**
     * 保存
     *
     * @param  projectDto
     * @return R
     */
    @SneakyThrows
    @PostMapping("/save")
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
        return projectService.groupProjectPage(params);
    }

    @PostMapping("upload")
    public R uploadTest(@RequestParam("file") MultipartFile file) {
        return R.ok(ossTemplate.upload(OssConstants.DEFAULT_BUCKET, file, true));
    }

    /**
     * 获取项目全部角色
     *
     * @param id String
     * @return Project
     */
    @GetMapping("/roles")
    public R roles(@RequestParam String projectId) {
        return projectService.roles(projectId);
    }

    /**
     * 获取项目指定角色的用户
     *
     * @param id String
     * @return Project
     */
    @GetMapping("/role/users")
    public R roleUsers(@Validated ProjectUserDto projectUserDto) {
        return projectService.roleUsers(projectUserDto);
    }

    /**
     * 保存角色下的用户
     *
     * @param id String
     * @return Project
     */
    @PostMapping("/role/users")
    public R saveRoleUsers(@Validated @RequestBody RoleUserDto roleUserDto) {
        return projectService.saveRoleUsers(roleUserDto);
    }

    /**
     * 删除角色下的用户
     *
     * @param id String
     * @return Project
     */
    @DeleteMapping("/role/users")
    public R delRoleUsers(@Validated @RequestBody RoleUserDto roleUserDto) {
        return projectService.delRoleUsers(roleUserDto);
    }


    /**
     * 获取角色权限
     *
     * @param id String
     * @return Project
     */
    @GetMapping("/role/permission")
    public R rolePermission(@RequestParam("roleId") String roleId) {
        return projectService.rolePermission(roleId);
    }


    /**
     * 获取系统中的用户
     *
     * @param id String
     * @return Project
     */
    @GetMapping("/users")
    public R users(@Validated ProjectUserDto projectUserDto) {
        return projectService.users(projectUserDto);
    }

    /**
     * 用户注册
     *
     * @param userDto
     * @return
     */
    @PostMapping("/user/register")
    public R users(@Validated @RequestBody UserDto userDto) {
        return projectService.userRegister(userDto);
    }


}
