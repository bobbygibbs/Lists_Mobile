package com.jillywiggens.mihaly.base.airports

import android.content.Context
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.ViewDelegate
import com.jillywiggens.mihaly.services.JacksonHelper
import com.jillywiggens.mihaly.services.ServiceFactory
import com.jillywiggens.mihaly.services.WeatherResponse
import com.jillywiggens.mihaly.services.WeatherService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AirportMapView(val context: Context) : ViewDelegate(), OnMapReadyCallback {

    override val resId = R.layout.layout_airport_map
    override lateinit var view: View

    private var map: GoogleMap? = null
    private var airports = mutableListOf<AirportWeatherInfo>()

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        loadLatLng().forEach {
            ServiceFactory.generate<WeatherService>().getWeatherData(it.latitude, it.longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { updateAirportData(it) },
                            { Log.e(AirportMapView::class.simpleName, "Failed to parse JSON", it) },
                            { map.setInfoWindowAdapter(WeatherInfoWindowAdapter(airports, context)) }
                    )
        }
        val eppley = LatLng(41.303031, -95.893952)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(eppley, 5f))
        map.setOnInfoWindowClickListener { map.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 10f)) }
    }

    private fun updateAirportData(response: WeatherResponse) {
        val airport = AirportWeatherInfo(
            name = JacksonHelper.readString(response.currentObservation, NAME_KEY),
            location = LatLng(
                JacksonHelper.readDouble(response.location, LATITUDE_KEY),
                JacksonHelper.readDouble(response.location, LONGITUDE_KEY)
            ),
            temperature = JacksonHelper.readInt(response.currentObservation, TEMPERATURE_KEY),
            windSpeed = JacksonHelper.readInt(response.currentObservation, WIND_SPEED_KEY),
            windDirection = Direction.fromDegrees(JacksonHelper.readInt(response.currentObservation, WIND_DIRECTION_KEY)),
            imageResId = context.resources.getIdentifier(
                    JacksonHelper.readString(response.currentObservation, WEATHER_IMAGE_KEY)
                        .dropLast(4)
                        .replace("wind_", ""),
                    "drawable",
                    context.packageName
            )
        )
        airports.add(airport)

        map?.addMarker(MarkerOptions()
                .title(airport.name)
                .position(airport.location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)))
    }

    private fun loadLatLng() = context.resources.getStringArray(R.array.locations)
            .map { it.toDoublePair() }
            .map { LatLng(it.first(), it.last()) }

    companion object {
        private fun String.toDoublePair() = split(",").map { it.toDouble() }

        private const val NAME_KEY = "name"
        private const val LATITUDE_KEY = "latitude"
        private const val LONGITUDE_KEY = "longitude"
        private const val TEMPERATURE_KEY = "Temp"
        private const val WIND_SPEED_KEY = "Winds"
        private const val WIND_DIRECTION_KEY = "Windd"
        private const val WEATHER_IMAGE_KEY = "Weatherimage"
    }
}