package com.mhmdawad.superest.presentation.authentication

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentCreateUserInfoBinding
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.presentation.MainActivity
import com.mhmdawad.superest.presentation.authentication.phone_auth.PhoneAuthViewModel
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.hideBottomNav
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CreateUserInfoFragment : Fragment() {

    private lateinit var binding: FragmentCreateUserInfoBinding

    private val authViewModel by activityViewModels<PhoneAuthViewModel>()
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()

    private var mImageUri: Uri? = null

    private val args by navArgs<CreateUserInfoFragmentArgs>()
    private val userInfoModel by lazy { args.userInfoModel }

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideBottomNav()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_user_info, container, false
        )
        return binding.run {
            fragment = this@CreateUserInfoFragment
            userInfo = userInfoModel
            root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    override fun onResume() {
        super.onResume()
        showUserImage()
    }

    private fun observeListener() {
        userInfoViewModel.userLocationLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.apply {
                    selectLocationEditText.setText(it)
                    selectLocationEditText.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.green)
                    )
                }
            }
        }
        authViewModel.userInfoLiveData.observe(viewLifecycleOwner) { info ->
            when (info) {
                is Resource.Success -> {
                    loadingDialog.hide()
                    showToast(info.data!!)
                    authViewModel.setUserInformationValue()
                    closeFragment()
                }

                is Resource.Error -> {
                    loadingDialog.hide()
                    showToast(info.msg!!)
                }

                is Resource.Loading -> loadingDialog.show()
            }
        }
    }

    fun submitUserInfo() = with(binding) {
        val userName = userNameEditText.text.toString().trim()
        val userLocation = selectLocationEditText.text.toString().trim()
        if (userName.isEmpty()) {
            showToast(getString(R.string.addUserName))
            return@with
        }
        if (mImageUri == null && userInfoModel == null) {
            showToast(getString(R.string.addUserImage))
            return@with
        }
        if (userLocation.isEmpty()) {
            showToast(getString(R.string.addUserLocation))
            return@with
        }
        authViewModel.uploadUserInformation(
            userName,
            mImageUri,
            userLocation)

    }

    fun selectUserLocation() {
        val action =
            CreateUserInfoFragmentDirections.actionCreateUserInfoFragmentToLocateUserLocationFragment()
        findNavController().navigate(action)
    }

    fun changeUserProfileImage() {
        selectImageFromGallery()
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            mImageUri = result.data?.data
            showUserImage()
        }

    private fun showUserImage() {
        if (mImageUri != null)
            binding.userProfileImage.setImageURI(mImageUri)
    }

}