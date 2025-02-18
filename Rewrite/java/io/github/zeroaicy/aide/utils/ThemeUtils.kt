package io.github.zeroaicy.aide.utils

import android.content.Context
import android.util.TypedValue


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/2/4
 */


fun Context.getAttrColor(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}
