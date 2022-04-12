package com.mhmdawad.superest.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        subscribeToUserTopic()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51Km8FVEfasvXYDI0caqxQXpFYBRgRMbGyzZCfiHrKa7gpuFScPbEs2NqDJe2jEYMSttE3HQZ3KCtX5KRoWwuPhV300VtgzV0ib"
        )
    }

    // subscribe to firebase cloud messaging topic with user id to just get user notifications.
    private fun subscribeToUserTopic() {
        val id = firebaseAuth.uid
        if (id != null)
            FirebaseMessaging.getInstance().subscribeToTopic(id.lowercase())
    }
}