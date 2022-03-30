package com.mhmdawad.superest.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mhmdawad.superest.model.convertMapToUserInfoModel
import com.mhmdawad.superest.util.USERS_COLLECTION
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class MainRepository
@Inject
constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
){

    suspend fun checkIfUserHasInfo(): Boolean {
        return try {
                val userUid = firebaseAuth.uid
                val userData =
                    fireStore.collection(USERS_COLLECTION).document(userUid!!).get().await()
            println(">>>>>>>>>>>>>>>x ${convertMapToUserInfoModel(userData.data!!)}")
                userData.data != null
            } catch (e: Exception) {
            println(">>>>>>>>>>>>e ${e.message}")
                false
            }
    }
}