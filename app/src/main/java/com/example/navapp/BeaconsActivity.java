package com.example.navapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gimbal.android.Gimbal;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.BeaconManager;

import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.Place;
import com.gimbal.android.Visit;
import com.gimbal.location.established.Location;


public class BeaconsActivity extends AppCompatActivity {
    private static final String GIMBAL_API_KEY = "3f3ef8ff-52f3-46d3-9a8b-d784680b4c85";
    private static int r = 0;
    private static final String RSSI = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gimbal.setApiKey(this.getApplication(), GIMBAL_API_KEY);

        BeaconManager manager = new BeaconManager();

        manager.startListening();
    }

    public void onVisitStart(Visit visit) {
        // This will be invoked when a place is entered
        android.util.Log.i("Notification: ", "Inside onVisitStart");
    }


    // This will be invoked upon beacon sighting
    public void onBeaconSighting(BeaconSighting sighting) {
        sighting.getBeacon();
        android.util.Log.i("RSSI: ", sighting.getRSSI().toString());
        android.util.Log.i("Beacon Name: ", sighting.getBeacon().toString());
    }

    public void locationDetected(Location location) {
        // this will be invoked when Gimbal has detected a reliable location
        android.util.Log.i("Notification: ", "Inside locationDetected");
    }
}
