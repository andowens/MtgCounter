package com.firerocks.mtgcounter.search.di

import com.firerocks.mtgcounter.search.ui.CardDetailFragment
import com.firerocks.mtgcounter.search.ui.CardSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchFragmentProvider {

    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun provideSearchFragment() : CardSearchFragment

    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun provideDetailFragment() : CardDetailFragment
}