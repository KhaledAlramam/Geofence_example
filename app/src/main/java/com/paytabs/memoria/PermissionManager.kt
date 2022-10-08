package com.paytabs.memoria

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission

class PermissionManager(
    activity: AppCompatActivity,
) {

    var isLocationPermissionGranted = false
    var isBackgroundLocationPermissionGranted = false
    private val locationPermissionProvider = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        isLocationPermissionGranted = granted
        if (!isBackgroundLocationPermissionGranted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestBackgroundLocation()
            }
        }
    }
    private val backgroundLocationPermissionProvider = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        isBackgroundLocationPermissionGranted = granted
    }


    init {
        isLocationPermissionGranted = checkLocationPermission(activity)
        isBackgroundLocationPermissionGranted = checkBGLocationPermission(activity)
    }

    private fun checkLocationPermission(activity: AppCompatActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) && checkSelfPermission(
                activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            (checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun checkBGLocationPermission(activity: AppCompatActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (checkSelfPermission(
                activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    fun requestUserLocationPermission() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestBackgroundLocation() {
        backgroundLocationPermissionProvider.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }
}