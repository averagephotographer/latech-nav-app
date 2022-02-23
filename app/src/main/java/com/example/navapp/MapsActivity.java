package com.example.navapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.navapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MapsActivity extends DrawerBaseActivity
    implements
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener,
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback

    {
        /**
         * Request code for location permission request.
         *
         * @see #onRequestPermissionsResult(int, String[], int[])
         */
        private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

        /**
         * Flag indicating whether a requested permission has been denied after returning in
         * {@link #onRequestPermissionsResult(int, String[], int[])}.
         */
        private boolean permissionDenied = false;

        private GoogleMap map;

        SearchView searchView;
        Button textView;
        boolean [] selectedService;
        ArrayList<Integer> servList = new ArrayList<>();
        String[] servArray = {"Printing Station", "Restroom", "Food"};

        ActivityMapsBinding activityMapBinding;
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            activityMapBinding = ActivityMapsBinding.inflate(getLayoutInflater());
            setContentView(activityMapBinding.getRoot());
            allocateActivityTitle("Map");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            searchView = findViewById(R.id.sv_location);
            textView = findViewById(R.id.textView);

            selectedService = new boolean[servArray.length];

            Button oButton = findViewById(R.id.overlayButton);

            Button floor1 = findViewById(R.id.floor1);

            Button floor2 = findViewById(R.id.floor2);

            Button floor3 = findViewById(R.id.floor3);

            Button floor4 = findViewById(R.id.floor4);

            oButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroundOverlayActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            floor1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroundOverlayActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroundOverlayActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            floor3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroundOverlayActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            floor4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroundOverlayActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            //Search bar
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    String location = searchView.getQuery().toString();
                    List<Address> addressList = null;
                    if(location != null || !location.equals("")){
                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        try{
                            addressList = geocoder.getFromLocationName(location,1);

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location));
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

            //Drop down box
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Initialize alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                    // set title
                    builder.setTitle("Services...");

                    // set dialog non cancelable
                    builder.setCancelable(false);

                    builder.setMultiChoiceItems(servArray, selectedService, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            // check condition
                            if (b) {
                                // when checkbox selected
                                // Add position  in lang list
                                servList.add(i);
                                // Sort array list
                                Collections.sort(servList);
                            } else {
                                // when checkbox unselected
                                // Remove position from langList
                                servList.remove(Integer.valueOf(i));
                            }
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for(int j = 0; j <servList.size(); j++){
                                plotMarker(servArray[servList.get(j)]);
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // dismiss dialog
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // use for loop
                            for (int j = 0; j < selectedService.length; j++) {
                                // remove all selection
                                selectedService[j] = false;
                                // clear language list
                                servList.clear();
                                // clear text view value
                                textView.setText("");
                            }

                            map.clear();
                        }
                    });
                    // show dialog
                    builder.show();
                }
            });
        }

        //PLaces markers for services
        public void plotMarker (String service){
            if(service == "Printing Station"){
                LatLng tolliverPM = new LatLng(32.526528647649464, -92.64882063776503);
                map.addMarker(new MarkerOptions().position(tolliverPM).title("Tolliver Printing Machine")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.printer)));

                LatLng bogardPM = new LatLng(32.52640965519668,-92.64548752289164);
                map.addMarker(new MarkerOptions().position(bogardPM).title("Bogard Printing Machine")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.printer)));

                LatLng iesbPM = new LatLng(32.52611772172414,-92.64361384794148);
                map.addMarker(new MarkerOptions().position(iesbPM).title("IESB Printing Machine")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.printer)));
            }

            if(service == "Restroom"){
                LatLng tolliverRR1 = new LatLng(32.52607292949391,-92.64892240716625);
                map.addMarker(new MarkerOptions().position(tolliverRR1).title("Tolliver Restroom #1")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.restroom)));

                LatLng bogardRR = new LatLng(32.526440165950156,-92.64581763137875);
                map.addMarker(new MarkerOptions().position(bogardRR).title("Bogard Restroom 1st Floor")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.restroom)));
            }
            if(service == "Food"){
                LatLng cfa = new LatLng(32.52694408327142,-92.6485453583843);
                map.addMarker(new MarkerOptions().position(cfa).title("Chick-fil-A")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.food)));

                LatLng bbq = new LatLng(32.526628066890815,-92.64899657456743);
                map.addMarker(new MarkerOptions().position(bbq).title("bbq")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.food)));

                LatLng sushi = new LatLng(32.52650835160256,-92.64905509236604);
                map.addMarker(new MarkerOptions().position(sushi).title("sushi")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.food)));

                LatLng pod = new LatLng(32.52632037835968,-92.64912272221017);
                map.addMarker(new MarkerOptions().position(pod).title("POD")
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.food)));
            }
        }

        @Override
        public void onMapReady (GoogleMap googleMap){
            map = googleMap;
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(32.52649980, -92.645448074)));
            map.moveCamera(CameraUpdateFactory.zoomTo(18));
            enableMyLocation();
            map.getUiSettings().setIndoorLevelPickerEnabled(false);
            //map.getUiSettings().setAllGesturesEnabled(false);
            // Create a LatLngBounds that includes the city of Adelaide in Australia.
            LatLngBounds adelaideBounds = new LatLngBounds(
                    new LatLng(32.52611447836993, -92.64626273239287), // SW bounds
                    new LatLng(32.526948557853814, -92.64511421685144)  // NE bounds
            );

// Constrain the camera target to the Adelaide bounds.
            map.setLatLngBoundsForCameraTarget(adelaideBounds);
            //Uses custom map

            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.campus));
            /*switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //check condition
                    if (isChecked){
                        //when switch button is checked
                        //Set night mode
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.campus));

                    }else{
                        //When switch button is unchecked
                        //Set light mode
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.campus_night));

                    }
                }
            });*/
    }

        //Allows for other icons to be used as markers
        private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
            // below line is use to generate a drawable.
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

            // below line is use to set bounds to our vector drawable.
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

            // below line is use to create a bitmap for our
            // drawable which we have added.
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            // below line is use to add bitmap in our canvas.
            Canvas canvas = new Canvas(bitmap);

            // below line is use to draw our
            // vector drawable in canvas.
            vectorDrawable.draw(canvas);

            // after generating our bitmap we are returning our bitmap.
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        /**
         * Enables the My Location layer if the fine location permission has been granted.
         */
        @SuppressLint("MissingPermission")
        private void enableMyLocation () {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

        @Override
        public boolean onMyLocationButtonClick () {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

        @Override
        public void onMyLocationClick (@NonNull Location location){
        // Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Current location:\n" + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

        // [START maps_check_location_permission_result]
        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
                return;
            }

            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Enable the my location layer if the permission has been granted.
                enableMyLocation();
            } else {
                // Permission was denied. Display an error message
                // [START_EXCLUDE]
                // Display the missing permission error dialog when the fragments resume.
                permissionDenied = true;
                // [END_EXCLUDE]
            }
        }
        // [END maps_check_location_permission_result]

        @Override
        protected void onResumeFragments () {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

        /**
         * Displays a dialog with error message explaining that the location permission is missing.
         */
        private void showMissingPermissionError () {
            PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}
