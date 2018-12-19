package com.firerocks.mtgcounter.bluetooth.di

import android.bluetooth.BluetoothAdapter
import com.firerocks.mtgcounter.bluetooth.BluetoothFragment
import com.firerocks.mtgcounter.bluetooth.BluetoothMVP
import com.firerocks.mtgcounter.bluetooth.BluetoothModel
import com.firerocks.mtgcounter.bluetooth.BluetoothPresenter
import com.firerocks.mtgcounter.data.Player
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BluetoothModule {

    @Provides
    fun provideBluetoothFragement(fragment: BluetoothFragment) : BluetoothFragment {
        return fragment
    }

    @Provides
    fun provideBluetoothPresenter(model: BluetoothMVP.Model, bluetoothAdapter: BluetoothAdapter?):
            BluetoothMVP.Presenter = BluetoothPresenter(model, bluetoothAdapter)

    @Provides
    fun providesBluetoothModel(): BluetoothMVP.Model = BluetoothModel(Player("", 0))

    @Provides
    fun provideBluetoothAdapter(): BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

}