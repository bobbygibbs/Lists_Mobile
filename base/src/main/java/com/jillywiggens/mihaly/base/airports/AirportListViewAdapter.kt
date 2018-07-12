package com.jillywiggens.mihaly.base.airports

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jillywiggens.mihaly.base.R
import kotlinx.android.synthetic.main.layout_airport_list_item_view.view.*

class AirportListViewAdapter(
        val presenter: AirportMapPresenter,
        val airports: List<AirportWeatherInfo>
) : RecyclerView.Adapter<AirportListViewAdapter.AirportListViewHolder>() {

    class AirportListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AirportListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_airport_list_item_view, parent, false)
    )

    override fun onBindViewHolder(holder: AirportListViewHolder, index: Int) {
        holder.itemView.apply {
            with(airports[index]) {
                titleTv.text = name
                tempTv.text = "$temperature°"
                weatherIv.setImageResource(context.resources.getIdentifier(
                        imageFileName,
                        "drawable",
                        context.packageName
                ))
                setOnClickListener {
                    presenter.mapDelegate.moveCameraTo(location)
                    presenter.closeListDrawer()
                    marker?.showInfoWindow()
                }
            }
        }
    }

    override fun getItemId(index: Int) = airports[index].hashCode().toLong()

    override fun getItemCount() = airports.size
}