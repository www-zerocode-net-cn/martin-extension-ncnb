package com.java2e.martin.extension.ncnb.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion ParseDataModel
 * @since 1.0
 */
@Data
public class ParseDataModel {
    @JSONField(
            ordinal = 10
    )
    private String dbType;
    @JSONField(
            ordinal = 20
    )
    private Module module;
    @JSONField(
            ordinal = 30
    )
    private Map<String, DataType> dataTypeMap = new TreeMap(new Comparator<String>() {
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    });
    @JSONField(
            ordinal = 40
    )
    private Map<String, Object> properties = new TreeMap(new Comparator<String>() {
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    });

    public ParseDataModel() {
    }

}

