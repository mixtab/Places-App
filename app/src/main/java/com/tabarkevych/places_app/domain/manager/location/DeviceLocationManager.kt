package com.tabarkevych.places_app.domain.manager.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tabarkevych.places_app.exeptions.LocationException
import com.tabarkevych.places_app.extensions.hasLocationPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceLocationManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location> {
        return callbackFlow {
            if (context.hasLocationPermission().not()) {
                throw LocationException("Missing location permission")
            }
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnable =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGpsEnable.not() && isNetworkEnable.not()) {
                throw LocationException("GPS is disabled")
            }
            val locationRequest = LocationRequest.create()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    val lastLocation = result.locations.lastOrNull()
                    lastLocation?.let { trySendBlocking(it) }
                }
            }
            fusedLocationClient.requestLocationUpdates (locationRequest, locationCallback, Looper.getMainLooper())
            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

}