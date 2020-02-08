package com.jillywiggens.mihaly.models

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jillywiggens.mihaly.models.books.Book
import com.jillywiggens.mihaly.models.books.BookDao

@Database(entities = [Book::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}