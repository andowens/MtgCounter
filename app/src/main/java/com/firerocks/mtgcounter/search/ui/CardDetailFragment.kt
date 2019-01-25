package com.firerocks.mtgcounter.search.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.search.view_model.SearchViewModel
import dagger.android.support.DaggerFragment
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import java.lang.Exception
import javax.inject.Inject

class CardDetailFragment : DaggerFragment() {

    private lateinit var model : SearchViewModel

    companion object {
        fun newInstance() : CardDetailFragment = CardDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        model = activity?.run {
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        model.mtgCard.observe(this, Observer<MtgCard> {
            Log.i("TAG", "DATA: ${it.name}")
        })
    }
}
