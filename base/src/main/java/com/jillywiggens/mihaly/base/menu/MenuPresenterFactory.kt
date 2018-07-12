package com.jillywiggens.mihaly.base.menu

import com.jillywiggens.mihaly.base.MainActivity
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by bobby on 4/21/2018.
 */

@Component(modules = arrayOf(MenuPresenterModule::class))
interface MenuPresenterFactory {
    fun injectPresenter(): MenuPresenter
}

@Module
class MenuPresenterModule(val activity: MainActivity) {

    @Provides
    fun provideActivity() = activity

    @Module
    companion object {
        @Provides
        fun providePresenter(presenter: MenuPresenter) = presenter
    }
}