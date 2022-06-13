package com.java2e.martin.extension.ncnb.mapper;

import com.java2e.martin.extension.ncnb.entity.DbVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 狮少
 * @since 2020-10-29
 */
public interface DbVersionMapper extends BaseMapper<DbVersion> {

    /**
     * 加载所有历史版本
     *
     * @param projectId
     * @return
     */
    List<String> loadHistory(String projectId);

    /**
     * 查询当前项目数据库中的最高版本
     *
     * @param projectId
     * @return
     */
    String dbversion(Map map);

    /**
     * 重建当前项目的基线
     *
     * @param projectId
     * @return
     */
    Integer rebaseline(Map map);

    /**
     * 根据projectId,dbKey查询已同步的版本
     *
     * @param map
     * @return
     */
    List<String> checkdbversion(Map map);
}
