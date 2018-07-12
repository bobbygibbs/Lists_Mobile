package com.jillywiggens.mihaly.base.airports

import android.support.v7.widget.LinearLayoutManager
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.ViewDelegate
import kotlinx.android.synthetic.main.layout_airport_map.view.*

class AirportMapView(val presenter: AirportMapPresenter) : ViewDelegate() {

    override val resId = R.layout.layout_airport_map
    override lateinit var view: View

    private var expanded = false

    fun bindAirportsList(airports: List<AirportWeatherInfo>) {
        view.listRv.adapter = AirportListViewAdapter(presenter, airports.sortedByDescending { it.temperature })
        view.listRv.layoutManager = LinearLayoutManager(presenter.context)
        view.listIb.setOnClickListener { toggleListDrawer() }
    }

    fun toggleListDrawer(expanded: Boolean = !this.expanded) {
        if (this.expanded != expanded) {
            this.expanded = expanded
            TransitionManager.beginDelayedTransition(view.headerLl, TransitionSet().addTransition(ChangeBounds()))
            view.listRv.visibility = if (expanded) View.VISIBLE else View.GONE
            view.listIb.setImageResource(if (expanded) R.drawable.ic_close else R.drawable.ic_list)
        }
    }
}