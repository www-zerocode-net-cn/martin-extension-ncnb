package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.extension.ncnb.entity.Approval;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 元数据审批  服务类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-26
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface ApprovalService extends MartinService<Approval> {

    /**
     * sql审批后，更新数据库中对应的版本信息
     * @param versionId
     * @return
     */
    R syncBdVersion(String versionId);
}
