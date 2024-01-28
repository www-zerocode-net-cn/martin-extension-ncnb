package com.java2e.martin.extension.ncnb.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.sql.Blob;
import java.io.Serializable;
import com.java2e.martin.common.bean.system.User;
import com.java2e.martin.common.core.annotation.BindField;
import com.java2e.martin.common.core.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 元数据审批 
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-26
 * @describtion
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("db_approval")
@ApiModel(value="Approval对象", description="元数据审批 ")
public class Approval implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "发起人")
    private String promoter;

    @ApiModelProperty(value = "审批版本")
    private String versionId;

    @ApiModelProperty(value = "审批人")
    private String approver;

    @ApiModelProperty(value = "目标数据库连接信息")
    private String  dbInfo;

    @ApiModelProperty(value = "审批SQL")
    private String  approveSql;

    @ApiModelProperty(value = "审批备注")
    private String approveRemark;

    @ApiModelProperty(value = "审批结果")
    private String approveResult;

    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "审批状态")
    private String approveStatus;

    @ApiModelProperty(value = "删除标识（0-正常,1-删除）")
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "乐观锁")
    private Integer revision;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updater;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
