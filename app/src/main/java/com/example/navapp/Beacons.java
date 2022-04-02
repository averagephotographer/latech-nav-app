package com.example.navapp;

import android.app.Application;
import android.os.Bundle;
import com.gimbal.android.Gimbal;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;

public class Beacons extends Application {
    private static final String GIMBAL_API_KEY = "16cfe38cfcda5d67c9f559e2b63e6d1c";

    private static final String GIMBAL_APP_API_KEY = "YOUR APP'S API KEY HERE";

    public void onCreate(Bundle savedInstanceState) {

        Gimbal.setApiKey(this, GIMBAL_API_KEY);

    }


}
