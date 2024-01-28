package com.java2e.martin.extension.ncnb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * api设计表
 * </p>
 *
 * @author shishao
 * @version 1.0
 * @date 2020-12-10
 * @describtion
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiDesign implements Serializable {


    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * gitlab配置信息
     */
    private byte[] gitlabConfig;

    /**
     * swagger 配置信息
     */
    private byte[] swaggerConfig;

    /**
     * swagger可配置项
     */
    private byte[] swaggerProps;

    /**
     * 表关系
     */
    private byte[] relationConfig;

    /**
     * 是否已经初始化CI
     */
    private Boolean flagStatusCi;


}
