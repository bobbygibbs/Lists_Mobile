package com.jillywiggens.mihaly.models.books

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
class Book(
        @PrimaryKey val id: Int,
        val title: String,
        val author: String,
        val year: Int,
        val pages: Int
)