package com.java2e.martin.extension.ncnb.socketio.impl;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.handler.SocketIOException;
import com.corundumstudio.socketio.listener.DataListener;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.WebsocketConstants;
import com.java2e.martin.common.core.support.SpringContextHelper;
import com.java2e.martin.common.websocket.socketio.SocketIoService;
import com.java2e.martin.common.websocket.socketio.util.ParseHeaderUtil;
import com.java2e.martin.common.websocket.socketio.vo.JoinLeaveRoomVo;
import com.java2e.martin.extension.ncnb.socketio.ErdSocketIoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author: liangcan
 * @version: 1.0
 * @date: 2022/4/10 11:50 下午
 * @describtion: ErdSocketIoServiceImpl
 */
@Slf4j
@Service
@RestController
public class ErdSocketIoServiceImpl implements SocketIoService, ErdSocketIoService {
    private final String MODULE = WebsocketConstants.PROJECT_NAMESPACE + "/erd";

    @Autowired
    SocketIOServer socketIOServer;

    @Override
    public SocketIOServer setEvent(SocketIOServer socketIOServer) {
        return socketIOServer;
    }

    @Override
    public SocketIOServer setBinaryEvent(SocketIOServer socketIOServer) {
        return socketIOServer;
    }

    @Override
    public SocketIOServer setNamespaceEvent(SocketIONamespace socketIONamespace, SocketIOServer socketIOServer) {

        /**
         * 用户进入页面的事件
         */
        socketIONamespace.addEventListener(WebsocketConstants.JOIN_ROOM, Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) {
                String username = ParseHeaderUtil.parseUserNameFromHeader(client.getHandshakeData());
                String projectId = ParseHeaderUtil.parseProjectIdFromHeader(client.getHandshakeData());
                if (StrUtil.isBlank(username) || StrUtil.isBlank(projectId)) {
                    client.sendEvent(WebsocketConstants.EVENT_ERROR, "请求参数非法");
                    throw new SocketIOException("请求参数非法");
                }
                Set<String> onlineUsers = initOnlineUserSet(client, projectId);
                client.joinRoom(projectId);
                onlineUsers.add(username);
                client.getNamespace().getRoomOperations(projectId).sendEvent(WebsocketConstants.JOIN_ROOM, new JoinLeaveRoomVo(username, onlineUsers.toArray()));
                log.info("用户{}加入协作", username);
            }
        });
        /**
         * 用户退出页面的事件
         */
        socketIONamespace.addEventListener(WebsocketConstants.LEAVE_ROOM, Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient client, Map data, AckRequest ackRequest) {
                String username = ParseHeaderUtil.parseUserNameFromHeader(client.getHandshakeData());
                String projectId = ParseHeaderUtil.parseProjectIdFromHeader(client.getHandshakeData());
                if (StrUtil.isBlank(username) || StrUtil.isBlank(projectId)) {
                    client.sendEvent(WebsocketConstants.EVENT_ERROR, "请求参数非法");
                    throw new SocketIOException("请求参数非法");
                }
                Set<String> onlineUsers = initOnlineUserSet(client, projectId);
                client.leaveRoom(projectId);
                onlineUsers.remove(username);
                client.getNamespace().getRoomOperations(projectId).sendEvent(WebsocketConstants.LEAVE_ROOM, new JoinLeaveRoomVo(username, onlineUsers.toArray()));
                log.info("用户{}离开协作", username);
            }
        });
        return socketIOServer;
    }

    /**
     * 初始化redis中的onlineUser对象
     *
     * @param client
     * @param projectId
     * @return
     */
    private Set<String> initOnlineUserSet(SocketIOClient client, String projectId) {
        RedissonClient redisson = (RedissonClient) SpringContextHelper.getBean(WebsocketConstants.REDISSON_SPRING_BEAN_NAME);
        return redisson.getSortedSet(getRoomKey(client, projectId));
    }

    /**
     * 当前房间所有onlineUser的redis键
     *
     * @param client
     * @param projectId
     * @return
     */
    private String getRoomKey(SocketIOClient client, String projectId) {
        return WebsocketConstants.SOCKET_IO_CACHE_PREFIX + client.getNamespace().getName().replace(StrUtil.SLASH, StrUtil.COLON) + StrUtil.COLON + projectId;
    }

    private String getUserOnlineRedisKey(Map data) {
        return data.get(WebsocketConstants.PROJECT_ID) + MODULE;
    }

    @Override
    public String getNamespace() {
        return MODULE;
    }

    @Override
    public R sendSocialLoginSuccessInfo(Map params) {
        log.info("token_info:{}", params);
        socketIOServer.getClient(UUID.fromString("daa94b44-219c-44e5-9a21-990a5fe21038")).sendEvent("martin:user:login:success", params);
        return R.ok("发送成功");
    }
}
