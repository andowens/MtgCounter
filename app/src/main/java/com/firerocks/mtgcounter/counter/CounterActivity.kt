package com.firerocks.mtgcounter.counter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.root.App
import javax.inject.Inject

class CounterActivity : AppCompatActivity(), CounterMVP.View {

    @Inject lateinit var presenter: CounterMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter_view)

        (application as App).appComponent.inject(this)
    }

    fun changeName(view: View) {
        presenter.onChangeName(view)
    }

    override fun changePlayerName(view: View) {

    }
}
