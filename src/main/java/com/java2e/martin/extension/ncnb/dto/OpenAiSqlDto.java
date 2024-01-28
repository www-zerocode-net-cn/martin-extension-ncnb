package com.java2e.martin.extension.ncnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/4/15 18:49
 * @describtion: OpenAiSqlDto
 */
@Data
public class OpenAiSqlDto {
    @NotBlank(message = "未获取到会话标识")
    String chatId;
    String schema;
    List<String> tables;
    @NotBlank(message = "未输入问题，赶紧提问吧")
    String command;
}
