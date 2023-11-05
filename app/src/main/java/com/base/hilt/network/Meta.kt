package com.base.hilt.network

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Meta {
    @JsonProperty("status")
    var isStatus = false

    @JsonProperty("message")
    var message: String? = null

    @JsonProperty("message_code")
    var messageCode: String? = null

    @JsonProperty("status_code")
    var statusCode = 0
}