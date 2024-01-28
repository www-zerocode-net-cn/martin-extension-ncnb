package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.extension.ncnb.entity.Datatype;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据域 服务类
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-04
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface DatatypeService extends MartinService<Datatype> {

}
