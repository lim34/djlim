package com.example.android.uamp;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.uamp.ui.BaseActivity;
import com.example.android.uamp.ui.MusicPlayerActivity;
import com.example.android.uamp.utils.LogHelper;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;


import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
//import com.squareup.okhttp.Headers;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Playlist;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matthew on 3/13/17.
 */

//implemented:    SpotifyPlayer.NotificationCallback

public class SpotifyHelp extends Activity {//implements ConnectionStateCallback {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "a0a55d8df4174242bea511482ebd6bf6";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "musync://callback";

    private static final int REQUEST_CODE = 1330;
    private static final String TAG = "SpotifyHelpTag";

    public String mattJSON = "nothingInJson";

    private OkHttpClient mClient = new OkHttpClient();
    private Headers mAuthHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.d(TAG, "Activity onCreate");
        //setContentView(R.layout.spotify_help_layout);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                case CODE:
                    break;
                case TOKEN:
                    //onLoggedIn();
//                    GET	/v1/users/{user_id}/playlists/{playlist_id}	Get a Playlist
//                    GET	/v1/users/{user_id}/playlists/{playlist_id}/tracks	Get a Playlist's Tracks

                    SpotifyApi spotifyApi = new SpotifyApi();
                    spotifyApi.setAccessToken(response.getAccessToken());

                    mAuthHeader = Headers.of("Authorization", "Bearer " + response.getAccessToken());

                    Intent newIntent;
                    newIntent = new Intent(this, MusicPlayerActivity.class);

