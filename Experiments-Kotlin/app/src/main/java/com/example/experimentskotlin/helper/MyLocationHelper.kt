package com.example.experimentskotlin.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.corenetwork.extension.getLocalizedString
import com.example.corenetwork.helpers.NumberFormatter
import com.example.experimentskotlin.R
import com.example.experimentskotlin.view.custom.StyledAlertDialogBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


/**
 * Класс хелпер для получения местоположения с проверкой Permissions
 *
 */
class MyLocationHelper(
    private val fragment: Fragment
) {
    private val context = fragment.context!!

    //region public
    /**
     * получить мое местоположение
     */
    fun getMyLocation(
        force: Boolean = false,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
        onNotAllowed: () -> Unit,
        onDisabled: () -> Unit,
        onGotLocation: (LatLng) -> Unit,
        onError: () -> Unit
    ) {
        this.force = force
        this.onGranted = onGranted
        this.onDenied = onDenied
        this.onNotAllowed = onNotAllowed
        this.onDisabled = onDisabled
        this.onGotLocation = onGotLocation
        this.onError = onError
        getMyLocation()
    }

    //must be called in onRequestPermissionsResult
    /**
     * по запросу разрешения результат
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the location-related task you need to do.
                sendLocation()
            } else {
                onDenied()
            }
        }
    }
    //endregion

    //region Private
    private var force = false
    private lateinit var onGranted: () -> Unit
    private lateinit var onDenied: () -> Unit
    private lateinit var onNotAllowed: () -> Unit
    private lateinit var onDisabled: () -> Unit
    private lateinit var onGotLocation: (LatLng) -> Unit
    private lateinit var onError: () -> Unit

    @Suppress("LocalVariableName", "PrivatePropertyName")
    private val REQUEST_LOCATION_CODE = 101

    /**
     * предоставлено разрешение на местоположение?
     */
    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * получить мое местоположение
     */
    private fun getMyLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isLocationPermissionGranted()) {
                sendLocation()
            } else {
                checkAndRequestLocationPermission()
            }
        } else {
            sendLocation()
        }

    }

    /**
     * проверить и запросить разрешение на размещение
     */
    private fun checkAndRequestLocationPermission() {
        if (!isLocationPermissionGranted()) {
            if (fragment.shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            } else {
                onNotAllowed()
                if (force) {
                    force = false
                    StyledAlertDialogBuilder(context)
                        .setMessage(context.getLocalizedString(R.string.location_permission_text))
                        .setNegativeButton(context.getLocalizedString(R.string.cancel)) { _, _ -> }
                        .setPositiveButton(R.string.settings) { _, _ ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:" + fragment.activity!!.packageName)
                            context.startActivity(intent)
                        }
                        .create()
                        .show()
                }
            }
        }
    }


    private lateinit var locationManager: LocationManager
    private var locationProviderClient: FusedLocationProviderClient? = null
    @SuppressLint("MissingPermission")
    @Synchronized
    /**
     * отправить местоположение
     */
    private fun sendLocation() {
        onGranted()
        if (locationProviderClient == null)
            locationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (isLocationEnabled(context)) {
            val lastLocation = locationProviderClient?.lastLocation
            if (lastLocation != null)
                lastLocation.addOnSuccessListener { loc ->
                    try {
                        onGotLocation(LatLng(loc.latitude, loc.longitude))
                    } catch (e: Throwable) {
                        requestLocationManually()
                    }
                }
            else
                onError.invoke()
        } else {
            onDisabled()
            if (force) {
                force = false
                StyledAlertDialogBuilder(context)
                    .setMessage(context.getLocalizedString(R.string.to_continue_enable_geolocation))
                    .setNegativeButton(context.getLocalizedString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(R.string.settings) { _, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    }
                    .create()
                    .show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    /**
     * запрос местоположения вручную
     */
    private fun requestLocationManually() {
        try {
            if (!::locationManager.isInitialized)
                locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            locationManager.requestSingleUpdate(criteria, object : LocationListener {
                override fun onLocationChanged(loc: Location) {
                    onGotLocation(LatLng(loc.latitude, loc.longitude))
                }
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, null)
        } catch (e: Throwable) {
            onError()
        }
    }

    @SuppressLint("InlinedApi")
    /**
     * расположение включено
     */
    private fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            // This is Deprecated in API 28
            val mode = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }
    //endregion

    //region Static
    companion object {
        /**
         * получить расстояние
         */
        fun getDistance(coordinate1: LatLng, coordinate2: LatLng?): Int {
            return try {
                val loc1 = Location("")
                loc1.latitude = coordinate1.latitude
                loc1.longitude = coordinate1.longitude

                val loc2 = Location("")
                loc2.latitude = coordinate2!!.latitude
                loc2.longitude = coordinate2!!.longitude

                loc1.distanceTo(loc2).toInt()
            } catch (it: Throwable) {
                Int.MAX_VALUE
            }
        }

        /**
         * расстояние до
         */
        fun distanceToString(distanceInMeters: Int, context: Context): String {
            return if (distanceInMeters < 1000) {
                "$distanceInMeters ${context.getLocalizedString(R.string.meters_dot)}"
            } else {
                val kilometersString = NumberFormatter.formatToStringTwoZeros(distanceInMeters / 1000f)
                "$kilometersString ${context.getLocalizedString(R.string.kilometers_dot)}"
            }
        }
    }
    //endregion
}