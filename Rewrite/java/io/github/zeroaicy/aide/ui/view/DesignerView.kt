package io.github.zeroaicy.aide.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.aide.ui.rewrite.databinding.LayoutDesignerRootBinding


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/24
 */


@SuppressLint("ViewConstructor")
class DesignerView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(
    context, attrs, defStyleAttr, defStyleRes
) {

    val _binding_designer_root by lazy {
        LayoutDesignerRootBinding.inflate(LayoutInflater.from(context), this, false)
    }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL
        this.removeAllViews()
        this.addView(_binding_designer_root.root)
    }


}