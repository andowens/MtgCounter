package com.firerocks.mtgcounter.counter

class CounterPresenter: CounterMVP.Presenter {
    private lateinit var mCounterView: CounterMVP.View

    override fun setView(counterView: CounterMVP.View) {
        mCounterView = counterView
    }
}