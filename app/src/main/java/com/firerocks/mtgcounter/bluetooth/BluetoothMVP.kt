package com.firerocks.mtgcounter.bluetooth

interface BluetoothMVP {

    interface Presenter{
        fun setView(view: BluetoothMVP.View)
    }

    interface View {
        fun getDefaultHealth(): Int
    }
}