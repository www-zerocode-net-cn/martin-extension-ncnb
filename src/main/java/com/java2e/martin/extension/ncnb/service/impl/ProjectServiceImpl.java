package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java2e.martin.common.api.dto.ProjectUserDto;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.common.api.dto.UserDto;
import com.java2e.martin.common.api.system.RemoteSystemRole;
import com.java2e.martin.common.api.system.RemoteSystemUser;
import com.java2e.martin.common.bean.system.Role;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.ProjectConstants;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.common.security.userdetail.MartinUser;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.dto.ProjectDto;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.entity.ProjectRole;
import com.java2e.martin.extension.ncnb.mapper.ProjectMapper;
import com.java2e.martin.extension.ncnb.service.ProjectRoleService;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import com.java2e.martin.extension.ncnb.util.Query;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * PDMan全局配置表 服务实现类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-26
 */
@Slf4j
@Service
public class ProjectServiceImpl extends MartinServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private RemoteSystemRole remoteSystemRole;

    @Autowired
    private RemoteSystemUser remoteSystemUser;

    @Autowired
    private ProjectRoleService projectRoleService;


    @Override
    public R projectService(String projectId) {
        log.info("projectId: {}", projectId);
        Project project = this.getById(projectId);
        HashMap<String, Object> map = new HashMap<>(3);
        try {
            map.put("configJSON", JsonUtil.parse(new String(project.getConfigJSON()), Map.class));
            map.put("projectJSON", JsonUtil.parse(new String(project.getProjectJSON()), Map.class));
            map.put("projectName", project.getProjectName());
        } catch (Exception e) {
            log.error("", e);
            return R.failed(e.getMessage());
        }
        return R.ok(map);
    }

    @SneakyThrows
    @Override
    public R initPersonProject(ProjectDto projectDto) {
        log.info("projectDto: {}", projectDto);
        R check = check(projectDto);
        if (check.invalid()) {
            return check;
        }
        Project project = new Project();
        project.setType(ProjectConstants.PERSON_PROJECT_FLAG);
        saveProject(projectDto, project);
        return R.ok("新建个人项目成功");
    }

    @Override
    public R roles(String id) {
        log.info("id: {}", id);
        if (StrUtil.isBlank(id)) {
            return R.failed("项目标识为空");
        }
        LambdaQueryWrapper<ProjectRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectRole::getProjectId, id);
        return R.ok(projectRoleService.list(queryWrapper));
    }

    @Override
    public R roleUsers(ProjectUserDto projectUserDto) {
        log.info("projectUserDto: {}", projectUserDto);
        R result = remoteSystemRole.roleUsers(projectUserDto);
        if (result.valid()) {
            return R.ok(result.getData());
        } else {
            return R.failed("获取角色下的用户失败");
        }
    }

    @Override
    public R users(ProjectUserDto projectUserDto) {
        log.info("projectUserDto: {}", projectUserDto);
        R result = remoteSystemUser.users(projectUserDto);
        log.info("result: {}", result);
        if (result.valid()) {
            return R.ok(result.getData());
        } else {
            return R.failed("获取用户失败");
        }
    }

    @Override
    public R saveRoleUsers(RoleUserDto roleUserDto) {
        log.info("roleUserDto: {}", roleUserDto);
        R result = remoteSystemRole.saveRoleUsers(roleUserDto);
        if (result.valid()) {
            return R.ok(result.getData());
        } else {
            return R.failed("获取用户失败");
        }
    }

    @Override
    public R delRoleUsers(RoleUserDto roleUserDto) {
        log.info("roleUserDto: {}", roleUserDto);
        R result = remoteSystemRole.delRoleUsers(roleUserDto);
        if (result.valid()) {
            return R.ok(result.getData());
        } else {
            return R.failed("删除角色下的用户失败");
        }
    }

    @Override
    public R userRegister(UserDto userDto) {
        log.info("userDto: {}", userDto);
        R result = remoteSystemUser.userRegister(userDto);
        if (result.valid()) {
            return R.ok(result.getData());
        } else {
            return R.failed("注册用户失败");
        }
    }

    @Override
    public R rolePermission(String roleId) {
        if (StrUtil.isBlank(roleId)) {
            return R.failed("roleId为空");
        }
        log.info("roleId: {}", roleId);
        R result = remoteSystemRole.rolePermission(roleId);
        log.info("result: {}", result);
        if (result.valid()) {
            return R.ok(result.getData());
        } else {
            return R.failed("获取角色权限失败");
        }
    }

    @SneakyThrows
    @Override
    public R initGroupProject(ProjectDto projectDto) {
        log.info("projectDto: {}", projectDto);
        R check = check(projectDto);
        if (check.invalid()) {
            return check;
        }
        Project project = new Project();
        project.setType(ProjectConstants.GROUP_PROJECT_FLAG);
        //保存项目
        saveProject(projectDto, project);
        //初始化团队项目角色
        log.info("初始化项目角色");
        List<Role> roles = Arrays.stream(ProjectConstants.ROLE_NAME).map(f -> {
            Role role = new Role();
            role.setRoleName(StrUtil.split(f, ":")[0]);
            role.setRoleCode(ProjectConstants.buildRoleCode(StrUtil.split(f, ":")[1], project.getId()));
            return role;
        }).collect(Collectors.toList());
        R<List<Role>> register = remoteSystemRole.register(roles);
        if (register.valid()) {
            List<ProjectRole> projectRoles = register.getData().stream().map(f -> {
                ProjectRole projectRole = new ProjectRole();
                projectRole.setProjectId(project.getId());
                projectRole.setRoleId(f.getId());
                projectRole.setRoleName(f.getRoleName());
                projectRole.setRoleCode(f.getRoleCode());
                return projectRole;
            }).collect(Collectors.toList());
            projectRoleService.saveBatch(projectRoles);
        } else {
            return R.failed("新建团队项目失败");
        }
        return R.ok("新建团队项目成功");

    }

    /**
     * 保存项目
     *
     * @param projectDto
     * @param project
     * @return
     * @throws JsonProcessingException
     */
    private boolean saveProject(ProjectDto projectDto, Project project) throws JsonProcessingException {
        configProject(projectDto, project);
        return this.save(project);
    }

    /**
     * 配置Project属性
     *
     * @param projectDto
     * @param project
     * @throws JsonProcessingException
     */
    public void configProject(ProjectDto projectDto, Project project) throws JsonProcessingException {
        BeanUtil.copyProperties(projectDto, project);

        project.setConfigJSON(JsonUtil.generate(projectDto.getConfigJSON()).getBytes());
        project.setProjectJSON(JsonUtil.generate(projectDto.getProjectJSON()).getBytes());
    }

    @SneakyThrows
    @Override
    public R saveProject(ProjectDto projectDto) {
        log.info("projectDto: {}", projectDto);
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        String id = projectDto.getId();
        if (StrUtil.isBlank(id)) {
            return R.failed("id为空");
        }
        wrapper.eq("id", id);
        Project project = new Project();
        this.configProject(projectDto, project);

        boolean update = this.update(project, wrapper);
        return R.ok(update);
    }

    @Override
    public R groupProjectPage(Map params) {
        LambdaQueryWrapper<Project> lambdaQueryWrapper = prepareProjectLambdaQueryWrapper(params);
        lambdaQueryWrapper.eq(Project::getType, ProjectConstants.GROUP_PROJECT_FLAG);
        return R.ok(this.page(new Query<>(params), lambdaQueryWrapper));
    }

    @Override
    public R personProjectPage(Map params) {
        LambdaQueryWrapper<Project> lambdaQueryWrapper = prepareProjectLambdaQueryWrapper(params);
        lambdaQueryWrapper.eq(Project::getType, ProjectConstants.PERSON_PROJECT_FLAG);
        return R.ok(this.page(new Query<>(params), lambdaQueryWrapper));
    }

    /**
     * 组装查询需要的参数
     *
     * @param params
     * @return
     */
    private LambdaQueryWrapper<Project> prepareProjectLambdaQueryWrapper(Map params) {
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
        String projectName = (String) params.get("projectName");
        lambdaQueryWrapper.eq(Project::getCreator, userId);
        if (StrUtil.isNotBlank(projectName)) {
            lambdaQueryWrapper.like(Project::getProjectName, projectName);
        }
        lambdaQueryWrapper.select(Project::getId, Project::getProjectName, Project::getDescription, Project::getTags, Project::getType);
        return lambdaQueryWrapper;
    }


    private R check(ProjectDto projectDto) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        Object projectName = projectDto.getProjectName();
        Object description = projectDto.getDescription();
        if (projectName == null) {
            return R.failed("项目名为空");
        }
        if (description == null) {
            return R.failed("项目描述为空");
        }
        wrapper.eq(Project::getProjectName, projectName);
        Project selectOne = this.getOne(wrapper);

        if (selectOne != null) {
            return R.failed("项目「" + projectName + "」已存在");
        }
        return R.ok("");
    }

    @Override
    protected void setEntity() {
        this.clz = Project.class;
    }
}
