package com.sto_opka91.cryptoexchange.ui.home
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.PopupMenu
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sto_opka91.cryptoexchange.LIST_OF_ICONS_COINS
import com.sto_opka91.cryptoexchange.LIST_OF_NAMES_COINS
import com.sto_opka91.cryptoexchange.data.Data
import com.sto_opka91.cryptoexchange.data.Icons
import com.sto_opka91.cryptoexchange.data.News
import com.sto_opka91.cryptoexchange.databinding.FragmentHomeBinding
import com.sto_opka91.cryptoexchange.ui.detailCoin.DetailFragment
import com.sto_opka91.cryptoexchange.ui.detailCoin.NewsAdapter



class HomeFragment : Fragment(), HomeAdapter.CoinItemClickListener, PopupMenu.OnMenuItemClickListener, NewsAdapter.UrlNewsClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var adapterNews: NewsAdapter
    private var listIconsCoins: ArrayList<Icons>? = null
    lateinit var detailFragment: DetailFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.imageButton.setOnClickListener {
            viewModel.getCoinsRetrofit()
            viewModel.coinsLiveData.observe(requireActivity()) { coins ->
                LIST_OF_NAMES_COINS.clear()
                for (i in 0 until coins.size) {
                    LIST_OF_NAMES_COINS.addAll(coins[i])
                }
                adapter.submitList(LIST_OF_NAMES_COINS)
            }
        }
    }
    private fun initAdapterNews(){
        adapterNews = NewsAdapter(this)
        binding.rcNewsHome.layoutManager= LinearLayoutManager(activity)
        binding.rcNewsHome.adapter = adapterNews
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        listIconsCoins = ArrayList(LIST_OF_ICONS_COINS)
        viewModel.getNewsForCoin()
        initAdapterNews()
        viewModel.liveNewsList.observe(viewLifecycleOwner){ list ->
            adapterNews.submitList(list)
            if(list.size==0){
                binding.tvDontConnect.visibility = View.VISIBLE
            }else  {
                binding.tvDontConnect.visibility = View.GONE
            }
        }
        adapter = HomeAdapter(listIconsCoins!!, this)
        binding.rcViewHome.adapter = adapter
        binding.rcViewHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.submitList(LIST_OF_NAMES_COINS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(coin: Data) {
        detailFragment = DetailFragment(coin)
        detailFragment.show(
            childFragmentManager,
            "AddToDoFragment"
        )
    }

    override fun onLongItemClicked(coin: Data, cardView: CardView) {

    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return false
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