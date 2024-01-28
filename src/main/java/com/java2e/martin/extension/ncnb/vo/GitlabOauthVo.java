package com.java2e.martin.extension.ncnb.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/10
 * @describtion GitlabOauthVo
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class GitlabOauthVo {
    @NotBlank(message = "url 不能为空")
    private String url;
    @NotBlank(message = "username 不能为空")
    private String username;
    @NotBlank(message = "password 不能为空")
    private String password;
    @NotBlank(message = "projectId 不能为空")
    private String projectId;
}
