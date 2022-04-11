package com.mhmdawad.superest.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51Km8FVEfasvXYDI0caqxQXpFYBRgRMbGyzZCfiHrKa7gpuFScPbEs2NqDJe2jEYMSttE3HQZ3KCtX5KRoWwuPhV300VtgzV0ib"
        )
    }
}