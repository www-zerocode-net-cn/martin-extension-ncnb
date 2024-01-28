package com.java2e.martin.extension.ncnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java2e.martin.common.data.mybatis.bean.JsonBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 模块 Mapper 接口
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-04
 * @describtion
 * @since 1.0
 */
public interface JsonBaseMapper extends BaseMapper<JsonBase> {

    /**
     * 根据名称获取json path
     *
     * @param id
     * @param name
     * @param path
     * @return
     */
    List<String> getPathByName(@Param("id") String id, @Param("name") String name, @Param("path") String path);

    /**
     * 移除部分json
     *
     * @param id
     * @param path
     * @return
     */
    Integer removeJson(@Param("id") String id, @Param("path") String path);

    /**
     * 插入json
     *
     * @param id       项目主键
     * @param path     要插入的path
     * @param uniqPath 指定path下，元素必须唯一
     * @param value    插入值
     * @return
     */
    Integer insertJson(@Param("id") String id, @Param("path") String path, @Param("value") String value);

    /**
     * 验证 json schema
     *
     * @param jsonSchema
     * @param value
     * @return
     */
    boolean jsonSchemaValid(@Param("jsonSchema") String jsonSchema, @Param("value") String value);
}
