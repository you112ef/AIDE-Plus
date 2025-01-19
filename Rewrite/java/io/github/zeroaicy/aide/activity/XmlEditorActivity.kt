package io.github.zeroaicy.aide.activity

import android.os.Bundle
import com.aide.ui.rewrite.databinding.ActivityXmlEditorBinding
import io.github.zeroaicy.aide.base.BaseAppActivity
import io.github.zeroaicy.util.Log


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/18
 */


class XmlEditorActivity : BaseAppActivity<ActivityXmlEditorBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && intent.hasExtra("path")) {
            val path: String? = intent.getStringExtra("path")
            Log.d("XmlEditorActivity", "Path: $path")
        } else {
            Log.e("XmlEditorActivity", "No path received in Intent!");
        }




    }
}