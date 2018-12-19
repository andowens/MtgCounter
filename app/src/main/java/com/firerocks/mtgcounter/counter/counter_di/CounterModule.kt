package com.firerocks.mtgcounter.counter.counter_di

import com.firerocks.mtgcounter.counter.CounterMVP
import com.firerocks.mtgcounter.counter.CounterPresenter
import com.firerocks.mtgcounter.counter.players.TwoPlayerFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CounterModule {

    @Provides
    fun provideCounterPresenter(): CounterMVP.Presenter = CounterPresenter()

    @Provides
    fun provideTwoPlayerFragment(fragment: TwoPlayerFragment): TwoPlayerFragment {
        return fragment
    }
}