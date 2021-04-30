package com.java2e.martin.extension.ncnb.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * PDMan全局配置表
 * </p>
 *
 * @author 狮少
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Project implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 配置JSON
     */
    @TableField("configJSON")
    private byte[] configJSON;

    /**
     * 项目JSON
     */
    @TableField("projectJSON")
    private byte[] projectJSON;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目介绍
     */
    private String description;

    /**
     * 乐观锁
     */
    private Integer revision;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;


}
