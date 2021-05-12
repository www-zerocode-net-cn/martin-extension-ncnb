package com.java2e.martin.extension.ncnb.service;

import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.mybatis.service.MartinService;
import com.java2e.martin.extension.ncnb.entity.Code;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 系统代码生成表 服务类
 * </p>
 *
 * @author 狮少
 * @version 1.0
 * @date 2020-09-14
 * @describtion
 * @since 1.0
 */
@Transactional(rollbackFor = Exception.class)
public interface CodeService extends MartinService<Code> {

    /**
     * 生成前后端代码
     *
     * @param code
     * @param response
     * @return
     */
    R generateCode(Code code, HttpServletResponse response);
}
