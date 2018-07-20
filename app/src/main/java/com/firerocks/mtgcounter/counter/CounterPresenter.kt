package com.firerocks.mtgcounter.counter

import android.view.View
import javax.inject.Inject

class CounterPresenter @Inject constructor(val mModel: CounterMVP.Model) : CounterMVP.Presenter {
    private lateinit var mCounterView: CounterMVP.View

    override fun setView(counterView: CounterMVP.View) {
        mCounterView = counterView
    }

    override fun onChangeName(name: String) {

    }
}