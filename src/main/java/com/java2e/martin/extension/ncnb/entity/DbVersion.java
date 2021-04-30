package com.java2e.martin.extension.ncnb.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 狮少
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DbVersion implements Serializable {

    @TableId(type= IdType.UUID)
    private String id;

    private String dbVersion;

    private String versionDesc;

    private String createdTime;

    private String projectId;


}
