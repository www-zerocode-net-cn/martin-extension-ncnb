package com.java2e.martin.extension.ncnb.entity;

import cn.hutool.core.lang.tree.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/8/8
 * @describtion DivNode
 * @since 1.0
 */
@Data
public class DivNode<SketchJsonObject> extends TreeNode<SketchJsonObject> {
    private static final long serialVersionUID = -7573117350348307760L;

    public DivNode(SketchJsonObject id, SketchJsonObject parentId) {
        this.setId(id);
        this.setParentId(parentId);
    }
}
