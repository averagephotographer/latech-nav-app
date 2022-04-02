package com.example.navapp;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gimbal.android.Gimbal;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.BeaconManager;

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

    public void onBeaconSighting(BeaconSighting sighting) {
        sighting.getBeacon();
        android.util.Log.i("RSSI: ", sighting.getRSSI().toString());
        android.util.Log.i("Beacon Name: ", sighting.getBeacon().toString());
        // This will be invoked upon beacon sighting
    }


}
