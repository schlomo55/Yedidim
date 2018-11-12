package com.example.schlomo.test7;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;


public class FirstActivity extends AppCompatActivity {

    String teamUrl;
    String teamTitle;
    ImageButton enter;
    EditText buttonCode;
    boolean checkExistTeam;
    int code;
    boolean teamHead;
    Firebase url;
    LinkedList<String> data;
    String str = "";
    boolean english = false;


    //function//

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this); //connected to firebase
        checkExistTeam = false;
        url = new Firebase("https://teams-yedidim.firebaseio.com/");
        setContentView(R.layout.activity_first);
        ArrayList<String> cList = new ArrayList<>();
        cList.add("  עברית  ");
        cList.add("  English  ");
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, R.id.txt, cList);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                english = false;
                switch (position)

                {

                    case 1:

                        english = true;

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enter = findViewById(R.id.imageButton3);
        buttonCode = findViewById(R.id.editText);
        final Intent intentInscription = new Intent(this, InscriptionActivity.class);
        final Intent intentWait = new Intent(this, WaitActivity.class);

        buttonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCode.getText().clear();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (english) {
                    Toast.makeText(FirstActivity.this, "בקרוב ...", Toast.LENGTH_SHORT).show();
                    return;
                }
                int numCode;
                try {
                    numCode = Integer.parseInt(buttonCode.getText().toString());
                    checkIfTeamExist(numCode, new FirebaseCallback() {
                        @Override
                        public void onCallback(String code2) {

                            System.out.println(checkExistTeam);
                            System.out.println(teamHead);
                            if (checkExistTeam && teamHead) {
                                System.out.println("is a team head ");
                                getValueFirebase(teamUrl, "ip", new FirebaseCallback() {
                                    @Override
                                    public void onCallback(String code2) {
                                        if (code2.equals("nul")) {
                                            intentInscription.putExtra("teamUrl", teamUrl);
                                            intentInscription.putExtra("teamHead", teamHead);
                                            intentInscription.putExtra("teamTitle", teamTitle);

                                            startActivity(intentInscription);
                                            finish();
                                        } else if (getMobileIP().equals(code2)) {

                                            intentWait.putExtra("teamHead", teamHead);
                                            intentWait.putExtra("teamUrl", teamUrl);
                                            intentWait.putExtra("teamTitle", teamTitle);
                                            startActivity(intentWait);
                                            finish();
                                        } else {
                                            Toast.makeText(FirstActivity.this, "You are not the team head", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                            } else if (checkExistTeam && !teamHead) {
                                System.out.println("is a player  ");

                                intentWait.putExtra("teamHead", teamHead);
                                intentWait.putExtra("teamUrl", teamUrl);
                                intentWait.putExtra("teamTitle", teamTitle);
                                startActivity(intentWait);
                                finish();
                            }
                        }
                    });
                    code = numCode;
                } catch (Exception e) {
                    Toast.makeText(FirstActivity.this,"נא להכניס קוד קבוצה !!",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void checkIfTeamExist(final int teamCode, final FirebaseCallback firebaseCallback) throws FirebaseException {
        if (teamCode < 1000 || teamCode > 99999)////select between 4 digit and 5 digit
        {
            checkExistTeam = false;
            teamHead = false;
            return;
        }

        url.addListenerForSingleValueEvent(new ValueEventListener() { //the lisener code
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                checkExistTeam = false;//init before check in server
                teamHead = false;
                String s = "";
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    teamUrl = dttSnapshot2.getRef().toString();

                    int myKey;
                    try {

                        teamTitle = dttSnapshot2.getKey();
                        s = dttSnapshot2.child("teamCode").getValue().toString();
                        myKey = Integer.parseInt(s);
                        System.out.println("teamUrl " + teamUrl);
                        System.out.println("myKey " + myKey);
                        if (teamCode > 9999) {
                            if (myKey == teamCode) {
                                checkExistTeam = true;
                                teamHead = true;
                                break;
                            }
                        } else {
                            myKey %= 10000;

                            if (myKey == teamCode) {
                                checkExistTeam = true;
                                break;
                            }

                            checkExistTeam = false;
                        }


                    } catch (Exception e)//in case the value in server is not int
                    {
                        checkExistTeam = false;
                    }
                }


                firebaseCallback.onCallback(s);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                throw new FirebaseException("FirebaseException");
            }
        });    //end of lisener code


    }

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child(myChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                str = dataSnapshot.getValue().toString();
                firebaseCallback.onCallback(str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    private interface FirebaseCallback {
        void onCallback(String code2);
    }
}