package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.service.GenDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/7/9
 * @describtion GenDocController
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("doc")
public class GenDocController {
    @Autowired
    private GenDocService genDocService;

    @PostMapping("uploadWordTemplate/{projectId}")
    public R uploadWordTemplate(@RequestParam("file") MultipartFile file, @PathVariable("projectId") String projectId) {
        return genDocService.uploadWordTemplate(file, projectId);
    }

    @GetMapping("downloadWordTemplate")
    public void downloadWordTemplate(@RequestParam Map params,  HttpServletResponse response) {
        genDocService.downloadWordTemplate((String) params.get("doctpl"), response);
    }

    @PostMapping("gendocx")
    public void genDataBaseDocx(@RequestBody Map params, HttpServletResponse response) {
        genDocService.genDataBaseDocx(params, response);
    }

}
