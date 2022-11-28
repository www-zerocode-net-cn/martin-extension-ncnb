package com.java2e.martin.extension.ncnb.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * sql执行记录表 
 * </p>
 *
 * @author zerocode
 * @version 1.0
 * @date 2022-11-28
 * @describtion
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="QueryHistory对象", description="sql执行记录表 ")
public class QueryHistory implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "SQL语句")
    private String sqlInfo;

    @ApiModelProperty(value = "乐观锁")
    private Integer revision;

    @ApiModelProperty(value = "删除标识（0-正常,1-删除）")
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    @BindField(entity = User.class, field = CommonConstants.USER_USERNAME)
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    @BindField(entity = User.class, field = CommonConstants.USER_USERNAME)
    private String updater;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
