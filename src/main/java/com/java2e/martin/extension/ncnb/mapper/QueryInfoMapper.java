package com.java2e.martin.extension.ncnb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java2e.martin.extension.ncnb.entity.QueryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sql信息表  Mapper 接口
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
public interface QueryInfoMapper extends BaseMapper<QueryInfo> {

    /**
     * 分页查询
     *
     * @param page
     * @param sql
     * @return
     */
    IPage exec(Page<Map> page, @Param("sql") String sql);

    /**
     * 分析sql执行计划
     *
     * @param sql
     * @return
     */
    List<Map> explain(String sql);
}
