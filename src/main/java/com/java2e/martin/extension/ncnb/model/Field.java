package com.java2e.martin.extension.ncnb.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Field implements Serializable {
    @JSONField(
            ordinal = 10
    )
    private String name;
    @JSONField(
            ordinal = 20
    )
    private String type;
    @JSONField(
            ordinal = 30
    )
    private String chnname;
    @JSONField(
            ordinal = 40
    )
    private String remark = "";
    @JSONField(
            ordinal = 50
    )
    private boolean pk;
    @JSONField(
            ordinal = 60
    )
    private boolean notNull;
    @JSONField(
            ordinal = 70
    )
    private boolean autoIncrement;
    @JSONField(
            ordinal = 80
    )
    private String defaultValue = "";

    public Field() {
    }

    public String toString() {
        return "Field{name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", remark='" + this.remark + '\'' + ", chnname='" + this.chnname + '\'' + ", pk=" + this.pk + ", notNull=" + this.notNull + ", autoIncrement=" + this.autoIncrement + ", defaultValue='" + this.defaultValue + '\'' + '}';
    }
}
