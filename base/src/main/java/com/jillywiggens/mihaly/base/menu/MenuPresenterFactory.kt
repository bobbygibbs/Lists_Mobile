package com.jillywiggens.mihaly.base.menu

import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by bobby on 4/21/2018.
 */

@Component(modules = arrayOf(MenuPresenterModule::class))
interface MenuPresenterFactory {
    fun injectPresenter() : MenuPresenter
}

@Module
class MenuPresenterModule {

    @Module
    companion object {
        @Provides fun providePresenter(presenter: MenuPresenter) = presenter
    }
}