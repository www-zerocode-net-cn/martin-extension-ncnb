package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.entity.ProjectRole;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ncnb
 * @version 1.0
 * @date 2022-10-22
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface ProjectRoleService extends MartinService<ProjectRole> {

}
