package com.firerocks.mtgcounter.main

import android.os.Bundle
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.counter.players.TwoPlayerFragment
import dagger.android.support.DaggerAppCompatActivity

class MtgCounterActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mtg_counter)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, TwoPlayerFragment.newInstance())
                    .commitAllowingStateLoss()
        }
    }
}
