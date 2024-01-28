package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.OssConstants;
import com.java2e.martin.common.oss.service.OssTemplate;
import com.java2e.martin.extension.ncnb.entity.DbChange;
import com.java2e.martin.extension.ncnb.entity.Project;
import com.java2e.martin.extension.ncnb.model.ModuleImage;
import com.java2e.martin.extension.ncnb.service.DbChangeService;
import com.java2e.martin.extension.ncnb.service.GenDocService;
import com.java2e.martin.extension.ncnb.service.ProjectService;
import com.java2e.martin.extension.ncnb.util.StringKit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/7/30
 * @describtion GenDocServiceImpl
 * @since 1.0
 */
@Slf4j
@Service
public class GenDocServiceImpl implements GenDocService {
    private String waterMark = "<w:r><w:rPr><w:noProof/></w:rPr><w:pict w14:anchorId=\"58771E30\"><v:shapetype id=\"_x0000_t136\" coordsize=\"21600,21600\" o:spt=\"136\" adj=\"10800\" path=\"m@7,l@8,m@5,21600l@6,21600e\"><v:formulas><v:f eqn=\"sum #0 0 10800\"/><v:f eqn=\"prod #0 2 1\"/><v:f eqn=\"sum 21600 0 @1\"/><v:f eqn=\"sum 0 0 @2\"/><v:f eqn=\"sum 21600 0 @3\"/><v:f eqn=\"if @0 @3 0\"/><v:f eqn=\"if @0 21600 @1\"/><v:f eqn=\"if @0 0 @2\"/><v:f eqn=\"if @0 @4 21600\"/><v:f eqn=\"mid @5 @6\"/><v:f eqn=\"mid @8 @5\"/><v:f eqn=\"mid @7 @8\"/><v:f eqn=\"mid @6 @7\"/><v:f eqn=\"sum @6 0 @5\"/></v:formulas><v:path textpathok=\"t\" o:connecttype=\"custom\" o:connectlocs=\"@9,0;@10,10800;@11,21600;@12,10800\" o:connectangles=\"270,180,90,0\"/><v:textpath on=\"t\" fitshape=\"t\"/><v:handles><v:h position=\"#0,bottomRight\" xrange=\"6629,14971\"/></v:handles><o:lock v:ext=\"edit\" text=\"t\" shapetype=\"t\"/></v:shapetype><v:shape id=\"PowerPlusWaterMarkObject1584793859\" o:spid=\"_x0000_s2049\" type=\"#_x0000_t136\" style=\"position:absolute;left:0;text-align:left;margin-left:0;margin-top:0;width:470.5pt;height:115pt;rotation:315;z-index:-251657216;mso-position-horizontal:center;mso-position-horizontal-relative:margin;mso-position-vertical:center;mso-position-vertical-relative:margin\" o:allowincell=\"f\" fillcolor=\"silver\" stroked=\"f\"><v:fill opacity=\".5\"/><v:textpath style=\"font-family:&quot;宋体&quot;;font-size:1pt\" string=\"水印\"/></v:shape></w:pict></w:r></xml-fragment>";

    @Autowired
    private OssTemplate minioOssTemplate;

    @Autowired
    private ProjectService projectService;

    @Autowired
    DbChangeService dbChangeService;

    @Override
    @SneakyThrows
    public void genDataBaseDocx(Map params, HttpServletResponse response) {
        String projectId = StringKit.nvl(params.get("projectId"), "");
        String dbKey = StringKit.nvl(params.get("dbKey"), "");
        String doctpl = StringKit.nvl(params.get("doctpl"), "");
        Map imgs = (Map) params.get("imgs");
        Assert.notBlank(projectId, "导出word文档：projectId为空");
        if (StrUtil.isBlank(doctpl)) {
            doctpl = OssConstants.DEFAULT_WORD_PATH;
        }
        String bucket = StrUtil.subBefore(doctpl, StrUtil.SLASH, false);
        log.info("bucket: {}", bucket);
        String fileName = StrUtil.subAfter(doctpl, StrUtil.SLASH, false);
        log.info("fileName: {}", fileName);
        InputStream tplIo = minioOssTemplate.download(bucket, fileName);
        //添加水印
        XWPFDocument xwpfDocument = addWaterMark(tplIo);
        //解析module和项目名称
        Map modulesAndName = parseModule(projectId);
        //填充module
        List<ModuleImage> moduleImages = fillModule(imgs, (List<JSONObject>) modulesAndName.get("modules"));

        //放入version信息
        List<DbChange> versions = dbChangeService.loadHistoryVersion(projectId, dbKey);
        String currentVersion = "0.0.0";
        List<DbChange> changeList = null;
        if (CollUtil.isNotEmpty(versions)) {
            changeList = versions.stream().sorted(Comparator.comparing(DbChange::getVersion, (x, y) -> {
                return VersionComparator.INSTANCE.compare(x, y);
            })).collect(Collectors.toList());
            currentVersion = versions.stream().filter(version -> version.getBaseVersion()).findFirst().get().getVersion();
            log.info("currentVersion: {}", currentVersion);
        }

        HashMap<String, Object> dataModel = new HashMap<>(3);
        dataModel.put("modules", moduleImages);
        dataModel.put("versions", changeList);
        dataModel.put("currentVersion", currentVersion);
        dataModel.put("projectName", modulesAndName.get("projectName"));

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("entities", policy)
                .bind("versions", policy)
                .bind("fields", policy)
                .build();
        XWPFTemplate template = XWPFTemplate.compile(xwpfDocument, config).render(dataModel);

        ServletOutputStream outputStream = response.getOutputStream();

        String type = (String) params.get("type");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + "out_template.docx" + "\"");
        template.write(outputStream);

