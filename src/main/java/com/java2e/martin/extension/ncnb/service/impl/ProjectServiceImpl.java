package com.java2e.martin.extension.ncnb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.mapper.ProjectMapper;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * PDMan全局配置表 服务实现类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-26
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    public R projectService(String projectId) {
        Project project = this.getById(projectId);
        HashMap<String, Object> map = new HashMap<>(3);
        try {
            map.put("configJSON", JsonUtil.parse(new String(project.getConfigJSON()), Map.class));
            map.put("projectJSON", JsonUtil.parse(new String(project.getProjectJSON()), Map.class));
            map.put("projectName", project.getProjectName());
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(map);
    }
}
