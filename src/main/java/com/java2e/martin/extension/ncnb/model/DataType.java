package com.java2e.martin.extension.ncnb.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion DataType
 * @since 1.0
 */
@Data
public class DataType implements Serializable {
    private String name;
    private String code;
    private String type;

    public DataType() {
    }


}
