package com.firerocks.mtgcounter.counter.counter_di

import com.firerocks.mtgcounter.counter.CounterMVP
import com.firerocks.mtgcounter.counter.presenter.CounterPresenter
import com.firerocks.mtgcounter.counter.view.CounterFragment
import dagger.Module
import dagger.Provides

@Module
class CounterModule {

    @Provides
    fun provideCounterPresenter(): CounterMVP.Presenter = CounterPresenter()

    @Provides
    fun provideCounterFragment(fragment: CounterFragment): CounterFragment {
        return fragment
    }
}