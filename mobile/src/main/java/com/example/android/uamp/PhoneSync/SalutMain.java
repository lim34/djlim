package com.example.android.uamp.PhoneSync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.android.uamp.R;
import com.example.android.uamp.ui.ActionBarCastActivity;
import com.example.android.uamp.ui.MusicPlayerActivity;
import com.example.android.uamp.utils.LogHelper;
import com.google.gson.Gson;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright (c) 2015 Peak Digital LLC
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy
 *    of this software and associated documentation files (the "Software"), to deal
 *    in the Software without restriction, including without limitation the rights
 *    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *    copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in all
 *    copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */


public class SalutMain extends ActionBarCastActivity {

    /*
        This simple activity demonstrates how to use the Salut library from a host and client perspective.
     */
//    String is_host;
    public static final String TAG = "SalutTestApp";
//    public SalutDataReceiver dataReceiver;
//    public SalutServiceData serviceData;
//    public Salut network;
//    public Button hostingBtn;
//    public Button discoverBtn;
    //SalutDataCallback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        salutusing = true;
        setContentView(R.layout.phonesync_main);

        hostingBtn = (Button) findViewById(R.id.btnHost);
        discoverBtn = (Button) findViewById(R.id.btnjoin);
        disconnectBtn = (Button) findViewById(R.id.btnDisconnect);

        hostingBtn.setOnClickListener(this);
        discoverBtn.setOnClickListener(this);
        disconnectBtn.setOnClickListener(this);

