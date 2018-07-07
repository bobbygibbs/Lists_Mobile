package com.jillywiggens.mihaly.base.airports

import android.content.Context
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.ViewDelegate
import com.jillywiggens.mihaly.services.ServiceFactory
import com.jillywiggens.mihaly.services.WeatherService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AirportMapView(val context: Context) : ViewDelegate(), OnMapReadyCallback {

    override val resId = R.layout.layout_airport_map
    override lateinit var view: View


    override fun onMapReady(map: GoogleMap) {
        loadLatLng().forEach {
            val pos = it
            ServiceFactory.generate<WeatherService>().getWeatherData(it.latitude, it.longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                val title = with(it.toString().split(",")) {
                                    get(indexOfLast { it.contains("name") } + 1)
                                }
                                map.addMarker(MarkerOptions().position(pos).title(title))
                            }
                    )
        }
        val eppley = LatLng(41.303031, -95.893952)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(eppley, 5f))
    }

    private fun loadLatLng() = context.resources.getStringArray(R.array.locations)
            .map { it.toDoublePair() }
            .map { LatLng(it.first(), it.last()) }

    private fun String.toDoublePair() = split(",").map { it.toDouble() }
}