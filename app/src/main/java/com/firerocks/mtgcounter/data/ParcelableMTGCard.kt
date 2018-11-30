package com.firerocks.mtgcounter.data

import android.os.Parcel
import android.os.Parcelable
import io.magicthegathering.kotlinsdk.model.card.MtgCardRuling
import java.io.Serializable

class ParcelableMTGCard() : Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    constructor(val name: String,
                val manaCost: String,
                val colors: List<String>?,
                val types: List<String>,
                val rarity: String,
                val setName: String,
                val text: String?,
                val imageUrl: String,
                val rulings: List<MtgCardRuling>?) : this() 

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableMTGCard> {
        override fun createFromParcel(parcel: Parcel): ParcelableMTGCard {
            return ParcelableMTGCard(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableMTGCard?> {
            return arrayOfNulls(size)
        }
    }

}