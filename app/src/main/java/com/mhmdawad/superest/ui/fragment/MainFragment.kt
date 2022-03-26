package com.mhmdawad.superest.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.mhmdawad.superest.R
import com.mhmdawad.superest.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfLoggedIn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(">>>>>>>>>> viewcreated")
    }

    private fun checkIfLoggedIn() {
        val currentUser = firebaseAuth.currentUser
        if(currentUser == null){
            showToast("User is Null")
        }else{
            showToast( "User is not Null")
        }
        println(">>>>>>> onstart")
    }
}