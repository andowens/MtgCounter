package com.firerocks.mtgcounter.root.di

import com.firerocks.mtgcounter.main.MtgCounterActivity
import dagger.Module
import com.firerocks.mtgcounter.bluetooth.di.BluetoothFragmentProvider
import com.firerocks.mtgcounter.counter.counter_di.FragmentProvider
import com.firerocks.mtgcounter.main.MtgCounterActivityModule
import com.firerocks.mtgcounter.search.di.SearchFragmentProvider
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MtgCounterActivityModule::class, FragmentProvider::class,
    BluetoothFragmentProvider::class, SearchFragmentProvider::class])
    abstract fun bindMtgCounterActivity(): MtgCounterActivity
}