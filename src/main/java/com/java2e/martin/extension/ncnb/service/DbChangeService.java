package com.java2e.martin.extension.ncnb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.entity.DbChange;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 版本表 服务类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-28
 */
@Transactional(rollbackFor = Exception.class)
public interface DbChangeService extends MartinService<DbChange> {

    /**
     * 查询历史版本
     *
     * @param map
     * @return
     */
    R loadHistory(Map map);


    /**
     * 查询历史版本，只查询版本信息
     *
     * @param projectId
     * @return
     */
    List<DbChange> loadHistoryVersion(String projectId,String dbKey);

    /**
     * 删除版本
     *
     * @param projectId
     * @return
     */
    R deleteHistory(String changeId);

    /**
     * 删除项目下所有版本版本
     *
     * @param map
     * @return
     */
    R deleteAllHistory(Map map);

    /**
     * byte[]字段转json
     *
     * @param dbChanges
     * @return
     */
    R getHashMapsByDbChanges(List<DbChange> dbChanges);
}
