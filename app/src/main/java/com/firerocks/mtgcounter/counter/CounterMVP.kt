package com.firerocks.mtgcounter.counter
import android.view.View

interface CounterMVP {
    interface Presenter {
        fun setView(counterView: CounterMVP.View)
        fun onChangeName(view: android.view.View)
    }

    interface View {
        fun changePlayerName(view: android.view.View)
    }

    interface Model {

    }
}