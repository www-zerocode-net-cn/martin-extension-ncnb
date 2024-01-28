package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.entity.Approval;
import com.java2e.martin.extension.ncnb.mapper.ApprovalMapper;
import com.java2e.martin.extension.ncnb.service.ApprovalService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 元数据审批  服务实现类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-26
 * @describtion
 * @since 1.0
 */
@Service
public class ApprovalServiceImpl extends MartinServiceImpl<ApprovalMapper, Approval> implements ApprovalService {
    @Override
    protected void setEntity() {
        this.clz = Approval.class;
    }

    @Override
    public R syncBdVersion(String versionId) {
        String userId = SecurityContextUtil.getAccessUser().getId();
        return R.ok(this.baseMapper.syncBdVersion(versionId, userId));
    }
}
