package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.oss.service.OssTemplate;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.service.WsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/5/19
 * @describtion WsController
 * @since 1.0
 */
@RestController
@RequestMapping("ws")
@ConditionalOnProperty(prefix = "martin.websocket", name = "enabled", havingValue = "true")
@Slf4j
public class WsController {

    @Autowired
    private WsService wsService;

    @Autowired
    private OssTemplate minioOssTemplate;

    @MessageMapping(value = "/project/diff/{projectId}")
    public void subscribeProjectDiff() {
    }

    @MessageMapping(value = "/project/online/user/{projectId}")
    public void subscribeProjectOnlineUserName() {
    }

    @MessageMapping(value = "/project/erd/user/up/{projectId}")
    public void userUpLine(@DestinationVariable("projectId") String projectId) {
        wsService.publishProjectOnlineUserName(projectId);
    }


    @GetMapping("/project/{module}/getAllOnlineUser/{projectId}")
    public R getAllOnlineUser(@PathVariable("module") String module, @PathVariable("projectId") String projectId) {
        return wsService.getAllOnlineUser(module, projectId);
    }

    /**
     * 测试客户端发送websocket专用
     */
    @MessageMapping(value = "/project1/erd/user/up/362033b5a80291740db91a22e57a9a9f")
    public void test() {
        log.info("123");
        System.out.println("123 = " + 123);
    }

    @PostMapping("upload")
    public R uploadTest(@RequestParam("file") MultipartFile file) {
        return R.ok(minioOssTemplate.upload(System.currentTimeMillis() + "", file, true));
    }

}
