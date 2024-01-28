package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.extension.ncnb.entity.TestJson;
import com.java2e.martin.extension.ncnb.mapper.TestJsonMapper;
import com.java2e.martin.extension.ncnb.service.TestJsonService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-02-26
 * @describtion
 * @since 1.0
 */
@Service
public class TestJsonServiceImpl extends MartinServiceImpl<TestJsonMapper, TestJson> implements TestJsonService {
    @Override
    protected void setEntity() {
        this.clz = TestJson.class;
    }
}
