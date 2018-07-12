package com.jillywiggens.mihaly.base.airports

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

@JsonDeserialize(using = AirportWeatherInfoDeserializer::class)
data class AirportWeatherInfo(
        val name: String,
        val location: LatLng,
        val temperature: Int,
        val windSpeed: Int,
        val windDirection: Direction,
        val imageFileName: String,
        var marker: Marker?
)