                    try {
                            mattGetPlaylist(newIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //String extraMattJSON = "this is my JSON";

                    //newIntent.putExtra("mattJSON", extraMattJSON);
                    startActivity(newIntent);
/*
                    SpotifyApi api = new SpotifyApi();

// Most (but not all) of the Spotify Web API endpoints require authorisation.
// If you know you'll only use the ones that don't require authorisation you can skip this step
                    api.setAccessToken(response.getAccessToken());

                    SpotifyService spotify = api.getService();

                    spotify.getPlaylist("1221609934", "4maetuHtO6QLzqalO3CevC", new Callback<Playlist>() {
                        @Override
                        public void success(Playlist playlist, Response response) {
                            Log.d("Playlist success", playlist.name);
                            Log.d("Playlist stuff", playlist.name);

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("Playlist failure", error.toString());
                        }
                    });

*/

//                    spotify.getAlbum("2r2r78NE05YjyHyVbVgqFn", new Callback<Album>() {
//                        @Override
//                        public void success(Album album, Response response) {
//                            Log.d("Album success", album.name);
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            Log.d("Album failure", error.toString());
//                        }
//                    });




//
//                    String Bingo = "https://api.spotify.com/v1/users/1221609934/playlists/4maetuHtO6QLzqalO3CevC";
//
//                    BufferedReader reader = null;
//                    try {
//                        URLConnection urlConnection = new URL(Bingo).openConnection();
//                        reader = new BufferedReader(new InputStreamReader(
//                                urlConnection.getInputStream(), "iso-8859-1"));
//                        StringBuilder sb = new StringBuilder();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            sb.append(line);
//                        }
//                        String Django = sb.toString();
//                        Log.e(TAG, "this is Django: " + Django);
//                    } catch (Exception e) {
//                        LogHelper.e(TAG, "Failed to parse the json for media list", e);
//                        //nothing
//                    } finally {
//                        if (reader != null) {
//                            try {
//                                reader.close();
//                            } catch (IOException e) {
//                                // ignore
//                            }
//                        }
//                    }
                    break;
                case ERROR:
                    break;
                case EMPTY:
                    break;
                case UNKNOWN:
                    break;
                default:
            }
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
//            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
//                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
//                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
//                    @Override
//                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
////                        mPlayer = spotifyPlayer;
////                        mPlayer.addConnectionStateCallback(SpotifyHelp.this);
////                        mPlayer.addNotificationCallback(SpotifyHelp.this);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.e("SpotifyHelp", "Could not initialize player: " + throwable.getMessage());
//                    }
//                });
//            }
//        }
//    }


//    @Override
//    public void onDestroy() {
//        Spotify.destroyPlayer(this);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onPlaybackEvent(PlayerEvent playerEvent) {
//        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
//        switch (playerEvent) {
//            // Handle event type as necessary
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onPlaybackError(Error error) {
//        Log.d("MainActivity", "Playback error received: " + error.name());
//        switch (error) {
//            // Handle error type as necessary
//            default:
//                break;
//        }
    //}

  //  @Override
 //   public void onLoggedIn() {


//        Log.d("spotifyhelp", "User logged in");
////        mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
//
//        String Bingo = "https://api.spotify.com/v1/users/1221609934/playlists/4maetuHtO6QLzqalO3CevC";
////        URL url = null;
//        //       HttpsURLConnection urlConnection = null;
//
//        try {
//            String json = readUrl("https://api.spotify.com/v1/users/1221609934/playlists/4maetuHtO6QLzqalO3CevC");
//        } catch(Exception e) {
//            Log.e("ERROR1", e.getMessage(), e);
//        }


//        try {
//            try {
//                url = new URL(Bingo);
//            } catch (Exception e) {
//                Log.e("ERROR", e.getMessage(), e);
//            }
//
//            try {
//                urlConnection = (HttpsURLConnection) url.openConnection();
//            } catch (Exception e) {
//                Log.e("ERROR", e.getMessage(), e);
//            }
//
//            try {
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                Log.d("MADE IT ", urlConnection.toString());
//            } catch (Exception e) {
//                Log.e("ERROR", e.getMessage(), e);
//            }
//            finally {
//                urlConnection.disconnect();
//            }
//        }
//        catch(Exception e) {
//            Log.e("ERROR", e.getMessage(), e);
//        }
//    }


//        Log.d("spotifyhelp", "User logged in");
////        mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
//        String Bingo = "https://api.spotify.com/v1/users/1221609934/playlists/4maetuHtO6QLzqalO3CevC/tracks";
//
//        BufferedReader reader = null;
//        try {
//            LogHelper.e(TAG, "ONE");
//
//            URLConnection urlConnection = new URL(Bingo).openConnection();
//            LogHelper.e(TAG, "TWO/2");
//
//            reader = new BufferedReader(new InputStreamReader(
//                    urlConnection.getInputStream(), "iso-8859-1"));
//            LogHelper.e(TAG, "TWO");
//            StringBuilder sb = new StringBuilder();
//            String line;
//            LogHelper.e(TAG, "Three");
//
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//            LogHelper.e(TAG, "FOUR");
//
//            String Django = sb.toString();
//
//            LogHelper.e(TAG, "Five");
//
//            Log.e(TAG, "this is Django: " + Django);
//        } catch (Exception e) {
//            LogHelper.e(TAG, "Failed to parse the json for media list", e);
//            //nothing
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    // ignore
//                }
//            }
//        }
//    }

//    @Override
//    public void onLoggedOut() {
//        Log.d("MainActivity", "User logged out");
//    }
//
//    @Override
//    public void onLoginFailed(Error error) {
//        Log.d("MainActivity", "Login failed");
//    }
//
//    @Override
//    public void onTemporaryError() {
//        Log.d("MainActivity", "Temporary error occurred");
//    }
//
//    @Override
//    public void onConnectionMessage(String message) {
//        Log.d("MainActivity", "Received connection message: " + message);
//    }

//    private static String readUrl(String urlString) throws Exception {
//        BufferedReader reader = null;
//        try {
//            Log.e("ERROR2", "some'n");
//
//            URL url = new URL(urlString);
//            Log.e("ERROR3", "some'n");
//
//            reader = new BufferedReader(new InputStreamReader(url.openStream()));
//            Log.e("ERROR4", "some'n");
//
//            StringBuffer buffer = new StringBuffer();
//            Log.e("ERROR5", "some'n");
//
//            int read;
//            char[] chars = new char[1024];
//            while ((read = reader.read(chars)) != -1)
//                buffer.append(chars, 0, read);
//
//            return buffer.toString();
//        } finally {
//            if (reader != null)
//                reader.close();
//        }
//    }

    public void mattGetPlaylist(final Intent aNewIntent) throws Exception {
        Request myRequest = new Request.Builder()
                .get()
                .headers(mAuthHeader)
                .url("https://api.spotify.com/v1/users/1221609934/playlists/4maetuHtO6QLzqalO3CevC")
                .build();

        mClient.newCall(myRequest).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response mResponse) throws IOException {
                if (mResponse.isSuccessful()) {
                    mattJSON = (mResponse.body().string());
                    Log.d("Playlist success: ", mattJSON);
                } else {
                    Log.d("Playlist not success: ", "sorry");
                }

                Log.d("waited: ", "ok");

                LogHelper.d(TAG, "changing to MusicPlayerActivity");
                startActivity(aNewIntent);
            }
        });
    }
}
