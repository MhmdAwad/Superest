package com.mhmdawad.superest.model

import com.google.firebase.firestore.DocumentSnapshot

data class OffersModel(
    val offerImage: String,
    val offerId: String
)

fun convertDocumentsToOfferList(document: List<DocumentSnapshot>): List<OffersModel>{
    val list = mutableListOf<OffersModel>()
    document.forEach {
        list.add(
            OffersModel(it["offerImage"].toString(), it["offerId"].toString())
        )
    }
    return list
}