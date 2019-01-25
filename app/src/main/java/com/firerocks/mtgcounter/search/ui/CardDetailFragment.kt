package com.firerocks.mtgcounter.search.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.search.view_model.SearchViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.android.synthetic.main.fragment_card_detail.*
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
        (activity as DaggerAppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activity?.postponeEnterTransition()
    }

    override fun onStop() {
        super.onStop()
        (activity as DaggerAppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        model = activity?.run {
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        model.mtgCard.observe(this, Observer<MtgCard> {

            Picasso.get().load(it.imageUrl).into(card_picture, object : Callback {
                override fun onSuccess() {
                    card_picture.doOnPreDraw {
                        activity?.startPostponedEnterTransition()
                    }
                }

                override fun onError(e: Exception?) {
                }
            })
        })
    }
}
