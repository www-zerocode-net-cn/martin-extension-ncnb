package com.java2e.martin.extension.ncnb.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.java2e.martin.common.data.mybatis.config.ErdJsonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName(value = "project",autoResultMap = true)
public class Project implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 配置JSON
     */
    @TableField(value = "configJSON",typeHandler = ErdJsonTypeHandler.class)
    private JSONObject configJSON;

    /**
     * 项目JSON
     */
    @TableField(value = "projectJSON",typeHandler = ErdJsonTypeHandler.class)
    private JSONObject projectJSON;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目介绍
     */
    private String description;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目标签
     */
    private String tags;

    /**
     * 乐观锁
     */
    private Integer revision;

    @ApiModelProperty(value = "删除标识（0-正常,1-删除）")
    @TableLogic
    private String delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
