package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.api.dto.ProjectUserDto;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.common.api.dto.UserDto;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.common.core.api.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-26
 */
@Transactional(rollbackFor = Exception.class)
public interface ProjectService extends MartinService<Project> {

    /**
     * 根据id查询project信息
     *
     * @param projectId
     * @return
     */
    R projectService(String projectId);

    /**
     * 新增一个项目,团队项目需要初始化角色信息
     *
     * @param map
     * @return
     */
    R initProject(Map map);

    /**
     * 获取项目全部角色
     *
     * @param id
     * @return
     */
    R roles(String id);


    /**
     * 分页获取项目某个角色下的用户
     *
     * @param projectUserDto
     * @return
     */
    R roleUsers(ProjectUserDto projectUserDto);

    /**
     * 分页获取系统中的的用户
     *
     * @param projectUserDto
     * @return
     */
    R users(ProjectUserDto projectUserDto);

    /**
     * 保存角色用户
     *
     * @param roleUserDto
     * @return
     */
    R saveRoleUsers(RoleUserDto roleUserDto);

    /**
     * 删除角色用户
     *
     * @param roleUserDto
     * @return
     */
    R delRoleUsers(RoleUserDto roleUserDto);

    /**
     * 注册新用户
     *
     * @param userDto
     * @return
     */
    R userRegister(UserDto userDto);

    /**
     * 获取角色权限
     * @param roleId
     * @return
     */
    R rolePermission(String roleId);
}
