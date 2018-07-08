package com.jillywiggens.mihaly.base.airports

import android.support.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

data class AirportWeatherInfo(
        val name: String,
        val location: LatLng,
        val temperature: Int,
        val windSpeed: Int,
        val windDirection: Direction,
        @DrawableRes var imageResId: Int,
        val marker: Marker?
)