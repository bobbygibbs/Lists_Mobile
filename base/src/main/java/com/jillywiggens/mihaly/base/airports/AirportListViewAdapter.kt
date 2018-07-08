package com.jillywiggens.mihaly.base.airports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.jillywiggens.mihaly.base.R
import kotlinx.android.synthetic.main.layout_airport_list_item_view.view.*

class AirportListViewAdapter(val mapView: AirportMapView, val airports: List<AirportWeatherInfo>) : BaseAdapter() {

    override fun isEmpty() = airports.isEmpty()

    override fun getView(index: Int, convertView: View?, parent: ViewGroup) = convertView ?:
        LayoutInflater.from(parent.context).inflate(R.layout.layout_airport_list_item_view, null).apply {
            with(airports[index]) {
                titleTv.text = name
                tempTv.text = "$temperature°"
                weatherIv.setImageResource(imageResId)
                setOnClickListener {
                    mapView.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 5f))
                    mapView.toggleListDrawer(false)
                    marker?.showInfoWindow()
                }
            }
        }

    override fun getItem(index: Int) = airports[index]

    override fun getItemId(index: Int) = airports[index].hashCode().toLong()

    override fun getCount() = airports.size
}