package com.firerocks.mtgcounter.counter.counter_di

import com.firerocks.mtgcounter.counter.players.BaseCounterFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CounterModule::class])
interface CounterFragComponent {

    fun inject(fragment: BaseCounterFragment)
}