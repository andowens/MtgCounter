package com.firerocks.mtgcounter.counter

interface CounterMVP {
    interface Presenter {
        fun setView(counterView: CounterMVP.View)
    }

    interface View {
    }

    interface Model {

    }
}