package com.java2e.martin.extension.ncnb.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java2e.martin.common.api.dto.RoleUserDto;
import com.java2e.martin.extension.ncnb.dto.ProjectBaseDto;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java2e.martin.extension.ncnb.util.Query;
import org.apache.ibatis.annotations.Param;

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
     *  @param userId
     * @param projectId
     * @param roleId
     */
    void bindProjectUser(@Param("userId") String userId, @Param("projectId") String projectId,@Param("roleId") String roleId);

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
    String currentUserRole(@Param("projectId") String projectId, @Param("userId") String userId, @Param("roleId") String roleId);
}
