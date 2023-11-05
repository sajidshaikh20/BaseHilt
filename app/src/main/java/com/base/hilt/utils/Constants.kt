package com.base.hilt.utils

import com.base.hilt.BuildConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object Constants {
    val JSON = jacksonObjectMapper()
    val PREF_NAME = BuildConfig.APPLICATION_ID
}