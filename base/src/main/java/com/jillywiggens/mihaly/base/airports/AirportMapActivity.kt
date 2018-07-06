package com.jillywiggens.mihaly.base.airports

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.services.ServiceFactory
import com.jillywiggens.mihaly.services.WeatherService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AirportMapActivity : FragmentActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_airport_map)

        (supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment)?.getMapAsync(this)
    }

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

    private fun loadLatLng() = resources.getStringArray(R.array.locations)
            .map { it.toDoublePair() }
            .map { LatLng(it.first(), it.last()) }

    private fun String.toDoublePair() = split(",").map { it.toDouble() }
}