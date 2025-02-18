package io.github.zeroaicy.aide.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.SystemProperties
import android.util.Log
import android.view.SurfaceControl
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import java.util.function.Consumer


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2024/9/26
 */


val supportBlur = SystemProperties.getBoolean(
    "ro.surface_flinger.supports_background_blur",
    false
) && !SystemProperties.getBoolean("persist.sys.sf.disable_blurs", false)


fun AlertDialog.setupWindowBlurListener() {
    val window = window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        window!!.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        val blurEnabledListener: Consumer<Boolean> =
            Consumer<Boolean> { enabled -> window.updateWindowForBlurs(enabled) }
        window.decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                window.windowManager.addCrossWindowBlurEnabledListener(blurEnabledListener)
            }

            override fun onViewDetachedFromWindow(v: View) {
                window.windowManager.removeCrossWindowBlurEnabledListener(blurEnabledListener)
            }
        })
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
        setOnShowListener { window?.updateWindowForBlurs(supportBlur) };
    }
}

@SuppressLint("Recycle")
fun Window.updateWindowForBlurs(blursEnabled: Boolean) {
    val mDimAmountWithBlur = 0.1f
    val mDimAmountNoBlur = 0.32f
    setDimAmount(if (blursEnabled) mDimAmountWithBlur else mDimAmountNoBlur)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        attributes = attributes.apply {
            blurBehindRadius = 20
        }
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
        if (blursEnabled) {
            val animator = ValueAnimator.ofInt(1, 53).apply {
                interpolator = DecelerateInterpolator()
            }
            try {
                val viewRootImpl = decorView.viewRootImpl ?: return
                val surfaceControl = viewRootImpl.surfaceControl
                animator.addUpdateListener { animation ->
                    try {
                        SurfaceControl.Transaction().apply {
                            val animatedValue = animation.animatedValue
                            if (animatedValue != null) {
                                setBackgroundBlurRadius(
                                    surfaceControl,
                                    animatedValue as Int
                                )
                            }
                        }.apply()
                    } catch (t: Throwable) {
                        Log.e(
                            "Window.updateWindowForBlurs",
                            "Blur behind dialog builder",
                            t
                        )
                    }
                }
            } catch (t: Throwable) {
                Log.e(
                    "Window.updateWindowForBlurs",
                    "Blur behind dialog builder",
                    t
                )
            }
            decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {}
                override fun onViewDetachedFromWindow(v: View) {
                    animator.cancel()
                }
            })
            animator.start()
        }
    }
}
