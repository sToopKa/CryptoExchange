package com.sto_opka91.cryptoexchange.ui.home

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sto_opka91.cryptoexchange.R
import com.sto_opka91.cryptoexchange.data.Data
import com.sto_opka91.cryptoexchange.data.Icons
import com.sto_opka91.cryptoexchange.databinding.ItemCurrentExchangeBinding


class HomeAdapter(val listIcons : ArrayList<Icons>, val listener: CoinItemClickListener) : ListAdapter<Data, HomeAdapter.Holder>(Comparator()) {


    class  Holder(view: View) : RecyclerView.ViewHolder(view){

        val binding = ItemCurrentExchangeBinding.bind(view)
        var itemTemp: Data? = null



        @SuppressLint("SuspiciousIndentation")
            fun bind(item: Data,listIcons: ArrayList<Icons>, listener: CoinItemClickListener? ) =  with(binding) {
                itemTemp = item
                tvNameValue.text = item.name
                tvCodeValue.text = item.symbol
                tvPriceValue.text = item.price_usd

                for (i in 0 until listIcons.size) {
                    if (item.symbol == listIcons[i].asset_id) {
                        item.iconUrl = listIcons[i].url
                    }
                }
            if(item.iconUrl==null){
                imCoinIcon.setImageResource(R.drawable.default_icon_transformed)
            }else{
                Picasso.get()
                    .load(item.iconUrl)
                    .into(imCoinIcon)
            }


            binding.cvItem.setOnClickListener() {
                listener?.onItemClick(item)
            }
            binding.cvItem.setOnLongClickListener {
                listener?.onLongItemClicked(
                    item,
                    binding.cvItem
                )
                true
            }
            }

    }

    class Comparator : DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_current_exchange, parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(getItem(position), listIcons, listener)
    }
    interface CoinItemClickListener{
        fun onItemClick(coin: Data)
        fun onLongItemClicked(coin: Data, cardView: CardView)
    }
}