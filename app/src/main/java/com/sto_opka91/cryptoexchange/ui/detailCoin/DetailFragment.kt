package com.sto_opka91.cryptoexchange.ui.detailCoin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sto_opka91.cryptoexchange.COLOR
import com.sto_opka91.cryptoexchange.R
import com.sto_opka91.cryptoexchange.data.Data
import com.sto_opka91.cryptoexchange.data.MarcketsItem
import com.sto_opka91.cryptoexchange.data.News
import com.sto_opka91.cryptoexchange.database.DataForResearchIdDatabase
import com.sto_opka91.cryptoexchange.databinding.FragmentDetailBinding



class DetailFragment(data: Data) : DialogFragment(), ShopAdapter.UrlClickListener, NewsAdapter.UrlNewsClickListener {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val coin = data
    private lateinit var viewModel: DetailViewModel
    private lateinit var adapterShop: ShopAdapter
    private lateinit var adapterNews: NewsAdapter
    private var isHere = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initCoinDetail()
        initAdapterNews()
        initViewModelandAdapterShop()
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    private fun initCoinDetail() = with(binding) {
        tvNameValFav.text = coin.name
        tvPriceVolFav.text = coin.price_usd
        tvRankVolFav.text = coin.rank.toString()
        tvSymbolValFav.text = coin.symbol
        tvPrice1hValFav.text = coin.percent_change_1h
        if (coin.percent_change_1h.contains('-')) {
            tvPrice1hValFav.setTextColor(COLOR)
        }
        tvPrice24hValFav.text = coin.percent_change_24h
        if (coin.percent_change_24h.contains('-')) {
            tvPrice24hValFav.setTextColor(COLOR)
        }
        tvPrice7dValFav.text = coin.percent_change_7d
        if (coin.percent_change_7d.contains('-')) {
            tvPrice7dValFav.setTextColor(COLOR)
        }
        imBack.setOnClickListener {
            dismiss()
        }
        imDone.setOnClickListener {
        if(isHere==true){
            isHere=false
            viewModel.deleteCoin(coin.name)
            imDone.setImageResource(R.drawable.ic_non_favorite)
        }else{
            isHere=true
            val insetCoin = DataForResearchIdDatabase(
                null,
                coin.id,
                coin.name
            )
            viewModel.insertCoin(insetCoin)
            imDone.setImageResource(R.drawable.ic_favorite)
        }
        }



    }
    private fun initAdapterNews(){
        adapterNews = NewsAdapter(this)
        binding.rcvNews.layoutManager = LinearLayoutManager(activity)
        binding.rcvNews.adapter = adapterNews
    }

    private  fun initViewModelandAdapterShop() {
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.allCoinsFromDB.observe(viewLifecycleOwner){list ->
            list.let{
                for(i in 0 until list.size){
                    if(coin.name==list[i].title){
                        isHere=true
                        binding.imDone.setImageResource(R.drawable.ic_favorite)
                        break
                    }
                    else
                        isHere=false
                }
            }
        }
        // Запускаем загрузку данных
        viewModel.getMarketsRetrofit(coin.id.toInt())
        viewModel.getNewsForCoin(coin.symbol)
        viewModel.liveNewsList.observe(viewLifecycleOwner){ list ->
            adapterNews.submitList(list)
        }

        viewModel.marketForAdd.observe(viewLifecycleOwner) { marketForAddList ->
            initAdapterShop(marketForAddList)
        }
    }

    private  fun initAdapterShop(marketForAddList: List<MarcketsItem>) {
        // Инициализация адаптера
        adapterShop = ShopAdapter(this)
        binding.rcShops.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcShops.adapter = adapterShop
        // Обновляем данные в адаптере
        adapterShop.submitList(marketForAddList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickBtn(item: MarcketsItem) {
        try {
            Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(takeIf { URLUtil.isValidUrl(item.urlMarket) }
                    ?.let{
                        item.urlMarket
                    }?:"https://google.com")).let{
                    ContextCompat.startActivity(requireContext(), it,null)
                }
        }catch(e: Exception){
            Toast.makeText(context, "The device don't load this document", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickIm(item: News) {
        try {
            Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(takeIf { URLUtil.isValidUrl(item.url) }
                    ?.let{
                        item.url
                    }?:"https://google.com")).let{
                    ContextCompat.startActivity(requireContext(), it,null)
                }
        }catch(e: Exception){
            Toast.makeText(context, "The device don't load this document", Toast.LENGTH_SHORT).show()
        }
    }
}
