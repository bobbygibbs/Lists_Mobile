package com.jillywiggens.mihaly.base.menu

import android.content.Context
import android.view.View
import com.jillywiggens.mihaly.models.Presenter

/**
 * Created by bobby on 3/25/2018.
 */
class MenuPresenter : Presenter() {

    override fun presentView(context: Context) = MenuView(context).view
}