package com.jillywiggens.mihaly.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jillywiggens.mihaly.base.menu.MenuPresenter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val presenter = MenuPresenter()
        setContentView(presenter.presentView(baseContext))
    }
}
