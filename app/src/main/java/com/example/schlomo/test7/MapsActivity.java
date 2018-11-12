package com.example.schlomo.test7;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_GETMESSAGE = 1041;
    public boolean myLocation = true;
    double location[] = new double[2];
    String gameUrl = "https://teams-yedidim.firebaseio.com/start";
    LatLng lng;
    Button buttonmenu;
    Firebase urlGame = new Firebase(gameUrl);
    GoogleMap googleMap;
    LatLng startLocation;
    boolean teamHead;
    String teamUrl;
    int numOfMission;
    private Location location2;
    private int numOfTeam;
    private String missionUrl;
    private TextView timer;
    private String teamTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        timer = findViewById(R.id.timer);
        timer(30000);
        numOfTeam = getIntent().getIntExtra("numOfTeam", 0);
        numOfMission = getIntent().getIntExtra("numOfMission", 0);
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        teamUrl = getIntent().getStringExtra("teamUrl");
        missionUrl = getIntent().getStringExtra("missionUrl");
        location = getIntent().getDoubleArrayExtra("startLocation");
        teamTitle = getIntent().getStringExtra("teamTitle");


        buttonmenu = findViewById(R.id.menu);
        buttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, Pop.class);
                i.putExtra("numOfMission", numOfMission);
                i.putExtra("missionUrl", missionUrl);
                i.putExtra("numOfTeam", numOfTeam);
                i.putExtra("teamHead", teamHead);
                i.putExtra("teamUrl", teamUrl);
                i.putExtra("teamTitle", teamTitle);
                startActivityForResult(i, REQUEST_CODE_GETMESSAGE);

            }

        });


        configure_button();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else
            googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                location2 = arg0;
                lng = new LatLng(arg0.getLatitude(), arg0.getLongitude());

            }
        });


        LatLng jce = new LatLng(31.770290, 35.193452);
//        LatLng jce = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
        //updateLocationUI();

        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getMyLocation();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jce, 13));

        System.out.println("lat: " + location[0]);
        System.out.println("lat: " + location[1]);
        LatLng loc = new LatLng(location[0], location[1]);

        System.out.println("finish");

        ////////////////////zoom set //////////////////
        googleMap.setMinZoomPreference(13.0f);
        googleMap.setMaxZoomPreference(20.0f);
        /////////
        googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(31.770290, 35.193442),
                        new LatLng(31.770290, 35.193452),
                        new LatLng(31.770290, 35.193412),
                        new LatLng(31.770290, 35.193492),
                        new LatLng(31.770290, 35.193652),
                        new LatLng(31.770290, 35.193752)));


        // Add a marker in Mission and move the camera
        //   LatLng jce = new LatLng(31.770290, 35.193452);
        LatLng bigSyna = new LatLng(31.776185, 35.216924);

        addMarktoGoogle(1, bigSyna);
        googleMap.addCircle(new CircleOptions().fillColor(50).center(jce));

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.stylemap));


    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }

        }


    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        System.out.println("Radius Value ," + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }

    public void getLocation(Firebase url, String child, final FirebaseCallback firebaseCallback) {


        url.child(child).addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                {
                    double lat = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                    double lng = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());

                    startLocation = new LatLng(lat, lng);

                    firebaseCallback.onCallback(startLocation);


                }


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void addMarktoGoogle(int j, LatLng mission) {
        MarkerOptions m;
        if (j > 0)
            m = new MarkerOptions().position(mission).title("משימה " + (j));
        else
            m = new MarkerOptions().position(mission).title("משימה אחרונה ");
        m.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
        googleMap.addMarker(m);


    }

    public void mark() {
        for (int j = 1; j <= numOfMission; j++) {
            //////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////
            //  ArrayList<String> items=getvalueJason("countries.json");/////////////////////////
            //                                                         ////////////////////////
            //////////////////////////////////////////////////////////////////////////////////
        }

    }

    private ArrayList<String> getvalueJson(String fileName) {
        JSONArray jsonArray = null;
        ArrayList<String> cList = new ArrayList<String>();
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray = new JSONArray(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    cList.add(jsonArray.getJSONObject(i).getString("cname"));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return cList;
    }

    public void lastMission() {

        try {
            getLocation(urlGame, "finish", new FirebaseCallback() {
                @Override
                public void onCallback(LatLng code2) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(code2));

                    addMarktoGoogle(-1, code2);
                    System.out.println("i cam here");

                }
            });


        } catch (Exception ignored) {
            Toast.makeText(MapsActivity.this, "connect your GPS please and try again ", Toast.LENGTH_SHORT).show();


        }
    }

    public void timer(long time) {
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {

                long sec = millisUntilFinished / 1000;
                long min = sec / (60);
                long hour = min / 60;
                sec %= 60;
                min %= 60;
                if (hour > 0)
                    timer.setText(hour + ":" + min + ":" + sec);
                else if (min > 0)
                    timer.setText(min + ":" + sec);
                else {
                    timer.setText(sec + " sec");
                }
            }

            public void onFinish() {
                timer.setText("done!");
                googleMap.clear();
                lastMission();
            }
        }.start();
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GETMESSAGE)
            if (Activity.RESULT_OK == resultCode) {

                if (data.getBooleanExtra("lastlocation", false))
                    try {

                        lastMission();

                    } catch (Exception e) {
                        System.out.println("the last mission fail");
                    }
                else if (data.getBooleanExtra("mylocation", false)) {
                    myLocation();
                }
            } else
                System.out.println("return Activity");
    }


    ///////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////get back data form pop////////////////////////////

    public void myLocation() {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
    }

    private interface FirebaseCallback {
        void onCallback(LatLng code2);
    }
}