package com.firerocks.mtgcounter.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.search.model.toParcelableMtgCard
import com.firerocks.mtgcounter.utils.adapters.CardAdapter
import com.lapism.searchview.Search
import com.lapism.searchview.widget.SearchAdapter
import dagger.android.support.DaggerFragment
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.android.synthetic.main.activity_card_search.*
import kotlinx.android.synthetic.main.card_search_view.*
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject
import androidx.core.util.Pair as UtilPair

class CardSearchFragment : DaggerFragment() {

    lateinit var searchResultAdapter: CardAdapter
    private val mViewModelJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mViewModelJob)

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
        return inflater.inflate(R.layout.activity_card_search, container, false)
    }

    /**
     * Helper function that helps get the application context if it can
     *
     * @param lambda Passed in function that lets you use the context.
     */
    private fun appContext (lambda: (Context) -> Unit) {
        activity?.applicationContext?.let {
            lambda(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultAdapter = CardAdapter(searchResult) { card, view ->

            activity?.let { activity ->

                val cardView = activity.findViewById<CardView>(R.id.search_cardView)
                val imageView = activity.findViewById<AppCompatImageView>(R.id.card_image)

//                val intent = Intent(context, CardDetailFragment::class.java)
//                val transitionName = getString(R.string.cardview_transition)
//                val transName = "test"
//                val pair1 = UtilPair.create(cardView as View, transitionName)
//                val pair2 = UtilPair.create(card_image as View, transName)
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
//                        pair1,
//                        pair2)


//                    intent.putExtra("card", card.toParcelableMtgCard())
                performTransition(view)
            }
        }


        search_list.adapter = searchResultAdapter
        search_list.layoutManager = LinearLayoutManager(activity)

        search_bar.setOnLogoClickListener {
            activity?.onBackPressed()
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

    private fun performTransition(view: View) {
        if (isDetached) {
            return
        }

        val detailFrag = CardDetailFragment.newInstance()

        val exitFade = Fade()
        exitFade.duration = 300
        this.exitTransition = exitFade

        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater
                .from(context).inflateTransition(android.R.transition.move))
        enterTransitionSet.startDelay = 1000
        enterTransitionSet.duration = 300
        detailFrag.sharedElementEnterTransition = enterTransitionSet

        val enterFade = Fade()
        enterFade.startDelay = 1300
        enterFade.duration = 300
        detailFrag.enterTransition = enterFade

        val fragTrans = activity?.supportFragmentManager?.beginTransaction()
        fragTrans?.add(R.id.frame_container, CardDetailFragment.newInstance())
        fragTrans?.addSharedElement(card_image, card_image.transitionName)
        fragTrans?.addSharedElement(view, search_cardView.transitionName)
        fragTrans?.replace(R.id.frame_container, detailFrag)
        fragTrans?.addToBackStack(null)
        fragTrans?.commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
