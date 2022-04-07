package com.mhmdawad.superest.util.helper

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mhmdawad.superest.util.extention.createMarkerOption

class GoogleMapMarkerHelper {
    private var locationMarker: Marker? = null
    private var locationOptions: MarkerOptions? = null
    fun addNewLocationMarker(locationIcon: BitmapDescriptor, googleMap: GoogleMap?) {
        if (locationMarker != null)
            locationMarker?.remove()

        if (locationOptions == null || locationOptions?.icon != locationIcon)
            locationOptions = createMarkerOption(googleMap?.cameraPosition?.target!!, locationIcon)

        locationMarker = googleMap?.addMarker(locationOptions!!)
    }
}