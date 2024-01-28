package com.java2e.martin.extension.ncnb.mapper;

import com.alibaba.fastjson.JSONObject;
import com.java2e.martin.extension.ncnb.entity.Module;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ModuleMapper extends BaseMapper<Module> {

    JSONObject getModuleById(@Param("id") String id, @Param("name") String name);

    List<String> getModulePathByName(@Param("id") String id, @Param("name") String name);

    JSONObject getModuleByPath(@Param("id") String id, @Param("name") String name, @Param("path") String path);
}
