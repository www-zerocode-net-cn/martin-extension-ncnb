package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.entity.DbVersion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 狮少
 * @since 2020-10-29
 */
@Transactional(rollbackFor = Exception.class)
public interface DbVersionService extends MartinService<DbVersion> {

    /**
     * 查询当前项目数据库中的最高版本
     *
     * @param map
     * @return
     */
    String dbversion(Map map);

    /**
     * 重建当前项目基线
     *
     * @param map
     * @return
     */

    Integer rebaseline(Map map);

    /**
     * 更新数据库中db版本
     *
     * @param map
     * @return
     */
    Boolean saveDbVersion(Map map);

    /**
     * 根据projectId,dbKey查询已同步的版本
     *
     * @param map
     * @return
     */
    List<String> checkdbversion(Map map);
}
