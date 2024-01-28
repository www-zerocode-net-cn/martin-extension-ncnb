package com.java2e.martin.extension.ncnb.mapper;

import com.java2e.martin.extension.ncnb.entity.Approval;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 元数据审批  Mapper 接口
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-26
 * @describtion
 * @since 1.0
 */
public interface ApprovalMapper extends BaseMapper<Approval> {

    Integer syncBdVersion(@Param("versionId") String versionId,@Param("userId") String userId);
}
