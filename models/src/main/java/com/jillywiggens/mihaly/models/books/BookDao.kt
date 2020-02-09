package com.jillywiggens.mihaly.models.books

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): Single<List<Book>>

    @Query("SELECT * FROM book WHERE id IN (:bookIds)")
    fun loadAllByIds(bookIds: IntArray): List<Book>

    @Query("SELECT * FROM book WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg books: Book): Completable

    @Delete
    fun delete(book: Book)
}