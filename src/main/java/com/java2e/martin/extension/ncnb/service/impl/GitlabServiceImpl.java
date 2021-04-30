package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.entity.ApiDesign;
import com.java2e.martin.extension.ncnb.exception.CDException;
import com.java2e.martin.extension.ncnb.service.ApiDesignService;
import com.java2e.martin.extension.ncnb.service.GitlabService;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.JsonUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.vo.GitlabOauthVo;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.RepositoryFileApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.CommitPayload;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/10
 * @describtion GitlabServiceImpl
 * @since 1.0
 */
@Service
@Slf4j
public class GitlabServiceImpl implements GitlabService {
    @Autowired
    private ApiDesignService apiDesignService;

    @Autowired
    private ProjectService projectService;

    @Override
    public R valid(GitlabOauthVo gitlabOauthVo) {
        boolean flag;
        try {
            GitLabApi api = GitLabApi.oauth2Login(gitlabOauthVo.getUrl(), gitlabOauthVo.getUsername(), gitlabOauthVo.getPassword());
            log.info("==gitlab auth token=={}", api.getAuthToken());
            flag = true;
        } catch (GitLabApiException e) {
            log.error("", e);
            return R.failed("gitlab认证失败");
        }
        if (flag) {
            String projectId = gitlabOauthVo.getProjectId();
            if (!exitApiDesign(projectId)) {
                try {
                    ApiDesign apiDesign = new ApiDesign()
                            .setProjectId(projectId)
                            .setGitlabConfig(JsonUtil.generate(gitlabOauthVo).getBytes());
                    apiDesignService.saveOrUpdate(apiDesign);
                } catch (JsonProcessingException e) {
                    log.error("", e);
                    return R.failed("无法保存 gitlab 信息，请查看服务端日志");
                }
            }
            return R.ok("gitlab认证成功");
        } else {
            return R.failed("gitlab认证失败");
        }
    }

    @Override
    public R init(Map<String, Object> map) {
        String projectId = (String) map.get("projectId");
        String projectName = (String) map.get("projectName");
        String projectDes = (String) map.get("projectDes");
        if (StrUtil.isBlank(projectId)) {
            return R.failed("projectId 为空");
        }
        if (StrUtil.isBlank(projectName)) {
            return R.failed("projectName 为空");
        }
        ApiDesign apiDesign = getApiDesign(projectId);
        if (apiDesign == null) {
            apiDesignService.save(new ApiDesign().setProjectId(apiDesign.getProjectId()));
            return R.failed("项目[" + projectName + "]未配置 gitlab 信息，请先配置好信息再操作");
        }
        if (apiDesign.getFlagStatusCi()) {
            return R.failed("项目[" + projectName + "]已经做过初始化");
        }
        GitlabOauthVo gitlabOauthVo = null;
        try {
            //通过项目id获取git认证信息
            gitlabOauthVo = getGitlabAuthVoByProjectId(apiDesign, projectName);
        } catch (CDException e) {
            log.error("", e);
            return R.failed(e.getMessage());
        }
        GitLabApi gitLabApi = null;
        try {
            gitLabApi = getGitlabApi(gitlabOauthVo);
        } catch (GitLabApiException e) {
            log.error("项目[" + projectName + "] gitlab 信息无法通过认证，请确认用户密码是否正确", e);
            return R.failed("项目[" + projectName + "] gitlab 信息无法通过认证，请确认用户密码是否正确");
        }
        ProjectApi projectApi = gitLabApi.getProjectApi();
        Project project = null;
        try {
            project = projectApi.getProject("nb", "ncnb");
        } catch (GitLabApiException e) {
            log.error("gitlab尚未初始化nb命名空间空间或ncnb项目", e);
            return R.failed("gitlab尚未初始化nb空间或ncnb项目");
        }
        Project exitProject = null;
        try {
            exitProject = projectApi.getProject("nb", projectId);
            if (exitProject != null) {
                return R.failed("项目[" + projectName + "] 已初始化ci信息，地址为：" + exitProject.getHttpUrlToRepo());
            }
        } catch (GitLabApiException e) {
            log.error("校验ci信息失败", e);
            return R.failed("校验ci信息失败");
        }
        Project forkedProject = null;
        try {
            forkedProject = projectApi.forkProject(project.getId(), project.getNamespace().getPath(), projectId, projectId);
        } catch (GitLabApiException e) {
            log.error("从项目[" + project.getName() + "]fork失败", e);
            return R.failed("从项目[" + project.getName() + "]fork失败");
        }
        apiDesign.setFlagStatusCi(true);
        apiDesignService.updateById(apiDesign);
        return R.ok(forkedProject.getHttpUrlToRepo());
    }

