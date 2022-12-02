package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.extension.ncnb.entity.QueryHistory;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * sql执行记录表  服务类
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface QueryHistoryService extends MartinService<QueryHistory> {

}
