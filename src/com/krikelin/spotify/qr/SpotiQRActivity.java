package com.krikelin.spotify.qr;

import com.google.zxing.integration.android.IntentIntegrator;

import android.os.Bundle;

public class SpotiQRActivity extends SWActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   ContentMatcher cm = new ContentMatcher();
     //  cm.setOnFinish(this);
       // cm.execute("828768608227");
      IntentIntegrator.initiateScan(this, "Barcode scanner is needed in order to get this working", "Do you want to install it", "Yes", "no");
        
    }
}