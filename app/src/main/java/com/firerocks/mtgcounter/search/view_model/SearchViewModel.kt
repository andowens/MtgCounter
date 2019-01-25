package com.firerocks.mtgcounter.search.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.magicthegathering.kotlinsdk.model.card.MtgCard

class SearchViewModel : ViewModel() {

    val mtgCard = MutableLiveData<MtgCard>()

    fun setCard(card: MtgCard) {
        Log.i("TAG", "CARD")
        mtgCard.value = card
    }
}