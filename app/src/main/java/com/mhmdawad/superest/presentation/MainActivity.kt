package com.mhmdawad.superest.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.ActivityMainBinding
import com.mhmdawad.superest.model.convertDocumentToProductList
import com.mhmdawad.superest.model.toMap
import com.mhmdawad.superest.util.ConnectionLiveData
import com.mhmdawad.superest.util.PRODUCTS
import com.mhmdawad.superest.util.extention.hideBottomNav
import com.mhmdawad.superest.util.extention.hideSystemUI
import com.mhmdawad.superest.util.extention.showBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private val connectionLiveData by lazy { ConnectionLiveData(this) }
    private var firstCheckInternetConnection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Superest)
        super.onCreate(savedInstanceState)
        hideSystemUI()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavigationView()
        observeNetworkConnection()
    }

    private fun observeNetworkConnection() {
        connectionLiveData.observe(this, {isInternetAvailable->
            if(isInternetAvailable && !firstCheckInternetConnection){
                Snackbar.make(binding.parentLayout, getString(R.string.backOnline), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getColor(R.color.green))
                    .show()
            }else if(!isInternetAvailable){
                Snackbar.make(binding.parentLayout, getString(R.string.connectionLost), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getColor(R.color.red))
                    .show()
            }
            firstCheckInternetConnection = false
        })
    }


    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener(this)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.shopFragment, R.id.cartFragment, R.id.favoriteFragment,
            R.id.exploreFragment, R.id.accountFragment, R.id.checkoutFragment -> showBottomNav()
            else -> hideBottomNav()
        }
    }

    override fun onPause() {
        super.onPause()
        firstCheckInternetConnection = true
    }

}