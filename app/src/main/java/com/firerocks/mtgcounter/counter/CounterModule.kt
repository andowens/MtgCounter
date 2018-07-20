package com.firerocks.mtgcounter.counter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CounterModule {
    @Provides
    @Singleton
    fun provideCounterPresenter(model: CounterMVP.Model): CounterMVP.Presenter = CounterPresenter(model)

    @Provides
    @Singleton
    fun providesCounterModel(): CounterMVP.Model = CounterModel()
}