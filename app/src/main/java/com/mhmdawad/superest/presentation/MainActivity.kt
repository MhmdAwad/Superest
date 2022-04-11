package com.mhmdawad.superest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.ActivityMainBinding
import com.mhmdawad.superest.model.convertDocumentToProductList
import com.mhmdawad.superest.model.toMap
import com.mhmdawad.superest.util.PRODUCTS
import com.mhmdawad.superest.util.extention.hideBottomNav
import com.mhmdawad.superest.util.extention.hideSystemUI
import com.mhmdawad.superest.util.extention.showBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Superest)
        super.onCreate(savedInstanceState)
        hideSystemUI()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavigationView()
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

}