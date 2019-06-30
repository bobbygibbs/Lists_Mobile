package com.jillywiggens.mihaly.base.books

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.ViewDelegate
import com.jillywiggens.mihaly.models.books.Book
import kotlinx.android.synthetic.main.layout_books_list.view.*

class BooklistView(val presenter: BooklistPresenter) : ViewDelegate() {

    override val resId = R.layout.layout_books_list
    override lateinit var view: View

    fun buildBooklist(booklist: List<Book>) =
            with(view) {
                listRv.layoutManager = LinearLayoutManager(presenter.context)
                listRv.adapter = BooklistViewAdapter(presenter, booklist)
                addBookFab.setOnClickListener { presenter.addBook() }
                listContainerSrl.setOnRefreshListener { presenter.loadBooks() }
            }

    fun finishRefresh(message: String? = null) {
        view.listContainerSrl.isRefreshing = false
        if (!message.isNullOrEmpty()) Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
    }
}