package com.base.hilt

import android.app.Application
import com.base.hilt.utils.MyPreference
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var mPref: MyPreference

    companion object {
        private var instance: MyApp? = null
        fun applicationContext(): MyApp {
            return instance as MyApp
        }
    }

    init {
        instance = this
    }
}