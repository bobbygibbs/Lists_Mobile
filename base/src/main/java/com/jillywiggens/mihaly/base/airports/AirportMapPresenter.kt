package com.jillywiggens.mihaly.base.airports

import android.content.Context
import com.fasterxml.jackson.databind.JsonNode
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.Presenter
import com.jillywiggens.mihaly.services.JacksonHelper

class AirportMapPresenter(val context: Context) : Presenter() {

    var mapDelegate = AirportMapDelegate(this)
    var airports = mutableListOf<AirportWeatherInfo>()

    lateinit var view: AirportMapView

    override fun createView() = AirportMapView(this).also { view = it }

    fun onAirportsLoaded() = view.bindAirportsList(airports)

    fun closeListDrawer() = view.toggleListDrawer(false)

    fun addAirportFromJson(map: GoogleMap, json: JsonNode) {
        airports.add(JacksonHelper.readValue(json, AirportWeatherInfo::class).apply {
            marker = map.addMarker(MarkerOptions()
                    .title(name)
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)))
        })
    }

    fun loadAirportLocations() = context.resources.getStringArray(R.array.locations)
            .map { it.toDoublePair() }
            .map { LatLng(it.first(), it.last()) }

    companion object {
        private fun String.toDoublePair() = split(",").map { it.toDouble() }
    }
}