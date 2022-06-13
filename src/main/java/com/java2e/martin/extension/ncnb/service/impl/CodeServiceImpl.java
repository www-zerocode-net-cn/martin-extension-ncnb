package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.CommonConstants;
import com.java2e.martin.common.data.mybatis.service.impl.MartinServiceImpl;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.entity.Code;
import com.java2e.martin.extension.ncnb.mapper.CodeMapper;
import com.java2e.martin.extension.ncnb.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统代码生成表 服务实现类
 * </p>
 *
 * @author 狮少
 * @version 1.0
 * @date 2020-09-14
 * @describtion
 * @since 1.0
 */
@Slf4j
@Service
public class CodeServiceImpl extends MartinServiceImpl<CodeMapper, Code> implements CodeService {
    @Override
    protected void setEntity() {
        this.clz = Code.class;
    }

    @Override
    public R generateCode(Code code, HttpServletResponse response) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        String pathName = SecurityContextUtil.getUser().getUsername() + IdUtil.simpleUUID();
        String projectPath = System.getProperty("user.dir") + File.separator + pathName;
        // 全局配置
        setGlobalConfig(projectPath, code, mpg);
        //数据源设置
        setDataSource(mpg, code);
        // 包配置
        PackageConfig pc = setPackageConfig(code, mpg);
        // 自定义配置
        setInjectionConfig(mpg, projectPath, pc, code);
        // 配置模板
        setTemplateConfig(mpg);
        // 策略配置
        setStrategyConfig(code, mpg);
        mpg.execute();
        ZipUtil.zip(projectPath);
        File file = new File(projectPath + ".zip");
        response.setHeader("content-type", "image/png");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + pathName + ".zip");
        byte[] buff = new byte[1024];
        //创建缓冲输入流
        BufferedInputStream bis = null;
        OutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int read = bis.read(buff);
            while (read != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                read = bis.read(buff);
            }
        } catch (IOException e) {
            log.error("", e);
            return R.failed("下载失败");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        FileUtil.del(file);
        FileUtil.del(projectPath);
        return R.ok("下载成功");
    }

    private void setStrategyConfig(Code code, AutoGenerator mpg) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("");
        strategy.setSuperServiceClass("MartinService");
        strategy.setSuperServiceImplClass("MartinServiceImpl");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        if (StrUtil.isNotBlank(code.getTablePrefix())) {
            strategy.setTablePrefix(code.getTablePrefix());
        }
        strategy.setEntitySerialVersionUID(true);
        strategy.setLogicDeleteFieldName("del_flag");
        // 公共父类
        strategy.setSuperControllerClass("");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("");
        String tableName = code.getTableName();
        String[] tables = tableName.split(",");
        strategy.setInclude(tables);
        strategy.setControllerMappingHyphenStyle(true);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(new TableFill(CommonConstants.CREATOR, FieldFill.INSERT));
        tableFills.add(new TableFill(CommonConstants.CREATE_TIME, FieldFill.INSERT));
        tableFills.add(new TableFill(CommonConstants.UPDATER, FieldFill.UPDATE));
        tableFills.add(new TableFill(CommonConstants.UPDATE_TIME, FieldFill.UPDATE));
        strategy.setTableFillList(tableFills);
        mpg.setStrategy(strategy);
    }

    private void setTemplateConfig(AutoGenerator mpg) {
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        templateConfig.setController("templates/controller.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
    }

    private void setInjectionConfig(AutoGenerator mpg, String projectPath, PackageConfig pc, Code code) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("moduleName", code.getModuleName());
                map.put("moduleCode", code.getModuleCode());
                setMap(map);
            }
        };
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // ant v5 ui 模板
        String enUsPath = "/templates/ui/locales/en-US.ts.vm";
        String zhCnPath = "/templates/ui/locales/zh-CN.ts.vm";
        String zhTwPath = "/templates/ui/locales/zh-TW.ts.vm";
        String addPath = "/templates/ui/components/Add.tsx.vm";
        String copyPath = "/templates/ui/components/Copy.tsx.vm";
        String deletePath = "/templates/ui/components/Delete.tsx.vm";
        String updatePath = "/templates/ui/components/Update.tsx.vm";
        String indexPath = "/templates/ui/index.tsx.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(enUsPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/locales/" + "en-US.ts";
            }
        });
        focList.add(new FileOutConfig(zhCnPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/locales/" + "zh-CN.ts";
            }
        });
        focList.add(new FileOutConfig(zhTwPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/locales/" + "zh-TW.ts";
            }
        });
        focList.add(new FileOutConfig(addPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/components/" + "Add.tsx";
            }
        });
        focList.add(new FileOutConfig(copyPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/components/" + "Copy.tsx";
            }
        });
        focList.add(new FileOutConfig(deletePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/components/" + "Delete.tsx";
            }
        });
        focList.add(new FileOutConfig(updatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/components/" + "Update.tsx";
            }
        });
        focList.add(new FileOutConfig(indexPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/ui/" + pc.getModuleName() + "/" + tableInfo.getEntityName().toLowerCase()
                        + "/" + "index.tsx";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    }

    private PackageConfig setPackageConfig(Code code, AutoGenerator mpg) {
        PackageConfig pc = new PackageConfig();
        //module 会生成新的模块，每个module一个目录，下面包含controller、entity、service、mapper
        pc.setModuleName(code.getModuleName());
        pc.setParent(code.getParent());
        mpg.setPackageInfo(pc);
        return pc;
    }

    private void setDataSource(AutoGenerator mpg, Code code) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(code.getDbUrl());
        dsc.setDriverName(code.getDbDriverName());
        dsc.setUsername(code.getDbUsername());
        dsc.setPassword(code.getDbPassword());
        dsc.setDbQuery(
                new MySqlQuery() {
                    /**
                     * 重写父类预留查询自定义字段<br>
                     * 这里查询的 SQL 对应父类 tableFieldsSql 的查询字段，默认不能满足你的需求请重写它<br>
                     * 模板中调用：  table.fields 获取所有字段信息，
                     * 然后循环字段获取 field.customMap 从 MAP 中获取注入字段如下  NULL 或者 PRIVILEGES
                     */
                    @Override
                    public String tableFieldsSql() {
                        return "SELECT " +
                                "TABLE_SCHEMA," +
                                "TABLE_NAME," +
                                "COLUMN_NAME," +
                                "ORDINAL_POSITION," +
                                "COLUMN_DEFAULT," +
                                "IS_NULLABLE," +
                                "DATA_TYPE," +
                                "CHARACTER_MAXIMUM_LENGTH," +
                                "NUMERIC_PRECISION," +
                                "NUMERIC_SCALE," +
                                "COLUMN_TYPE," +
                                "COLUMN_KEY," +
                                "EXTRA," +
                                "COLUMN_COMMENT " +
                                " FROM " +
                                " information_schema.`COLUMNS`  " +
                                " WHERE " +
                                " TABLE_SCHEMA = 'martin' " +
                                " AND table_name = '%s' " +
                                " ORDER BY " +
                                " TABLE_NAME," +
                                " ORDINAL_POSITION ";
                    }


                    @Override
                    public String fieldName() {
                        return "COLUMN_NAME";
                    }


                    @Override
                    public String fieldType() {
                        return "COLUMN_TYPE";
                    }


                    @Override
                    public String fieldComment() {
                        return "COLUMN_COMMENT";
                    }


                    @Override
                    public String fieldKey() {
                        return "COLUMN_KEY";
                    }

                    @Override
                    public String[] fieldCustom() {
                        return new String[]{"TABLE_SCHEMA","TABLE_NAME","COLUMN_NAME","ORDINAL_POSITION","COLUMN_DEFAULT","IS_NULLABLE","DATA_TYPE","CHARACTER_MAXIMUM_LENGTH","NUMERIC_PRECISION","NUMERIC_SCALE","COLUMN_TYPE","COLUMN_KEY","EXTRA","COLUMN_COMMENT"};
                    }
                }
        );
        mpg.setDataSource(dsc);
    }

    private void setGlobalConfig(String projectPath, Code code, AutoGenerator mpg) {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setFileOverride(true);
        gc.setAuthor(code.getAuthor());
        gc.setOpen(false);
        gc.setIdType(IdType.UUID);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);
    }
}
