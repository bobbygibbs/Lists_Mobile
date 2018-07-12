package com.jillywiggens.mihaly.base.airports

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.View
import com.fasterxml.jackson.databind.JsonNode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.ViewDelegate
import com.jillywiggens.mihaly.services.JacksonHelper
import com.jillywiggens.mihaly.services.WeatherServiceFactory
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
            WeatherServiceFactory.generate().getWeatherData(it.latitude, it.longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { addAirportFromJson(it) },
                            { Log.e(AirportMapView::class.simpleName, "Failed to parse JSON", it) },
                            {
                                map.setInfoWindowAdapter(WeatherInfoWindowAdapter(airports, context))
                                view.listRv.adapter = AirportListViewAdapter(this, airports.sortedByDescending { it.temperature })
                                view.listRv.layoutManager = LinearLayoutManager(context)
                            }
                    )
        }
        val eppley = LatLng(41.303031, -95.893952)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(eppley, 5f))
        map.setOnInfoWindowClickListener { map.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 10f)) }
        view.listIb.setOnClickListener { toggleListDrawer() }
        map.setOnMapClickListener { toggleListDrawer(false) }
    }

    private fun addAirportFromJson(json: JsonNode) {
        airports.add(JacksonHelper.readValue(json, AirportWeatherInfo::class).apply {
            marker = map?.addMarker(MarkerOptions()
                    .title(name)
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)))
        })
    }

    fun toggleListDrawer(expanded: Boolean = !this.expanded) {
        if (this.expanded != expanded) {
            this.expanded = expanded
            TransitionManager.beginDelayedTransition(view.headerLl, TransitionSet().addTransition(ChangeBounds()))
            view.listRv.visibility = if (expanded) View.VISIBLE else View.GONE
            view.listIb.setImageResource(if (expanded) R.drawable.ic_close else R.drawable.ic_list)
        }
    }

    private fun loadLatLng() = context.resources.getStringArray(R.array.locations)
            .map { it.toDoublePair() }
            .map { LatLng(it.first(), it.last()) }

    companion object {
        private fun String.toDoublePair() = split(",").map { it.toDouble() }
    }
}