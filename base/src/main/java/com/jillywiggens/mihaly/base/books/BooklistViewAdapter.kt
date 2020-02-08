package com.jillywiggens.mihaly.base.books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.books.Book
import kotlinx.android.synthetic.main.layout_books_list_item_view.view.*

class BooklistViewAdapter(val presenter: BooklistPresenter, val booklist: List<Book>) : RecyclerView.Adapter<BooklistViewAdapter.BooklistViewHolder>() {

    class BooklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BooklistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_books_list_item_view, parent, false)
    )

    override fun getItemCount() = booklist.size

    override fun getItemId(index: Int) = booklist[index].id.toLong()

    override fun onBindViewHolder(holder: BooklistViewHolder, index: Int) {
        holder.itemView.apply {
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
