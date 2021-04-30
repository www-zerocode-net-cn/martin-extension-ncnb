package com.java2e.martin.extension.ncnb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class DbChange implements Serializable {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("baseVersion")
    private Boolean baseVersion;

    /**
     * 版本变动
     */
    private byte[] changes;

    /**
     * project主键
     */
    @TableField("projectId")
    private String projectId;

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
    @TableField("versionDate")
    private String versionDate;

    /**
     * 版本描述
     */
    @TableField("versionDesc")
    private String versionDesc;


}
