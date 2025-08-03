package com.jillywiggens.mihaly.models

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jillywiggens.mihaly.models.books.Book
import com.jillywiggens.mihaly.models.books.BookDao


@Database(entities = [Book::class], version = 3, exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        val Migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE book ADD COLUMN finished INTEGER NOT NULL DEFAULT '0'")
            }
        }

        val Migration_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE book ADD COLUMN failedToUploadState INTEGER NOT NULL DEFAULT '0'")
            }
        }
    }
}