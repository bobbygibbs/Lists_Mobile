package com.jillywiggens.mihaly.models

import android.content.Context
import android.view.View
import androidx.room.Room
import com.jillywiggens.mihaly.models.AppDatabase.Companion.Migration_1_2
import com.jillywiggens.mihaly.models.AppDatabase.Companion.Migration_2_3

/**
 * Created by bobby on 3/25/2018.
 */
abstract class Presenter {
    abstract val TAG: String
    abstract fun createView(): View
    open fun destroyView() { }
    fun generateDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "mihaly")
            .addMigrations(Migration_1_2, Migration_2_3)
            .build()
}