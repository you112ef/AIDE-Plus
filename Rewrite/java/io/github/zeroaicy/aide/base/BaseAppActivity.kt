@file:Suppress("DEPRECATION")

package io.github.zeroaicy.aide.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.viewbinding.ViewBinding
import com.aide.ui.rewrite.R
import com.hjq.language.MultiLanguages
import com.hjq.language.OnLanguageListener
import io.github.zeroaicy.aide.preference.ZeroAicySetting
import io.github.zeroaicy.aide.ui.windowpreferences.WindowPreferencesManager
import io.github.zeroaicy.aide.utils.ActivityBinding
import io.github.zeroaicy.aide.utils.ActivityBindingDelegate
import io.github.zeroaicy.util.BarUtils
import io.github.zeroaicy.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Locale

/*
author : 罪慾
date : 2024/12/25 09:59
description : QQ3115093767
*/


abstract class BaseAppActivity<VB : ViewBinding> : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate() {

    protected val mScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (shouldApplyEdgeToEdgePreference()) {
            val windowPreferencesManager = WindowPreferencesManager(this)
            windowPreferencesManager.applyEdgeToEdgePreference(window)
        }

        this.setContentViewWithBinding()
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        enableFollowSystem(true)
    }


    override fun onResume() {
        super.onResume()
        enableFollowSystem(true)
    }

    override fun onStart() {
        super.onStart()
        setStatusBar(window)
    }


    override fun attachBaseContext(newBase: Context) {
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(newBase))
    }

    open fun shouldApplyEdgeToEdgePreference(): Boolean {
        return true
    }


    @SuppressLint("ObsoleteSdkInt")
    fun setStatusBar(window: Window) {
        setNavBar(window)
        if (true) {
            if (Build.VERSION.SDK_INT < 23 && ZeroAicySetting.isLightTheme()) {
                window.statusBarColor = getThemeAttrColor(android.R.attr.colorPrimaryDark)
            } else {
                window.statusBarColor = getThemeAttrColor(android.R.attr.colorPrimary)
            }
        } else {
            window.statusBarColor = getThemeAttrColor(android.R.attr.colorPrimaryDark)
        }

        if (Build.VERSION.SDK_INT >= 23) {
            val decorView = window.decorView
            if (ZeroAicySetting.isLightTheme()) {
                decorView.systemUiVisibility = decorView.systemUiVisibility or 8192
            } else {
                decorView.systemUiVisibility = decorView.systemUiVisibility and -8193
            }
        }
    }


    private fun setNavBar(window: Window) {
        if (Build.VERSION.SDK_INT >= 27 && ZeroAicySetting.isLightTheme()) {
            BarUtils.setNavBarColor(
                window,
                ContextCompat.getColor(window.context, android.R.color.white)
            )
            BarUtils.setNavBarLightMode(window, true)
            return
        }

        if (!ZeroAicySetting.isLightTheme() && dp2px(BarUtils.getNavBarHeight().toFloat()) < dp2px(
                50f
            )
        ) {
            BarUtils.setNavBarColor(window, "#ff212121".toColorInt())
            //BarUtils.setNavBarColor(window, ContextCompat.getColor(window.getContext(), R.color.app_background));
        }
    }

    private fun getThemeAttrColor(attrId: Int): Int {
        val a = obtainStyledAttributes(intArrayOf(attrId))
        try {
            return a.getColor(0, 0)
        } finally {
            a.recycle()
        }
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    private fun enableFollowSystem(recreate: Boolean) {
        if (ZeroAicySetting.enableFollowSystem()) {
            if (ZeroAicySetting.isNightMode(this)) {
                if (ZeroAicySetting.isLightTheme()) {
                    //修改主题为暗主题
                    ZeroAicySetting.setLightTheme(false)
                    if (recreate) recreate()
                }
            } else {
                if (!ZeroAicySetting.isLightTheme()) {
                    //修改主题为亮主题
                    ZeroAicySetting.setLightTheme(true)
                    if (recreate) recreate()
                }
            }
        }


        // 是否需要重启

        val restart = booleanArrayOf(false)

        MultiLanguages.setOnLanguageListener(object : OnLanguageListener {
            override fun onAppLocaleChange(oldLocale: Locale, newLocale: Locale) {
                restart[0] = true
                Log.i(
                    "MultiLanguages",
                    "监听到应用切换了语种，旧语种：$oldLocale，新语种：$newLocale"
                )
            }

            override fun onSystemLocaleChange(oldLocale: Locale, newLocale: Locale) {
                restart[0] = true
                Log.i(
                    "MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale +
                            "，是否跟随系统：" + MultiLanguages.isSystemLanguage(this@BaseAppActivity)
                )
            }
        })

        if (restart[0]) {
            // recreate();
            startActivity(Intent(this, this.javaClass))
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out)
            finish()
        }

    }

}
