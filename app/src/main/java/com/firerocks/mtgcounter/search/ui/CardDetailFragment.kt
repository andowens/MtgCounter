package com.firerocks.mtgcounter.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firerocks.mtgcounter.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_mtg_counter.*

class CardDetailFragment : DaggerFragment() {

    companion object {
        fun newInstance() : CardDetailFragment = CardDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_card_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
