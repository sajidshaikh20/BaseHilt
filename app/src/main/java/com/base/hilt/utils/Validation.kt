package com.base.hilt.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

/**
 * Package Name : com.appname.structure.utils
 * Project Name : sajid_shaikh_base
 */

object Validation {

    /**
     * method is used for checking valid email id format.
     *
     * @param email email address as String
     * @return boolean true for valid false for invalid
     */
    fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * method is used for checking password is in valid format.
     *
     * @param password password as String
     *
     * 1 Upper case, 1 Lower case, 1 Special Characters and 6-12 characters
     * @return boolean true for valid false for invalid
     */
    fun isValidPassword(password: String): Boolean {
        DebugLog.e(password)
//        val patn = "^(^(?=.*?[A-Z])(?=.*?[a-z]))((?=.*?[0-9])|(?=.*?[#?!@\$%^&*-])).{8,}\$"
        val patn =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\$&+,:;=?@#|_<>.^*()%!-])[A-Za-z\\d\$&+,:;=?@#|_<>.^*()%!-]{8,}"
        val pattern = Pattern.compile(patn)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }


    /**
     * method is used for checking if string is empty or not.
     *
     * @param mString as String
     * @return boolean true if [mString] is notnull.
     */
    fun isNotNull(mString: String?): Boolean {
        return when {
            mString == null -> {
                false
            }
            mString.equals("", ignoreCase = true) -> {
                false
            }
            mString.equals("N/A", ignoreCase = true) -> {
                false
            }
            mString.equals("[]", ignoreCase = true) -> {
                false
            }
            mString.equals("null", ignoreCase = true) -> {
                false
            }
            else -> !mString.equals("{}", ignoreCase = true)
        }
    }
}
