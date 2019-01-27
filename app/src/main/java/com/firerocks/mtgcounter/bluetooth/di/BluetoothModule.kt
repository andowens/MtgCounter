package com.firerocks.mtgcounter.bluetooth.di

import android.bluetooth.BluetoothAdapter
import com.firerocks.mtgcounter.bluetooth.view.BluetoothFragment
import com.firerocks.mtgcounter.bluetooth.BluetoothMVP
import com.firerocks.mtgcounter.bluetooth.model.BluetoothModel
import com.firerocks.mtgcounter.bluetooth.presenter.BluetoothPresenter
import com.firerocks.mtgcounter.counter.model.Player
import dagger.Module
import dagger.Provides

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