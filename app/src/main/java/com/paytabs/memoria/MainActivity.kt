package com.paytabs.memoria

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.paytabs.memoria.databinding.ActivityMainBinding


@SuppressLint("UnspecifiedImmutableFlag")
class MainActivity : AppCompatActivity() {

    private lateinit var geofencingClient: GeofencingClient
    private val geofenceList = ArrayList<Geofence>()
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
    private val permissionManager by lazy {
        PermissionManager(this)
    }
    lateinit var binding: ActivityMainBinding
    private var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val latLng: LatLng? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.extras?.getParcelable("latLng", LatLng::class.java)
            } else {
                result.data?.extras?.getParcelable("latLng")
            }
            addPointToGeofence(latLng)
        }
    }

    private fun addPointToGeofence(latLng: LatLng?) {
        latLng?.apply {
            createGeofenceList(this.latitude, this.longitude)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        geofencingClient = LocationServices.getGeofencingClient(this)

        if (!permissionManager.isLocationPermissionGranted) {
            permissionManager.requestUserLocationPermission()
        } else if (!permissionManager.isBackgroundLocationPermissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionManager.requestBackgroundLocation()
            }
        } else {
        }
        initUi()


    }

    private fun initUi() {
        binding.apply {
            button.setOnClickListener {
                addGeofence(getGeofencingRequest())
            }
            button2.setOnClickListener {
                val intent = Intent(this@MainActivity, SelectLatLngActivity::class.java)
                activityResultLauncher.launch(intent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(geofencingRequest: GeofencingRequest) {
        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(this@MainActivity, "sdasdasd", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(this@MainActivity, "aaaaaaaa", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun createGeofenceList(latitude: Double, longitude: Double) {
        geofenceList.add(
            Geofence.Builder().setRequestId("1").setCircularRegion(
                latitude, longitude, GEOFENCE_RADIUS_IN_METERS
            ).setExpirationDuration(1000* 60*60*24)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )

    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }
}