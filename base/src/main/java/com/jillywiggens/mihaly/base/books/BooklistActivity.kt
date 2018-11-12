package com.jillywiggens.mihaly.base.books

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class BooklistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val presenter = BooklistPresenter(this)
        val viewDelegate = presenter.createView()
        setContentView(viewDelegate.resId)
        viewDelegate.view = findViewById(android.R.id.content)

        presenter.loadBooks()
    }
}