        makeNetwork();
        initializeToolbar();
//
//
//        /*Create a data receiver object that will bind the callback
//        with some instantiated object from our app. */
//        dataReceiver = new SalutDataReceiver(this, this);
//
//
//        /*Populate the details for our awesome service. */
//        serviceData = new SalutServiceData("testAwesomeService", 60606,
//                "HOST");
//
//        /*Create an instance of the Salut class, with all of the necessary data from before.
//        * We'll also provide a callback just in case a device doesn't support WiFi Direct, which
//        * Salut will tell us about before we start trying to use methods.*/
//        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
//            @Override
//            public void call() {
//                // wiFiFailureDiag.show();
//                // OR
//                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
//            }
//        });

    }

    public void myMusicWasClicked(View v) {
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);

        salutusing = false;
        //store "this" inside this intent below
        //newIntent.


        //   Gson gson = new Gson();
        //  String parent = gson.toJson(this,SalutMain.class);

        //   newIntent.putExtra("parent", parent);

        LogHelper.d(TAG, "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }

//    public void syncWasClicked(View v) {
//
//        sendSongOut();

        //store "this" inside this intent below
        //newIntent.


        //   Gson gson = new Gson();
        //  String parent = gson.toJson(this,SalutMain.class);

        //   newIntent.putExtra("parent", parent);

//    }

//
//    private void setupNetwork() {
//        if (hostingBtn.getText() == "StopService") {
//            if (network.isRunningAsHost) {
//                network.disconnectFromDevice();
//                network.forceDisconnect();
//                network.unregisterClient(false);
//                hostingBtn.setText("Start Service");
//                discoverBtn.setAlpha(1f);
//                discoverBtn.setClickable(true);
//            }
//        }
//        if (!network.isRunningAsHost) {
//            network.startNetworkService(new SalutDeviceCallback() {
//                @Override
//                public void call(SalutDevice salutDevice) {
//                    Toast.makeText(getApplicationContext(), "Device: " + salutDevice.instanceName + " connected.", Toast.LENGTH_SHORT).show();
//                }
//            });
//            hostingBtn.setText("Stop Service");
//            discoverBtn.setAlpha(0.5f);
//            discoverBtn.setClickable(false);
//        }
//    }

//    private void discoverServices() {
//        if (!network.isRunningAsHost && !network.isDiscovering) {
//            network.discoverNetworkServices(new SalutCallback() {
//                @Override
//                public void call() {
//                    Toast.makeText(getApplicationContext(), "Device: " + network.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();
//                    connectDevices(network.foundDevices.get(0));
//                }
//            }, true);
//            discoverBtn.setText("Stop Discovery");
//            hostingBtn.setAlpha(0.5f);
//            hostingBtn.setClickable(false);
//        } else {
//            network.stopServiceDiscovery(true);
//            discoverBtn.setText("Discover Services");
//            hostingBtn.setAlpha(1f);
//            hostingBtn.setClickable(false);
//        }
//    }

//    public void connectDevices(SalutDevice foundDevice) {
//        network.registerWithHost(foundDevice, new SalutCallback() {
//            @Override
//            public void call() {
//                Log.d(TAG, "We're now registered.");
//                Toast.makeText(getApplicationContext(), "Device connected", Toast.LENGTH_SHORT).show();
//            }
//        }, new SalutCallback() {
//            @Override
//            public void call() {
//                Log.d(TAG, "We failed to register.");
//                Toast.makeText(getApplicationContext(), "Device Had an Error connecting", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /*Create a callback where we will actually process the data.*/
//    @Override
//    public void onDataReceived(Object data) {
//
//        Log.d(TAG, "received something!!!!");

       // MediaIdSender mIdSenderHere;

//        mIdSenderHere = LoganSquare.parse((MediaIdSender)data, MediaIdSender.class);

        //Data Is Received
//        try {
//            MediaIdSender mIdSenderHere = LoganSquare.parse((String)data, MediaIdSender.class);
//            Log.d(TAG, mIdSenderHere.mySong);  //See you on the other side!
//
//            //Do other stuff with data.
//        } catch (IOException ex) {
//            Log.e(TAG, "Failed to parse network data.");
//        }
//    }

//    @Override
//    public void onClick(View v) {
//
//        if (!Salut.isWiFiEnabled(getApplicationContext())) {
//            Toast.makeText(getApplicationContext(), "Please enable WiFi first.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (v.getId() == R.id.btnHost) {
//            setupNetwork();
//
//
//            SharedPreferences sharedPrefs = getSharedPreferences(
//                    "APPLICATION_PREFERENCES", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            is_host = "Host";
//            editor.putString("IsHost", is_host);
//            editor.apply();
//            String testHost = sharedPrefs.getString("IsHost", null);
//            Log.d(TAG, "placed HOST Bro " + testHost);
//
//        } else if (v.getId() == R.id.btnjoin) {
//            if (network.isRunningAsHost) {
//                network.disconnectFromDevice();
//                network.forceDisconnect();
//                network.unregisterClient(false);
//            }
//            discoverServices();
//
//            SharedPreferences sharedPrefs = getSharedPreferences(
//                    "APPLICATION_PREFERENCES", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            is_host = "Friend";
//            editor.putString("IsHost", is_host);
//            editor.apply();
//            String testHost = sharedPrefs.getString("IsHost", null);
//            Log.d(TAG, "placed FRIEND Bro" + testHost);
//        }
//    }

//    public void sendSongOut() {
//        //Log.d(TAG, "Song recieve: " + song);
//
//        SharedPreferences sharedPref = getSharedPreferences("APPLICATION_PREFERENCES", Context.MODE_PRIVATE);
//
//        String song = sharedPref.getString("songSelected", null);
//
//
//        MediaIdSender mIDSender = new MediaIdSender();
//        mIDSender.mySong = song;
//
//        Log.d(TAG, "SalutMain got this! : " + mIDSender.mySong);
//
//
//        network.sendToAllDevices(mIDSender, new SalutCallback() {
//            public void call() {
//                Log.e(TAG, "Oh no! The song did not send :(");
//            }
//        });
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (network.isRunningAsHost) {
//            network.disconnectFromDevice();
//            network.forceDisconnect();
//            network.unregisterClient(false);
//        }
//        if (network.isDiscovering) {
//            network.stopServiceDiscovery(true);
//        } else
//            network.stopNetworkService(false);
//    }
}