package com.mhmdawad.superest.util.extention

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.mhmdawad.superest.R
import java.util.*


private var listener: ViewTreeObserver.OnGlobalLayoutListener? = null

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}


fun Fragment.closeFragment() {
    findNavController().popBackStack()
}

fun Fragment.stopKeyBoardListener() {
    if (listener != null)
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(listener)
}

fun Fragment.handleKeyBoardApparition(aboveView: View) {
    stopKeyBoardListener()
    with(view!!) {
        listener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            getWindowVisibleDisplayFrame(r)
            val heightDiff: Int = bottom - r.bottom
            val suggestionsBarHeight =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    25f,
                    activity!!.resources.displayMetrics
                ).toInt()
            aboveView.translationY = -(heightDiff + suggestionsBarHeight).toFloat()
        }
        viewTreeObserver?.addOnGlobalLayoutListener(listener)
    }
}

fun Fragment.isServicesOK(): Boolean {
    val available =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext())
    when {
        available == ConnectionResult.SUCCESS -> {
            //everything is fine and the user can make map requests
            return true
        }
        GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
            //an error occurred but we can resolve it
            val dialog: Dialog? = GoogleApiAvailability.getInstance()
                .getErrorDialog(requireActivity(), available, 101)
            dialog?.show()
        }
        else -> {
            showToast(getString(R.string.cannotMakeGPSRequest))
        }
    }
    return false
}

fun Fragment.isMapsEnabled(): Boolean {
    val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        buildAlertMessageNoGps()
        return false
    }
    return true
}

fun Fragment.buildAlertMessageNoGps() {
    val builder = AlertDialog.Builder(requireContext())
    builder.setMessage(getString(R.string.enableGPS))
        .setIcon(R.drawable.location)
        .setCancelable(false)
        .setPositiveButton(getString(R.string.enable)) { _, _ ->
            val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(enableGpsIntent)
        }
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun Fragment.bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Fragment.getCityNameFromLocation(locationLatLng: LatLng): String{
    val geocoder = Geocoder(requireContext(), Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(locationLatLng.latitude, locationLatLng.longitude, 1)
    return addresses[0].getAddressLine(0)
}
