package com.base.hilt.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.base.hilt.R

object Utils {
    /**
     * Common progress dialog
     */
    @SuppressLint("InflateParams")
    @JvmStatic
    fun progressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_progress, null)
        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    /**
     * Get Application Name
     * @param context context
     * @return application name
     */
    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }
}