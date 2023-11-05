package com.base.hilt.network

import com.base.hilt.utils.Validation
import com.fasterxml.jackson.databind.JsonNode

object HttpCommonMethod {

    /**
     * check whether API response is success or not
     */
    fun isSuccessResponse(responseCode: Int): Boolean {
        return responseCode in 200..300
    }

    /**
     * check Error Message
     */
    fun getErrorMessage(error: JsonNode?): String {
        var value = ""
        if (error != null) {
            when {
                error.isArray -> {
                    for (objInArray in error) {
                        value = (objInArray).toString()
                    }
                }
                error.isContainerNode -> {
                    val it: Iterator<Map.Entry<String, JsonNode>> = error.fields()
                    while (it.hasNext()) {
                        val field = it.next()
                        value = if (Validation.isNotNull(value)) {
                            value + "," + removeArrayBrace(field.value.toString())
                        } else {
                            removeArrayBrace(field.value.toString())
                        }
                    }
                }
                else -> {
                    value = (error).toString()
                }
            }
        }
        return value
    }

    /**
     * Remove [] from Error Objects when there are multiple errors
     *
     * @param message as String
     * @return replacedString
     */
    fun removeArrayBrace(message: String): String {
        return message.replace("[\"", "").replace("\"]", "").replace(".", "")
    }
}
