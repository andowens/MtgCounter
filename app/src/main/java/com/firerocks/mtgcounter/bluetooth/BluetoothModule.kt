package com.firerocks.mtgcounter.bluetooth

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BluetoothModule {
    @Provides
    @Singleton
    fun provideBluetoothPresenter(): BluetoothMVP.Presenter = BluetoothPresenter()
}