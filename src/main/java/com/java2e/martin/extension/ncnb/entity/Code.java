package com.java2e.martin.extension.ncnb.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统代码生成表
 * </p>
 *
 * @author 狮少
 * @version 1.0
 * @date 2020-09-14
 * @describtion
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_code")
@ApiModel(value = "Code对象", description = "系统代码生成表")
public class Code implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private Long id;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表注释")
    private String tableComment;

    @ApiModelProperty(value = "表前缀")
    private String tablePrefix;

    @ApiModelProperty(value = "所属模块")
    private String moduleName;


    @ApiModelProperty(value = "模块编码")
    private String moduleCode;

    @ApiModelProperty(value = "包名")
    private String parent;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "url")
    private String dbUrl;

    @ApiModelProperty(value = "驱动名称")
    private String dbDriverName;

    @ApiModelProperty(value = "用户名")
    private String dbUsername;

    @ApiModelProperty(value = "密码")
    private String dbPassword;

    @ApiModelProperty(value = "所属租户")
    private Integer tenantId;

    @ApiModelProperty(value = "删除标识（0-正常,1-删除）")
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.UPDATE)
    private String updater;


    private String menu;


}
