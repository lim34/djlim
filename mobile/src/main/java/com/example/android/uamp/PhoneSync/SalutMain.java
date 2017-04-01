package com.example.android.uamp.PhoneSync;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.uamp.R;
import com.example.android.uamp.ui.ActionBarCastActivity;
import com.example.android.uamp.ui.MusicPlayerActivity;
import com.example.android.uamp.utils.LogHelper;

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

/**
 * This class is when we land when the app is launched and we first declare listeners
 * for the different buttons that we have on the interface relating to our "network"
 * We declared network and all of its inter-workings in ActionBarCastActivity in hopes
 * that the object would persist but then we better understood the android activity lifecycle
 * and when we went to check if the device was a host in the MusicPlayerActivity it would
 * tell us that we were not since the object was destroyed between activities.
 *
 * Also when the connection was successful with the other phone we would lose control
 * couldn't severe connection to reset things. Then the user goes to connect again and a connection
 * already exists so the phone crashes and restarts.
 */
public class SalutMain extends ActionBarCastActivity {

    public static final String TAG = "SalutMain";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // disables a onCreate line of code in ActionBarCastActivity
        // from running while this class is running.
        salutusing = true;

        setContentView(R.layout.phonesync_main);


        //Setting Buttons on the Main Activity
        hostingBtn = (Button) findViewById(R.id.btnHost);
        discoverBtn = (Button) findViewById(R.id.btnjoin);
        disconnectBtn = (Button) findViewById(R.id.btnDisconnect);

        hostingBtn.setOnClickListener(this);
        discoverBtn.setOnClickListener(this);
        disconnectBtn.setOnClickListener(this);

        //Creates an instance of the Salut class, with all of the necessary data from before.
        makeNetwork();

        //this is a part of the ActionBarCastActivity and needs to be called.
        initializeToolbar();
    }

    public void myMusicWasClicked(View v) {
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);

        //sets this to false so other sections of code can be run in ActionBarCastActivity
        salutusing = false;

        LogHelper.d(TAG, "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }
}