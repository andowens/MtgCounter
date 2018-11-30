package com.firerocks.mtgcounter.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.utils.adapters.CardAdapter
import com.lapism.searchview.Search
import com.lapism.searchview.widget.SearchAdapter
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.android.synthetic.main.activity_card_search.*
import kotlinx.android.synthetic.main.card_search_view.*
import kotlinx.coroutines.*
import retrofit2.Response
import androidx.core.util.Pair as UtilPair

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

        val cardView = findViewById<CardView>(R.id.search_cardView)
        val imageView = findViewById<AppCompatImageView>(R.id.card_image)

        searchResultAdapter = CardAdapter(searchResult) { card ->
            val intent = Intent(this, CardDetailActivity::class.java)
            val transitionName = getString(R.string.cardview_transition)
            val trannyName = "test"
            val pair1 = UtilPair.create(cardView as View, transitionName)
            val pair2 = UtilPair.create(card_image as View, trannyName)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    pair1)

            intent.putExtra("card", card)
            startActivity(intent, options.toBundle())
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
