package com.java2e.martin.extension.ncnb.codegen.swagger;

import cn.hutool.core.util.StrUtil;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.InlineModelResolver;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/14
 * @describtion 参考swagger-codegen生成带注释的api
 * @since 1.0
 */
@Component
@Slf4j
public class ApiControllerCodegen extends DefaultGenerator {


    /**
     * 覆盖此方法，改变加载模板的位置，这样就能自定义模板
     *
     * @param name
     * @return
     */
    @Override
    public Reader getTemplateReader(String name) {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(getCPResourcePath(name));
            if (is == null) {
                is = new FileInputStream(new File(name)); // May throw but never return a null value
            }
            return new InputStreamReader(is, "UTF-8");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        throw new RuntimeException("can't load template " + name);
    }

    /**
     * 覆盖该方法，可以自定义模板中的字段，mustache没有表达式，只提供基本的渲染
     *
     * @param config
     * @param tag
     * @param ops
     * @param allModels
     * @return
     */
    @Override
    protected Map<String, Object> processOperations(CodegenConfig config, String tag, List<CodegenOperation> ops, List<Object> allModels) {
        Map<String, Object> operations = new HashMap<String, Object>();
        Map<String, Object> objs = new HashMap<String, Object>();
        objs.put("classname", config.toApiName(tag));
        objs.put("pathPrefix", config.toApiVarName(tag));

        operations.put("upperClassName", StrUtil.upperFirst(tag));
        operations.put("lowerClassName", StrUtil.lowerFirst(tag));
        // check for operationId uniqueness
        Set<String> opIds = new HashSet<String>();
        int counter = 0;
        for (CodegenOperation op : ops) {
            String opId = op.nickname;
            if (opIds.contains(opId)) {
                counter++;
                op.nickname += "_" + counter;
            }
            opIds.add(opId);
        }
        objs.put("operation", ops);

        operations.put("operations", objs);
        operations.put("package", config.apiPackage());


        Set<String> allImports = new TreeSet<String>();
        for (CodegenOperation op : ops) {
            allImports.addAll(op.imports);
        }

        List<Map<String, String>> imports = new ArrayList<Map<String, String>>();
        for (String nextImport : allImports) {
            Map<String, String> im = new LinkedHashMap<String, String>();
            String mapping = config.importMapping().get(nextImport);
            if (mapping == null) {
                mapping = config.toModelImport(nextImport);
            }
            if (mapping != null) {
                im.put("import", mapping);
                if (!imports.contains(im)) { // avoid duplicates
                    imports.add(im);
                }
            }
        }

        operations.put("imports", imports);

        // add a flag to indicate whether there's any {{import}}
        if (imports.size() > 0) {
            operations.put("hasImport", true);
        }
        config.postProcessOperations(operations);
        config.postProcessOperationsWithModels(operations, allModels);
        if (objs.size() > 0) {
            List<CodegenOperation> os = (List<CodegenOperation>) objs.get("operation");

            if (os != null && os.size() > 0) {
                CodegenOperation op = os.get(os.size() - 1);
                op.hasMore = false;
            }
        }
        return operations;
    }

    @Override
    public List<File> generate() {

        if (swagger == null || config == null) {
            throw new RuntimeException("missing swagger input or config!");
        }
        configureGeneratorProperties();
        configureSwaggerInfo();

        // resolve inline models
        InlineModelResolver inlineModelResolver = new InlineModelResolver();
        inlineModelResolver.flatten(swagger);

        List<File> files = new ArrayList<File>();
        // models
        List<Object> allModels = new ArrayList<Object>();
        //generateModels(files, allModels);
        // apis
        List<Object> allOperations = new ArrayList<Object>();
        generateApis(files, allOperations, allModels);

        // supporting files
        Map<String, Object> bundle = buildSupportFileBundle(allOperations, allModels);
//        generateSupportingFiles(files, bundle);
        config.processSwagger(swagger);
        return files;
    }

