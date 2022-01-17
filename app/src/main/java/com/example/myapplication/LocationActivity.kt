package com.example.myapplication

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.Intent
import android.location.Geocoder
import android.widget.*
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class LocationActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // initialize fused location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btSearchBar.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 111)

        else
            btSearchBar.isEnabled = true

        searchBar()

        btSearchBar.setOnClickListener{
            getSearchedLocation()
        }

        btGetLocation.setOnClickListener{
            getCurrentLocation()
        }

        btOpenMap.setOnClickListener{
            openMap()
        }
    }

    private fun searchBar() {
        val autotextView = findViewById<AutoCompleteTextView>(R.id.autoTextView)
        // Get the array of classes
        val classes = resources.getStringArray(R.array.Classes)
        // Create adapter and add in AutoCompleteTextView
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, classes
        )
        autotextView.setAdapter(adapter)
    }

    private fun getSearchedLocation() {

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)

            return
        }


        var city = autoTextView.text.toString()
        var gc = Geocoder(this, Locale.getDefault())
        var addresses = gc.getFromLocationName(city, 0)
        var address = addresses.get(0)

        tvLatitude.text = "Latitude: ${address.latitude}"
        tvLongitude.text = "Longitude: ${address.longitude}"


    }

    private fun getCurrentLocation() {
        // checking language permission
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)

            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known location or current location
                // latitude = location.latitude
                longitude = location.longitude
                tvLatitude.text = "Latitude: ${location.latitude}"
                tvLongitude.text = "Longitude: ${location.longitude}"
                tvProvider.text = "Provider: ${location.provider}"

                btOpenMap.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            btSearchBar.isEnabled = true
        }

        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openMap() {
        startActivity(Intent(this, MapsActivity::class.java))
    }


}