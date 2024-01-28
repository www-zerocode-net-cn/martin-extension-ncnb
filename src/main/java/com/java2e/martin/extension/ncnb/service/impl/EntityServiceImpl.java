package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.extension.ncnb.entity.Entity;
import com.java2e.martin.extension.ncnb.mapper.EntityMapper;
import com.java2e.martin.extension.ncnb.service.EntityService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 元数据 服务实现类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-04
 * @describtion
 * @since 1.0
 */
@Service
public class EntityServiceImpl extends MartinServiceImpl<EntityMapper, Entity> implements EntityService {
    @Override
    protected void setEntity() {
        this.clz = Entity.class;
    }
}
