package com.jillywiggens.mihaly.models

import android.content.Context
import android.view.View

/**
 * Created by bobby on 3/25/2018.
 */
abstract class Presenter {
    abstract fun presentView(context: Context): View
}