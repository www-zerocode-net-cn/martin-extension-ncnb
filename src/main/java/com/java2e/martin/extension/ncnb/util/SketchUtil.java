package com.java2e.martin.extension.ncnb.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.java2e.martin.extension.ncnb.entity.SketchJsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/8/8
 * @describtion SketchUtil
 * @since 1.0
 */
@Slf4j
public class SketchUtil {
    private static final String DIV_TEMPLATE = "<div style=\"left: {}px;top: {}px;position: absolute;{}\">{}";
    private static final String ANTD_TEMPLATE = "<div style=\"left: {}px;top: {}px;position: absolute;{}\">{}</div>";
    private static final String ANTD_COL_HALF = "<Col flex={\"0 0 {}\"}>";
    private static final String ANTD_COL = "<Col flex={\"0 0 {}\"}></Col>";
    private static final String ANTD_COL_NAME = "<Col flex={\"0 0 {}\"}>{}</Col>";
    private static final String ANTD_ROW = "<Row style={{height: '{}'}}></Row>";

    @SneakyThrows
    public static void main(String[] args) {
        LinkedList<String> resultDiv = new LinkedList<>();

        String json = FileUtil.readUtf8String("C:\\Users\\lc\\Desktop\\login.json");

        JSONObject jsonObject = JsonUtil.parse(json, JSONObject.class);
        JSONArray layers = (JSONArray) jsonObject.get("layers");
        log.info("layers: {}", layers.toList(JSONObject.class));
        layers.toList(JSONObject.class).stream().forEach(div -> {
            log.info("{'type': '{}','name':'{}','rect':'{}'}", div.get("type"), div.get("name"), div.get("rect"));
        });
        List<SketchJsonObject> sketchJsonObjects = layers.toList(SketchJsonObject.class);
        log.info("X排序: {}", "===========================");
        sketchJsonObjects = sketchJsonObjects.stream()
                .sorted(((t1, s) -> t1.getByPath("rect.x", Integer.class).compareTo(s.getByPath("rect.x", Integer.class))))
                .collect(Collectors.toList());

        sketchJsonObjects.forEach(div -> {
            log.info("{'type': '{}','name':'{}','rect':'{}'}", div.get("type"), div.get("name"), div.get("rect"));
        });
        log.info("Y排序: {}", "===========================");
        sketchJsonObjects = sketchJsonObjects.stream()
                .sorted(((t1, s) -> t1.getByPath("rect.y", Integer.class).compareTo(s.getByPath("rect.y", Integer.class))))
                .collect(Collectors.toList());
        sketchJsonObjects.forEach(div -> {
            log.info("{'type': '{}','name':'{}','rect':'{}'}", div.get("type"), div.get("name"), div.get("rect"));
        });

        log.info("树结构转换: {}", "===========================");
        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        List<SketchJsonObject> tmp = sketchJsonObjects;
        String parseType = "antd";
        final String[] parentDiv = {""};
        sketchJsonObjects.forEach(div -> {
            if ("antd".equals(parseType)) {
                parentDiv[0] = findParentAntdDiv(tmp, div);
            } else if ("absolute".equals(parseType)) {
                parentDiv[0] = findParentDiv(tmp, div);

            }
            TreeNode<String> node = new TreeNode<>(OBJECTID(div), parentDiv[0], NAME(div), Y(div));
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            BeanUtil.copyProperties(div, objectObjectHashMap);
            node.setExtra(objectObjectHashMap);
            nodeList.add(node);
        });


        //转换器,将结果转换成树结构
        List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0",
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    tree.putAll(treeNode.getExtra());
                });
        log.info("treeNodes: {}", treeNodes);

        List<String> divResult = new LinkedList<>();


        if (StrUtil.equals(parseType, "absolute")) {
            //渲染结果为多级div
            recursionToDiv(treeNodes, divResult);
            //输出文件
            FileUtil.del("C:\\Users\\lc\\Desktop\\DIV.json");
            FileUtil.appendLines(divResult, "C:\\Users\\lc\\Desktop\\DIV.json", "UTF-8");
        } else if (StrUtil.equals(parseType, "antd")) {
            Object rect = treeNodes.get(0).get("rect");
            log.info("rect: {}", rect);
            Double y = JSONUtil.parse(rect).getByPath("y", Double.class);
            log.info("y: {}", y);
            if (y > 0) {
                divResult.add(StrUtil.format(ANTD_ROW, NumberUtil.formatPercent(y / 1080, 2)));
            }
            //渲染结果为antd组件
            recursionToAntd(treeNodes, divResult, sketchJsonObjects);
            //输出文件
            FileUtil.del("C:\\Users\\lc\\Desktop\\antd.json");
            FileUtil.appendLines(divResult, "C:\\Users\\lc\\Desktop\\antd.json", "UTF-8");
        }
    }

    private static SketchJsonObject findParent(List<SketchJsonObject> sketchJsonObjects, String parentId) {
        Optional<SketchJsonObject> firstResult = sketchJsonObjects.stream()
                .filter(f -> OBJECTID(f).equals(parentId))
                .findFirst();
        if (firstResult.isPresent()) {
            SketchJsonObject sketchJsonObject = firstResult.get();
            return sketchJsonObject;
        }
        return null;
    }


    /**
     * 渲染为ant组件
     *
     * @param treeNodes
     * @param divResult
     * @param sketchJsonObjects
     */
    private static void recursionToAntd(List<Tree<String>> treeNodes, List<String> divResult, List<SketchJsonObject> sketchJsonObjects) {
        CopyOnWriteArrayList<Tree<String>> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.addAll(treeNodes);
        copyOnWriteArrayList.stream().forEach(
                tree -> {
                    JSONObject jsonObject = JSONUtil.parseObj(tree);
                    String type = TYPE(jsonObject);
                    boolean isShape = StrUtil.equals(type, "shape") || StrUtil.equals(type, "slice");
                    String formatResult = StrUtil.format(ANTD_TEMPLATE
                            , X(jsonObject)
                            , Y(jsonObject)
                            , fixWidthAndHeight(CSS(jsonObject), isShape).replace("[", "").replace("]", "").replace("\"", "").replace(",", "")
                            , isShape ? "" : jsonObject.get("name")
                    );
                    if (tree.getChildren() != null) {
                        //当前节点为夹心父节点时
                        if (!StrUtil.equals(tree.getParentId(), "0")) {
//                            String childCol = StrUtil.format(ANTD_COL_HALF, NumberUtil.formatPercent(WIDTH(jsonObject) / (Integer) getByPathFromTree(tree, "rect.width"), 2));
//                            divResult.add(childCol);
                            SketchJsonObject parent = findParent(sketchJsonObjects, tree.getParentId());
                            String leftCol = StrUtil.format(ANTD_COL_HALF, NumberUtil.formatPercent(WIDTH(jsonObject) / WIDTH(parent), 2));
                            log.info("{}", leftCol);
                            divResult.add(leftCol);
                            divResult.add("<Row>");
//                        divResult.add(formatResult);
//                        log.info("{}", formatResult);
                            recursionToAntd(tree.getChildren(), divResult, sketchJsonObjects);
                            divResult.add("</Row>");
                            log.info("</Row>");
                            divResult.add("</Col>");
                            log.info("</Col>");
                        } else {
                            //当前节点的父节点为0节点
                            divResult.add("<Row>");
                            String leftCol = StrUtil.format(ANTD_COL, NumberUtil.formatPercent(X(jsonObject) / 1920, 2));
                            log.info("{}", leftCol);
                            divResult.add(leftCol);
//                        divResult.add(formatResult);
//                        log.info("{}", formatResult);
                            recursionToAntd(tree.getChildren(), divResult, sketchJsonObjects);
                            divResult.add("</Row>");
                            log.info("</Row>");
                        }

                    } else {
                        //当前节点为夹心子节点时
                        if (!StrUtil.equals(tree.getParentId(), "0")) {
                            SketchJsonObject parent = findParent(sketchJsonObjects, tree.getParentId());
                            Tree<String> parentTree = tree.getParent();
                            //找出和当前节点高度相同的节点，放到一行内
                            List<Tree<String>> sameHeightEle = StrUtil.equals(parentTree.getId(), "0") ? null : findSameHeightEle(tree.getParent(), jsonObject);
                            if (CollUtil.isNotEmpty(sameHeightEle)) {
                                //同高度有元素
                                sameHeightEle.add(tree);
                                divResult.add("<Row>");
                                log.info("{}", "<Row>");
                                sameHeightEle.stream()
                                        .sorted((s1, s2) -> X(JSON(s1)) < X(JSON(s2)) ? -1 : 1)
                                        .forEach(f -> {
                                            String leftCol = StrUtil.format(ANTD_COL_NAME, NumberUtil.formatPercent(WIDTH(jsonObject) / WIDTH(parent), 2),f.getName());
                                            divResult.add(leftCol);
                                            log.info("{}", leftCol);
                                        });
                                divResult.add("</Row>");
                                log.info("{}", "</Row>");
                            } else {
                                //同高度没有元素
                                String leftCol = StrUtil.format(ANTD_COL, NumberUtil.formatPercent(WIDTH(jsonObject) / WIDTH(parent), 2));
                                divResult.add(leftCol);
                                log.info("{}", leftCol);
                            }
                        } else {
                            //当前节点的父节点为0节点
                            divResult.add("<Row>");
                            log.info("<Row>");
                            String leftCol = StrUtil.format(ANTD_COL, NumberUtil.formatPercent(X(jsonObject) / 1920, 2));
                            log.info("{}", leftCol);
                            divResult.add(leftCol);
                            String eleCol = StrUtil.format(ANTD_COL_HALF, NumberUtil.formatPercent(WIDTH(jsonObject) / 1920, 2));
                            divResult.add(eleCol);
                            log.info(eleCol);
                            divResult.add(tree.get("name").toString());
                            log.info("{}", tree.get("name").toString());
                            divResult.add("</Col>");
                            log.info("</Col>");
                            divResult.add("</Row>");
                            log.info("</Row>");
                        }
                    }
                }
        );
    }


    /**
     * 找出和当前元素同高度的元素
     *
     * @param parentTree
     * @param jsonObject
     */
    private static List<Tree<String>> findSameHeightEle(Tree<String> parentTree, JSONObject jsonObject) {
        List<Tree<String>> children = parentTree.getChildren();
        if (CollUtil.isEmpty(children)) {
            return null;
        }
        List<Tree<String>> repeatTree = new ArrayList<>();
        List<Tree<String>> result = children.stream().filter(tree -> {
            JSONObject current = JSONUtil.parseObj(tree);
            double currentH = Y(current) + NumberUtil.div(HEIGHT(current), new Double(2));
            double jsonH = Y(jsonObject) + NumberUtil.div(HEIGHT(jsonObject), new Double(2));
            boolean b = (currentH == jsonH || Math.abs(currentH - jsonH) < 1);
            if (b) {
                repeatTree.add(tree);
            }
            return b;
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(repeatTree)) {
            children.removeAll(repeatTree);
        }
        return result;
    }


    /**
     * 渲染为绝对定位的html
     *
     * @param treeNodes
     * @param divResult
     */
    private static void recursionToDiv(List<Tree<String>> treeNodes, List<String> divResult) {
        treeNodes.stream().forEach(
                tree -> {
                    JSONObject jsonObject = JSONUtil.parseObj(tree);
                    String type = TYPE(jsonObject);
                    boolean isShape = StrUtil.equals(type, "shape") || StrUtil.equals(type, "slice");
                    String formatResult = StrUtil.format(DIV_TEMPLATE
                            , X(jsonObject)
                            , Y(jsonObject)
                            , fixWidthAndHeight(CSS(jsonObject), isShape).replace("[", "").replace("]", "").replace("\"", "").replace(",", "")
                            , isShape ? "" : jsonObject.get("name")
                    );
                    if (tree.getChildren() != null) {
                        divResult.add("<div>");
                        log.info("{}", formatResult);
                        recursionToDiv(tree.getChildren(), divResult);
                        divResult.add("</div>");
                        log.info("</div>");
                    } else {
                        divResult.add(formatResult + "</div>");
                        log.info("{}", formatResult + "</div>");
                    }
                }
        );
    }


    /**
     * 查找当前节点的父节点
     *
     * @param sketchJsonObjects
     * @param sketchObject
     * @return
     */
    private static String findParentAntdDiv(List<SketchJsonObject> sketchJsonObjects, SketchJsonObject sketchObject) {
        //获取离它最近的父元素作为父元素，因为它有可能有很多个父元素
        Optional<SketchJsonObject> first = sketchJsonObjects.stream()
                .filter(notText -> SHAPE(notText).equals("shape") || SHAPE(notText).equals("slice"))
                .filter(less -> X(less) < X(sketchObject) && Y(less) < Y(sketchObject))
                .filter(large -> X(large) + WIDTH(large) > X(sketchObject) + WIDTH(sketchObject) && Y(large) + HEIGHT(large) > Y(sketchObject) + HEIGHT(sketchObject))
                .sorted((sort1, sort2) -> X(sort1) - X(sort2) > 0 ? -1 : 1)
                .sorted((sort1, sort2) -> Y(sort1) - Y(sort2) > 0 ? -1 : 1)
                .findFirst();


        if (first.isPresent()) {
            return first.get().get("objectID").toString();
        } else {
            return "0";
        }
    }

    private static String SHAPE(SketchJsonObject notText) {
        return notText.getByPath("type", String.class);
    }

    private static Double Y(JSONObject less) {
        return less.getByPath("rect.y", Double.class);
    }

    private static Double X(JSONObject large) {
        return large.getByPath("rect.x", Double.class);
    }


    private static JSONObject JSON(Tree<String> tree) {
        return JSONUtil.parseObj(tree);
    }

    /**
     * 查找当前节点的父节点
     *
     * @param sketchJsonObjects
     * @param sketchObject
     * @return
     */
    private static String findParentDiv(List<SketchJsonObject> sketchJsonObjects, SketchJsonObject sketchObject) {
        final String[] parent = {"0"};
        final boolean[] find = {false};
        for (SketchJsonObject div : sketchJsonObjects) {
            if ("group".equals(div.get("type"))) {
                parent[0] = OBJECTID(div);
            }
            //组不找父节点
            if (!"group".equals(div.get("type"))
                    //类型相同
                    && isEquals(div, sketchObject, "type")
                    //名称相同
                    && isEquals(div, sketchObject, "name")
                    //x相同
                    && isEquals(div, sketchObject, "rect.x")
                    //y相同
                    && isEquals(div, sketchObject, "rect.y")) {
                find[0] = true;
                break;
            }
        }
        if (find[0] && parent[0] != null) {
            return parent[0];
        } else {
            return "0";
        }
    }

    private static boolean isEquals(SketchJsonObject div, SketchJsonObject sketchObject, String key) {
        return div.getByPath(key).equals(sketchObject.getByPath(key));
    }

    private static String fixWidthAndHeight(String css, boolean isShape) {
        if (StrUtil.contains(css, "\"width:")) {
            if (isShape) {
                //形状的宽度保留
                css = ReUtil.replaceAll(css, "(\"width: [0-9]*([.][0-9]+)?)", "$1px");
            } else {
                //字体宽度去掉，不然会变形
                css = ReUtil.replaceAll(css, "(\"width: [0-9]*([.][0-9]+)?;\")", "");
            }
        }
        if (StrUtil.contains(css, "\"height:")) {
            css = ReUtil.replaceAll(css, "(\"height: [0-9]*([.][0-9]+)?)", "$1px");
        }
        if (StrUtil.contains(css, "\"border:")) {
            css = ReUtil.replaceAll(css, "(\"border: [0-9]*([.][0-9]+)?)", "$1px");
        }
        return css;
    }

    private static String NAME(SketchJsonObject div) {
        return div.get("name").toString();
    }

    private static String OBJECTID(SketchJsonObject div) {
        return div.get("objectID").toString();
    }


    private static String TYPE(JSONObject jsonObject) {
        return jsonObject.get("type").toString();
    }

    private static Double WIDTH(JSONObject jsonObject) {
        return jsonObject.getByPath("rect.width", Double.class);
    }

    private static Double HEIGHT(JSONObject jsonObject) {
        return jsonObject.getByPath("rect.height", Double.class);
    }

    private static String CSS(JSONObject jsonObject) {
        return jsonObject.get("css").toString();
    }


    private static Object getByPathFromTree(Tree tree, String path) {
        JSON parse = JSONUtil.parse(tree);
        return parse.getByPath(path, Object.class);
    }
}
