package com.jillywiggens.mihaly.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import kotlin.reflect.KClass

object JacksonHelper {

    fun readString(jsonNode: JsonNode, jsonKey: String) = jsonNode.safelyGet(jsonKey).asText("")

    fun readInt(jsonNode: JsonNode, jsonKey: String) = jsonNode.safelyGet(jsonKey).asInt(0)

    fun readDouble(jsonNode: JsonNode, jsonKey: String) = jsonNode.safelyGet(jsonKey).asDouble(0.0)

    fun <T : Any> readValue(jsonNode: JsonNode, clazz: KClass<T>) = ObjectMapper().readValue(jsonNode.toString(), clazz.java) as T

    inline fun <reified T : Any> readValues(jsonNode: JsonNode, om: ObjectMapper) = om.readValue<List<T>>(jsonNode.toString(), object : TypeReference<List<T>>(){}) as List<T>

    private fun JsonNode.safelyGet(jsonKey: String) = if (has(jsonKey)) get(jsonKey) else throw IOException("No key matching '$jsonKey' found in expected JSON node")
}