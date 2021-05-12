package com.java2e.martin.extension.ncnb.codegen.mybatis;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.java2e.martin.extension.ncnb.entity.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/15
 * @describtion 生成controller/service/mapper
 * @since 1.0
 */
@Slf4j
@Component
public class MybatisPlusCodegen {
    public List<File> generateService(Code code, String outputDir) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        log.info("==生成代码保存路径=={}", outputDir);
        // 全局配置
        setGlobalConfig(outputDir, code, mpg);
        //数据源设置
        setDataSource(mpg, code);
        // 配置模板
        setTemplateConfig(mpg);
        // 策略配置
        setStrategyConfig(code, mpg);
        // 包配置
        PackageConfig pc = setPackageConfig(code, mpg);
        // 自定义配置
        setInjectionConfig(mpg, outputDir, pc, code);
        mpg.execute();
        List<File> files = FileUtil.loopFiles(outputDir);
        //过滤掉entity与controller，交给swagger生成，因为有些校验需要swagger控制
        files.stream()
                .forEach(file -> {
                    if (file.getPath().contains(File.separator + "entity") || file.getPath().contains(File.separator + "controller")) {
                        FileUtil.del(file);
                    }
                });
        log.info("==共生成{}个文件=={}",
                files.size(),
                FileUtil.loopFiles(outputDir).stream().map(file -> file.getPath()).collect(Collectors.toList())
        );


        return FileUtil.loopFiles(outputDir);
    }

    private void setStrategyConfig(Code code, AutoGenerator mpg) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("");
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
        //配置哪些表要生成代码
        String tables = code.getTableName();
        strategy.setInclude(tables);
        strategy.setControllerMappingHyphenStyle(true);
        ArrayList<TableFill> tableFills = new ArrayList<>();
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

    private void setInjectionConfig(AutoGenerator mpg, String outputDir, PackageConfig pc, Code code) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("moduleName", code.getModuleName());
                setMap(map);
            }
        };
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return outputDir + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
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
                    public String[] fieldCustom() {
                        return new String[]{"NULL", "PRIVILEGES"};
                    }
                }
        );
        mpg.setDataSource(dsc);
    }

    private void setGlobalConfig(String outputDir, Code code, AutoGenerator mpg) {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir + "src/main/java");
        gc.setFileOverride(true);
        gc.setAuthor(code.getAuthor());
        gc.setOpen(false);
        //实体属性 Swagger2 注解
        gc.setSwagger2(true);
        //不生成controller/entity，交给swagger去生成
        gc.setControllerName("");
        gc.setEntityName("");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);
    }


}
