package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.vo.GitlabOauthVo;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.CommitPayload;

import java.util.List;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/10
 * @describtion GitlabService
 * @since 1.0
 */
public interface GitlabService {
    /**
     * 验证git地址、账号有效性
     *
     * @param gitlabOauthVo
     * @return
     */
    R valid(GitlabOauthVo gitlabOauthVo);

    /**
     * 初始化项目的gitlab
     *
     * @param map
     * @return
     */
    R init(Map<String, Object> map);

    /**
     * 将生成的文件提交到git
     *
     * @param projectId
     * @param commitPayload
     * @return
     */
    R commit(String projectId, CommitPayload commitPayload);

    /**
     * 绑定设置CommitAction的action状态
     *
     * @param projectId
     * @param apis
     * @param branch
     */
    void bindCommitAction(String projectId, List<CommitAction> apis, String branch);
}
