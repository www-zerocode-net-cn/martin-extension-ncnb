package com.java2e.martin.extension.ncnb.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author: liangcan
 * @version: 1.0
 * @date: 2022/11/11 13:34
 * @describtion: ProjectDto
 */
@Data
public class ProjectBaseDto {
    private String id;
    private String projectName;
    private String tags;
    private String description;
    private String type;

}
