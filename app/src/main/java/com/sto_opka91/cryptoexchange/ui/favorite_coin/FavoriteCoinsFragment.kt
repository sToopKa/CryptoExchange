package com.sto_opka91.cryptoexchange.ui.favorite_coin
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sto_opka91.cryptoexchange.LIST_OF_ICONS_COINS
import com.sto_opka91.cryptoexchange.LIST_OF_NAMES_COINS
import com.sto_opka91.cryptoexchange.R
import com.sto_opka91.cryptoexchange.data.Data
import com.sto_opka91.cryptoexchange.database.DataForResearchIdDatabase
import com.sto_opka91.cryptoexchange.databinding.FragmentFavoriteCoinsBinding
import com.sto_opka91.cryptoexchange.ui.detailCoin.DetailFragment
import com.sto_opka91.cryptoexchange.ui.home.HomeAdapter


class FavoriteCoinsFragment : Fragment(), HomeAdapter.CoinItemClickListener, PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFavoriteCoinsBinding? = null
    private val binding get() = _binding!!


    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var viewModel: FavoritCoinsViewModel
    private lateinit var coinAdapter : HomeAdapter
    private lateinit var selectedCoin: DataForResearchIdDatabase
    private  lateinit var coinListFromDatabase: ArrayList<DataForResearchIdDatabase>
    lateinit var detailFragment: DetailFragment

    private fun init() {
        val items = ArrayList<String>()
        for(i in 0 until LIST_OF_NAMES_COINS.size)
        {
            items.add(LIST_OF_NAMES_COINS[i].name)
        }
        initCoinAdapter()
        initViewModel()
        initSpinner(items)

    }
    private fun initViewModel(){
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(FavoritCoinsViewModel::class.java)
        viewModel.allCoinsFromDB.observe(viewLifecycleOwner){list ->
            list.let{

                    val listCoins = ArrayList<Data>()

                    for(i in 0 until list.size){
                        for(j in 0 until LIST_OF_NAMES_COINS.size){
                            if(list[i].idCoin== LIST_OF_NAMES_COINS[j].id){
                                listCoins.add(LIST_OF_NAMES_COINS[j])
                            }
                        }
                    }
                    coinListFromDatabase = list as ArrayList<DataForResearchIdDatabase>
                    coinAdapter.submitList(listCoins)

        }

        }
    }
    private fun initSpinner(items: ArrayList<String>){
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        autoCompleteTextView = binding.spListCoins
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.dropDownHeight = resources.getDimensionPixelSize(R.dimen.dropdown_height)

        val searchView = binding.svSpinner
        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            for(i in 0 until LIST_OF_NAMES_COINS.size){
                if(selectedItem== LIST_OF_NAMES_COINS[i].name){
                    val insetCoin = DataForResearchIdDatabase(
                        null,
                        LIST_OF_NAMES_COINS[i].id,
                        LIST_OF_NAMES_COINS[i].name
                    )
                    viewModel.insertCoin(insetCoin)
                }
            }
            Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()

            autoCompleteTextView.clearFocus()
        }

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                autoCompleteTextView.postDelayed({ autoCompleteTextView.showDropDown() }, 200)
            } else {
                autoCompleteTextView.dismissDropDown()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                autoCompleteTextView.postDelayed({ autoCompleteTextView.showDropDown() }, 200)
                return true
            }
        })

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                adapter.filter.filter(editable.toString())
            }
        })
    }

    private fun initCoinAdapter(){
        binding.rcVIewFavoriteCoins.setHasFixedSize(true)
        binding.rcVIewFavoriteCoins.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        coinAdapter = HomeAdapter(LIST_OF_ICONS_COINS, this)
        binding.rcVIewFavoriteCoins.adapter = coinAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteCoinsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteCoinsFragment()
    }

    override fun onItemClick(coin: Data) {
        detailFragment = DetailFragment(coin)
        detailFragment.show(
            childFragmentManager,
            "AddToDoFragment"
        )
    }

    override fun onLongItemClicked(coin: Data, cardView: CardView) {
        for(i in 0 until coinListFromDatabase.size){
            if(coin.id == coinListFromDatabase[i].idCoin){
                selectedCoin = coinListFromDatabase[i]
            }
        }
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(requireContext(), cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {

        if(p0?.itemId == R.id.delete_coin) {
            viewModel.deleteCoin(selectedCoin)
            return true
        }
        return false
    }
}





