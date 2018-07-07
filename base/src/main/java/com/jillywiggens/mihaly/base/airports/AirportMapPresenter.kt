package com.jillywiggens.mihaly.base.airports

import android.content.Context
import com.jillywiggens.mihaly.models.Presenter

class AirportMapPresenter : Presenter() {

    override fun createView(context: Context) = AirportMapView(context)
}