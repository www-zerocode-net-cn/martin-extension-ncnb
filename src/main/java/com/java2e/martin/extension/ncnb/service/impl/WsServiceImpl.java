package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.util.StrUtil;
import com.java2e.martin.common.core.api.ApiErrorCode;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.security.userdetail.MartinUser;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.service.WsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/7/15
 * @describtion WsServiceImpl
 * @since 1.0
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "martin.websocket",name = "enabled",havingValue = "true")
public class WsServiceImpl implements WsService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public R getAllOnlineUser(String module, String projectId) {
        String allUserKey = projectId + StrUtil.COLON + module;
        String username = SecurityContextUtil.getAccessUser().getUsername();
        log.info("当前登录用户为：{}", username);
        redisTemplate.opsForSet().add(allUserKey, username);
        Set<String> onlineUsers = (Set<String>) redisTemplate.opsForSet().members(allUserKey);
        return R.ok(onlineUsers);
    }

    @Override
    public void publishProjectOnlineUserName(String projectId) {
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("type", "add");
        msg.put("username", "user");
        redisTemplate.opsForSet().add(projectId + StrUtil.COLON + "erd", "user");
        messagingTemplate.convertAndSend("/topic/project/erd/online/user/" + projectId, msg);
    }

    @Override
    public void subscribeProjectDiff(String projectId) {
        messagingTemplate.convertAndSend("/topic/project/diff/" + projectId, "{}}");
    }

    @Override
    public R publishProjectDiff(Object diff, String projectId) {
        String destination = "/topic/project/diff/" + projectId;
        log.info("开始发布差异：{}", destination);
        //往该地址推送差异
        messagingTemplate.convertAndSend(destination, diff);
        log.info("结束发布差异：");
        return R.ok(diff);
    }
}
