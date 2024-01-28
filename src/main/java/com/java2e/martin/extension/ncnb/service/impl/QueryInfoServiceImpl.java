package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java2e.martin.common.bean.system.User;
import com.java2e.martin.common.core.api.ApiErrorCode;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.exception.StatefulException;
import com.java2e.martin.common.core.support.SpringContextHelper;
import com.java2e.martin.common.data.dynamic.datasource.ConnectionSubspaceTypeEnum;
import com.java2e.martin.common.data.dynamic.datasource.LogicDsMeta;
import com.java2e.martin.common.data.dynamic.datasource.SqlHelperDsContextHolder;
import com.java2e.martin.common.data.dynamic.datasource.SqlHelperDsManager;
import com.java2e.martin.common.data.dynamic.spring.SpringSqlHelperDsManager;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.extension.ncnb.entity.QueryHistory;
import com.java2e.martin.extension.ncnb.entity.QueryInfo;
import com.java2e.martin.extension.ncnb.event.QueryHistoryEvent;
import com.java2e.martin.extension.ncnb.mapper.QueryInfoMapper;
import com.java2e.martin.extension.ncnb.service.QueryHistoryService;
import com.java2e.martin.extension.ncnb.service.QueryInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * sql信息表  服务实现类
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-12-02
 * @describtion
 * @since 1.0
 */
@Slf4j
@Service
public class QueryInfoServiceImpl extends MartinServiceImpl<QueryInfoMapper, QueryInfo> implements QueryInfoService {

    @Autowired
    private QueryHistoryService queryHistoryService;

    @Override
    protected void setEntity() {
        this.clz = QueryInfo.class;
    }

    @Override
    public R exec(Map params) {
        log.info("params: {}", params);
        String sql = (String) params.get("sql");
        String dbName = (String) params.get("dbName");
        String queryId = (String) params.get("queryId");
        sql = sql.replace(";", "");
        Page page = new Page();
        BeanUtil.fillBeanWithMap(params, page, true);
        if (page.getSize() > 100) {
            page.setSize(100);
        }
        IPage query = null;
        try {
            long start = System.currentTimeMillis();
            query = this.baseMapper.exec(page, sql);
            long end = System.currentTimeMillis();

            QueryHistory queryHistory = new QueryHistory();
            queryHistory.setTitle("");
            queryHistory.setSqlInfo(sql);
            queryHistory.setDbName(dbName);
            queryHistory.setQueryId(queryId);
            queryHistory.setDuration(Long.valueOf((int) (end - start)));
            queryHistoryService.saveLog(queryHistory);
        } catch (Exception e) {
            log.error("执行SQL异常:", e);
            Throwable causedBy = ExceptionUtil.getCausedBy(e, SQLException.class);
            return R.failed(causedBy.getMessage());
        }
        HashMap<Object, Object> result = new HashMap<>(2);
        if (query.getTotal() > 0) {
            Map o = (Map) query.getRecords().get(0);
            result.put("columns", o.keySet());
        } else {
            result.put("columns", new ArrayList<>());
        }
        result.put("tableData", query);
        return R.ok(result);
    }

    @Override
    public R explain(Map params) {
        log.info("params: {}", params);
        String sql = (String) params.get("sql");
        String dbName = (String) params.get("dbName");
        String queryId = (String) params.get("queryId");
        sql = sql.replace(";", "");

        List<Map> query = null;
        try {
            long start = System.currentTimeMillis();
            query = this.baseMapper.explain(sql);
            long end = System.currentTimeMillis();

            QueryHistory queryHistory = new QueryHistory();
            queryHistory.setTitle("");
            queryHistory.setSqlInfo(sql);
            queryHistory.setDbName(dbName);
            queryHistory.setQueryId(queryId);
            queryHistory.setDuration(Long.valueOf((int) (end - start)));
            SpringContextHelper.publishEvent(new QueryHistoryEvent(queryHistory));
        } catch (Exception e) {
            log.error("执行SQL异常:", e);
            Throwable causedBy = ExceptionUtil.getCausedBy(e, SQLException.class);
            return R.failed(causedBy.getMessage());
        }
        HashMap<Object, Object> result = new HashMap<>(2);
        if (CollUtil.isNotEmpty(query)) {
            Map o = query.get(0);
            result.put("columns", o.keySet());
        } else {
            result.put("columns", new ArrayList<>());
        }
        result.put("tableData", query);
        return R.ok(result);
    }
}
