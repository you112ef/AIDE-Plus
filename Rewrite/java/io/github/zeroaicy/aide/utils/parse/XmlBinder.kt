package io.github.zeroaicy.aide.utils.parse


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/31
 */


import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.aide.ui.rewrite.R
import com.aide.ui.rewrite.databinding.LayoutDesignerTagChildBinding
import com.aide.ui.rewrite.databinding.LayoutDesignerTagParentBinding
import io.github.dingyi222666.view.treeview.TreeNode
import io.github.dingyi222666.view.treeview.TreeNodeEventListener
import io.github.dingyi222666.view.treeview.TreeView
import io.github.dingyi222666.view.treeview.TreeViewBinder
import io.github.zeroaicy.aide.bean.XmlElement
import io.github.zeroaicy.aide.utils.getAttrColor

class XmlBinder : TreeViewBinder<XmlElement>(), TreeNodeEventListener<XmlElement> {

    override fun createView(parent: ViewGroup, viewType: Int): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            LayoutDesignerTagParentBinding.inflate(layoutInflater, parent, false).root
        } else {
            LayoutDesignerTagChildBinding.inflate(layoutInflater, parent, false).root
        }
    }


    override fun bindView(
        holder: TreeView.ViewHolder,
        node: TreeNode<XmlElement>,
        listener: TreeNodeEventListener<XmlElement>
    ) {
        val view = holder.itemView

        val itemView = view.findViewById<Space>(R.id.space)!!
        itemView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            width = node.depth * 15.dp
        }

        val tvName = view.findViewById<TextView>(R.id.tv_name)!!
        tvName.setTextColor(tvName.context.getAttrColor(R.attr.colorOnSurface))
        val element = node.data!!
        tvName.text = element.name
    }

    override fun getItemViewType(node: TreeNode<XmlElement>): Int {
        return if (node.isChild) 1 else 0
    }

    override fun onClick(node: TreeNode<XmlElement>, holder: TreeView.ViewHolder) {
        //super.onClick(node, holder)
        val view = holder.itemView
        val tvName = view.findViewById<TextView>(R.id.tv_name)!!
        tvName.setTextColor(Color.parseColor("#2196F2"))
    }

}


inline val Int.dp get() = (Resources.getSystem().displayMetrics.density * this + 0.5f).toInt()