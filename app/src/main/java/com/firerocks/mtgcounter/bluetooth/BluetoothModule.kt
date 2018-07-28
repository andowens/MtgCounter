package com.firerocks.mtgcounter.bluetooth

import android.bluetooth.BluetoothAdapter
import com.firerocks.mtgcounter.data.Player
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BluetoothModule {
    @Provides
    @Singleton
    fun provideBluetoothPresenter(model: BluetoothMVP.Model, bluetoothAdapter: BluetoothAdapter?):
            BluetoothMVP.Presenter = BluetoothPresenter(model, bluetoothAdapter)

    @Provides
    @Singleton
    fun providesBluetoothModel(): BluetoothMVP.Model = BluetoothModel(Player("", 0))

    @Provides
    @Singleton
    fun provideBluetoothAdapter(): BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
}