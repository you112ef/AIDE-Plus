package io.github.zeroaicy.aide.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.aide.ui.rewrite.databinding.ActivityAndroidXmlEditorBinding
import io.github.zeroaicy.aide.base.BaseAppActivity

class AndroidXmlEditorActivity: BaseAppActivity<ActivityAndroidXmlEditorBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

    }
}