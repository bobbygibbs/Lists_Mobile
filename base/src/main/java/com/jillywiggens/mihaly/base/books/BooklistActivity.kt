package com.jillywiggens.mihaly.base.books

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class BooklistActivity : AppCompatActivity() {

    lateinit var presenter: BooklistPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BooklistPresenter(this)
        val viewDelegate = presenter.createView()
        setContentView(viewDelegate.resId)
        viewDelegate.view = findViewById(android.R.id.content)

        presenter.loadBooks()
    }

    override fun onDestroy() {
        presenter.destroyView()
        super.onDestroy()
    }
}