package com.jillywiggens.mihaly.base.airports

import android.support.annotation.IdRes
import com.google.android.gms.maps.model.LatLng

data class AirportWeatherInfo(
        val name: String,
        val location: LatLng,
        val temperature: Int,
        val windSpeed: Int,
        val windDirection: Direction,
        @IdRes var imageResId: Int
)