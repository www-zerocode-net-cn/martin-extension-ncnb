package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.extension.ncnb.entity.Field;
import com.java2e.martin.extension.ncnb.mapper.FieldMapper;
import com.java2e.martin.extension.ncnb.service.FieldService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字段  服务实现类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-04
 * @describtion
 * @since 1.0
 */
@Service
public class FieldServiceImpl extends MartinServiceImpl<FieldMapper, Field> implements FieldService {
    @Override
    protected void setEntity() {
        this.clz = Field.class;
    }
}
