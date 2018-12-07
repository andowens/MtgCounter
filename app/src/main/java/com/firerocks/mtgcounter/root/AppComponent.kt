package com.firerocks.mtgcounter.root

import com.firerocks.mtgcounter.bluetooth.BluetoothActivity
import com.firerocks.mtgcounter.bluetooth.BluetoothModule
import com.firerocks.mtgcounter.counter.CounterFragment
import com.firerocks.mtgcounter.counter.CounterModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CounterModule::class, BluetoothModule::class])
interface AppComponent {

    fun inject(target: CounterFragment)
    fun inject(target: BluetoothActivity)
}