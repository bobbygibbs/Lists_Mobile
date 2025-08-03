package com.jillywiggens.mihaly.base.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jillywiggens.mihaly.base.databinding.LayoutBooksListItemViewBinding
import com.jillywiggens.mihaly.models.books.Book

class BooklistViewAdapter(val presenter: BooklistPresenter, val booklist: List<Book>) : RecyclerView.Adapter<BooklistViewAdapter.BooklistViewHolder>() {

    data class BooklistViewHolder(val binding: LayoutBooksListItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BooklistViewHolder(
        LayoutBooksListItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = booklist.size

    override fun getItemId(index: Int) = booklist[index].id.toLong()

    override fun onBindViewHolder(holder: BooklistViewHolder, index: Int) {
        holder.binding.apply {
            with (booklist[index]) {
                titleTv.text = title
                authorTv.text = author
                yearTv.text = year.toString()
                pagesTv.text = pages.toString()
                finishBtn.setOnClickListener {
                    presenter.finishBook(this)
                }
                deleteBtn.setOnClickListener {
                    presenter.deleteBook(this)
                }
            }
        }
    }
}
