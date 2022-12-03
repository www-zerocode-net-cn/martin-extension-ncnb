package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.extension.ncnb.entity.QueryInfo;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * sql信息表  服务类
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface QueryInfoService extends MartinService<QueryInfo> {

    /**
     * 执行sql，并且返回列和分页数据
     * @param map
     * @return
     */
    Object exec(Map map);
}
