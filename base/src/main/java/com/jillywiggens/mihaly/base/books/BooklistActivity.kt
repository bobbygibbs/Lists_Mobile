package com.jillywiggens.mihaly.base.books

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BooklistActivity : AppCompatActivity() {

    lateinit var presenter: BooklistPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BooklistPresenter(this).apply {
            setContentView(createView())
        }

        presenter.loadBooks()
    }

    override fun onDestroy() {
        presenter.destroyView()
        super.onDestroy()
    }
}