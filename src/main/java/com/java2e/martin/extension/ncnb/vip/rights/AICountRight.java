package com.java2e.martin.extension.ncnb.vip.rights;

import cn.hutool.core.util.StrUtil;
import com.java2e.martin.common.data.redis.RedisUtil;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.common.vip.rights.BaseRight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/5/7 15:59
 * @describtion: ProjectVersionCountRight
 */
@Slf4j
@Component
public class AICountRight implements BaseRight<Integer> {
    private Integer limit = 10;
    public static final String redisItem = "ai_count";

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public Integer load() {
        String userId = SecurityContextUtil.getAccessUser().getId();
        String formatRedisKey = StrUtil.format(redisKey, userId);
        String securityCount = redisUtil.hashGet(formatRedisKey, redisItem);
        Integer count = Integer.valueOf(aes.decryptStr(securityCount));
        log.info("count: {}", count);
        return securityCount == null ? 0 : count;
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
        return StrUtil.format("AI建模次数已超过{}个,请隔天再用", limit);
    }
}
