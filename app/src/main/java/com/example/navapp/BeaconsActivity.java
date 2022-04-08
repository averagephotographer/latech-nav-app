package com.example.navapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

//    String beacon_rssi_list[] = {"1","2","3","4","5","6","7","8","9","10"};
//    ArrayAdapter<String> arr;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beacons);

        Gimbal.setApiKey(this.getApplication(), GIMBAL_API_KEY);
        setUpGimbalPlaceManager();
        Gimbal.start(); // note: make sure to get location permission from user before this, see docs

//        l = findViewById(R.id.gimbal_information_list);
//        arr = new ArrayAdapter<String>(
//                this,
//                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
//                beacon_rssi_list);
//        l.setAdapter(arr);

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
//                String beacon_info_string = "" + sighting.getBeacon() + "\t" + sighting.getRSSI();
                String beacon_name = sighting.getBeacon().getName();
                Integer beacon_rssi = sighting.getRSSI();

                // get last digit of beacon
                Integer beacon_number = Integer.parseInt(beacon_name.substring(beacon_name.length() - 1));
//                beacon_rssi_list[beacon_number - 1] = "Beacon " + beacon_number + "\t" + sighting.getRSSI();
//                arr.notifyDataSetChanged();


//                android.util.Log.i("beacon_number_java", "" + beacon_number);

                //                for (Integer item : beacon_rssi_list)
//                    android.util.Log.i("loop_obs", "Beacon " + item.toString());


                android.util.Log.i("oBS: sighting", "" + sighting.toString());
                android.util.Log.i("oBS: visits", "" + visits.toString());
                // This will be invoked when a beacon assigned to a place within a current visit is sighted.
            }
        };

        PlaceManager.getInstance().addListener(placeEventListener);

    }

}
