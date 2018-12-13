package com.firerocks.mtgcounter.counter.counter_di

import com.firerocks.mtgcounter.counter.CounterMVP
import com.firerocks.mtgcounter.counter.CounterPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CounterModule {
    @Provides
    @Singleton
    fun provideCounterPresenter(): CounterMVP.Presenter = CounterPresenter()
}