package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.extension.ncnb.entity.QueryInfo;
import com.java2e.martin.extension.ncnb.mapper.QueryInfoMapper;
import com.java2e.martin.extension.ncnb.service.QueryInfoService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sql信息表  服务实现类
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
@Service
public class QueryInfoServiceImpl extends MartinServiceImpl<QueryInfoMapper, QueryInfo> implements QueryInfoService {
    @Override
    protected void setEntity() {
        this.clz = QueryInfo.class;
    }
}
