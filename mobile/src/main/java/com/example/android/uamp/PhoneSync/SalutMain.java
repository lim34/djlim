package com.example.android.uamp.PhoneSync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.android.uamp.R;
import com.example.android.uamp.ui.MusicPlayerActivity;
import com.example.android.uamp.utils.LogHelper;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import static android.R.attr.fragment;

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

//View.OnClickListener
public class SalutMain extends Activity implements SalutDataCallback {

    /*
        This simple activity demonstrates how to use the Salut library from a host and client perspective.
     */

    public static final String TAG = "SalutMain";
    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public Salut network;
    private WiFiDirectServicesList servicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonesync_main);

        /*Create a data receiver object that will bind the callback
        with some instantiated object from our app. */
        dataReceiver = new SalutDataReceiver(this, this);

        /*Populate the details for our awesome service. */
        serviceData = new SalutServiceData("MuSync", 60606,
                "Host");

        /*Create an instance of the Salut class, with all of the necessary data from before.
        * We'll also provide a callback just in case a device doesn't support WiFi Direct, which
        * Salut will tell us about before we start trying to use methods.*/
        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                // wiFiFailureDiag.show();
                // OR
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });

        if(network.isRunningAsHost)
        {
            network.disconnectFromDevice();
            network.forceDisconnect();
            network.stopNetworkService(true);
            network.unregisterClient(false);
            setupNetwork();
        }
        else {
            setupNetwork();
        }
    }

    public void myMusicWasClicked(View v) {
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);
        LogHelper.d(TAG, "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }

    public void joinWasClicked(View v) {
        setContentView(R.layout.join);
        if(network.isRunningAsHost) {
            network.disconnectFromDevice();
            network.forceDisconnect();
            network.stopNetworkService(true);
            network.unregisterClient(false);
        }
        servicesList = new WiFiDirectServicesList();
        getFragmentManager().beginTransaction()
                .add(R.id.container_root, servicesList, "services").commit();
        discoverServices();
    }

    private void setupNetwork()
    {
        if(!network.isRunningAsHost)
        {
            network.startNetworkService(new SalutDeviceCallback() {
                @Override
                public void call(SalutDevice salutDevice) {
                    Toast.makeText(getApplicationContext(), "Device: " + salutDevice.instanceName + " connected.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            network.disconnectFromDevice();
            network.forceDisconnect();
            network.stopNetworkService(true);
            network.unregisterClient(false);
            setupNetwork();
        }
    }

    private void discoverServices()
    {
        if(!network.isRunningAsHost && !network.isDiscovering)
        {
            network.discoverNetworkServices(new SalutCallback() {
                @Override
                public void call() {
                    WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager()
                            .findFragmentByTag("services");
                    if (fragment != null) {
                        WiFiDirectServicesList.WiFiDevicesAdapter adapter = ((WiFiDirectServicesList.WiFiDevicesAdapter) fragment
                                .getListAdapter());
                        adapter.add(network.foundDevices.get(0));
                    }
//                    Toast.makeText(getApplicationContext(), "Device: " + network.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Device:" + network.foundDevices.get(0).instanceName + " found.");
                    connectDevices(network.foundDevices.get(0));
                }
            }, true);
//            connectDevices();

        }
        else
        {
            Log.d(TAG, "discovery stopped/retrying");
            network.stopServiceDiscovery(true);
            discoverServices();
        }
    }

    public void connectDevices(SalutDevice foundDevice) {
        network.registerWithHost(foundDevice, new SalutCallback() {
            @Override
            public void call() {
                Log.d(TAG, "We're now registered.");
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                Log.d(TAG, "We failed to register.");
            }
        });
    }
    /*Create a callback where we will actually process the data.*/
    @Override
    public void onDataReceived(Object o) {
        //Data Is Received
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        network.disconnectFromDevice();
        network.forceDisconnect();
        network.stopNetworkService(true);
        network.unregisterClient(false);
    }
}
