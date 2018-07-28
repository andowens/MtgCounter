package com.firerocks.mtgcounter.bluetooth

import java.util.*

interface BluetoothMVP {

    interface Presenter{
        fun setView(view: BluetoothMVP.View)

        fun menuNewGame(onResult: (Int) -> Unit)

        fun discoverBluetooth()

        fun upClicked()

        fun downClicked()

        fun nameClicked()
    }

    interface View {
        fun getDefaultHealth(): Int
    }

    interface Model {
        fun addObserver(observer: Observer)
    }
}