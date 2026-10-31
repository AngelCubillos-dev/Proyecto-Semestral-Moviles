package com.example.proyectomastermartini.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

/** Obtiene la ubicación actual usando solo el LocationManager del sistema (sin Google Play Services). */
object LocationHelper {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onResult: (Location?) -> Unit) {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .firstOrNull { runCatching { manager.isProviderEnabled(it) }.getOrDefault(false) }

        if (provider == null) {
            onResult(null)
            return
        }
        try {
            LocationManagerCompat.getCurrentLocation(
                manager,
                provider,
                null as CancellationSignal?,
                ContextCompat.getMainExecutor(context)
            ) { location -> onResult(location) }
        } catch (e: SecurityException) {
            onResult(null)
        }
    }

    fun format(lat: Double, lon: Double): String = "%.5f, %.5f".format(lat, lon)
}