    protected void generateApis(List<File> files, List<Object> allOperations, List<Object> allModels) {
        if (!isGenerateApis) {
            return;
        }
        Map<String, List<CodegenOperation>> paths = processPaths(swagger.getPaths());
        Set<String> apisToGenerate = null;
        String apiNames = System.getProperty("apis");
        if (apiNames != null && !apiNames.isEmpty()) {
            apisToGenerate = new HashSet<String>(Arrays.asList(apiNames.split(",")));
        }
        if (apisToGenerate != null && !apisToGenerate.isEmpty()) {
            Map<String, List<CodegenOperation>> updatedPaths = new TreeMap<String, List<CodegenOperation>>();
            for (String m : paths.keySet()) {
                if (apisToGenerate.contains(m)) {
                    updatedPaths.put(m, paths.get(m));
                }
            }
            paths = updatedPaths;
        }
        for (String tag : paths.keySet()) {
            try {
                List<CodegenOperation> ops = paths.get(tag);
                Collections.sort(ops, new Comparator<CodegenOperation>() {
                    @Override
                    public int compare(CodegenOperation one, CodegenOperation another) {
                        return ObjectUtils.compare(one.operationId, another.operationId);
                    }
                });
                Map<String, Object> operation = processOperations(config, tag, ops, allModels);

                operation.put("hostWithoutBasePath", getHostWithoutBasePath());
                operation.put("basePath", basePath);
                operation.put("basePathWithoutHost", basePathWithoutHost);
                operation.put("contextPath", contextPath);
                operation.put("baseName", tag);
                operation.put("apiPackage", config.apiPackage());
                operation.put("modelPackage", config.modelPackage());
                operation.putAll(config.additionalProperties());
                operation.put("classname", config.toApiName(tag));
                operation.put("classVarName", config.toApiVarName(tag));
                operation.put("importPath", config.toApiImport(tag));
                operation.put("classFilename", config.toApiFilename(tag));

                if (!config.vendorExtensions().isEmpty()) {
                    operation.put("vendorExtensions", config.vendorExtensions());
                }

                // Pass sortParamsByRequiredFlag through to the Mustache template...
                boolean sortParamsByRequiredFlag = true;
                if (this.config.additionalProperties().containsKey(CodegenConstants.SORT_PARAMS_BY_REQUIRED_FLAG)) {
                    sortParamsByRequiredFlag = Boolean.valueOf(this.config.additionalProperties().get(CodegenConstants.SORT_PARAMS_BY_REQUIRED_FLAG).toString());
                }
                operation.put("sortParamsByRequiredFlag", sortParamsByRequiredFlag);

                processMimeTypes(swagger.getConsumes(), operation, "consumes");
                processMimeTypes(swagger.getProduces(), operation, "produces");

                allOperations.add(new HashMap<String, Object>(operation));
                for (int i = 0; i < allOperations.size(); i++) {
                    Map<String, Object> oo = (Map<String, Object>) allOperations.get(i);
                    if (i < (allOperations.size() - 1)) {
                        oo.put("hasMore", "true");
                    }
                }

                for (String templateName : config.apiTemplateFiles().keySet()) {
                    if(templateName.contains("apiController")){
                        continue;
                    }
                    String filename = config.apiFilename(templateName, tag);
                    if (!config.shouldOverwrite(filename) && new File(filename).exists()) {
                        LOGGER.info("Skipped overwriting " + filename);
                        continue;
                    }

                    File written = processTemplateToFile(operation, templateName, filename);
                    if (written != null) {
                        files.add(written);
                    }
                }

                if(isGenerateApiTests) {
                    // to generate api test files
                    for (String templateName : config.apiTestTemplateFiles().keySet()) {
                        String filename = config.apiTestFilename(templateName, tag);
                        // do not overwrite test file that already exists
                        if (new File(filename).exists()) {
                            LOGGER.info("File exists. Skipped overwriting " + filename);
                            continue;
                        }

                        File written = processTemplateToFile(operation, templateName, filename);
                        if (written != null) {
                            files.add(written);
                        }
                    }
                }


                if(isGenerateApiDocumentation) {
                    // to generate api documentation files
                    for (String templateName : config.apiDocTemplateFiles().keySet()) {
                        String filename = config.apiDocFilename(templateName, tag);
                        if (!config.shouldOverwrite(filename) && new File(filename).exists()) {
                            LOGGER.info("Skipped overwriting " + filename);
                            continue;
                        }

                        File written = processTemplateToFile(operation, templateName, filename);
                        if (written != null) {
                            files.add(written);
                        }
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException("Could not generate api file for '" + tag + "'", e);
            }
        }
        if (System.getProperty("debugOperations") != null) {
            LOGGER.info("############ Operation info ############");
            Json.prettyPrint(allOperations);
        }

    }

    private String getHostWithoutBasePath() {
        StringBuilder hostBuilder = new StringBuilder();
        hostBuilder.append(getScheme());
        hostBuilder.append("://");
        if (!StringUtils.isEmpty(swagger.getHost())) {
            hostBuilder.append(swagger.getHost());
        } else {
            hostBuilder.append("localhost");
            LOGGER.warn("'host' not defined in the spec. Default to 'localhost'.");
        }
        return hostBuilder.toString();
    }

}
