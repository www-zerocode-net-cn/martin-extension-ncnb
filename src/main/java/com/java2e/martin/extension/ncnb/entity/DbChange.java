package com.java2e.martin.extension.ncnb.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.java2e.martin.common.bean.system.User;
import com.java2e.martin.common.core.annotation.BindField;
import com.java2e.martin.common.core.constant.CommonConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 版本表
 * </p>
 *
 * @author 狮少
 * @since 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("db_change")
public class DbChange implements Serializable {

    @TableId(type = IdType.UUID)
    private String id;

    private Boolean baseVersion;

    /**
     * 版本变动
     */
    private byte[] changes;

    /**
     * project主键
     */
    private String projectId;

    /**
     * 数据库标识
     */
    private String dbKey;

    /**
     * project配置
     */
    @TableField("projectJSON")
    private byte[] projectJSON;

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本创建时间
     */
    private String versionDate;

    /**
     * 版本描述
     */
    private String versionDesc;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @BindField(entity = User.class, field = CommonConstants.USER_USERNAME)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
