package com.example.navapp;

import android.app.Application;
import android.os.Bundle;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.BeaconManager;

public class Beacons extends Application {
    private static final String GIMBAL_API_KEY = "3f3ef8ff-52f3-46d3-9a8b-d784680b4c85";

    public void onCreate(Bundle savedInstanceState) {

        Gimbal.setApiKey(this, GIMBAL_API_KEY);

        BeaconManager manager = new BeaconManager();

        manager.startListening();

    }

    public void onBeaconSighting(BeaconSighting sighting) {
        sighting.getRSSI();
        // This will be invoked upon beacon sighting
    }


}
