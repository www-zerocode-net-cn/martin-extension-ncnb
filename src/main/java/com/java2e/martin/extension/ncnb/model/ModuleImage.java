package com.java2e.martin.extension.ncnb.model;

import com.deepoove.poi.data.PictureRenderData;
import lombok.Data;

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
public class ModuleImage implements Serializable, Cloneable {
    private String path;
    private InputStream inputStream;
    private PictureRenderData image;

}
