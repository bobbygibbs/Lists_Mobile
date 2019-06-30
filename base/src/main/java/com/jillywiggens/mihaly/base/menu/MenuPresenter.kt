package com.jillywiggens.mihaly.base.menu

import android.app.Activity
import android.content.Intent
import com.jillywiggens.mihaly.base.MainActivity
import com.jillywiggens.mihaly.models.Presenter
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by bobby on 3/25/2018.
 */
class MenuPresenter @Inject constructor(val activity: MainActivity) : Presenter() {

    override val TAG = "MENU"

    override fun createView() = MenuView(activity.baseContext, this)

    inline fun <reified T : Activity> pushPageToScreen(newPage: KClass<T>) = activity.startActivity(Intent(activity, newPage.java))
}