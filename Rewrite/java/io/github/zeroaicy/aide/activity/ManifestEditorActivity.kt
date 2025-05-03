package io.github.zeroaicy.aide.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aide.ui.rewrite.databinding.ActivityManifestXmlEditorBinding
import io.github.zeroaicy.aide.base.BaseAppActivity
import io.github.zeroaicy.util.Log


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/18
 */

const val CLICK_THRESHOLD = 200

class ManifestEditorActivity : BaseAppActivity<ActivityManifestXmlEditorBinding>() {

    private val systemResourceTheme = mutableListOf<String>()
    private val systemResourceThemeID = mutableListOf<Int>()

    private var xmlPath: String? = null


    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && intent.hasExtra("path")) {
            xmlPath = intent.getStringExtra("path")
            Log.d("XmlEditorActivity", "Path: $xmlPath")
        } else {
            Log.e("XmlEditorActivity", "No path received in Intent!")
        }

        initData()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(_binding.root) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.setPadding(0, statusBarHeight, 0, navigationBarHeight)
            ViewCompat.onApplyWindowInsets(view, insets)
        }
//        setupDrawer()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)/*
        setupDrawerStart()
        setupDrawerEnd()
*/
    }

    private fun initData() {
        /// 初始化系统的主题名
        val systemThemeClass = android.R.style::class.java
        systemThemeClass.fields.forEach { field ->
            if (field.name.startsWith("Theme")) {
                val resID: Int = field.getInt(null)
                val resourceName = resources.getResourceName(resID)
                systemResourceTheme.add("@$resourceName")
                systemResourceThemeID.add(resID)
            }
        }


    }
}