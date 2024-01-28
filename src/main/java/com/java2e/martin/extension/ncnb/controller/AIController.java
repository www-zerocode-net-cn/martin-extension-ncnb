package com.java2e.martin.extension.ncnb.controller;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import com.java2e.martin.common.vip.annotation.VIP;
import com.java2e.martin.common.vip.enums.VIPLevelEnum;
import com.java2e.martin.common.vip.enums.VIPModuleEnum;
import com.java2e.martin.extension.ncnb.dto.OpenAiSqlDto;
import com.java2e.martin.extension.ncnb.service.AIService;
import com.java2e.martin.extension.ncnb.vip.rights.AICountRight;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * <p>
 * Chat SQL 控制器
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-04-15
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @ApiOperation(value = "AI生成sql ", nickname = "sql", notes = "AI生成sql ", tags = {"ai",})
    @MartinLog("AI生成sql")
    @RequestMapping(value = "/sql", method = RequestMethod.POST)
    @VIP(module = VIPModuleEnum.ERD,vipLevel = {VIPLevelEnum.NONE,VIPLevelEnum.PRO}, rights = {AICountRight.class}, reset = true)
    public R sql(@ApiParam(value = "", required = true) @Valid @RequestBody OpenAiSqlDto openAiSqlDto) {
        return aiService.sql(openAiSqlDto);
    }

    @ApiOperation(value = "AI生成sql", nickname = "sql", notes = "AI生成sql ", tags = {"ai",})
    @MartinLog("AI生成sql")
    @RequestMapping(value = "/sqlTranslateOrRequest", method = RequestMethod.POST)
    @VIP(module = VIPModuleEnum.ERD,vipLevel = {VIPLevelEnum.NONE,VIPLevelEnum.PRO}, rights = {AICountRight.class}, reset = true)
    public R sqlTranslateOrRequest(@ApiParam(value = "", required = true) @Valid @RequestBody OpenAiSqlDto openAiSqlDto) {
        return aiService.sqlTranslateOrRequest(openAiSqlDto);
    }
}

