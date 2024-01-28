package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.dto.OpenAiSqlDto;
import com.java2e.martin.extension.ncnb.entity.Approval;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Chat SQL  服务类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-04-15
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface AIService {

    void ai();

    void ai1();

    R ai2();

    R sql(OpenAiSqlDto openAiSqlDto);

    R sqlTranslateOrRequest(OpenAiSqlDto openAiSqlDto);
}
