package com.firerocks.mtgcounter.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.utils.adapters.CardAdapter
import com.lapism.searchview.Search
import com.lapism.searchview.widget.SearchAdapter
import com.squareup.picasso.Picasso
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_card_search.*
import retrofit2.Response

class CardSearchActivity : AppCompatActivity() {

    lateinit var searchResultAdapter: CardAdapter

    private val searchResult: ArrayList<MtgCard> by lazy {
        ArrayList<MtgCard>()
    }

    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_search)

        searchResultAdapter = CardAdapter(searchResult) { card ->
            Log.i("TAG", card.name)
        }

        search_list.adapter = searchResultAdapter
        search_list.layoutManager = LinearLayoutManager(this)

        search_bar.setOnLogoClickListener {
            onBackPressed()
        }

        search_bar.setOnQueryTextListener(object : Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence?): Boolean {

                searchResult.clear()

                MtgCardApiClient.getCardsByPartialNameObservable(query.toString(), 50, 1)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { cards ->
                            searchResult.addAll(cards)
                            searchResultAdapter.notifyDataSetChanged()
                        }

                search_bar.close()
                return true
            }


            override fun onQueryTextChange(newText: CharSequence?) {
            }

        })

    }

    private suspend fun getCard(name: String) : List<MtgCard>? {

        val cardsResponse: Response<List<MtgCard>> = MtgCardApiClient.getCardsByExactName(name,
                50,
                1)

        return cardsResponse.body()
    }
}
