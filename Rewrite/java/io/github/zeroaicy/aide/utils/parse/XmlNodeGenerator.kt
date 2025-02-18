package io.github.zeroaicy.aide.utils.parse


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/31
 */


import io.github.dingyi222666.view.treeview.AbstractTree
import io.github.dingyi222666.view.treeview.TreeNode
import io.github.dingyi222666.view.treeview.TreeNodeGenerator
import io.github.zeroaicy.aide.bean.XmlElement

class XmlNodeGenerator : TreeNodeGenerator<XmlElement> {

    override suspend fun fetchChildData(targetNode: TreeNode<XmlElement>): Set<XmlElement> {
        return targetNode.data!!.children.toSet()
    }

    override fun createNode(
        parentNode: TreeNode<XmlElement>,
        currentData: XmlElement,
        tree: AbstractTree<XmlElement>
    ): TreeNode<XmlElement> {
        return TreeNode(
            data = currentData,
            depth = parentNode.depth + 1,
            name = currentData.name,
            id = tree.generateId(),
            hasChild = currentData.children.isNotEmpty(),
            isChild = currentData.children.isNotEmpty(),
            expand = false
        )
    }

    override fun createRootNode(): TreeNode<XmlElement>? {
        return null // 由解析器创建根节点
    }
}