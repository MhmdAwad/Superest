package com.mhmdawad.superest.presentation.authentication.phone_auth

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentPhoneNumberAuthenticationBinding
import com.mhmdawad.superest.model.PhoneVerificationModel
import com.mhmdawad.superest.util.*
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.handleKeyBoardApparition
import com.mhmdawad.superest.util.extention.showToast
import com.mhmdawad.superest.util.extention.stopKeyBoardListener
import com.mhmdawad.superest.util.state.MainAuthState
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class PhoneNumberAuthFragment : Fragment() {
    private lateinit var binding: FragmentPhoneNumberAuthenticationBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    private val authViewModel by activityViewModels<PhoneAuthViewModel>()

    private var verificationId: String? = null
    private var verificationToken: PhoneAuthProvider.ForceResendingToken? = null

    private var verificationTimeOut: Long = 0

    private var validPhoneNumber: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_phone_number_authentication, container, false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener { closeFragment() }
        observeListener()
    }

    private fun observeListener() {
        authViewModel.phoneMainAuthLiveData.observe(viewLifecycleOwner, {
            when (it) {
                /* some cases the phone number can be instantly verified without needing to send or enter a verification code
                    so here we will login with credential and we observe when login to open MainFragment if user has already an account
                    or open createUserFragment to add user info in app .
                 */
                is MainAuthState.SuccessWithCredential -> {
                    authViewModel.signInWithPhoneAuthCredential(it.data)
                    authViewModel.setPhoneAuthLiveData(MainAuthState.Idle)
                    loadingDialog.hide()
                }
                /*
                    Case two if user will verify with code has sent to him so here will open checkCodeAuth Fragment to check if
                    verification code has user added is correct .
                 */
                is MainAuthState.SuccessWithCode -> {
                    verificationId = it.verificationId
                    verificationToken = it.verificationToken
                    navigateToCheckPhoneNumberAuthFragment(it.verificationId, it.verificationToken)
                    authViewModel.setPhoneAuthLiveData(MainAuthState.Idle)
                    loadingDialog.hide()

                }
                // If an error occurred will notify user with error message.
                is MainAuthState.Error -> {
                    loadingDialog.hide()
                    when (it.error) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            showToast(getString(R.string.please_check_internet_connection))
                        }
                        else -> {
                            showToast(getString(R.string.errorMessage))
                        }
                    }
                }
                // show loading dialog while send phone verification .
                is MainAuthState.Loading -> loadingDialog.show()
                // hide loading dialog when app wait reaction from user.
                is MainAuthState.Idle -> loadingDialog.hide()
            }
        })

        authViewModel.signInStatusLiveData.observe(viewLifecycleOwner, {
            when (it) {
                // When had an error with automatically login app will push an error message.
                is Resource.Error -> {
                    loadingDialog.hide()
                    showToast(it.msg!!)
                }
                /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
                   or open createUserFragment to add user info in app .
                */
                is Resource.Success -> {
                    loadingDialog.hide()
                    navigateToMainFragment()
                }
                is Resource.Loading-> loadingDialog.show()
            }
        })

    }

    private fun navigateToMainFragment() {
        val action =
            PhoneNumberAuthFragmentDirections.actionPhoneNumberAuthenticationFragmentToMainFragment()
        findNavController().navigate(action)
    }

    fun checkPhoneNumber() {
        val phoneNumber = binding.phoneNumberEditText.text.toString().trim()
        val countryCode = binding.countryCodePicker.selectedCountryCode
        when {
            phoneNumber.isEmpty() -> {
                showToast(getString(R.string.please_add_your_phone_number_to_continue))
            }
            phoneNumber.toInt() < 4 -> {
                showToast(getString(R.string.please_add_a_valid_phone_number))
            }
            else -> {
                /* Check this is the first verification sent from this mobile and not a second one by check if firebase timeout
                   has finished . */
                validPhoneNumber = "+$countryCode$phoneNumber"
                if (verificationTimeOut == 0L || verificationTimeOut < System.currentTimeMillis()) {
                    sendFirstSMSVerification(validPhoneNumber)
                } else {
                    navigateToCheckPhoneNumberAuthFragment(verificationId!!, verificationToken!!)
                }
            }
        }
    }

    private fun sendFirstSMSVerification(validPhoneNumber: String) {
        authViewModel.setPhoneAuthLiveData(MainAuthState.Loading)
        signInWithPhoneNumber(validPhoneNumber)
    }

    private fun signInWithPhoneNumber(validPhoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(validPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(authViewModel.phoneAuthCallBack())
            .build()
        verificationTimeOut = (System.currentTimeMillis() + 60000L)
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun navigateToCheckPhoneNumberAuthFragment(
        verificationId: String,
        verificationToken: PhoneAuthProvider.ForceResendingToken
    ) {
        val verificationModel =
            PhoneVerificationModel(verificationId, verificationToken, validPhoneNumber)
        val action =
            PhoneNumberAuthFragmentDirections.
            actionPhoneNumberAuthFragmentToCheckCodeAuthFragment(verificationModel)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        handleKeyBoardApparition(binding.authByPhoneNumberFAB)
    }

    override fun onStop() {
        super.onStop()
        stopKeyBoardListener()
    }
}