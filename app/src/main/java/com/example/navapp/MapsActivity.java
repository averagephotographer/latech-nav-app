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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.model.GroundOverlayOptions;
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

        private GoogleMap mMap;

        Button textView;
        boolean [] selectedService;
        ArrayList<Integer> servList = new ArrayList<>();
        String[] servArray = {"Classrooms", "Professors", "Resources"};
        String[] countries={"India","Australia","West indies","indonesia","Indiana",
                "South Africa","England","Bangladesh","Srilanka","singapore"};

        String[][] prof1 = {{"Dr. Turner","","32.52578280595976","-92.64495279639961"},{"Dr. Choi","","",""},{"Dr. Prather","","",""},
                {"Dr. O'Neal","","",""},{"Professor Lounge","","",""},{"Dr. Cox","","",""},{"Dr. Biggs","","",""},
                {"Dr. Glisson","","",""},{"Dr. Bowman","","",""},{"Dr. Abdoulahi","","",""},
                {"Dr. Gates","","",""}, {"Dr. Hyde","","",""}};

        String[][] class1 = {{"","","",""},{"","","",""}};

        String[][] re1 = {{"Admin Office" , "", "32.525778000264914","-92.64488641172647"}};/*,
                {"Stairs -> Floor 2","","32.52578280595976","-92.64495279639961"}};,{"NETH103: Machinery I","","",""},{"NETH101: Data Mining Rese Lab","","",""},
                {"NETH100: Power Systems Lab","","",""}, {"NETH102: Electrical Distribution","","",""},
                {"NETH104: Machinery II","","",""}, {"NETH106: Instrument Room","","",""}, {"Elevator -> Floor 2","","",""},
                {"NETH118: Storage","","",""},{"Mens Bathroom","","",""},
                {"Janitor","","",""},{"Women Bathroom","","",""},{"Faculty Men","","",""},{"Conference Room","","",""},
                {"Storage","","",""},{"The Grid","","",""}, {"Computer Lab I","","",""}, {"Computer Lab II","","",""},
                {"Storage","","",""},{"Mens Bathroom","","",""}, {"Stairs -> Floor 2","","",""},
                {"Artificial Intelligence","","",""},{"Stairs -> Floor 2","","",""},{"Optoelectronics Lab","","",""},
                {"Student Organizations","","",""}, {"Stairs -> Floor 2","","",""}};*/


        ActivityMapsBinding activityMapBinding;
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            activityMapBinding = ActivityMapsBinding.inflate(getLayoutInflater());
            setContentView(activityMapBinding.getRoot());
            allocateActivityTitle("Map");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            textView = findViewById(R.id.textView);

            selectedService = new boolean[servArray.length];

            Button oButton = findViewById(R.id.overlayButton);

            Button floor1 = findViewById(R.id.floor1);

            Button floor2 = findViewById(R.id.floor2);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,countries);
            AutoCompleteTextView textView=(AutoCompleteTextView)findViewById(R.id.txtcountries);
            textView.setThreshold(3);
            textView.setAdapter(adapter);

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
                    mMap.clear();
                    LatLng nethken = new LatLng(32.525641920516314, -92.64477126104399);

                    GroundOverlayOptions neth = new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken_floor1))
                            .position(nethken, 76f, 46f);

                    mMap.addGroundOverlay(neth);
                    LatLng tolliverPM = new LatLng(32.525600627794965, -92.64475918225845);

                    mMap.addMarker(new MarkerOptions().position(tolliverPM).title("classroom")
                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.classroom_dot)));
                }
            });

            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMap.clear();
                    LatLng nethken = new LatLng(32.525641920516314, -92.64477126104399);

                    GroundOverlayOptions neth = new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken_floor2))
                            .position(nethken, 76f, 46f);

                    mMap.addGroundOverlay(neth);

                    /*for (int i = 0; i < re1.length; i++) {
                        double x = Double.parseDouble(re1[i][2]);
                        double y = Double.parseDouble(re1[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(re1[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.resource_dot)));
                    }*/
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

                            mMap.clear();
                        }
                    });
                    // show dialog
                    builder.show();
                }
            });
        }

        //PLaces markers for services
        public void plotMarker (String service){
            if(service == "Classrooms"){
                return;
            }

            if(service == "Professors"){
                return;
            }
            if(service == "Resources") {

            }
        }

        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;

            //Add nethken overlay
            LatLng neth = new LatLng(32.525641920516314, -92.64477126104399);
            GroundOverlayOptions nethken = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken))
                    .position(neth, 76f, 46f);
            mMap.addGroundOverlay(nethken);

            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);

            //zooms into nethken
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(32.52565148675839, -92.64475211432803)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
            enableMyLocation();

            //removes bogard floor buttons
            mMap.getUiSettings().setIndoorLevelPickerEnabled(false);

            //Set custom json map
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.campus));

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng arg0)
                {
                    String cords = arg0.toString();
                    android.util.Log.i("onMapClick", cords);
                }
            });

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
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
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
