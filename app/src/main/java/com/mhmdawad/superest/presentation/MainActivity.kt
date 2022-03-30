package com.mhmdawad.superest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mhmdawad.superest.R
import com.mhmdawad.superest.util.extention.hideSystemUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Superest)
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContentView(R.layout.activity_main)
    }

}