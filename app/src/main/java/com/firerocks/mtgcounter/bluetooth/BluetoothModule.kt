package com.firerocks.mtgcounter.bluetooth

import com.firerocks.mtgcounter.data.Player
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BluetoothModule {
    @Provides
    @Singleton
    fun provideBluetoothPresenter(model: BluetoothMVP.Model): BluetoothMVP.Presenter = BluetoothPresenter(model)

    @Provides
    @Singleton
    fun providesBluetoothModel(): BluetoothMVP.Model = BluetoothModel(Player("", 0))
}