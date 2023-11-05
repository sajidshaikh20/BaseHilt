package com.base.hilt.network

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.Nullable
import org.json.JSONObject

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class ResponseData<T> {

    @JsonProperty("data")
    var data: T? = null

    override fun toString(): String {
        return "ResponseWrapper{" +
                "data=" + data.toString()
    }

    @JsonProperty("meta")
    val meta: Meta? = null

    @Nullable
    @JsonProperty("errors")
    var jsonError: JSONObject? = null

}
