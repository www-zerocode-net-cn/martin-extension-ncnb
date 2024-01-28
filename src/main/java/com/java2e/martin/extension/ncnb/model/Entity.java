package com.java2e.martin.extension.ncnb.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Entity implements Serializable {
    @JSONField(
            ordinal = 10
    )
    private String title;
    @JSONField(
            ordinal = 20
    )
    private String chnname;
    @JSONField(
            ordinal = 30
    )
    private List<Field> fields = new ArrayList();

    public Entity() {
    }

}

