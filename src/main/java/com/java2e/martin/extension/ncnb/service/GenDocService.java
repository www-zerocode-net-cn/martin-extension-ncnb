package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.core.api.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/7/30
 * @describtion GenDocService
 * @since 1.0
 */
public interface GenDocService {
    /**
     * 下载数据库设计说明
     *
     * @param params
     * @param response
     */
    void genDataBaseDocx(Map params, HttpServletResponse response);

    /**
     * 下载word模板
     *
     * @param doctpl
     * @param response
     */
    void downloadWordTemplate(String doctpl, HttpServletResponse response);

    /**
     * 上传word模板
     *
     * @param file
     * @param projectId
     * @return
     */
    R uploadWordTemplate(MultipartFile file, String projectId);
}
