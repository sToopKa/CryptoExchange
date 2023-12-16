package com.sto_opka91.cryptoexchange

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sto_opka91.cryptoexchange.databinding.ActivityMainBinding
import com.sto_opka91.cryptoexchange.ui.home.HomeViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        lifecycleScope.launch {
            LIST_OF_ICONS_COINS = viewModel.loadIcons()
        }
        Handler(Looper.myLooper()!!).postDelayed({
            viewModel.getCoinsRetrofit()
            observeViewModel()
            binding = ActivityMainBinding.inflate(layoutInflater)
        }, 2000)
    }


    private fun observeViewModel() {

        viewModel.coinsLiveData.observe(this) { coins ->
            LIST_OF_NAMES_COINS.clear()
            for (i in 0 until coins.size) {
                LIST_OF_NAMES_COINS.addAll(coins[i])
            }

            setContentView(binding.root)
            val navView: BottomNavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}