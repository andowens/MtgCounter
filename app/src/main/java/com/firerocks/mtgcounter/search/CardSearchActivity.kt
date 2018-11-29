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
import kotlinx.coroutines.*
import retrofit2.Response

class CardSearchActivity : AppCompatActivity() {

    lateinit var searchResultAdapter: CardAdapter
    private val mViewModelJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mViewModelJob)

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

                mUiScope.launch {
                    val exactName = getCardByExactName(query.toString())
                    val partialName = getCardByPartialName(query.toString())

                    if (exactName != null && !exactName.isEmpty()) {
                        updateUI(exactName)
                    } else {
                        partialName?.let {
                            updateUI(partialName)
                        }
                    }
                }

                search_bar.close()
                return true
            }


            override fun onQueryTextChange(newText: CharSequence?) {
            }

        })

    }

    private fun updateUI(cards: List<MtgCard>) {
        searchResult.addAll(cards)
        searchResultAdapter.notifyDataSetChanged()
    }


    private suspend fun getCardByExactName(name: String) : List<MtgCard>? = coroutineScope {

        val suspend = async(Dispatchers.Default) {
            val cardsResponse: Response<List<MtgCard>> = MtgCardApiClient.getCardsByExactName(name,
                    50,
                    1)

            cardsResponse.body()
        }

        suspend.await()
    }

    private suspend fun getCardByPartialName(partial: String) : List<MtgCard>? = coroutineScope {

        val suspend = async(Dispatchers.Default) {
            val cardsResponse: Response<List<MtgCard>> = MtgCardApiClient.getCardsByPartialName(
                    partial,
                    50,
                    1)

            cardsResponse.body()
        }

        suspend.await()
    }
}
