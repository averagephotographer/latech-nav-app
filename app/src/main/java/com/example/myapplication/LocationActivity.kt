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
import com.google.android.gms.maps.GoogleMap
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

        btSearchBar.isEnabled = true
        searchBar()

        btSearchBar.setOnClickListener{
            getSearchedLocation()
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, classes)
        autotextView.setAdapter(adapter)
    }

    private fun getSearchedLocation() {
        var city = autoTextView.text.toString()
        println(city)
        var gc = Geocoder(this, Locale.getDefault())
        var addresses = gc.getFromLocationName(city, 1)
        var address = addresses.get(0)

        tvLatitude.text = "Latitude: ${address.latitude}"
        tvLongitude.text = "Longitude: ${address.longitude}"
    }

    private fun openMap() {
        startActivity(Intent(this, MapsActivity::class.java))
    }


}