/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.example.android.uamp.SpotifyHelp;
import com.example.android.uamp.utils.LogHelper;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Utility class to get a list of MusicTrack's based on a server-side JSON
 * configuration.
 */
public class RemoteJSONSource extends Activity implements MusicProviderSource {

    private static final String TAG = LogHelper.makeLogTag(RemoteJSONSource.class);

    protected static final String CATALOG_URL =
//        "https://api.spotify.com/v1/albums/2r2r78NE05YjyHyVbVgqFn";
        "http://storage.googleapis.com/automotive-media/music.json";

//    protected static final String CATALOG_URL =
//            "https://api.spotify.com/v1/users/1221609934/playlists/4maetuHtO6QLzqalO3CevC";


    private static final String JSON_MUSIC = "items";
    private static final String JSON_TITLE = "name";
    private static final String JSON_ALBUM = "album";
    private static final String JSON_ARTIST = "artists[0].name";
//    private static final String JSON_GENRE = "genre";
    private static final String JSON_SOURCE = "uri";
    private static final String JSON_IMAGE = "album[0].href";
    private static final String JSON_TRACK_NUMBER = "track_number";
    private static final String JSON_TOTAL_TRACK_COUNT = "track_number";
    private static final String JSON_DURATION = "duration_ms";

//    private static final String JSON_MUSIC = "music";
//    private static final String JSON_TITLE = "title";
//    private static final String JSON_ALBUM = "album";
//    private static final String JSON_ARTIST = "artist";
//    private static final String JSON_GENRE = "genre";
//    private static final String JSON_SOURCE = "source";
//    private static final String JSON_IMAGE = "image";
//    private static final String JSON_TRACK_NUMBER = "trackNumber";
//    private static final String JSON_TOTAL_TRACK_COUNT = "totalTrackCount";
//    private static final String JSON_DURATION = "duration";

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        try {
            //int slashPos = CATALOG_URL.lastIndexOf('/');
            //String path = CATALOG_URL.substring(0, slashPos + 1);

           // JSONObject jsonObj = fetchJSONFromUrl(CATALOG_URL);
      //      SpotifyHelp SpotifyHelpJson = new SpotifyHelp();



         //   Bundle bundle = getIntent().getExtras();
         //   String hereJson = bundle.getString("mattJson");

            Intent myIntent = getIntent();
            String hereJson = myIntent.getExtras().getString("mattJson");

            //String hereJson = "dafdaf";

            Log.d("HERE JSON: ", hereJson);
            JSONObject jsonObj = new JSONObject(hereJson);

            ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
            if (jsonObj != null) {
                JSONArray jsonTracks = jsonObj.getJSONArray(JSON_MUSIC);

                if (jsonTracks != null) {
                    for (int j = 0; j < jsonTracks.length(); j++) {
                        tracks.add(buildFromJSON(jsonTracks.getJSONObject(j)));
                    }
                }
            }
            return tracks.iterator();
        } catch (JSONException e) {
            LogHelper.e(TAG, e, "Could not retrieve music list");
            throw new RuntimeException("Could not retrieve music list", e);
        }
    }

    private MediaMetadataCompat buildFromJSON(JSONObject json) throws JSONException {
        String title = json.getString(JSON_TITLE);
        String album = json.getString(JSON_ALBUM);
        String artist = json.getString(JSON_ARTIST);
        String genre = "Hippity-Hoppity";
        String source = json.getString(JSON_SOURCE);
        String iconUrl = json.getString(JSON_IMAGE);
        int trackNumber = json.getInt(JSON_TRACK_NUMBER);
        int totalTrackCount = json.getInt(JSON_TOTAL_TRACK_COUNT);
        int duration = json.getInt(JSON_DURATION); // ms

        LogHelper.d(TAG, "Found music track: ", json);

        // Media is stored relative to JSON file

//        if (!source.startsWith("http")) {
//            source = basePath + source;
//        }
//        if (!iconUrl.startsWith("http")) {
//            iconUrl = basePath + iconUrl;
//        }

        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.
        String id = String.valueOf(source.hashCode());

        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)
                .build();
    }

    /**
     * Download a JSON file from a server, parse the content and return the JSON
     * object.
     *
     * @return result JSONObject containing the parsed representation.
     */
    private JSONObject fetchJSONFromUrl(String urlString) throws JSONException {
        BufferedReader reader = null;
        try {

//
//            URL url = new URL(Bingo);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            Log.d("URL IS: ", urlConnection.toString());
//            try {
//                InputStream inputStreamObject = SpotifyHelp.class.getResourceAsStream(urlConnection.getContent());
//                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
//                StringBuilder responseStrBuilder = new StringBuilder();
//
//                String inputStr;
//                while ((inputStr = streamReader.readLine()) != null)
//                    responseStrBuilder.append(inputStr);
//
//                JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
//
//
            URLConnection urlConnection = new URL(urlString).openConnection();
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "iso-8859-1"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            LogHelper.e(TAG, "Failed to parse the json for media list", e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
