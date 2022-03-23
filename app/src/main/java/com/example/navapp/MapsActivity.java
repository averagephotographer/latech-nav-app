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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.maps.model.Marker;
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

        private int floor;

        Button textView;
        boolean [] selectedService;
        ArrayList<Integer> servList = new ArrayList<>();
        String[] servArray = {"Classrooms", "Professors", "Resources"};
        String[] countries={"India","Australia","West indies","indonesia","Indiana",
                "South Africa","England","Bangladesh","Srilanka","singapore"};

        String[][] prof1 = {{"Dr. Turner","","32.52577997908046","-92.64494910836221"},{"Dr. Choi","","32.525781675208066","-92.64499101787806"},{"Dr. Prather","","32.52578337133561","-92.64502856880426"},
                {"Dr. O'Neal","","32.52578478477521","-92.64506410807371"},{"Dr. Cox","","32.52551481740722","-92.64453705400229"},{"Dr. Biggs","","32.525479481304224","-92.64453705400229"},
                {"Dr. Glisson","","32.52558124924317","-92.64442943036556"},{"Dr. Bowman","","32.52576584468266","-92.64450687915087"},{"Dr. Abdoulahi","","32.525768106186476","-92.64454644173385"},
                {"Dr. Gates","","32.52576980231428","-92.64459673315288"}, {"Dr. Hyde","","32.525772346505946","-92.64470033347605"}};

        String[][] class1 = {{"NETH105","","32.52568132093739","-92.64510601758957"},{"NETH120","","32.525668882653775","-92.64491591602562"},
                {"Tom Emory Lecture Hall","","32.5255343229301","-92.64472112059592"},{"NETH153","","32.52564146188608","-92.64442540705204"}};

        String[][] re1 = {{"Admin Office" , "Need assistance?", "32.52577997908046","-92.6448592543602"},
                {"NETH103: Machinery I","","32.52561630261767","-92.64510802924633"},{"NETH101: Data Mining Rese Lab","","32.52555128425088","-92.6451164111495"},
                {"NETH100: Power Systems Lab","","32.525540824770104","-92.64499738812447"}, {"NETH102: Electrical Distribution","","32.52559340485038","-92.64499738812447"},
                {"NETH104: Machinery II","","32.52562676208967","-92.64499101787806"}, {"NETH106: Instrument Room","","32.52567255760138","-92.64502018690108"},
                {"Mens Bathroom","","32.5256883881432","-92.64481533318758"},
                {"Women Bathroom","","32.52563552543013","-92.64481734484436"},
                {"The Grid","","32.52565390017351","-92.64469999819994"}, {"Computer Lab I","","32.52564485414647","-92.64462288469075"}, {"Computer Lab II","","32.52564683296495","-92.64457024633883"},
                {"Mens Bathroom","","32.52566633845918","-92.64451190829277"},
                {"Artificial Intelligence","","32.52552160193739","-92.64443177729844"},{"Optoelectronics Lab","","32.52576273511486","-92.64445658773184"},
                {"Student Organizations","","32.52576838887443","-92.64465440064669"}};

        String[][] prof2 = {{"Dr. Hartmann","","32.52578817703018","-92.6450765132904"}, {"Dr. El-Awadi","","32.52578704627854","-92.64502689242363"},
                {"Dr. Bhattari","","32.5257859155269","-92.64498800039291"}, {"Dr. Hutchinson","","32.5257861982148","-92.64495078474283"}, {"Dr. Chen","","32.52578082714428","-92.64491323381662"},
                {"Dr. Green","","32.52578082714428","-92.64487367123365"},{"Dr. Liu","","32.52578082714428","-92.64484014362097"},{"Dr. Nassar","","32.52577884832872","-92.64480259269475"},
                {"Dr. Cox","","32.525774608009584","-92.64470435678959"},{"Dr. Dai","","32.525774608009584","-92.64459773898123"},{"Dr. Chowriappa","","32.525771215754105","-92.64455180615187"},
                {"Dr. Box","","32.525769236938345","-92.64451425522566"},{"Dr. Cherry","","32.52576754081052","-92.64447435736656"},{"Dr. Greechie","","32.52576754081052","-92.64444317668676"},
                {"Dr. Min", "", "32.52559001258808","-92.64442641288042"}};

        String[][] class2 = {{"NETH209","","32.52568697470208","-92.64510668814181"},{"NETH243","","32.52565700974508","-92.64442473649979"},
                {"NETH244","","32.525579553111804","-92.64452900737524"},{"NETH216","","32.52567905943139","-92.64496352523565"}};

        String [][] re2 = {{"Electronics I","","32.52554874005298","-92.64511406421663"},{"Computer Room","","32.52561941219054","-92.64511004090309"},{"Mens Bathroom","","32.525688670831435","-92.64450989663601"},
                {"Womens Bathroom","","32.525662380823015","-92.64456454664469"},{"Access Grid Room","","32.5256654903943","-92.64462489634751"},{"Automatic Controls","","32.52566859996549","-92.6446946337819"},
                {"Circuits & PLC Lab","","32.52567170953655","-92.64476772397758"},{"Mens Bathroom","","32.52567199222484","-92.64477007091044"},
                {"Circuits II","","32.525675667172315","-92.64483880251645"},{"Circuits I","","32.525678776743156","-92.64490585774183"},
                {"Solid State Devices","","32.52558746839125","-92.64499470591545"},{"Microwaves","","32.52552951722195","-92.64499671757221"}};


        ActivityMapsBinding activityMapBinding;
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            activityMapBinding = ActivityMapsBinding.inflate(getLayoutInflater());
            setContentView(activityMapBinding.getRoot());
            allocateActivityTitle("Map");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            Button filter = findViewById(R.id.filter);

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
                    floor = 1;
                    mMap.clear();
                    LatLng nethken = new LatLng(32.525641920516314, -92.64477126104399);

                    GroundOverlayOptions neth = new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken_floor1))
                            .position(nethken, 76f, 46f);

                    mMap.addGroundOverlay(neth);

                }
            });

            floor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floor = 2;
                    mMap.clear();
                    LatLng nethken = new LatLng(32.525641920516314, -92.64477126104399);

                    GroundOverlayOptions neth = new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken_floor2))
                            .position(nethken, 76f, 46f);

                    mMap.addGroundOverlay(neth);

                }
            });


            //Drop down box
            filter.setOnClickListener(new View.OnClickListener() {
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
            if(floor == 2) {
                if (service == "Classrooms") {
                    for (int i = 0; i < class2.length; i++) {
                        double x = Double.parseDouble(class2[i][2]);
                        double y = Double.parseDouble(class2[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(class2[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.classroom_dot))
                                .snippet(class2[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());

                                    return row;
                                }
                            });
                        }

                    }
                }

                if (service == "Professors") {
                    for (int i = 0; i < prof2.length; i++) {
                        double x = Double.parseDouble(prof2[i][2]);
                        double y = Double.parseDouble(prof2[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(prof2[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.professor_dot))
                                .snippet(prof2[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());

                                    return row;
                                }
                            });
                        }

                    }
                }

                if (service == "Resources") {
                    for (int i = 0; i < re2.length; i++) {
                        double x = Double.parseDouble(re2[i][2]);
                        double y = Double.parseDouble(re2[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(re2[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.resource_dot))
                                .snippet(re2[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());

                                    return row;
                                }
                            });
                        }

                    }
                }
            }
            if(floor == 1) {
                if (service == "Classrooms") {
                    for (int i = 0; i < class1.length; i++) {
                        double x = Double.parseDouble(class1[i][2]);
                        double y = Double.parseDouble(class1[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(class1[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.classroom_dot))
                                .snippet(class1[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());

                                    return row;
                                }
                            });
                        }

                    }
                }

                if (service == "Professors") {
                    for (int i = 0; i < prof1.length; i++) {
                        double x = Double.parseDouble(prof1[i][2]);
                        double y = Double.parseDouble(prof1[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(prof1[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.professor_dot))
                                .snippet(prof1[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());

                                    return row;
                                }
                            });
                        }

                    }
                }

                if (service == "Resources") {
                    for (int i = 0; i < re1.length; i++) {
                        double x = Double.parseDouble(re1[i][2]);
                        double y = Double.parseDouble(re1[i][3]);
                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(re1[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.resource_dot))
                                .snippet(re1[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());

                                    return row;
                                }
                            });
                        }

                    }
                }
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
            mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
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
