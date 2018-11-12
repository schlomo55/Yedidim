package com.example.schlomo.test7;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class InscriptionActivity extends AppCompatActivity {
    EditText telNum;
    ImageButton letGo;
    EditText nameHeadTeam;
    String teamTitle;
    EditText teamName;
    Firebase url;
    String teamUrl;
    boolean teamHead;
    boolean bnameHeadTeam = false, btelNum = false, bteamName = false;


    String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        telNum = findViewById(R.id.tel);
        letGo = findViewById(R.id.letGo);
        nameHeadTeam = findViewById(R.id.nameTeamHead);
        teamName = findViewById(R.id.team);

        teamUrl = getIntent().getStringExtra("teamUrl");
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        teamTitle = getIntent().getStringExtra("teamTitle");
        url = new Firebase(teamUrl);



        teamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bteamName = true;
                teamName.getText().clear();
            }
        });

        nameHeadTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnameHeadTeam = true;
                nameHeadTeam.getText().clear();
            }
        });


        final Intent intentWait = new Intent(this, WaitActivity.class);

        telNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btelNum = true;
                telNum.getText().clear();
            }
        });

        letGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(!teamName.getText().equals("") && bteamName && !nameHeadTeam.getText().equals("") && bnameHeadTeam && !telNum.getText().toString().equals("") && btelNum)) {
                    Toast.makeText(InscriptionActivity.this, "need to fill all information ", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    setParameter(telNum, "teamNum");
                    setParameter(teamName, "teamName");
                    setParameter(nameHeadTeam, "teamHead");
                    setParameter(getMobileIP(),"ip");
                } catch (Exception e) {
                    System.out.println(teamUrl);
                }

                intentWait.putExtra("teamHead", teamHead);
                intentWait.putExtra("teamUrl", teamUrl);
                intentWait.putExtra("teamTitle", teamTitle);
                startActivity(intentWait);
                finish();

            }
        });


    }
    public static String getMobileIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {

                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("Exception in Get IP Address: " + ex.toString());
        }
        return null;
    }
    public void setParameter(final TextView textView, String mychild) {


        url.child(mychild).setValue(textView.getText().toString());

    }
    public void setParameter(final String ip, String mychild) {


        url.child(mychild).setValue(ip);

    }
}

