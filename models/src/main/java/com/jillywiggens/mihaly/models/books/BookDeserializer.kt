package com.jillywiggens.mihaly.models.books

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class BookDeserializer(vc: Class<Any>? = null) : StdDeserializer<Book>(vc) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?) : Book =
        with(p.codec.readTree<JsonNode>(p)) {
            return Book(
                    get("id").asInt(),
                    get("title").asText(),
                    get("author").asText(),
                    get("year").asInt(),
                    get("pages").asInt()
            )
        }
}