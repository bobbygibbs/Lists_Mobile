package com.jillywiggens.mihaly.base.airports

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.SupportMapFragment
import com.jillywiggens.mihaly.base.R

class AirportMapActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val presenter = AirportMapPresenter(baseContext)
        val viewDelegate = presenter.createView()
        setContentView(viewDelegate.resId)
        viewDelegate.view = findViewById(android.R.id.content)

        (supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment)?.getMapAsync(presenter.mapDelegate)
    }
}