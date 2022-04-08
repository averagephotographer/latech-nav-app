package com.example.navapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gimbal.android.Gimbal;
import com.gimbal.android.BeaconSighting;

import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;

import java.util.List;



public class BeaconsActivity extends AppCompatActivity {
    private static final String GIMBAL_API_KEY = "3f3ef8ff-52f3-46d3-9a8b-d784680b4c85";
    private static int r = 0;
    private static final String RSSI = "";
    private PlaceManager placeManager;
    ListView l;

    String beacon_rssi_list[] = {
            "Spot 1", "Spot 2",
            "Spot 3"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beacons);

        Gimbal.setApiKey(this.getApplication(), GIMBAL_API_KEY);
        setUpGimbalPlaceManager();
        Gimbal.start(); // note: make sure to get location permission from user before this, see docs

        l = findViewById(R.id.gimbal_information_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                beacon_rssi_list);
        l.setAdapter(arr);

        android.util.Log.i("isStarted", "" + Gimbal.isStarted());
    }

    private void setUpGimbalPlaceManager() {

        PlaceEventListener placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {
                super.onVisitStart(visit);
                android.util.Log.i("onVisitStart", "overridden");
            }

            @Override
            public void onVisitEnd(Visit visit) {
                android.util.Log.i("onVisitEnd", "overridden");
                super.onVisitEnd(visit);
            }

            public void onBeaconSighting(BeaconSighting sighting, List<Visit> visits) {

                android.util.Log.i("oBS: sighting", "" + sighting.toString());
                android.util.Log.i("oBS: visits", "" + visits.toString());
                // This will be invoked when a beacon assigned to a place within a current visit is sighted.
            }
        };

        PlaceManager.getInstance().addListener(placeEventListener);

    }

}
