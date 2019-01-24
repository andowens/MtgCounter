package com.firerocks.mtgcounter.search.di

import com.firerocks.mtgcounter.search.ui.CardDetailFragment
import com.firerocks.mtgcounter.search.ui.CardSearchFragment
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchFragment(fragment: CardSearchFragment) : CardSearchFragment {
        return fragment
    }

    @Provides
    fun provideDetailFragment(fragment: CardDetailFragment) : CardDetailFragment {
        return fragment
    }
}