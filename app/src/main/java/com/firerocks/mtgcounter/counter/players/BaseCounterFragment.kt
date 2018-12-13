package com.firerocks.mtgcounter.counter.players

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.firerocks.mtgcounter.counter.counter_di.CounterFragComponent
import com.firerocks.mtgcounter.counter.counter_di.CounterModule
import com.firerocks.mtgcounter.counter.counter_di.DaggerCounterFragComponent


class BaseCounterFragment : Fragment() {

    private lateinit var counterComponent: CounterFragComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        counterComponent = DaggerCounterFragComponent.builder()
                .counterModule(CounterModule())
                .build()

    }

}
