package com.example.navapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gimbal.android.Beacon;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.BeaconManager;

import com.gimbal.android.Place;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
import com.gimbal.location.established.Location;

import java.util.List;



public class BeaconsActivity extends AppCompatActivity {
    private static final String GIMBAL_API_KEY = "3f3ef8ff-52f3-46d3-9a8b-d784680b4c85";
    private static int r = 0;
    private static final String RSSI = "";
    private PlaceManager placeManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gimbal.setApiKey(this.getApplication(), GIMBAL_API_KEY);
        Gimbal.start();

        BeaconManager manager = new BeaconManager();



        manager.startListening();

        android.util.Log.i("place_manager", "" + PlaceManager.getInstance().currentVisits());

        BeaconManager bm = new BeaconManager();
        bm.addListener(new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting beaconSighting) {

                super.onBeaconSighting(beaconSighting);

                android.util.Log.i("beacon1", beaconSighting.getBeacon().toString());
                android.util.Log.i("beacon2", String.format("", beaconSighting.getRSSI()));
            }
        });

        PlaceEventListener placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {

                super.onVisitStart(visit);
                //do anything
            }

            @Override
            public void onVisitEnd(Visit visit) {

                super.onVisitEnd(visit);
                //do anything
            }

            public void onBeaconSighting(BeaconSighting sighting, List<Visit> visits) {
                android.util.Log.i("beacon1", sighting.getBeacon().toString());
                android.util.Log.i("beacon2", String.format("", sighting.getRSSI()));
                // This will be invoked when a beacon assigned to a place within a current visit is sighted.
            }
        };

        placeManager = PlaceManager.getInstance();
        placeManager.addListener(placeEventListener);
        placeManager.startMonitoring();

        CommunicationManager.getInstance().startReceivingCommunications();

    }

    public void onVisitStart(Visit visit) {
        // This will be invoked when a place is entered
        android.util.Log.i("Notification ", "Inside onVisitStart");
    }


    // This will be invoked upon beacon sighting
    public void onBeaconSighting(BeaconSighting sighting, List<Visit> visits) {
        Beacon beacon = sighting.getBeacon();
        android.util.Log.i("beacon name", beacon.getName());
        android.util.Log.i("RSSI: ", sighting.getRSSI().toString());
        android.util.Log.i("Beacon Name: ", sighting.getBeacon().toString());
        android.util.Log.i("visits", visits.toString());
    }

    public void locationDetected(Location location) {
        // this will be invoked when Gimbal has detected a reliable location
        android.util.Log.i("Notification: ", "Inside locationDetected");
    }
}
