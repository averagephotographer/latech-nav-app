package com.example.navapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.BeaconSighting;

import com.gimbal.android.GimbalDebugger;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
import com.gimbal.logging.GimbalLogConfig;

import java.util.List;

public class BeaconsActivity extends AppCompatActivity {
    private static final String GIMBAL_API_KEY = "3f3ef8ff-52f3-46d3-9a8b-d784680b4c85";
    private PlaceManager placeManager;
    private PlaceEventListener placeEventListener;
    private BeaconEventListener beaconEventListener;
    private BeaconManager beaconManager;
    private String TAG = "beacon";

    public ArrayAdapter<String> listAdapter;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Gimbal.setApiKey(getApplication(),
                GIMBAL_API_KEY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons);


        initView();
        monitorPlace();
        listenBeacon();

        CommunicationManager.getInstance().startReceivingCommunications();
    }

    private void listenBeacon() {
        BeaconEventListener beaconEventListener = getBeaconEventListener();
        BeaconManager beaconManager = new BeaconManager();
        beaconManager.addListener(beaconEventListener);
        beaconManager.startListening();
    }

    private void monitorPlace() {
        placeEventListener = getPlaceEventListener();

        placeManager = PlaceManager.getInstance();
        placeManager.addListener(placeEventListener);
        placeManager.startMonitoring();
    }

    private void initView() {
        GimbalLogConfig.enableUncaughtExceptionLogging();
        listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1);

        listView = (ListView) findViewById(R.id.alist);

        listView.setAdapter(listAdapter);
        listAdapter.add(" Gimbal API Key got Set Successfuly");
        listAdapter.notifyDataSetChanged();
        GimbalDebugger.enableBeaconSightingsLogging();
    }

    private BeaconEventListener getBeaconEventListener() {
        android.util.Log.i(TAG, "BeaconEventListener started sucessfully...");
        BeaconEventListener beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting beaconSighting) {

                super.onBeaconSighting(beaconSighting);


                listAdapter.add(String.format("Name of  Beacon is %s",
                        beaconSighting.getBeacon().getName()));
                listAdapter.add(String.format("UUID is %s", beaconSighting
                        .getBeacon().getUuid()));
                listAdapter.add(String.format("RSSI is %s",
                        beaconSighting.getRSSI()));
                listAdapter.add(String.format("Battery Level is %s",
                        beaconSighting.getBeacon().getBatteryLevel()));
                listAdapter.add(String.format("Temprature data is %s",
                        beaconSighting.getBeacon().getTemperature()));

            }
        };
        return beaconSightingListener;
    }

    private PlaceEventListener getPlaceEventListener() {

        PlaceEventListener obj = new PlaceEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sight, List<Visit> visit) {


                super.onBeaconSighting(sight, visit);

                listAdapter.add(String.format("Beacon Found: %s",
                        sight.getBeacon()));
                listAdapter.add(String.format("Name of Beacon is %s", sight
                        .getBeacon().getName()));
                listAdapter.add(String.format("Identifier  is %s", sight
                        .getBeacon().getIdentifier()));
                listAdapter.add(String.format("RSSI is %s", sight.getRSSI()));
                listAdapter.add(String.format("UUID is %s", sight.getBeacon()
                        .getUuid()));
                listAdapter.add(String.format("Temprature is%s", sight
                        .getBeacon().getTemperature()));
                listAdapter.add(String.format("BatteryLevel is %s", sight
                        .getBeacon().getBatteryLevel()));
                listAdapter.add(String.format("Icon URL is %s", sight
                        .getBeacon().getIconURL()));

                listAdapter.add(String.format("Start Visit for %s", visit
                        .iterator().toString()));

            }

            public void onVisitStart(Visit visit) {
                super.onVisitStart(visit);

                listAdapter.add(String.format("Start Visit for %s", visit
                        .getPlace().getName()));

                Toast.makeText(getApplicationContext(),
                        visit.getPlace().getName(), Toast.LENGTH_SHORT).show();
                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onVisitEnd(Visit visit) {
                // TODO Auto-generated method stub
                super.onVisitEnd(visit);

                listAdapter.add(String.format("End Visit for %s", visit
                        .getPlace().getName()));
                listAdapter.notifyDataSetChanged();

            }

        };
        return obj;
    }

}
