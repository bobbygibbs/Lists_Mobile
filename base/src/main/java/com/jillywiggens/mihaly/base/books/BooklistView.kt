package com.jillywiggens.mihaly.base.books

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.ViewDelegate
import com.jillywiggens.mihaly.models.books.Book
import kotlinx.android.synthetic.main.layout_books_list.view.*

class BooklistView(val presenter: BooklistPresenter) : ViewDelegate() {

    override val resId = R.layout.layout_books_list
    override lateinit var view: View

    fun buildBooklist(booklist: List<Book>) {
        view.listRv.layoutManager = LinearLayoutManager(presenter.context)
        view.listRv.adapter = BooklistViewAdapter(presenter, booklist)
        view.addBookFab.setOnClickListener { presenter.addBook() }
        view.listContainerSrl.setOnRefreshListener { presenter.loadBooks() }
    }

    fun finishRefresh() {
        view.listContainerSrl.isRefreshing = false
    }
}