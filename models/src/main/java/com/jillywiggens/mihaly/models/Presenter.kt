package com.jillywiggens.mihaly.models

import android.content.Context

/**
 * Created by bobby on 3/25/2018.
 */
abstract class Presenter {
    abstract fun createView(context: Context): ViewDelegate
}