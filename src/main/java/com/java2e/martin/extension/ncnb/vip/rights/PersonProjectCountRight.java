package com.java2e.martin.extension.ncnb.vip.rights;

import cn.hutool.core.util.StrUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.redis.RedisUtil;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.common.vip.rights.BaseRight;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/5/7 15:59
 * @describtion: PersonProjectCountRight
 */
@Slf4j
@Component
public class PersonProjectCountRight implements BaseRight<Integer> {
    private Integer limit = 1;
    private String redisItem = "person_project_count";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProjectService projectService;

    @Override
    public Integer load() {
        String userId = SecurityContextUtil.getAccessUser().getId();
        String formatRedisKey = StrUtil.format(redisKey, userId);
        String securityCount = redisUtil.hashGet(formatRedisKey, redisItem);
        Integer count = null;
        if (securityCount == null) {
            R<Integer> r = projectService.personProjectCount();
            if (r.valid()) {
                redisUtil.hashPut(formatRedisKey, redisItem, aes.encryptBase64(String.valueOf(r.getData())), timeout);
                count = r.getData();
            }
        } else {
            count = Integer.valueOf(aes.decryptStr(securityCount));
        }
        return count;
    }

    @Override
    public void reset() {
        String userId = SecurityContextUtil.getAccessUser().getId();
        String formatRedisKey = StrUtil.format(redisKey, userId);
        redisUtil.hashPut(formatRedisKey, redisItem, aes.encryptBase64(String.valueOf(this.load() + 1)), timeout);
    }

    @Override
    public boolean valid(boolean reset) {
        Integer count = this.load();
        if (!reset) {
            count = count - 1;
        }
        return count < limit;
    }

    @Override
    public String msg() {
        return StrUtil.format("个人项目已超过{}个", limit);
    }
}
