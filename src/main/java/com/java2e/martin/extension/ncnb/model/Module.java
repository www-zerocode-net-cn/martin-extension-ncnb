package com.java2e.martin.extension.ncnb.model;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Module implements Serializable, Cloneable {
    @JSONField(
            ordinal = 10
    )
    private String name;
    @JSONField(
            ordinal = 20
    )
    private String code;
    @JSONField(
            ordinal = 30
    )
    private List<Entity> entities = new ArrayList();
    private String key;
    private String label;
    private List<ModuleImage> graphImages;
    public Module() {
    }
}


