package com.firerocks.mtgcounter.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firerocks.mtgcounter.R
import com.squareup.picasso.Picasso
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.android.synthetic.main.card_search_view.view.*

class CardAdapter(private val cards: ArrayList<MtgCard>, private val listener: (MtgCard) -> Unit )
    : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_search_view, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.bind(cards[position], listener)
    }

    class CardViewHolder(val iv: View) : RecyclerView.ViewHolder(iv) {
        fun bind(card: MtgCard, listener: (MtgCard) -> Unit) {
            val cardView = iv as CardView



            cardView.setOnClickListener { listener(card) }
        }
    }

}