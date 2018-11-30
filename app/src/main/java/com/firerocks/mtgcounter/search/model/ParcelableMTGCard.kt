package com.firerocks.mtgcounter.search.model

import android.os.Parcel
import android.os.Parcelable
import io.magicthegathering.kotlinsdk.model.card.MtgCard

data class ParcelableMTGCard(val name: String,
                        val manaCost: String,
                        val colors: List<String>?,
                        val types: List<String>,
                        val rarity: String,
                        val setName: String,
                        val text: String?,
                        val imageUrl: String,
                        val rulings: List<String>?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(manaCost)
        parcel.writeStringList(colors)
        parcel.writeStringList(types)
        parcel.writeString(rarity)
        parcel.writeString(setName)
        parcel.writeString(text)
        parcel.writeString(imageUrl)
        parcel.writeStringList(rulings)
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

fun MtgCard.toParcelableMtgCard() : ParcelableMTGCard {

    // Convert rulings to a list of strings to make things easier for parcelable
    val rulingsList = rulings?.asSequence()?.map {
        it.text
    }?.toList()

    return ParcelableMTGCard(name,
            manaCost,
            colors,
            types,
            rarity,
            setName,
            text,
            imageUrl,
            rulingsList)
}