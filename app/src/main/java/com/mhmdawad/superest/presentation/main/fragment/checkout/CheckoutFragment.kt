package com.mhmdawad.superest.presentation.main.fragment.checkout

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentCheckoutBinding
import com.mhmdawad.superest.model.CheckoutModel
import com.mhmdawad.superest.model.PaymentModel
import com.mhmdawad.superest.presentation.authentication.UserInfoViewModel
import com.mhmdawad.superest.util.DISPLAY_DIALOG
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.hide
import com.mhmdawad.superest.util.extention.show
import com.mhmdawad.superest.util.extention.showToast
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.view.CardMultilineWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CheckoutFragment : BottomSheetDialogFragment() {

    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val checkoutViewModel by viewModels<CheckoutViewModel>()

    private lateinit var binding: FragmentCheckoutBinding
    private val args by navArgs<CheckoutFragmentArgs>()
    private val totalCost by lazy { args.totalCost }
    private lateinit var paymentLauncher: PaymentLauncher
    private lateinit var paymentIntentClientSecret: String

    @Inject
    @Named(DISPLAY_DIALOG)
    lateinit var displayAlert: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        return  binding.run {
            fragment = this@CheckoutFragment
            loadData = false
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
        initPaymentLauncher()
        startCheckout()
    }

    private fun observeListener() {
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner, { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    val checkoutModel =
                        CheckoutModel(userInfo.data?.userLocationName!!, totalCost, "Payment")
                    binding.checkoutModel = checkoutModel
                }
                is Resource.Error -> {
                    showToast(userInfo.msg!!)
                }
            }
        })

        checkoutViewModel.paymentIntentClientSecretLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    paymentIntentClientSecret = it.data!!
                    binding.loadData = false
                }
                is Resource.Error -> {
                    showAlertDialog(getString(R.string.error), getString(R.string.paymentError))
                    closeFragment()
                }
                is Resource.Loading -> {
                    binding.loadData = true
                }
            }
        })
    }

    private fun initPaymentLauncher() {
        val paymentConfiguration =
            PaymentConfiguration.getInstance(requireContext().applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )
    }

    private fun startCheckout() {
        val amount = (totalCost * 100).toInt().toString()
        PaymentModel(amount).let {
            checkoutViewModel.createPaymentIntent(it)
        }
    }

    fun selectPaymentMethod(paymentSlider: ImageView, cardInputWidget: CardMultilineWidget) {
        if (cardInputWidget.isShown) {
            paymentSlider.animate().rotationBy(-90f).start()
            cardInputWidget.animate().scaleY(0f).withEndAction {
                cardInputWidget.hide()
            }.start()
        } else {
            paymentSlider.animate().rotationBy(90f).start()
            cardInputWidget.animate().scaleY(1f).withEndAction {
                cardInputWidget.show()
            }.start()
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        val isOrderSubmitted = when (paymentResult) {
            is PaymentResult.Completed -> {
                true
            }
            is PaymentResult.Canceled -> {
                false
            }
            is PaymentResult.Failed -> {
                false
            }
        }
        binding.loadData = false
        navigateToOrderStatusFragment(isOrderSubmitted)
    }

    private fun navigateToOrderStatusFragment(isOrderSubmitted: Boolean) {
        val action =
            CheckoutFragmentDirections.actionCheckoutFragmentToOrderStatusFragment(isOrderSubmitted)
        findNavController().navigate(action)
    }

    fun orderNow(cardInputWidget: CardMultilineWidget) {
        if (!cardInputWidget.isShown && !cardInputWidget.validateCardNumber()) {
            showToast(getString(R.string.addPaymentMethod))
            return
        }

        cardInputWidget.paymentMethodCreateParams?.let { params ->
            val confirmParams = ConfirmPaymentIntentParams
                .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
            paymentLauncher.confirm(confirmParams)
            binding.loadData = true

        }
    }

    private fun showAlertDialog(title: String, message: String) {
        displayAlert.apply {
            setTitle(title)
            setMessage(message)
            show()
        }
    }

    fun closeDialog() {
        closeFragment()
    }
}