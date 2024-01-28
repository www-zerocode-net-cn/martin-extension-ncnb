package com.java2e.martin.extension.ncnb.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.extension.ncnb.dto.ProjectBaseDto;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java2e.martin.extension.ncnb.entity.ProjectRole;
import com.java2e.martin.extension.ncnb.util.Query;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PDMan全局配置表 Mapper 接口
 * </p>
 *
 * @author 狮少
 * @since 2020-10-26
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 绑定项目与用户关系
     *
     * @param userId
     * @param projectId
     * @param roleId
     */
    void bindProjectUser(@Param("userId") String userId, @Param("projectId") String projectId, @Param("roleId") String roleId);

    /**
     * 批量绑定项目与用户关系
     *
     * @param userIds
     * @param projectId
     * @param roleId
     */
    void batchBindProjectUser(@Param("userIds") List<String> userIds, @Param("projectId") String projectId, @Param("roleId") String roleId);

    /**
     * 分页查询项目
     *
     * @param query
     * @param param
     * @return
     */
    Page<ProjectBaseDto> projectPage(Query query, @Param("param") Map param);

    /**
     * 从团队中移除用户
     *
     * @param roleUserDto
     */
    void removeUserFromGroup(RoleUserDto roleUserDto);

    /**
     * 获取当前用户，当前项目的最大角色
     *
     * @param projectId
     * @param userId
     * @return
     */
    ProjectRole currentUserRole(@Param("projectId") String projectId, @Param("userId") String userId);

    /**
     * 统计今天打开的项目
     *
     * @return
     */
    Integer queryToday();

    /**
     * 统计昨天打开的项目
     *
     * @return
     */
    Integer queryYesterday();

    /**
     * 统计本月打开的项目
     *
     * @return
     */
    Integer queryMonth();

    /**
     * 统计全部项目
     *
     * @return
     */
    Integer queryTotal();

    /**
     * 统计个人项目数量
     *
     * @return
     */
    Integer queryPersonTotal();


    /**
     * 统计个人项目数量
     *
     * @return
     */
    Integer queryGroupTotal();

    /**
     * 获取当前项目可以审批sql的人员，获取角色为管理员和项目拥有者
     *
     * @param projectId
     * @param userId
     * @return
     */
    List<ProjectRole> approvalUserRole(@Param("projectId") String projectId, @Param("userId") String userId);

    /**
     * 统计项目版本数量
     *
     * @param wrapper
     * @return
     */
    Integer projectCount(HashMap<String, Object> param);

    /**
     * 统计项目版本数量
     *
     * @param wrapper
     * @return
     */
    Integer projectCountByUserIdAndType(@Param("userId") String userId, @Param("type") String type);

    /**
     * 统计项目版本数量
     *
     * @param wrapper
     * @return
     */
    Integer projectVersionCount(HashMap<String, Object> param);

}
