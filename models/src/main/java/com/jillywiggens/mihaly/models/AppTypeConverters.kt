package com.jillywiggens.mihaly.models

import androidx.room.TypeConverter

class AppTypeConverters {
    @TypeConverter
    fun fromFailedToUploadStateToInt(state: FailedToUploadState): Int = state.ordinal

    @TypeConverter
    fun fromIntToFailedToUploadState(state: Int): FailedToUploadState = FailedToUploadState.entries.first { it.ordinal == state }
}