        template.close();
    }

    @Override
    public void downloadWordTemplate(String doctpl, HttpServletResponse response) {
        if (StrUtil.isBlank(doctpl)) {
            doctpl = OssConstants.DEFAULT_WORD_PATH;
        }
        log.info("doctpl: {},response: {}", doctpl, response);
        String bucket = StrUtil.subBefore(doctpl, StrUtil.SLASH, false);
        log.info("bucket: {}", bucket);
        String fileName = StrUtil.subAfter(doctpl, StrUtil.SLASH, false);
        log.info("fileName: {}", fileName);
        InputStream fis = minioOssTemplate.download(bucket, fileName);
        XWPFDocument xwpfDocument = null;
        try {
            xwpfDocument = addWaterMark(fis);
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            xwpfDocument.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            log.error("download template fail:{}", e);
        } finally {
            if (xwpfDocument != null) {
                try {
                    xwpfDocument.close();
                } catch (IOException e) {
                    log.error("close xwpfDocument fail:{}", e);
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public R uploadWordTemplate(MultipartFile file, String projectId) {
        log.info("file: {},projectId: {}", file, projectId);
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        String minioFileName = OssConstants.PROJECT_MODULE_ERD_BUCKET + projectId + StrUtil.SLASH + fileName;
        return R.ok(minioOssTemplate.upload(OssConstants.DEFAULT_BUCKET, minioFileName, inputStream, false));
    }

    /**
     * 填充module
     *
     * @param imgs
     * @param modules
     * @return
     * @throws IOException
     */
    private List<ModuleImage> fillModule(Map imgs, List<JSONObject> modules) {
        List<ModuleImage> collect = modules.stream().map(jsonObject ->
        {
            ModuleImage moduleImage = JSONUtil.toBean(jsonObject, ModuleImage.class);
            String name = moduleImage.getName();
            PictureRenderData pictureRenderData = null;
            String imageBase = (String) imgs.get(name);
            if (StrUtil.isNotBlank(imageBase)) {
                pictureRenderData = Pictures.ofBase64(imageBase, PictureType.PNG).fitSize().create();
            }
            moduleImage.setImage(pictureRenderData);
            return moduleImage;
        }).collect(Collectors.toList());
        return collect;
    }

    private Map parseModule(String projectId) {
        log.info("projectId: {}", projectId);
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.eq("id", projectId);
        Project project = projectService.getOne(wrapper);

        JSON projectJson = JSONUtil.parse(project.getProjectJSON());
        List<JSONObject> modules = projectJson.getByPath("modules", List.class);
        Map result = new HashMap(2);
        result.put("modules", modules);
        result.put("projectName", project.getProjectName());
        return result;
    }

    private XWPFDocument addWaterMark(InputStream tplIo) throws IOException, XmlException {
        String waterMarkValue = "ERD Online";
        XWPFDocument xwpfDocument = new XWPFDocument(tplIo);
        XWPFHeaderFooterPolicy xFooter = new XWPFHeaderFooterPolicy(xwpfDocument);
        XWPFHeader header = xFooter.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
        List<XWPFParagraph> paragraphs = header.getParagraphs();
        for (XWPFParagraph graph : paragraphs) {
            String paraText = graph.getCTP().xmlText();
            //如果已经有水印了，那么就进行替换
            if (paraText.contains("id=\"PowerPlusWaterMarkObject")) {
                replaceWaterMark(graph, waterMarkValue);
            } else {
                //如果没有水印就添加
                String newParaText = waterMark.replace("水印", waterMarkValue);
                String newText = paraText.replace("</xml-fragment>", newParaText);
                XmlToken token = XmlToken.Factory.parse(newText);
                graph.getCTP().set(token);
            }
        }
        return xwpfDocument;
    }

    /**
     * 将水印中的文字替换成传进来的字符串
     *
     * @param graph          要替换的段落
     * @param waterMarkValue 水印文字
     * @throws IOException
     * @throws XmlException
     */
    private void replaceWaterMark(XWPFParagraph graph, String waterMarkValue) throws XmlException {
        log.info("graph: {},waterMarkValue: {}", graph, waterMarkValue);
        String paraText = graph.getCTP().xmlText();
        //<v:shape id=\"PowerPlusWaterMarkObject
        if (paraText.contains("id=\"PowerPlusWaterMarkObject")) {
            String beginStr = "string=\"";
            int begin = paraText.indexOf(beginStr) + beginStr.length();
            int end = paraText.indexOf("\"", begin);
            String oldWaterMarkText = paraText.substring(begin, end);
            String newText = paraText.replace("string=\"" + oldWaterMarkText + "\"",
                    "string=\"" + waterMarkValue + "\"");
            XmlToken token = XmlToken.Factory.parse(newText);
            graph.getCTP().set(token);
        }
    }
}
