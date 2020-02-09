package com.jillywiggens.mihaly.base.books

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.jillywiggens.mihaly.base.R
import com.jillywiggens.mihaly.models.FailedToUploadState
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

    override val TAG = "BOOKS"

    lateinit var view: BooklistView

    private var disposables = CompositeDisposable()

    private val bookService = BookServiceFactory.generate()

    private val db by lazy { generateDatabase(context) }

    init {
        if (disposables.isDisposed) disposables = CompositeDisposable()
    }

    override fun createView() = BooklistView(this).also { view = it }

    override fun destroyView() = disposables.clear()

    fun loadBooks() {
        disposables.add(bookService.getBooks()
                .map { JacksonHelper.readValues<Book>(
                        it,
                        ObjectMapper().registerModule(
                                SimpleModule().addDeserializer(Book::class.java, BookDeserializer())
                        )
                ) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    try {
                        view.buildBooklist(it)
                        cacheBooksFromServer(it)
                    } catch (e: Throwable) {
                        Log.d(TAG, "missing pages", e)
                    } finally {
                        view.finishRefresh()
                    }
                },
                        {
                            Log.d(TAG, "missing pages", it)
                            view.finishRefresh("Yikes")
                            showCachedResults()
                        }
                ))
    }

    fun addBook() =
            with((context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_books_dialog_add, null)) {
                AlertDialog.Builder(context)
                        .setTitle(R.string.add_book)
                        .setMessage(R.string.prompt_add_book)
                        .setView(this)
                        .setPositiveButton(R.string.add) { _, _ ->
                            disposables.add(bookService.addBook(Book(
                                    0,
                                    titleEt.text.toString(),
                                    authorEt.text.toString(),
                                    yearEt.text.toString().toInt(),
                                    pagesEt.text.toString().toInt()
                            ))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            { loadBooks() },
                                            {
                                                Log.d(TAG, "missing pages", it)
                                                cacheSingleBook(Book(
                                                        0,
                                                        titleEt.text.toString(),
                                                        authorEt.text.toString(),
                                                        yearEt.text.toString().toInt(),
                                                        pagesEt.text.toString().toInt(),
                                                        failedToUploadState = FailedToUploadState.Add
                                                ))
                                            }
                                    ))
                        }
                        .setNegativeButton(android.R.string.no, null)
                        .show()
            }

    fun finishBook(book: Book) {
        disposables.add(bookService.finishBook(book.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { loadBooks() },
                        { Log.d(TAG, "missing pages", it) }
                ))
        Toast.makeText(context, R.string.congrations, Toast.LENGTH_LONG).show()
    }

    fun deleteBook(book: Book) =
            AlertDialog.Builder(context)
                    .setTitle(R.string.delete_book)
                    .setMessage(R.string.confirm_delete_book)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        disposables.add(bookService.deleteBook(book.id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { loadBooks() },
                                        {
                                            Log.d(TAG, "missing pages", it)
                                            cacheSingleBook(book.apply {
                                                failedToUploadState = FailedToUploadState.Delete
                                            })
                                        }
                                ))
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()

    private fun cacheBooksFromServer(serverBooks: List<Book>) {
        disposables.add(db.bookDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        { localBooks ->
                            localBooks.filter {
                                it.failedToUploadState != FailedToUploadState.Add
                            }.filterNot { freeToDeleteBook ->
                                serverBooks.map { it.id }.contains(freeToDeleteBook.id)
                            }.forEach { db.bookDao().delete(it) }

                            localBooks.filter {
                                it.failedToUploadState == FailedToUploadState.Add
                            }.onEach { failedToAddBook ->
                                disposables.add((bookService.addBook(failedToAddBook).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({ db.bookDao().delete(failedToAddBook) }, { Log.d(TAG, "missing pages", it) })))
                            }

                            val failedToDeleteBookIds = localBooks.filter {
                                it.failedToUploadState == FailedToUploadState.Delete
                            }.map {
                                it.id
                            }.onEach {
                                disposables.add(bookService.deleteBook(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ Log.d(TAG, "found pages") }, { Log.d(TAG, "missing pages", it) }))
                            }
                            disposables.add(db.bookDao().insertAll(*serverBooks.filterNot { failedToDeleteBookIds.contains(it.id) }.toTypedArray())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ Log.d(TAG, "found pages") }, { Log.d(TAG, "missing pages", it) }))
                        },
                        {
                            disposables.add(db.bookDao().insertAll(*serverBooks.toTypedArray())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ Log.d(TAG, "found pages") }, { Log.d(TAG, "missing pages", it) }))
                        }
                ))
    }

    private fun cacheSingleBook(book: Book) {
        disposables.add(db.bookDao().insertAll(book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.d(TAG, "found pages") }, { Log.d(TAG, "missing pages", it) }))
    }

    private fun showCachedResults() {
        disposables.add(db.bookDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::buildBooklist) { Log.d(TAG, "missing pages", it) })
    }
}