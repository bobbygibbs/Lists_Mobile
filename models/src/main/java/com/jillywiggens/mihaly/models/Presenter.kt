package com.jillywiggens.mihaly.models

/**
 * Created by bobby on 3/25/2018.
 */
abstract class Presenter {
    abstract val TAG: String
    abstract fun createView(): ViewDelegate
    open fun destroyView() { }
}