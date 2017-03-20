package com.example.android.uamp.PhoneSync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.android.uamp.R;
import com.example.android.uamp.ui.MusicPlayerActivity;
import com.example.android.uamp.utils.LogHelper;

/**
 * Created by loyd on 3/20/2017.
 */

public class sendJson extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_selected);
    }
    public void setItem(View v){
        Intent newIntent;
        newIntent = new Intent(this, MusicPlayerActivity.class);
        LogHelper.d("SendJson", "changing to MusicPlayerActivity");
        startActivity(newIntent);
    }
}
