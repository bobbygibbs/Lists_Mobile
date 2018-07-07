package com.jillywiggens.mihaly.services

import com.fasterxml.jackson.databind.JsonNode
import java.io.IOException

object JacksonHelper {

    fun readString(jsonNode: JsonNode, jsonKey: String) : String {
        if (jsonNode.has(jsonKey)) {
            return jsonNode.get(jsonKey).asText("")
        }
        throw IOException("No key matching '$jsonKey' found in expected JSON node")
    }
}