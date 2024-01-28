package com.java2e.martin.extension.ncnb.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.extension.ncnb.entity.ProjectRole;
import com.java2e.martin.extension.ncnb.mapper.ProjectRoleMapper;
import com.java2e.martin.extension.ncnb.service.ProjectRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ncnb
 * @version 1.0
 * @date 2022-10-22
 * @describtion
 * @since 1.0
 */
@Service
public class ProjectRoleServiceImpl extends MartinServiceImpl<ProjectRoleMapper, ProjectRole> implements ProjectRoleService {
    @Override
    protected void setEntity() {
        this.clz = ProjectRole.class;
    }
}
