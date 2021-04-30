package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.extension.ncnb.service.GitlabService;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.vo.GitlabOauthVo;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/2
 * @describtion GitlabController
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ci")
public class GitlabController {
    @Autowired
    private GitlabService gitlabService;

    @GetMapping("/projects")
    public List<Project> gitlab() {
        List<Project> projects = null;
        try {
            GitLabApi api = GitLabApi.oauth2Login("https://gitlab.java2e.com/", "martin", "12345678");
            projects = api.getProjectApi().getProjects();
            //读取空闲的可用端口
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            System.out.println("port = " + port);
            Project project = new Project();
            project.setId(13);
            project.setName("name1");
//            api.getProjectApi().deleteProject(project);
//            api.getProjectApi().createProject(project, "http://gitlab.java2e.com/root/ncnb.git");
            projects = api.getProjectApi().getProjects();
        } catch (GitLabApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @PostMapping("/valid")
    public R fork(@RequestBody GitlabOauthVo gitlabOauthVo) {
        return gitlabService.valid(gitlabOauthVo);
    }


    @PostMapping("/init")
    public R fork(@RequestBody Map<String,Object> map) {

        return gitlabService.init(map);
    }


}
