package com.java2e.martin.extension.ncnb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java2e.martin.common.api.dto.ProjectUserDto;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.common.api.dto.UserDto;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.dto.ProjectDto;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.common.core.api.R;
import io.swagger.models.auth.In;
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
    R initPersonProject(ProjectDto map);

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
     *
     * @param roleId
     * @param projectId
     * @return
     */
    R rolePermission(String roleId, String projectId);

    /**
     * 新增团队项目
     *
     * @param map
     * @return
     */
    R initGroupProject(ProjectDto projectDto);

    /**
     * 设置Project属性
     *
     * @param projectDto
     * @param project
     * @throws JsonProcessingException
     */
    void configProject(ProjectDto projectDto, Project project) throws JsonProcessingException;

    /**
     * 保存团队项目
     *
     * @param projectDto
     * @return
     */
    R saveProject(ProjectDto projectDto);

    /**
     * 查询团队列表分页
     *
     * @param params
     * @return
     */
    R groupProjectPage(Map params);

    /**
     * 查询个人项目
     *
     * @param params
     * @return
     */
    R personProjectPage(Map params);

    /**
     * 统计个人项目数量
     *
     * @param params
     * @return
     */
    R<Integer> personProjectCount();

    /**
     * 最近项目
     *
     * @param params
     * @return
     */
    R recentProjectPage(Map params);

    /**
     * 保存权限
     *
     * @param map
     * @return
     */
    R saveCheckedOperations(Map map);

    /**
     * 查询统计信息
     *
     * @return
     */
    R statistic();

    /**
     * 获取当前用户在项目中的最大角色
     *
     * @param projectId
     * @return
     */
    R currentRolePermission(String projectId);

    /**
     * 获取当前项目可以审批sql的人员，获取角色为管理员和项目拥有者
     *
     * @param projectId
     * @return
     */
    R getApprovalUsers(String projectId);

    /**
     * 统计团队项目数量
     *
     * @return
     */
    R<Integer> groupProjectCount();

    /**
     * 统计项目版本数量
     * @return
     */
    R<Integer> projectVersionCount();

    /**
     * 统计注册人数
     * @return
     */
    R<Integer> personCount();

}
