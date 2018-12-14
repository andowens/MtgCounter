package com.firerocks.mtgcounter.counter.counter_di

import com.firerocks.mtgcounter.counter.players.TwoPlayerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class FragmentProvider {

    @ContributesAndroidInjector(modules = [CounterModule::class])
    abstract fun provideTwoPlayerFragment() : TwoPlayerFragment
}