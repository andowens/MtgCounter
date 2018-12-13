package com.firerocks.mtgcounter.root

import com.firerocks.mtgcounter.bluetooth.BluetoothActivity
import com.firerocks.mtgcounter.bluetooth.BluetoothModule
import com.firerocks.mtgcounter.counter.players.TwoPlayerFragment
import com.firerocks.mtgcounter.counter.counter_di.CounterModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, BluetoothModule::class])
interface AppComponent {

    fun inject(target: BluetoothActivity)
}