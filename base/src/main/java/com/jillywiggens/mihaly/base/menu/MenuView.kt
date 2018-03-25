package com.jillywiggens.mihaly.base.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.jillywiggens.mihaly.base.R
import kotlinx.android.synthetic.main.layout_menu.view.*

/**
 * Created by bobby on 3/25/2018.
 */
class MenuView(context: Context) {

    val view: View

    init {
        val parent = FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        view = LayoutInflater.from(context).inflate(R.layout.layout_menu, parent, true)
        view.booksBtn.setOnClickListener { goToBooks() }
    }

    fun goToBooks() {
        view.booksBtn.text = "None Found"
    }
}