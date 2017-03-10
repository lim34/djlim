package com.example.android.uamp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.uamp.PhoneSync.PhoneSync;
import com.example.android.uamp.ui.MusicPlayerActivity;
import com.example.android.uamp.utils.LogHelper;

import java.util.Random;


/**
 * Created by loyd on 3/3/2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final Random rand = new Random();
    private static final String TAG = "MainActivity";
    IntentFilter mIntentFilter;
    WifiP2pManager.Channel channel;
    WifiP2pManager mManager;
    PhoneSync connectP;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.d(TAG, " Was started");
        setContentView(R.layout.activity_main);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = (WifiP2pManager.Channel) mManager.initialize(this, getMainLooper(), null);
        PhoneSync tempP = new PhoneSync(mManager, channel);
        connectP = tempP;
        connectP.startRegistration();
        randomPin();
    }


    public void myMusicWasClicked(View v) {
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);
        LogHelper.d(TAG, "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }

    public void joinWasClicked(View v) {
        connectP.discoverService();
    }

    public void randomPin() {
        int  randPin = rand.nextInt(8888) + 1111;
        TextView pin = (TextView) this.findViewById(R.id.txtPin);
        pin.setText("Host Pin\n [ " + randPin + " ]");
    }
}
