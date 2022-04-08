package com.example.navapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
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
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
        SharedPreferences sharedPreferences;
        public static final String fileName = "login";
        public static final String Username = "username";
        private static final String GIMBAL_API_KEY = "3f3ef8ff-52f3-46d3-9a8b-d784680b4c85";
        private PlaceManager placeManager;

        int[][] beaconDistance = {{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{9,0},{10,0}};

        private int min = 0;

        private String beaconName;

        private int beaconVal;

        /**
         * Flag indicating whether a requested permission has been denied after returning in
         * {@link #onRequestPermissionsResult(int, String[], int[])}.
         */
        private boolean permissionDenied = false;

        private GoogleMap mMap;

        private int floor;

        LatLng nethken = new LatLng(32.525665490440126,-92.64472849667071);

        Button textView;
        boolean [] selectedService;
        ArrayList<Integer> servList = new ArrayList<>();
        String[] servArray = {"Classrooms", "Professors", "Resources"};
        String[] countries={"Dr. Turner","Dr. Choi","Dr. Prather","Dr. O'Neal","Dr. Cox","Dr. Biggs","Dr. Glisson","Dr. Bowman","Dr. Abdoulahi","Dr. Gates","Dr. Hyde","Dr. Hyde",
                "NETH105","NETH120","NETH140","NETH153","Admin Office","NETH103: Machinery I","NETH101: Data Mining Rese Lab","NETH100: Power Systems Lab","NETH104: Machinery II",
                "Professor's Lounge","Mens Bathroom","Women Bathroom","The Grid","Computer Lab I","Computer Lab II","Artificial Intelligence","Optoelectronics Lab","Student Organizations"};

        String[][] prof1 = {{"Dr. Turner","","32.525801180673014","-92.6449068635702", "turner"},{"Dr. Choi","","32.525801180673014","-92.64494743198156", "choi"},{"Dr. Prather","","32.525801180673014","-92.64498598873614", "prather"},
                {"Dr. O'Neal","","32.525801180673014","-92.64502186328173", "oneal"},{"Dr. Cox","","32.525540824770104","-92.6444934681058", "empty"},{"Dr. Biggs","","32.52550350985576","-92.6444934681058", "biggs"},
                {"Dr. Glisson","","32.52560160281711","-92.64438852667809", "glisson"},{"Dr. Bowman","","32.52578224058392","-92.64446564018726", "bowman"},{"Dr. Abdoulahi","","32.52578224058392","-92.64450185000896", "empty"},
                {"Dr. Gates","","32.52578224058392","-92.64455012977122", "gates"}, {"Dr. Hyde","","32.52578224058392","-92.64465942978859", "glisson"}};

        String[][] class1 = {{"NETH105","","32.52570252255319","-92.64506142586468"},{"NETH120","","32.52568697470208","-92.64487635344267"},
                {"NETH140","Tom Emory Lecture Hall","32.525549588118956","-92.64468055218458"},{"NETH153","","32.52566944803032","-92.64438852667809"}};

        String[][] re1 = {{"Admin Office" , "Need assistance?", "32.525801180673014","-92.64481633901596"},
                {"NETH103: Machinery I","","32.525644571458116","-92.64506142586468"},{"NETH101: Data Mining Rese Lab","","32.5255767262261","-92.64506142586468"},
                {"NETH100: Power Systems Lab","","32.525561461041846","-92.64495413750412"}, {"NETH104: Machinery II","","32.52565333479684","-92.64495413750412"},
                {"Professor's Lounge","","32.52568697470208","-92.64482203871012"},{"Mens Bathroom","","32.52571072050998","-92.64477141201495"},{"Women Bathroom","","32.52568527857272","-92.64477141201495"},
                {"The Grid","","32.5256751017958","-92.64465607702734"}, {"Computer Lab I","","32.5256751017958","-92.64458365738392"}, {"Computer Lab II","","32.5256751017958","-92.6445249840617"},
                {"Mens Bathroom","","32.5256883881432","-92.64446698129177"},
                {"Artificial Intelligence","","32.52554421703429","-92.64438852667809"},{"Optoelectronics Lab","","32.52578224058392","-92.64441300183535"},
                {"Student Organizations","","32.52578224058392","-92.6446084678173"}};

        String[][] prof2 = {{"Dr. Matthew Hartmann","Program Chair and Lecturer","32.52578817703018","-92.6450765132904"}, {"Zakaria El-Awadi","","32.52578704627854","-92.64502689242363"},
                {"Dr. Prashanna Bhattari","Assistant Professor","32.5257859155269","-92.64498800039291"}, {"Aaron Hutchinson","Assistant Professor","32.5257861982148","-92.64495078474283"}, {"Dr. Jinyuan Chen","Assistant Professor","32.52578082714428","-92.64491323381662"},
                {"Nathan Green","Assistant Professor","32.52578082714428","-92.64487367123365"},{"Dr. Don Liu","Professor","32.52578082714428","-92.64484014362097"},{"Dr. Raj Nassar","Professor Emeritus","32.52577884832872","-92.64480259269475"},
                {"Dr. Weizhong Dai","Professor – Mathematics and Statistics, Program Chair – Computational Analysis Modeling","32.525774608009584","-92.64459773898123"},{"Dr. Pradeep Chowriappa","Assistant Professor","32.525771215754105","-92.64455180615187"},
                {"Dr. Kevin Cherry","Lecturer","32.52576754081052","-92.64447435736656"},{"Dr. Richard Greechie","Professor Emeritus","32.52576754081052","-92.64444317668676"},
                {"Dr. Manki Min", "Associate Professor", "32.52559001258808","-92.64442641288042"}};

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
            sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
            if(!(sharedPreferences.contains(Username))){
                Intent i = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(i);

            }
            allocateActivityTitle("Map");

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            Button filter = findViewById(R.id.filter);

            selectedService = new boolean[servArray.length];

            Button oButton = findViewById(R.id.overlayButton);

            Button floor1 = findViewById(R.id.floor1);

            Button floor2 = findViewById(R.id.floor2);

            Button search = findViewById(R.id.search);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,countries);
            AutoCompleteTextView textView=(AutoCompleteTextView)findViewById(R.id.txtcountries);
            textView.setThreshold(3);
            textView.setAdapter(adapter);

            Gimbal.setApiKey(this.getApplication(), GIMBAL_API_KEY);
            setUpGimbalPlaceManager();
            Gimbal.start();



            oButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), BeaconsActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

            floor1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floor = 1;
                    mMap.clear();
                    //LatLng nethken = new LatLng(32.525665490440126,-92.64472849667071);

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
                    //LatLng nethken = new LatLng(32.525665490440126,-92.64472849667071);

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

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String desiredRoom = textView.getText().toString();
                    android.util.Log.i("onVisitEnd", desiredRoom);
                    for(int i = 0; i < prof1.length; i++){
                        android.util.Log.i("onVisitEnd", prof1[i][0]);
                        boolean isEqual = desiredRoom.equals(prof1[i][0]);
                        if(isEqual){

                            searchCamera(Double.parseDouble(prof1[i][2]),Double.parseDouble(prof1[i][3]), i);

                            android.util.Log.i("onVisitEnd", "they equal");
                            //LatLng searchedRoom = new LatLng(0,0);
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedRoom, 15));
                        }
                    }
                }
            });

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

                    //Gets name of the beacon that was sighted
                    beaconName = sighting.getBeacon().getName();

                    //Gets the number at the end of the beacon
                    beaconVal = Integer.parseInt(beaconName.substring(beaconName.length() - 1)) -1;

                    //Sets the RSSI value according to the scanned beacon
                    beaconDistance[beaconVal][1] = sighting.getRSSI();

                    //Checks for the smallest RSSI value among all 10 beacons
                    for(int i = 0; i < beaconDistance.length; i++){
                        if (beaconDistance[i][1] < min){
                            min = beaconDistance[i][1];
                            if(i < 6){
                                floor = 1;
                                mMap.clear();
                                //LatLng nethken = new LatLng(32.525665490440126,-92.64472849667071);

                                GroundOverlayOptions neth = new GroundOverlayOptions()
                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken_floor1))
                                        .position(nethken, 76f, 46f);

                                mMap.addGroundOverlay(neth);
                            }

                            else{
                                floor = 2;
                                mMap.clear();
                                //LatLng nethken = new LatLng(32.525665490440126,-92.64472849667071);

                                GroundOverlayOptions neth = new GroundOverlayOptions()
                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken_floor2))
                                        .position(nethken, 76f, 46f);

                                mMap.addGroundOverlay(neth);
                            }
                        }
                    }



                    android.util.Log.i("oBS: sighting", "" + sighting.toString());
                    android.util.Log.i("oBS: visits", "" + visits.toString());
                    // This will be invoked when a beacon assigned to a place within a current visit is sighted.
                }
            };

            PlaceManager.getInstance().addListener(placeEventListener);

        }

        public void searchCamera (double x, double y, int i) {
            LatLng searchedRoom = new LatLng(x, y);

            Marker marker = mMap.addMarker(new MarkerOptions().position(searchedRoom).title(prof1[i][0])
                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.professor_dot))
                    .snippet(prof1[i][1]));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedRoom, 22));
            marker.showInfoWindow();
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


                    }
                }

                if (service == "Professors") {
                    for (int i = 0; i < prof2.length; i++) {
                        double x = Double.parseDouble(prof2[i][2]);
                        double y = Double.parseDouble(prof2[i][3]);

                        String name = prof2[i][4];
                        int id = getResources().getIdentifier(name, "drawable", getPackageName());
                        Drawable drawable = getResources().getDrawable(id);

                        LatLng resource = new LatLng(x, y);
                        mMap.addMarker(new MarkerOptions().position(resource).title(prof2[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.professor_dot))
                                .snippet(prof2[i][1]));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView pic = (ImageView) row.findViewById(R.id.image);

                                    pic.setBackground(drawable);
                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());


                                    return row;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    return null;
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

                    }
                }

                if (service == "Professors") {
                    for (int i = 0; i < prof1.length; i++) {
                        double x = Double.parseDouble(prof1[i][2]);
                        double y = Double.parseDouble(prof1[i][3]);
                        LatLng resource = new LatLng(x, y);

                        //String name = prof1[i][4];
                        //int id = getResources().getIdentifier(name, "drawable", getPackageName());
                        //Drawable drawable = getResources().getDrawable(id);

                        mMap.addMarker(new MarkerOptions().position(resource).title(prof1[i][0])
                                .icon(BitmapFromVector(getApplicationContext(), R.drawable.professor_dot))
                                .snippet(prof1[i][1]));

                        //android.util.Log.i("onMapClick", String.valueOf(drawable));

                        if (mMap != null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker marker) {
                                    View row = getLayoutInflater().inflate(R.layout.custom_address,null);
                                    TextView title = (TextView) row.findViewById(R.id.title);
                                    TextView snippet = (TextView) row.findViewById(R.id.snippet);
                                    ImageView image = (ImageView) row.findViewById(R.id.image);

                                    title.setText(marker.getTitle());
                                    snippet.setText(marker.getSnippet());
                                    //image.setBackground(drawable);

                                    return row;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    return null;
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


                    }
                }
            }
        }

        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;

            //Add nethken overlay
            //LatLng neth = new LatLng(32.525665490440126,-92.64472849667071);
            GroundOverlayOptions neth = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.nethken))
                    .position(nethken, 76f, 46f);
            mMap.addGroundOverlay(neth);

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
               == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
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
