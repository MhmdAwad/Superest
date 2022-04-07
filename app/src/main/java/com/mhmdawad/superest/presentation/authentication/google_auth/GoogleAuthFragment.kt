package com.mhmdawad.superest.presentation.authentication.google_auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentGoogleAuthBinding
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class GoogleAuthFragment : Fragment() {

    private lateinit var binding: FragmentGoogleAuthBinding
    private val googleAuthViewModel by viewModels<GoogleAuthViewModel>()

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_google_auth, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
        configureGoogleSignIn()
    }

    private fun observeListener() {
        googleAuthViewModel.googleAuthLiveData.observe(viewLifecycleOwner, {userState->
            when(userState){
                is Resource.Loading-> loadingDialog.show()
                is Resource.Success->{
                    navigateToMainFragment()
                    loadingDialog.hide()
                }
                is Resource.Error->{
                    loadingDialog.hide()
                    showToast(userState.msg!!)
                    closeFragment()
                }
            }
        })
    }

    private fun navigateToMainFragment() {
        val action = GoogleAuthFragmentDirections.actionGoogleAuthFragmentToMainFragment()
        findNavController().navigate(action)
    }

    // start google authentication intent to show all google save accounts to choose an account to sign in with it.
    private fun configureGoogleSignIn() {
        val mGoogleSignInClient = GoogleSignIn.getClient(
            requireContext(),
            googleSignInOptions
        )
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLauncher.launch(signInIntent)
    }


    private val googleLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                googleAuthViewModel.handleGoogleAuthRequest(task,getString(R.string.errorMessage))
        }
}