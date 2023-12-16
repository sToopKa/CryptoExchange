package com.sto_opka91.cryptoexchange.ui.detailCoin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sto_opka91.cryptoexchange.R
import com.sto_opka91.cryptoexchange.data.MarcketsItem
import com.sto_opka91.cryptoexchange.databinding.ShopItemLayoutBinding


class ShopAdapter (val listener: UrlClickListener) : ListAdapter<MarcketsItem, ShopAdapter.Holder>(Comparator()) {


    class  Holder(view: View, val listener: UrlClickListener?) : RecyclerView.ViewHolder(view){

        val binding = ShopItemLayoutBinding.bind(view)
        private var itemTemp: MarcketsItem? = null

        @SuppressLint("SuspiciousIndentation")
        fun bind(item: MarcketsItem) =  with(binding) {
            itemTemp = item
            tvNameValueShop.text = item.name
            val price = roundToTwoDecimalPlaces(item.price_usd)
            tvPriceValueShop.text = price
            btnGoShop.setOnClickListener{
                listener?.onClickBtn(item)
            }
        }
        private fun roundToTwoDecimalPlaces(value: Double): String {
            return String.format("%.4f", value)
    }
    }

    class Comparator : DiffUtil.ItemCallback<MarcketsItem>(){
        override fun areItemsTheSame(oldItem: MarcketsItem, newItem: MarcketsItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MarcketsItem, newItem: MarcketsItem): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_item_layout, parent,false)
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface UrlClickListener{
        fun onClickBtn(item: MarcketsItem)
    }

}