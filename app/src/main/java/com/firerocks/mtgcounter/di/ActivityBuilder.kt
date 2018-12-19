package com.firerocks.mtgcounter.di

import com.firerocks.mtgcounter.main.MtgCounterActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import android.app.Activity
import com.firerocks.mtgcounter.bluetooth.di.BluetoothFragmentProvider
import com.firerocks.mtgcounter.counter.counter_di.FragmentProvider
import com.firerocks.mtgcounter.main.MtgCounterActivityModule
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MtgCounterActivityModule::class, FragmentProvider::class,
    BluetoothFragmentProvider::class])
    abstract fun bindMtgCounterActivity(): MtgCounterActivity
}