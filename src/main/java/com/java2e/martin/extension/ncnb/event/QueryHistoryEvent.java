package com.java2e.martin.extension.ncnb.event;

import com.java2e.martin.extension.ncnb.entity.QueryHistory;
import org.springframework.context.ApplicationEvent;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/9/18
 * @describtion LogEvent
 * @since 1.0
 */
public class QueryHistoryEvent extends ApplicationEvent {
    public QueryHistoryEvent(QueryHistory source) {
        super(source);
    }
}
