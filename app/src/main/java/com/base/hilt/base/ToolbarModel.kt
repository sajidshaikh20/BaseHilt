package com.base.hilt.base

data class ToolbarModel(
    var isVisible: Boolean = false,
    var isBottomNavVisible: Boolean= false,
    var title: String? = null,
    var backbtn: Boolean = false,
    val flname: Boolean = false,
)