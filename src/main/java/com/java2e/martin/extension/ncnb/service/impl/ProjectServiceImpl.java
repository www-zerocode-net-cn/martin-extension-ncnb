package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.api.dto.ProjectUserDto;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.common.api.system.RemoteSystemRole;
import com.java2e.martin.common.api.system.RemoteSystemUser;
import com.java2e.martin.common.bean.system.Role;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.ProjectConstants;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.entity.ProjectRole;
import com.java2e.martin.extension.ncnb.mapper.ProjectMapper;
import com.java2e.martin.extension.ncnb.service.ProjectRoleService;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
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
    public R initProject(Map map) {
        log.info("map: {}", map);
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        Object projectName = map.get("projectName");
        Object description = map.get("description");
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
        } else {
            Project project = new Project();
            BeanUtil.copyProperties(map, project);

            project.setConfigJSON(JsonUtil.generate(map.get("configJSON")).getBytes());
            project.setProjectJSON(JsonUtil.generate(map.get("projectJSON")).getBytes());
            boolean save = this.save(project);
            Object type = map.get("type");
            //初始化项目角色
            if (ObjectUtil.isNotNull(type) && ObjectUtil.equal(type, 2)) {
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
            }
            return R.ok(save);
        }
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
    protected void setEntity() {
        this.clz = Project.class;
    }
}
