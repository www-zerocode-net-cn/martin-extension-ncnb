package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.java2e.martin.extension.ncnb.codegen.mybatis.MybatisPlusCodegen;
import com.java2e.martin.extension.ncnb.codegen.swagger.ApiControllerCodegen;
import com.java2e.martin.extension.ncnb.entity.Code;
import com.java2e.martin.extension.ncnb.exception.CDException;
import com.java2e.martin.extension.ncnb.service.GitlabService;
import com.java2e.martin.extension.ncnb.service.SwaggerCodegenService;
import com.java2e.martin.common.core.api.R;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.config.CodegenConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.CommitPayload;
import org.gitlab4j.api.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/11
 * @describtion SwaggerCodegenServiceImpl
 * @since 1.0
 */
@Service
@Slf4j
public class SwaggerCodegenServiceImpl implements SwaggerCodegenService {
    @Autowired
    private GitlabService gitlabService;

    @Autowired
    private ApiControllerCodegen apiControllerCodegen;

    @Autowired
    private MybatisPlusCodegen mybatisPlusCodegen;

    @Override
    public R springBoot(Map map) {
        String outputDir = System.getProperty("user.dir") + File.separator + IdUtil.simpleUUID() + File.separator;
        //生成 service/mapper
        List<File> services = generateService(outputDir);
        //生成entity/controller
        List<File> apis = generateApi(outputDir);
        //合并两个结果
        apis.addAll(services);
        List<CommitAction> commitActions = null;
        try {
            commitActions = readFileForCommit(apis, outputDir);
        } catch (CDException e) {
            log.error("读取文件内容失败", e);
            return R.failed(e.getMessage());
        }
        //设置CommitAction的action状态
        gitlabService.bindCommitAction("4726ce6c8b394119a6a199eaf690dbe0", commitActions, "master");
        //提交本次生成结果到git
        CommitPayload commitPayload = new CommitPayload();
        commitPayload.setActions(commitActions);
        commitPayload.setAuthorEmail("martin114@foxmail.com");
        commitPayload.setAuthorName("ncnb");
        commitPayload.setBranch("master");
        commitPayload.setCommitMessage("test");
        return gitlabService.commit("4726ce6c8b394119a6a199eaf690dbe0", commitPayload);
//        return null;
    }

    private List<File> generateApi(String outputDir) {
        CodegenConfigurator configurator = new CodegenConfigurator();
        configurator.setLang("spring");
        configurator.setLibrary("spring-boot");
        configurator.setInputSpec("swagger.json");
        configurator.setApiPackage("com.java2e.ncnb.controller");
        configurator.setModelPackage("com.java2e.ncnb.entity");
        configurator.addAdditionalProperty("title", "lc520");
        configurator.addAdditionalProperty("configPackage", "com.java2e.ncnb.configuration");
        configurator.addAdditionalProperty("basePackage", "com.java2e.ncnb");
        //只生成代理接口，具体方法需要自己实现
        configurator.addAdditionalProperty("interfaceOnly", false);
        configurator.addAdditionalProperty("delegatePattern", false);
        configurator.addAdditionalProperty("delegateMethod", false);
        configurator.addAdditionalProperty("singleContentTypes", true);
        configurator.addAdditionalProperty("java8", true);
        //是否异步执行
        configurator.addAdditionalProperty("async", false);
        configurator.addAdditionalProperty("useTags", false);
        configurator.addAdditionalProperty("useBeanValidation", true);
        configurator.addAdditionalProperty("implicitHeaders", false);
        //生成swagger配置
        configurator.addAdditionalProperty("swaggerDocketConfig", true);
        configurator.addAdditionalProperty("useOptional", true);
        //生成Feign注解，只有spring cloud 的时候有效
        configurator.addAdditionalProperty("openFeign", true);
        configurator.addAdditionalProperty("defaultInterfaces", true);

        configurator.setOutputDir(outputDir);


        //配置pom里面的信息
        configurator.setGroupId("com.java2e");
        configurator.setArtifactId("ncnb");
        configurator.setArtifactVersion("1.1");


        ClientOptInput input = configurator.toClientOptInput();
        //生成带注释的api
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.API_DOCS, "true");
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.API_TESTS, "true");
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "true");
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "true");
//        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "swaggerDocumentationConfig.mustache");
        apiControllerCodegen.setGeneratorPropertyDefault(CodegenConstants.WITH_XML, "true");

        //配置需要生成哪些SUPPORTING_FILES文件,需要带着后缀才能生效，比如.java
        //区分大小写
        System.setProperty(CodegenConstants.SUPPORTING_FILES, "SwaggerDocumentationConfig.java,pom.xml");

        //不生成.swagger-codegen/VERSION, .swagger-codegen-ignore
        apiControllerCodegen.setGenerateSwaggerMetadata(false);
        return apiControllerCodegen.opts(input).generate();
    }

    private List<File> generateService(String outputDir) {
        Code code = new Code();
        code.setAuthor("ncnb");
        code.setDbDriverName("com.mysql.cj.jdbc.Driver");
        code.setDbPassword("root");
        code.setDbUrl("jdbc:mysql://martin-mysql:3306/erd?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
        code.setDbUsername("root");
        code.setModuleName("");
        code.setParent("com.java2e.ncnb");
        code.setTableName(new String[]{"api_design"});
        return mybatisPlusCodegen.generateService(code, outputDir);
    }

    /**
     * 提取文件内容准备提交到git
     *
     * @param apis
     * @param outputDir
     * @return
     * @throws CDException
     */
    private List<CommitAction> readFileForCommit(List<File> apis, String outputDir) throws CDException {
        List<CommitAction> commitActions = new ArrayList<>();
        if (CollUtil.isNotEmpty(apis)) {
            commitActions = apis.stream().map(file -> {
                CommitAction commitAction = new CommitAction();
                //替换路径分隔符，git以"/"作为路径分隔符
                String filePath = file.getPath().replace(outputDir, "").replace("\\", "/");
                commitAction.setFilePath(filePath);
                try {
                    commitAction.setContent(FileUtils.getFileContentAsString(file, null));
                } catch (IOException e) {
                    log.error("===生成文件内容报错===", e);
                    throw new CDException("===生成文件内容报错===" + e.getMessage());
                }
                return commitAction;
            }).collect(Collectors.toList());
        }
        return commitActions;
    }
}
