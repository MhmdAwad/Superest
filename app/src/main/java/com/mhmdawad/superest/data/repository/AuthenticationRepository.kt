package com.mhmdawad.superest.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.model.convertMapToUserInfoModel
import com.mhmdawad.superest.model.toMap
import com.mhmdawad.superest.util.*
import com.mhmdawad.superest.util.helper.SharedPreferenceHelper
import com.mhmdawad.superest.util.state.MainAuthState
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
    ): Resource<Unit?> {
        return try {
            val uploadedImagePath = uploadUserImage(imageUri)
            val firebaseUserCollection = firebaseFirestore.collection(USERS_COLLECTION)
            val userDocumentId = firebaseAuth.uid!!
            val userInfoModel =
                UserInfoModel(userDocumentId, userName, uploadedImagePath, userLocation)
            firebaseUserCollection.document(userDocumentId).set(userInfoModel.toMap()).await()
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.errorCreateAccount))
        }
    }

    private suspend fun uploadUserImage(imageUri: Uri): String {
        val uploadingResult =
            firebaseStorage.reference.child("${USERS_COLLECTION}/${System.currentTimeMillis()}.jpg")
                .putFile(imageUri).await()
        return uploadingResult.metadata?.reference?.downloadUrl?.await().toString()
    }

    suspend fun signInWithCredential(credential: AuthCredential): Resource<Unit?> {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.errorMessage))
        }
    }

    // check if user has data into firebase firestore or not.
    suspend fun getUserInformation(userInfoLiveData: MutableLiveData<Resource<UserInfoModel>>) {
        try {
            val userUid = firebaseAuth.uid!!
            val task = firebaseFirestore.collection(USERS_COLLECTION).document(userUid).get().await()
            val userInfoModel = convertMapToUserInfoModel(task.data!!)
            userInfoLiveData.postValue(Resource.Success(userInfoModel))
        } catch (e: Exception) {
            userInfoLiveData.postValue(Resource.Error(context.getString(R.string.errorMessage)))
        }
    }

}