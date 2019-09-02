package com.jillywiggens.mihaly.base.books

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BooklistActivity : AppCompatActivity() {

    lateinit var presenter: BooklistPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BooklistPresenter(this).apply {
            createView().let { viewDelegate ->
                setContentView(viewDelegate.resId)
                viewDelegate.view = findViewById(android.R.id.content)
            }
        }

        presenter.loadBooks()
    }

    override fun onDestroy() {
        presenter.destroyView()
        super.onDestroy()
    }
}