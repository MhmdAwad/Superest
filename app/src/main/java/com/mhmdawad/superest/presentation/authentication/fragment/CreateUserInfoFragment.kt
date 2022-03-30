package com.mhmdawad.superest.presentation.authentication.fragment

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
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentCreateUserInfoBinding
import com.mhmdawad.superest.presentation.authentication.AuthenticationViewModel
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.showToast
import com.mhmdawad.superest.util.state.UserAuthState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CreateUserInfoFragment : Fragment() {

    private lateinit var binding: FragmentCreateUserInfoBinding

    private val authViewModel by activityViewModels<AuthenticationViewModel>()

    private var mImageUri: Uri? = null

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_user_info, container, false
        )
        binding.fragment = this
        return binding.root
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
        authViewModel.userLocationLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.apply {
                    selectLocationEditText.setText(it)
                    selectLocationEditText.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.green)
                    )
                }
            }
        })
        authViewModel.userInfoLiveData.observe(viewLifecycleOwner, { info ->
            when (info) {
                is UserAuthState.Success -> {
                    loadingDialog.hide()
                    closeFragment()
                }
                is UserAuthState.Error -> {
                    loadingDialog.hide()
                    showToast(info.error)
                }
                is UserAuthState.Loading -> loadingDialog.show()
            }
        })
    }

    fun submitUserInfo() = with(binding) {
        val userName = userNameEditText.text.toString().trim()
        val userLocation = selectLocationEditText.text.toString().trim()
        if (userName.isEmpty()) {
            showToast(getString(R.string.addUserName))
            return@with
        }
        if (mImageUri == null) {
            showToast(getString(R.string.addUserImage))
            return@with
        }
        if (userLocation.isEmpty()) {
            showToast(getString(R.string.addUserLocation))
            return@with
        }
        authViewModel.uploadUserInformation(userName, mImageUri!!, userLocation)
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