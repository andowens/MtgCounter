package com.firerocks.mtgcounter.bluetooth

import java.util.*

interface BluetoothMVP {

    interface Presenter{
        fun setView(view: BluetoothMVP.View)
    }

    interface View {
        fun getDefaultHealth(): Int
    }

    interface Model {
        fun addObserver(observer: Observer)
    }
}