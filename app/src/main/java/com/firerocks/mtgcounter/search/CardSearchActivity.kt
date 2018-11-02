package com.firerocks.mtgcounter.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firerocks.mtgcounter.R
import com.squareup.picasso.Picasso
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_card_search.*

class CardSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_search)

//        search_view.setOnClickListener {
//            MtgCardApiClient.getCardsByPartialNameObservable(search_id.text.toString(), 2, 1)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe { cards ->
//                        Log.i("TAG", "Card Name: ${cards[0].multiverseid}")
//                        Picasso.get().load(cards[0].imageUrl).into(image)
//                    }
//        }
    }
}
