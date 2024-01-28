package com.java2e.martin.extension.ncnb.event;

import com.java2e.martin.common.api.system.RemoteSystemLog;
import com.java2e.martin.common.bean.system.Log;
import com.java2e.martin.common.data.dynamic.datasource.SqlHelperDsContextHolder;
import com.java2e.martin.extension.ncnb.entity.QueryHistory;
import com.java2e.martin.extension.ncnb.service.QueryHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/9/18
 * @describtion LogListener
 * @since 1.0
 */
@Slf4j
@Component
public class QueryHistoryListener {
    @Autowired
    private QueryHistoryService queryHistoryService;

    @Async
    @Order
    @EventListener(QueryHistoryEvent.class)
    public void saveLog(QueryHistoryEvent event) {
        SqlHelperDsContextHolder.switchTo(null);
        QueryHistory queryHistory = (QueryHistory) event.getSource();
        queryHistoryService.save(queryHistory);
    }
}
