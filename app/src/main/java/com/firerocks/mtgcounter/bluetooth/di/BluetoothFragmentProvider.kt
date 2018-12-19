package com.firerocks.mtgcounter.bluetooth.di

import com.firerocks.mtgcounter.bluetooth.BluetoothFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BluetoothFragmentProvider {

    @ContributesAndroidInjector(modules = [BluetoothModule::class])
    abstract fun provideBluetoothFragment() : BluetoothFragment
}