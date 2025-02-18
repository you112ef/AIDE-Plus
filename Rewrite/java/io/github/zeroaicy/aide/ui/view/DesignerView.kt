package io.github.zeroaicy.aide.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import com.aide.ui.rewrite.databinding.LayoutDesignerRootBinding


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/24
 */


const val DISPLAY_VIEW = 0
const val DISPLAY_DESIGN = 1
const val DISPLAY_BLUEPRINT = 2

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

    var interceptTouchEvent: Boolean= true

    private var _displayType: Int = DISPLAY_DESIGN
    var displayType: Int
        get() = _displayType
        set(value) {
            when (displayType) {
                DISPLAY_BLUEPRINT,
                DISPLAY_DESIGN -> interceptTouchEvent = true
                DISPLAY_VIEW -> interceptTouchEvent = false
            }
            _displayType = displayType
            invalidate()
        }

    private val paint: Paint

    private val _binding_designer_root by lazy {
        LayoutDesignerRootBinding.inflate(LayoutInflater.from(context), this, false)
    }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL
        this.removeAllViews()
        this.addView(_binding_designer_root.root)

        setWillNotDraw(false)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        isFocusable = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return interceptTouchEvent
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN->{
                //允许传递点击事件时不触发选中事件
                if (!interceptTouchEvent) return super.onTouchEvent(event)
            }
            MotionEvent.ACTION_CANCEL->{

            }
            MotionEvent.ACTION_UP->{

            }
        }
        return super.onTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }

    private fun toBlueprintPaint() {
        paint.style = Paint.Style.STROKE
        paint.setPathEffect(DashPathEffect(floatArrayOf(5f, 5f), 0f))
        paint.strokeWidth = 2f
        paint.color = -0xbf3b01
    }

    private fun toDesignPaint() {
        paint.style = Paint.Style.STROKE
        paint.setPathEffect(DashPathEffect(floatArrayOf(5f, 5f), 0f))
        paint.strokeWidth = 1f
        paint.color = Color.GRAY
    }

    private fun toSelectPaint() {
        paint.style = Paint.Style.FILL
        paint.setPathEffect(null)
        paint.strokeWidth = 4f
        paint.color = -0xe77909
    }



}
