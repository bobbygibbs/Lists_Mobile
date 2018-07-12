package com.jillywiggens.mihaly.base.airports

import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.jillywiggens.mihaly.services.WeatherServiceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AirportMapDelegate(val presenter: AirportMapPresenter) : OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        presenter.loadAirportLocations().forEach {
            WeatherServiceFactory.generate().getWeatherData(it.latitude, it.longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { presenter.addAirportFromJson(map, it) },
                            { Log.e(AirportMapView::class.simpleName, "Failed to parse JSON", it) },
                            {
                                map.setInfoWindowAdapter(WeatherInfoWindowAdapter(presenter.airports, presenter.context))
                                presenter.onAirportsLoaded()
                            }
                    )
        }
        map.setOnInfoWindowClickListener { moveCameraTo(it.position, 10f) }
        map.setOnMapClickListener { presenter.closeListDrawer() }
        moveCameraTo(HOME)
    }

    fun moveCameraTo(location: LatLng, zoom: Float = 5f) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    companion object {
        @JvmStatic
        val HOME = LatLng(41.303031, -95.893952)
    }
}