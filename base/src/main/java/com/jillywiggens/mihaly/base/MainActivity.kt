package com.jillywiggens.mihaly.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jillywiggens.mihaly.base.menu.DaggerMenuPresenterFactory
import com.jillywiggens.mihaly.base.menu.MenuPresenter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var presenter : MenuPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val presenter = DaggerMenuPresenterFactory.builder()
                .build()
                .injectPresenter()
        setContentView(presenter.presentView(baseContext))
    }
}
