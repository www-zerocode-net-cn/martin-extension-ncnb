package com.java2e.martin.extension.ncnb.entity;

import cn.hutool.json.JSONObject;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/8/8
 * @describtion SketchJsonObject
 * @since 1.0
 */
public class SketchJsonObject extends JSONObject implements Comparable<SketchJsonObject> {
    @Override
    public int compareTo(SketchJsonObject o) {
        return 0;
    }
}
