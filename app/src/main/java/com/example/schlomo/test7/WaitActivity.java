package com.example.schlomo.test7;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import static android.text.TextUtils.concat;


public class WaitActivity extends AppCompatActivity {
    TextView code;
    boolean teamHead;
    String teamUrl;
    String finalRes;
    String gameUrl = "https://teams-yedidim.firebaseio.com/start";
    String cityUrl = "https://teams-yedidim.firebaseio.com/start/city";
    String missionUrl;
    Firebase urlTeam;
    Firebase urlGame;
    double location[] = new double[2];
    Intent intentMaps;
    boolean go = false;

    int numOfquestion;
    int numOfteam;
    private LatLng startLocation;
    private String teamTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        intentMaps = new Intent(this, MapsActivity.class);
        code = findViewById(R.id.textView6);
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        teamUrl = getIntent().getStringExtra("teamUrl");
        teamTitle = getIntent().getStringExtra("teamTitle");

        getLocation(gameUrl, "start", new FirebaseCallback2() {
            @Override
            public void onCallback(LatLng code2) {

            }
        });

        configure_button();
        urlTeam = new Firebase(teamUrl);
        urlTeam.child("teamCode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int player = Integer.parseInt(dataSnapshot.getValue().toString());


                player %= 10000;
                code.setText(String.valueOf(player));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        getValueFirebase(gameUrl, "numOfTeam", new FirebaseCallback() {
            @Override
            public void onCallback(String code2) {
                numOfteam = Integer.parseInt(code2);
            }
        });
        getValueFirebase(cityUrl, new FirebaseCallback() {
            @Override
            public void onCallback(String code2) {
                missionUrl = code2;
                getValueFirebase(missionUrl, "num", new FirebaseCallback() {
                    @Override
                    public void onCallback(String code2) {
                        numOfquestion = Integer.parseInt(code2);
                        startGame();
                    }
                });
            }
        });


    }

    public void startGame() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!go) {
                    handler.postDelayed(this, 5 * 1000);
                    waitForGame(new FirebaseCallback() {
                        @Override
                        public void onCallback(String code2) {

                        }
                    });

                }

            }
        }, 1000 * 2);
    }


    public void waitForGame(final FirebaseCallback firebaseCallback) {
        urlGame = new Firebase("https://teams-yedidim.firebaseio.com/start/startGame");
        urlGame.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                {
//


                    String s = dataSnapshot.getValue().toString();
                    firebaseCallback.onCallback(s);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(s);
                    if (s.equals("true")) {

                        go = true;
                        System.out.println("go : " + go);
                        intentMaps.putExtra("numOfMission", numOfquestion);
                        intentMaps.putExtra("missionUrl", missionUrl);
                        intentMaps.putExtra("numOfTeam", numOfteam);
                        intentMaps.putExtra("teamHead", teamHead);
                        intentMaps.putExtra("teamUrl", teamUrl);
                        intentMaps.putExtra("startLocation", location);
                        intentMaps.putExtra("teamTitle", teamTitle);
                        startActivity(intentMaps);
                        finish();
                    }

                }


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }

        }

    }

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child(myChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                firebaseCallback.onCallback(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void getValueFirebase(String url, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                firebaseCallback.onCallback(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void getLocation(String linkurl, String child, final FirebaseCallback2 firebaseCallback) {
        Firebase url = new Firebase(linkurl);


        url.child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                {
                    location[0] = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                    location[1] = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());

                    startLocation = new LatLng(location[0], location[1]);

                    firebaseCallback.onCallback(startLocation);


                }


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private interface FirebaseCallback {
        void onCallback(String code2);
    }

    private interface FirebaseCallback2 {
        void onCallback(LatLng code2);
    }





}

