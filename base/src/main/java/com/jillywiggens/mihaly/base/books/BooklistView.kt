package com.jillywiggens.mihaly.base.books

import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jillywiggens.mihaly.base.databinding.LayoutBooksListBinding
import com.jillywiggens.mihaly.models.books.Book

class BooklistView(context: Context, val presenter: BooklistPresenter) : RelativeLayout(context, null, 0) {

    lateinit var binding: LayoutBooksListBinding

    init {
        binding = LayoutBooksListBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun buildBooklist(booklist: List<Book>) =
            with(binding) {
                listRv.layoutManager = LinearLayoutManager(presenter.context)
                listRv.adapter = BooklistViewAdapter(presenter, booklist)
                addBookFab.setOnClickListener { presenter.addBook() }
                listContainerSrl.setOnRefreshListener { presenter.loadBooks() }
            }

    fun finishRefresh(message: String? = null) {
        binding.listContainerSrl.isRefreshing = false
        if (!message.isNullOrEmpty()) Toast.makeText(binding.root.context, message, Toast.LENGTH_LONG).show()
    }
}