package com.jillywiggens.mihaly.base.airports

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.jillywiggens.mihaly.base.R
import kotlinx.android.synthetic.main.layout_weather_info.view.*

class WeatherInfoWindowAdapter(private val airports: List<AirportWeatherInfo>, context: Context) : InfoWindowAdapter {

    private val view = LayoutInflater.from(context).inflate(R.layout.layout_weather_info, null)

    override fun getInfoContents(marker: Marker) = view.apply {
        airports.find { it.location == marker.position }?.let {
            titleTv.text = marker.title
            windIv.setImageResource(it.windDirection.resId)
            windTv.text = "${it.windSpeed} MPH"
            tempTv.text = "${it.temperature}°"
            tempTv.background = it.imageFileName.toCircleDrawable(context)
        }
    }

    override fun getInfoWindow(marker: Marker): View? = null

    companion object {
        fun String.toCircleDrawable(context: Context) = RoundedBitmapDrawableFactory.create(
                context.resources,
                BitmapFactory.decodeResource(
                        context.resources,
                        context.resources.getIdentifier(this, "drawable", context.packageName)
                )
        ).apply {
            isCircular = true
            alpha = 100
        }
    }
}