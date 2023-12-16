package com.sto_opka91.cryptoexchange.ui.detailCoin

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sto_opka91.cryptoexchange.R
import com.sto_opka91.cryptoexchange.data.News
import com.sto_opka91.cryptoexchange.databinding.NewsItemLayoutBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewsAdapter(val listener: UrlNewsClickListener): ListAdapter<News, NewsAdapter.Holder>(NewsAdapter.Comparator()) {
    class  Holder(view: View, val listener: UrlNewsClickListener?) : RecyclerView.ViewHolder(view){

        val binding = NewsItemLayoutBinding.bind(view)
        private var itemTemp: News? = null

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SuspiciousIndentation")
        fun bind(item: News) =  with(binding) {
            itemTemp = item
            tvtime.text = formattedDate(item.time)
            tvDescription.text = item.title
            imGoToResource.setOnClickListener{
                listener?.onClickIm(item)
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        private fun formattedDate(time: String): String{
            val dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }

    class Comparator : DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent,false)
        return Holder(view, listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface UrlNewsClickListener{
        fun onClickIm(item: News)
    }
}