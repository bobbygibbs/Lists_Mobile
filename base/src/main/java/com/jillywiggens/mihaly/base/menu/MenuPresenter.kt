package com.jillywiggens.mihaly.base.menu

import android.content.Context
import com.jillywiggens.mihaly.models.Presenter
import javax.inject.Inject

/**
 * Created by bobby on 3/25/2018.
 */
class MenuPresenter @Inject constructor(): Presenter() {

    override fun presentView(context: Context) = MenuView(context).view
}