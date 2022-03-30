package com.mhmdawad.superest.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.model.toMap
import com.mhmdawad.superest.util.*
import com.mhmdawad.superest.util.state.MainAuthState
import com.mhmdawad.superest.util.state.UserAuthState
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class AuthenticationRepository
@Inject
constructor(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
) {

    fun checkIfFirstAppOpened(): Boolean = sharedPreferenceHelper.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn(): Boolean {
        val user = firebaseAuth.currentUser
        return user != null
    }

    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): UserAuthState {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            UserAuthState.Success
        } catch (e: Exception) {
            UserAuthState.Error(context.getString(R.string.errorMessage))
        }
    }


    fun phoneAuthCallBack(_phoneMainAuthLiveData: MutableLiveData<MainAuthState>): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _phoneMainAuthLiveData.value =
                    MainAuthState.SuccessWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _phoneMainAuthLiveData.value = MainAuthState.Error(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _phoneMainAuthLiveData.value =
                    MainAuthState.SuccessWithCode(verificationId, token)
            }
        }
    }

    suspend fun uploadUserInformation(
        userName: String,
        imageUri: Uri,
        userLocation: String
    ): UserAuthState {
        return try {
            val uploadedImagePath = uploadUserImage(imageUri)
            val firebaseUserCollection = firebaseFirestore.collection(USERS_COLLECTION)
            val userDocumentId = firebaseAuth.uid!!
            val userInfoModel =
                UserInfoModel(userDocumentId, userName, uploadedImagePath, userLocation)
            firebaseUserCollection.document(userDocumentId).set(userInfoModel.toMap()).await()
            UserAuthState.Success
        } catch (e: Exception) {
            UserAuthState.Error(context.getString(R.string.errorCreateAccount))
        }
    }

    private suspend fun uploadUserImage(imageUri: Uri): String {
        val uploadingResult =
            firebaseStorage.reference.child("${USERS_COLLECTION}/${System.currentTimeMillis()}.jpg")
                .putFile(imageUri).await()
        return uploadingResult.metadata?.reference?.downloadUrl?.await().toString()
    }

}