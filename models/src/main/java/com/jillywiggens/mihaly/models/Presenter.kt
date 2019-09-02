package com.jillywiggens.mihaly.models

import android.content.Context
import androidx.room.Room

/**
 * Created by bobby on 3/25/2018.
 */
abstract class Presenter {
    abstract val TAG: String
    abstract fun createView(): ViewDelegate
    open fun destroyView() { }
    fun generateDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "mihaly").build()
}