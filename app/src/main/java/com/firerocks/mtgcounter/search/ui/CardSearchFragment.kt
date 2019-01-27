package com.firerocks.mtgcounter.search.ui

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.search.view_model.SearchViewModel
import com.firerocks.mtgcounter.utils.adapters.CardAdapter
import com.lapism.searchview.Search
import com.lapism.searchview.widget.SearchAdapter
import dagger.android.support.DaggerFragment
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.android.synthetic.main.fragment_card_search.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import androidx.core.util.Pair as UtilPair

class CardSearchFragment : DaggerFragment() {

    lateinit var searchResultAdapter: CardAdapter
    private val mViewModelJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mViewModelJob)
    private lateinit var model : SearchViewModel

    companion object {
        fun newInstance() : CardSearchFragment = CardSearchFragment()
    }

    private val searchResult: ArrayList<MtgCard> by lazy {
        ArrayList<MtgCard>()
    }

    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Need to get the activity scope so that this fragment and the detail fragment share the
        // same viewmodel
        model = activity?.run {
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        searchResultAdapter = CardAdapter(searchResult) { card, cardView ->

            performTransition()
            model.setCard(card)
        }


        search_list.adapter = searchResultAdapter
        search_list.layoutManager = LinearLayoutManager(activity)

        search_bar.setOnLogoClickListener {
            activity?.onBackPressed()
        }

        search_bar.setOnQueryTextListener(object : Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence?): Boolean {

                searchResult.clear()

                // Coroutine to get the cards from the interwebs
                mUiScope.launch {
                    val exactName = getCardByExactName(query.toString())
                    val partialName = getCardByPartialName(query.toString())

                    // So depending on if exactName has a value we display either the list of partials
                    // or the exact card
                    if (exactName != null && !exactName.isEmpty()) {
                        updateRecyclerView(exactName)
                    } else {
                        partialName?.let {
                            updateRecyclerView(partialName)
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

    /**
     * Helper funciton that sets up the transition to the detail fragment.
     *
     */
    private fun performTransition() {
        if (isDetached) {
            return
        }

        val detailFrag = CardDetailFragment.newInstance()

        // Create transition animation
        val exitSlide = Slide(Gravity.BOTTOM)
        exitSlide.duration = 300

        this.exitTransition = exitSlide

        val fragTrans = activity?.supportFragmentManager?.beginTransaction()
        fragTrans?.replace(R.id.frame_container, detailFrag)
        fragTrans?.addToBackStack(null)
        fragTrans?.commitAllowingStateLoss()
    }

    /**
     * Helper function to update the recycler view with the new data
     *
     * @param cards The new list of cards to display
     */
    private fun updateRecyclerView(cards: List<MtgCard>) {
        searchResult.addAll(cards)
        searchResultAdapter.notifyDataSetChanged()
    }

    /**
     * Suspending function used to asynchronously get the card by it's exact name
     *
     * @param name The name of the card to look up
     * @return A list of cards if there are any should be just one since it is a get card by exact
     *         name.
     */
    private suspend fun getCardByExactName(name: String) : List<MtgCard>? = coroutineScope {

        val suspend = async(Dispatchers.Default) {
            val cardsResponse: Response<List<MtgCard>> = MtgCardApiClient.getCardsByExactName(name,
                    50,
                    1)

            cardsResponse.body()
        }

        suspend.await()
    }

    /**
     * Suspending function used to asynchronously get the cards based on the string of text
     * passed in.
     *
     * @param partial The partial name to look up
     * @return A list of cards of there are any.
     */
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
