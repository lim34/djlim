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


public class SalutMain extends ActionBarCastActivity {

    /*
        This simple activity demonstrates how to use the Salut library from a host and client perspective.
     */
    public static final String TAG = "SalutMain";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // disables a onCreate line of code in ActionBarCastActivity
        // from running while this class is running.
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
    }

    public void myMusicWasClicked(View v) {
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);

        salutusing = false;

        LogHelper.d(TAG, "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }
}