    @Override
    public R commit(String projectId, CommitPayload commitPayload) {
        GitLabApi gitLabApi = null;
        try {
            gitLabApi = getGitlabApiByProjectId(projectId);
        } catch (CDException e) {
            log.error("", e.getMessage());
            return R.failed(e.getMessage());
        }
        CommitsApi commitsApi = gitLabApi.getCommitsApi();

        ProjectApi projectApi = gitLabApi.getProjectApi();
        Project project = null;
        try {
            project = projectApi.getProject("nb", projectId);
        } catch (GitLabApiException e) {
            log.error("gitlab尚未初始化nb命名空间空间或ncnb项目", e);
            return R.failed("gitlab尚未初始化nb空间或[ " + projectId + " ]项目");
        }
        try {
            Commit commit = commitsApi.createCommit(project,
                    commitPayload.getBranch(),
                    commitPayload.getCommitMessage(),
                    commitPayload.getStartBranch(),
                    commitPayload.getAuthorEmail(),
                    commitPayload.getAuthorName(),
                    commitPayload.getActions()
            );
            return R.ok(commit);
        } catch (GitLabApiException e) {
            log.error("", e);
            return R.failed("代码提交失败");
        }
    }

    @Override
    public void bindCommitAction(String projectId, List<CommitAction> apis, String branch) {
        apis.stream().forEach(action -> {
            String filePath = action.getFilePath();
            GitLabApi gitLabApi = null;
            try {
                gitLabApi = getGitlabApiByProjectId(projectId);
            } catch (CDException e) {
                log.error("", e.getMessage());
            }
            ProjectApi projectApi = gitLabApi.getProjectApi();
            Project project = null;
            try {
                project = projectApi.getProject("nb", projectId);
            } catch (GitLabApiException e) {
                log.error("gitlab尚未初始化nb命名空间空间或ncnb项目", e);
            }
            RepositoryFileApi repositoryFileApi = gitLabApi.getRepositoryFileApi();
            try {
                repositoryFileApi.getFile(project, filePath, branch);
                action.setAction(CommitAction.Action.UPDATE);
            } catch (GitLabApiException e) {
                if (e.getMessage().contains(String.valueOf(HttpStatus.HTTP_NOT_FOUND))) {
                    action.setAction(CommitAction.Action.CREATE);
                } else {
                    log.error("检验文件是否存在失败", e);
                }
            }
        });
    }

    public GitLabApi getGitlabApiByProjectId(String projectId) throws CDException {
        ApiDesign apiDesign = getApiDesign(projectId);
        if (apiDesign == null) {
            throw new CDException("项目未配置 gitlab 信息，请先配置好信息再操作");
        }
        GitlabOauthVo gitlabOauthVo = null;
        try {
            //通过项目id获取git认证信息
            gitlabOauthVo = getGitlabAuthVoByProjectId(apiDesign, "");
        } catch (CDException e) {
            throw new CDException(e.getMessage());
        }
        GitLabApi gitLabApi = null;
        try {
            gitLabApi = getGitlabApi(gitlabOauthVo);
        } catch (GitLabApiException e) {
            throw new CDException("项 gitlab 信息无法通过认证，请确认用户密码是否正确");
        }
        return gitLabApi;
    }


    public GitlabOauthVo getGitlabAuthVoByProjectId(ApiDesign apiDesign, String projectName) throws CDException {
        GitlabOauthVo gitlabOauthVo = null;
        try {
            byte[] gitlabConfig = apiDesign.getGitlabConfig();
            if (gitlabConfig == null) {
                throw new CDException("项目[" + projectName + "]未配置 gitlab 信息，请先配置好 gitlab 信息再操作");
            }
            gitlabOauthVo = JsonUtil.parse(new String(gitlabConfig), GitlabOauthVo.class);
        } catch (IOException e) {
            throw new CDException(e.getMessage());
        }
        if (gitlabOauthVo == null) {
            throw new CDException("项目[" + projectName + "]未配置 gitlab 信息，请先配置好 gitlab 信息再操作");
        }
        return gitlabOauthVo;
    }

    /**
     * 判断当前项目是否已经生成过ci信息
     *
     * @param projectId
     * @return
     */
    private boolean exitApiDesign(String projectId) {
        ApiDesign apiDesign = getApiDesign(projectId);
        if (apiDesign == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取ci信息
     *
     * @param projectId
     * @return
     */
    private ApiDesign getApiDesign(String projectId) {
        QueryWrapper<ApiDesign> wrapper = new QueryWrapper<>();
        wrapper.eq("project_id", projectId);
        return apiDesignService.getOne(wrapper);
    }

    /**
     * 获取gitlab api实例
     *
     * @param gitlabOauthVo
     * @return
     */
    private GitLabApi getGitlabApi(GitlabOauthVo gitlabOauthVo) throws GitLabApiException {
        return GitLabApi.oauth2Login(gitlabOauthVo.getUrl(), gitlabOauthVo.getUsername(), gitlabOauthVo.getPassword());
    }

}
