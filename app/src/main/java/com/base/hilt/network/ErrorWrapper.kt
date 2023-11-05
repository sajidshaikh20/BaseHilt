package com.base.hilt.network

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ErrorWrapper {
    @JsonProperty("errors")
    var errors: JsonNode? = null

    @JsonProperty("meta")
    val meta: Meta? = null
}
