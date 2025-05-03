package io.github.zeroaicy.aide.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.aide.ui.rewrite.databinding.ActivityJsonEditorBinding
import io.github.zeroaicy.aide.base.BaseAppActivity

class JsonEditorActivity: BaseAppActivity<ActivityJsonEditorBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }
}