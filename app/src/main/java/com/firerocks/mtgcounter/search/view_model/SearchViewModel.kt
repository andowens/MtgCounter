package com.firerocks.mtgcounter.search.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.magicthegathering.kotlinsdk.model.card.MtgCard

class SearchViewModel : ViewModel() {

    val cardsList = MutableLiveData<List<MtgCard>>()

    override fun onCleared() {
        super.onCleared()
    }
}