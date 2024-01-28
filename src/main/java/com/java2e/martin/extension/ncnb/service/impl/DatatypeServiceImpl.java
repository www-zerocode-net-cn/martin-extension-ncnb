package com.java2e.martin.extension.ncnb.service.impl;

import com.java2e.martin.extension.ncnb.entity.Datatype;
import com.java2e.martin.extension.ncnb.mapper.DatatypeMapper;
import com.java2e.martin.extension.ncnb.service.DatatypeService;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据域 服务实现类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-04
 * @describtion
 * @since 1.0
 */
@Service
public class DatatypeServiceImpl extends MartinServiceImpl<DatatypeMapper, Datatype> implements DatatypeService {
    @Override
    protected void setEntity() {
        this.clz = Datatype.class;
    }
}
