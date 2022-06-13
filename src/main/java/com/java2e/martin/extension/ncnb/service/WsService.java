package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.core.api.R;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/7/15
 * @describtion WsService
 * @since 1.0
 */
public interface WsService {


    /**
     * 获取当前项目下
     *
     *
     * @param module
     * @param projectId
     * @return
     */
    R getAllOnlineUser(String module, String projectId);


    /**
     * 订阅项目在线用户名
     *
     * @param projectId
     */
    void publishProjectOnlineUserName(String projectId);


    /**
     * 订阅项目差量变化
     *
     * @param projectId
     */
    void subscribeProjectDiff(String projectId);

    /**
     * 接收并发布前端的erd json变化
     *
     * @param diff
     * @param projectId
     * @return
     */
    R publishProjectDiff(Object diff, String projectId);

}
