package com.jillywiggens.mihaly.services

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherResponse @JsonCreator constructor(
        @JsonProperty("location") val location: JsonNode,
        @JsonProperty("time") val time: JsonNode,
        @JsonProperty("data") val data: JsonNode,
        @JsonProperty("currentobservation") val currentObservation: JsonNode
)