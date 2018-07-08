package com.jillywiggens.mihaly.base.airports

import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
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
import kotlinx.android.synthetic.main.layout_airport_map.view.*

class AirportMapView(val context: Context) : ViewDelegate(), OnMapReadyCallback {

    override val resId = R.layout.layout_airport_map
    override lateinit var view: View

    private var airports = mutableListOf<AirportWeatherInfo>()
    private var expanded = false

    var map: GoogleMap? = null

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        loadLatLng().forEach {
            ServiceFactory.generate<WeatherService>().getWeatherData(it.latitude, it.longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { updateAirportData(it) },
                            { Log.e(AirportMapView::class.simpleName, "Failed to parse JSON", it) },
                            {
                                map.setInfoWindowAdapter(WeatherInfoWindowAdapter(airports, context))
                                view.listLv.adapter = AirportListViewAdapter(this, airports.sortedByDescending { it.temperature })
                            }
                    )
        }
        val eppley = LatLng(41.303031, -95.893952)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(eppley, 5f))
        map.setOnInfoWindowClickListener { map.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 10f)) }
        view.listIb.setOnClickListener { toggleListDrawer() }
        map.setOnMapClickListener { toggleListDrawer(false) }
    }

    private fun updateAirportData(response: WeatherResponse) {
        val name = JacksonHelper.readString(response.currentObservation, NAME_KEY)
        val location = LatLng(
                JacksonHelper.readDouble(response.location, LATITUDE_KEY),
                JacksonHelper.readDouble(response.location, LONGITUDE_KEY)
        )
        airports.add(AirportWeatherInfo(
            name = name,
            location = location,
            temperature = JacksonHelper.readInt(response.currentObservation, TEMPERATURE_KEY),
            windSpeed = JacksonHelper.readInt(response.currentObservation, WIND_SPEED_KEY),
            windDirection = Direction.fromDegrees(JacksonHelper.readInt(response.currentObservation, WIND_DIRECTION_KEY)),
            imageResId = context.resources.getIdentifier(
                    JacksonHelper.readString(response.currentObservation, WEATHER_IMAGE_KEY)
                        .dropLast(4)
                        .replace("wind_", ""),
                    "drawable",
                    context.packageName
            ),
            marker = map?.addMarker(MarkerOptions()
                .title(name)
                .position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)))
        ))
    }

    fun toggleListDrawer(expanded: Boolean = !this.expanded) {
        if (this.expanded != expanded) {
            this.expanded = expanded
            TransitionManager.beginDelayedTransition(view.headerLl, TransitionSet().addTransition(ChangeBounds()))
            view.listLv.visibility = if (expanded) View.VISIBLE else View.GONE
            view.listIb.setImageResource(if (expanded) R.drawable.ic_close else R.drawable.ic_list)
        }
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