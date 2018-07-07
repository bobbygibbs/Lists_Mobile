package com.jillywiggens.mihaly.base.airports

import android.content.Context
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
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

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        loadLatLng().forEach {
            ServiceFactory.generate<WeatherService>().getWeatherData(it.latitude, it.longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { updateAirportData(it) },
                            { Log.e(AirportMapView::class.simpleName, "Failed to parse JSON", it) }
                    )
        }
        val eppley = LatLng(41.303031, -95.893952)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(eppley, 5f))
    }

    private fun updateAirportData(response: WeatherResponse) {
        val airportName = JacksonHelper.readString(response.currentObservation, NAME_KEY)
        val airportLatitude = JacksonHelper.readString(response.location, LATITUDE_KEY).toDouble()
        val airportLongitude = JacksonHelper.readString(response.location, LONGITUDE_KEY).toDouble()
        map?.addMarker(MarkerOptions().title(airportName).position(LatLng(airportLatitude, airportLongitude)))
    }

    private fun loadLatLng() = context.resources.getStringArray(R.array.locations)
            .map { it.toDoublePair() }
            .map { LatLng(it.first(), it.last()) }

    companion object {
        private fun String.toDoublePair() = split(",").map { it.toDouble() }

        private const val NAME_KEY = "name"
        private const val LATITUDE_KEY = "latitude"
        private const val LONGITUDE_KEY = "longitude"
    }
}