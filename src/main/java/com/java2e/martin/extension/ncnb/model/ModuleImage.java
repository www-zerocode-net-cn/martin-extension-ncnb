package com.java2e.martin.extension.ncnb.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.deepoove.poi.data.PictureRenderData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion ModuleImage
 * @since 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleImage implements Serializable, Cloneable {
    private String  name;
    private String  chnname;
    private JSONArray entities;
    private JSONArray graphCanvas;
    private JSONArray associations;

    private PictureRenderData image;

}
