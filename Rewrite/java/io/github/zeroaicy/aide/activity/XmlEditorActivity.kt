package io.github.zeroaicy.aide.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.aide.ui.rewrite.R
import com.aide.ui.rewrite.databinding.ActivityXmlEditorBinding
import io.github.dingyi222666.view.treeview.Tree
import io.github.dingyi222666.view.treeview.TreeNode
import io.github.dingyi222666.view.treeview.TreeView
import io.github.zeroaicy.aide.base.BaseAppActivity
import io.github.zeroaicy.aide.bean.XmlElement
import io.github.zeroaicy.aide.bean.XmlParser
import io.github.zeroaicy.aide.utils.parse.XmlBinder
import io.github.zeroaicy.aide.utils.parse.XmlNodeGenerator
import io.github.zeroaicy.util.Log
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.abs


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/18
 */

const val CLICK_THRESHOLD = 200

class XmlEditorActivity : BaseAppActivity<ActivityXmlEditorBinding>() {

    private val systemResourceTheme = mutableListOf<String>()
    private val systemResourceThemeID = mutableListOf<Int>()

    private var xmlPath: String? = null


    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isDrawerStartOpened) {
                    closeDrawer(GravityCompat.START)
                } else if (isDrawerEndOpened) {
                    closeDrawer(GravityCompat.END)
                } else {
                    finish()
                    // 关闭界面逻辑
                }
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && intent.hasExtra("path")) {
            xmlPath = intent.getStringExtra("path")
            _binding.layoutDesignerStart.toolbar.subtitle = xmlPath
            Log.d("XmlEditorActivity", "Path: $xmlPath")
        } else {
            Log.e("XmlEditorActivity", "No path received in Intent!")
        }

        initData()
        this.enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(_binding.root) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.setPadding(0, statusBarHeight, 0, navigationBarHeight)
            ViewCompat.onApplyWindowInsets(view, insets)
        }
        setupDrawer()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        setupDrawerStart()
        setupDrawerEnd()

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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDrawer() {
        _binding.layoutDesignerRoot.openEnd.apply {
            setOnClickListener {
                openDrawer(GravityCompat.END)
            }
            setVerticalDragListener()
        }
        _binding.layoutDesignerRoot.openStart.apply {
            setOnClickListener {
                openDrawer(GravityCompat.START)
            }
            setVerticalDragListener()
        }


        _binding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(view: View) {
                lockDrawer()
            }

            override fun onDrawerClosed(view: View) {
                unlockDrawer()
            }
        })


        _binding.drawerLayout.setOnTouchListener { _: View?, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeDrawerAll()
            }
            false
        }

    }

    private fun setupDrawerStart() {
        _binding.layoutDesignerStart.apply {
            linearController.setOnClickListener {
                if (baseSettings.visibility == View.VISIBLE) {
                    imageviewController.setImageResource(R.drawable.ic_arrow_drop_down_24px)
                    baseSettings.visibility = View.GONE
                }else if (baseSettings.visibility == View.GONE){
                    baseSettings.visibility = View.VISIBLE
                    imageviewController.setImageResource(R.drawable.ic_arrow_drop_up_24px)

                }
                designerUiModeDisplay.setThreshold(99999)
                designerUiModeDisplay.clearFocus()

            }

            designerUiVisibilityIsShowAppbar.setOnCheckedChangeListener { _, isChecked ->
                _binding.layoutDesignerRoot.appbar.visibility =
                    if (isChecked) View.VISIBLE else View.GONE
            }

            designerUiVisibilityIsShowSystemUi.setOnCheckedChangeListener { _, isChecked ->
                val windowInsetsController =
                    WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                if (!isChecked) {
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

                    val lp: WindowManager.LayoutParams = window.attributes
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        lp.layoutInDisplayCutoutMode =
                            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    }
                    window.setAttributes(lp)
                } else {
                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                }
            }
        }


    }

    private fun setupDrawerEnd(){
        _binding.layoutDesignerEnd.apply {
            treeview.apply {
                supportHorizontalScroll = true
                bindCoroutineScope(lifecycleScope)
                selectionMode = TreeView.SelectionMode.NONE
                val tree = Tree.createTree<XmlElement>()
                tree.generator = XmlNodeGenerator()
                this.tree = tree
                binder = XmlBinder()
                val rootElement = XmlParser().parse(File(xmlPath.toString()).inputStream())
                tree.rootNode = TreeNode(
                    data = rootElement,
                    depth = 0,
                    name = rootElement.name,
                    id = Tree.ROOT_NODE_ID,
                    hasChild = rootElement.children.isNotEmpty(),
                    isChild = true,
                    expand = true
                )
            }

            lifecycleScope.launch {
                treeview.refresh()
            }
        }
    }


    fun openDrawer(gravity: Int) {
        _binding.drawerLayout.openDrawer(gravity)
    }


    fun closeDrawer(gravity: Int) {
        _binding.drawerLayout.closeDrawer(gravity)
    }

    fun closeDrawerAll() {
        closeDrawer(GravityCompat.START)
        closeDrawer(GravityCompat.END)
    }

    fun lockDrawer() {
        if (isDrawerStartOpened)
            _binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_OPEN,
                GravityCompat.START
            )
        if (isDrawerEndOpened)
            _binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_OPEN,
                GravityCompat.END
            )
    }

    fun unlockDrawer() {
        if (!isDrawerStartOpened)
            _binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.START
            )
        if (!isDrawerEndOpened)
            _binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END
            )
    }

    fun isDrawerLocked(gravity: Int): Boolean =
        _binding.drawerLayout.getDrawerLockMode(gravity) == DrawerLayout.LOCK_MODE_LOCKED_OPEN

    val isDrawerStartOpened: Boolean
        get() = _binding.drawerLayout.isDrawerOpen(GravityCompat.START)

    val isDrawerEndOpened: Boolean
        get() = _binding.drawerLayout.isDrawerOpen(GravityCompat.END)


    private var initialY = 0f
    private var currentTranslationY = 0f
    private var isDragging = false

    @SuppressLint("ClickableViewAccessibility")
    private fun View.setVerticalDragListener() {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.rawY
                    isDragging = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.rawY - initialY
                    if (abs(deltaY) > 20) {  // 增加拖动判定阈值
                        isDragging = true
                        currentTranslationY += deltaY
                        translationY = currentTranslationY
                        translationY = currentTranslationY
                        initialY = event.rawY
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        performClick()
                    }
                    true
                }

                else -> false
            }
        }
    }
}

private fun toggleContentWithHeightAnimation(target: View) {
    if (target.visibility == View.VISIBLE) {
        val initialHeight = target.height
        val anim = ValueAnimator.ofInt(initialHeight, 0).apply {
            duration = 300
            addUpdateListener {
                target.layoutParams.height = it.animatedValue as Int
                target.requestLayout()
            }
            doOnEnd {
                target.visibility = View.GONE
                target.layoutParams.height = initialHeight
            }
        }
        anim.start()
    } else {
        target.visibility = View.VISIBLE
        target.measure(
            View.MeasureSpec.makeMeasureSpec((target.parent as View).width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val targetHeight = target.measuredHeight
        val anim = ValueAnimator.ofInt(0, targetHeight).apply {
            duration = 300
            addUpdateListener {
                target.layoutParams.height = it.animatedValue as Int
                target.requestLayout()
            }
            doOnEnd {
                target.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
        anim.start()
    }
}