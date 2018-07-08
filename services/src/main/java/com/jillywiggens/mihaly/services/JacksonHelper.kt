package com.jillywiggens.mihaly.services

import com.fasterxml.jackson.databind.JsonNode
import java.io.IOException

object JacksonHelper {

    fun readString(jsonNode: JsonNode, jsonKey: String) =
        if (jsonNode.has(jsonKey))
            jsonNode.get(jsonKey).asText("")
        else
            throwError(jsonKey)

    fun readInt(jsonNode: JsonNode, jsonKey: String) =
            if (jsonNode.has(jsonKey))
                jsonNode.get(jsonKey).asInt(0)
            else
                throwError(jsonKey)

    fun readDouble(jsonNode: JsonNode, jsonKey: String) =
        if (jsonNode.has(jsonKey))
            jsonNode.get(jsonKey).asDouble(0.0)
        else
            throwError(jsonKey)

    private fun throwError(jsonKey: String): Nothing = throw IOException("No key matching '$jsonKey' found in expected JSON node")
}