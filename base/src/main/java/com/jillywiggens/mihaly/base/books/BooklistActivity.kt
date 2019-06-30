package com.jillywiggens.mihaly.base.books

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class BooklistActivity : AppCompatActivity() {

    lateinit var presenter: BooklistPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with (BooklistPresenter(this)) {
            createView().let { viewDelegate ->
                setContentView(viewDelegate.resId)
                viewDelegate.view = findViewById(android.R.id.content)
            }

            loadBooks()
        }
    }

    override fun onDestroy() {
        presenter.destroyView()
        super.onDestroy()
    }
}