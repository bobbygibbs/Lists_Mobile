package com.jillywiggens.mihaly.base.menu

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jillywiggens.mihaly.base.books.BooklistActivity
import com.jillywiggens.mihaly.base.databinding.LayoutMenuBinding

/**
 * Created by bobby on 3/25/2018.
 */
class MenuView(context: Context, presenter: MenuPresenter) : ConstraintLayout(context, null, 0) {

    var binding: LayoutMenuBinding =
        LayoutMenuBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.booksBtn.setOnClickListener {
            presenter.pushPageToScreen(BooklistActivity::class)
        }
    }
}