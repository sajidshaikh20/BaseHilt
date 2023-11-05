package com.base.hilt.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.base.hilt.utils.DebugLog
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import java.util.*
import javax.inject.Inject

/**
 * Set Locale in application
 */
class LocaleManager @Inject constructor() {

    @Inject
    lateinit var mPref: MyPreference

    @SuppressLint("ObsoleteSdkInt")
    private fun updateResources(context_: Context, language: String): Context {

        DebugLog.print("languages:$language")
        var context = context_
        val config = context.resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale)
        } else {
            setSystemLocaleLegacy(config, locale)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(locale)
            context = context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
        return context
    }

    fun setLocale(context: Context?): Context {
        var languageCode = mPref.getValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.EN_CODE)
        if (languageCode.isNullOrEmpty()) {
            languageCode = PrefKey.EN_CODE
        }
        return updateResources(
            context!!, languageCode
        )
    }

    private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun setSystemLocale(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }

    fun setNewLocale(context: Context, language: String) {
        DebugLog.e("setNewLocale" + language)
        mPref.setValueString(PrefKey.SELECTED_LANGUAGE, language)
        updateResources(context, language)
    }
}
