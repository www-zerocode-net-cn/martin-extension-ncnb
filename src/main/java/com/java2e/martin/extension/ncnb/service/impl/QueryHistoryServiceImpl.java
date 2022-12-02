package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.extension.ncnb.entity.QueryHistory;
import com.java2e.martin.extension.ncnb.mapper.QueryHistoryMapper;
import com.java2e.martin.extension.ncnb.service.QueryHistoryService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sql执行记录表  服务实现类
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
@Service
public class QueryHistoryServiceImpl extends MartinServiceImpl<QueryHistoryMapper, QueryHistory> implements QueryHistoryService {
    @Override
    protected void setEntity() {
        this.clz = QueryHistory.class;
    }
}
