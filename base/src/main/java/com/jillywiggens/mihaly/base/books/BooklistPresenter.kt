package com.jillywiggens.mihaly.base.books

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.Presenter
import com.jillywiggens.mihaly.models.books.Book
import com.jillywiggens.mihaly.models.books.BookDeserializer
import com.jillywiggens.mihaly.services.JacksonHelper
import com.jillywiggens.mihaly.services.books.BookServiceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_books_dialog_add.view.*

class BooklistPresenter(val context: Context) : Presenter() {

    lateinit var view: BooklistView

    private var disposables = CompositeDisposable()

    private val bookService = BookServiceFactory.generate()

    override fun createView() = BooklistView(this).also { view = it }

    override fun destroyView() = disposables.clear()

    init {
        if (disposables.isDisposed) disposables = CompositeDisposable()
    }

    fun loadBooks() {
        disposables.add(bookService.getBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    try {
                        view.buildBooklist(
                                JacksonHelper.readValues(
                                        it,
                                        ObjectMapper().registerModule(
                                                SimpleModule().addDeserializer(Book::class.java, BookDeserializer())
                                        )
                                )
                        )
                        view.finishRefresh()
                    } catch (e: Throwable) {
                        Log.d("BOOKS", "missing pages", e)
                    }
                },
                        {
                            Log.d("BOOKS", "missing pages", it)
                        }
                ))
    }

    fun addBook() {
        val dialogView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_books_dialog_add, null)
        AlertDialog.Builder(context)
                .setTitle(R.string.add_book)
                .setMessage(R.string.prompt_add_book)
                .setView(dialogView)
                .setPositiveButton(R.string.add) { di, _ ->
                    disposables.add(bookService.addBook(Book(
                            0,
                            dialogView.titleEt.text.toString(),
                            dialogView.authorEt.text.toString(),
                            dialogView.yearEt.text.toString().toInt(),
                            dialogView.pagesEt.text.toString().toInt()
                    ))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { loadBooks() },
                                    { Log.d("BOOKS", "missing pages", it) }
                            ))
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    fun deleteBook(book: Book) {
        AlertDialog.Builder(context)
                .setTitle(R.string.delete_book)
                .setMessage(R.string.confirm_delete_book)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    disposables.add(bookService.deleteBook(book.id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { loadBooks() },
                                    { Log.d("BOOKS", "missing pages", it) }
                            ))
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }
}