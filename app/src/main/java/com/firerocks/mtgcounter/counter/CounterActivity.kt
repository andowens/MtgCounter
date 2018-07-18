package com.firerocks.mtgcounter.counter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.root.App
import javax.inject.Inject

class CounterActivity : AppCompatActivity() {

    @Inject lateinit var presenter: CounterMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent.inject(this)
    }
}
