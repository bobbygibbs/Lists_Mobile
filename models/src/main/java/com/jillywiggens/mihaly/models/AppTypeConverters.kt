package com.jillywiggens.mihaly.models

import androidx.room.TypeConverter

class AppTypeConverters {
    @TypeConverter
    fun fromFailedToUploadStateToInt(state: FailedToUploadState) = state.ordinal

    @TypeConverter
    fun fromIntToFailedToUploadState(state: Int) = FailedToUploadState.values().first { it.ordinal == state }
}