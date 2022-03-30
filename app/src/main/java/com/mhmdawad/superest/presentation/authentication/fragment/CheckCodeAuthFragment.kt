package com.mhmdawad.superest.presentation.authentication.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentCheckPhoneNumberAuthBinding
import com.mhmdawad.superest.presentation.authentication.AuthenticationViewModel
import com.mhmdawad.superest.util.*
import com.mhmdawad.superest.util.extention.*
import com.mhmdawad.superest.util.state.UserAuthState
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CheckCodeAuthFragment : Fragment() {

    private val args by navArgs<CheckCodeAuthFragmentArgs>()
    private val authViewModel by activityViewModels<AuthenticationViewModel>()
    private lateinit var binding: FragmentCheckPhoneNumberAuthBinding
    private var isResendTextViewEnabled = false
    private val verificationModel by lazy { args.verificationModel }

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_check_phone_number_auth, container, false
        )
        binding.fragment = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        startMinuteCountDown()
        observeListener()
    }

    private fun initViews() {
        binding.backButton.setOnClickListener { closeFragment() }
    }

    private fun startMinuteCountDown() {
        object : CountDownTimer(COUNT_DOWN_DELAY, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendTimerTextView.text =
                    getString(R.string.countDown, (millisUntilFinished / 1000))
            }
            override fun onFinish() {
                changeResendTextViewsStyle(false)
            }
        }.start()
    }

    fun changeResendTextViewsStyle(showTimer: Boolean) = with(binding) {
        if (showTimer) {
            resendTimerTextView.show()
            isResendTextViewEnabled = false
            resendCodeTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.offWhite
                )
            )
        } else {
            resendTimerTextView.hide()
            isResendTextViewEnabled = true
            resendCodeTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
    }

    private fun observeListener() {
        authViewModel.signInStatusLiveData.observe(viewLifecycleOwner, {
            when (it) {
                // When had an error with automatically login app will push an error message.
                is UserAuthState.Error -> {
                    loadingDialog.hide()
                    showToast(it.error)
                }
                /* Here we will login with credential and we observe when login to open MainFragment if user has already an account
                   or open createUserFragment to add user info in app .
                */
                is UserAuthState.Success -> {
                    loadingDialog.hide()
                    navigateToMainFragment()
                }
                is UserAuthState.Loading->{
                    loadingDialog.show()
                }
            }
        })
    }

    private fun navigateToMainFragment() {
        val action =
            CheckCodeAuthFragmentDirections.actionCheckPhoneNumberAuthFragmentToMainFragment()
        findNavController().navigate(action)
    }

    // send sms code that user entered to check it correct to verify phone number .
    fun verifyPhoneNumber() {
        val smsCode = binding.verifyCodeNumberEditText.text.toString()
        if (smsCode.isEmpty()) {
            showToast(getString(R.string.type_verification_code_first))
        } else {
            val credential =
                PhoneAuthProvider.getCredential(verificationModel.verificationId, smsCode)
            authViewModel.signInWithPhoneAuthCredential(credential)
        }
    }

    fun resendCode() {
        if (isResendTextViewEnabled) {
            changeResendTextViewsStyle(true)
            startMinuteCountDown()
        }
        resendVerificationCode()
    }

    /*
      TODO 2: refactor all pushed code
      TODO 3: create google auth
      TODO 4: create fb auth
     */

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(verificationModel.phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(authViewModel.phoneAuthCallBack())
            .setForceResendingToken(verificationModel.verificationToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onResume() {
        super.onResume()
        handleKeyBoardApparition(binding.verifyCodeNumberFAB)
    }

    override fun onStop() {
        super.onStop()
        stopKeyBoardListener()
    }
}