package com.jillywiggens.mihaly.models.books

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jillywiggens.mihaly.models.FailedToUploadState

@Entity(tableName = "book")
class Book(
        @PrimaryKey val id: Int,
        val title: String,
        val author: String,
        val year: Int,
        val pages: Int,
        val finished: Boolean = false,
        var failedToUploadState: FailedToUploadState = FailedToUploadState.None
)