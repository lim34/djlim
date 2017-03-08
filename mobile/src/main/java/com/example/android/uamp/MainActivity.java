package com.example.android.uamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
    private PhoneSync connectP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.d(TAG, " Was started");
        setContentView(R.layout.activity_main);
        randomPin();
    }

    public void myMusicWasClicked(View v) {
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);
        LogHelper.d(TAG, "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }

    public void randomPin() {
        int  randPin = rand.nextInt(8888) + 1111;
        TextView pin = (TextView) this.findViewById(R.id.txtPin);
        pin.setText("Host Pin\n [ " + randPin + " ]");
    }
}
