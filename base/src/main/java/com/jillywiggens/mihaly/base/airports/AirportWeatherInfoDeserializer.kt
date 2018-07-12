package com.jillywiggens.mihaly.base.airports

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.google.android.gms.maps.model.LatLng
import com.jillywiggens.mihaly.services.JacksonHelper

class AirportWeatherInfoDeserializer : JsonDeserializer<AirportWeatherInfo>() {

    override fun deserialize(parser: JsonParser, context: DeserializationContext?): AirportWeatherInfo {
        with(parser.codec.readTree<JsonNode>(parser)) {
            val currentObservation by lazy { get("currentobservation") }
            val location by lazy { get("location") }
            val name = JacksonHelper.readString(currentObservation, NAME_KEY)
            val position = LatLng(
                    JacksonHelper.readDouble(location, LATITUDE_KEY),
                    JacksonHelper.readDouble(location, LONGITUDE_KEY)
            )
            return AirportWeatherInfo(
                    name = name,
                    location = position,
                    temperature = JacksonHelper.readInt(currentObservation, TEMPERATURE_KEY),
                    windSpeed = JacksonHelper.readInt(currentObservation, WIND_SPEED_KEY),
                    windDirection = Direction.fromDegrees(JacksonHelper.readInt(currentObservation, WIND_DIRECTION_KEY)),
                    imageFileName = JacksonHelper.readString(currentObservation, WEATHER_IMAGE_KEY)
                            .dropLast(4)
                            .replace("wind_", ""),
                    marker = null
            )
        }
    }

    companion object {
        private const val NAME_KEY = "name"
        private const val LATITUDE_KEY = "latitude"
        private const val LONGITUDE_KEY = "longitude"
        private const val TEMPERATURE_KEY = "Temp"
        private const val WIND_SPEED_KEY = "Winds"
        private const val WIND_DIRECTION_KEY = "Windd"
        private const val WEATHER_IMAGE_KEY = "Weatherimage"
    }

}