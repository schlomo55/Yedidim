package com.example.schlomo.test7;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MissionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_Question = 2001;

    ImageButton exit;
    String str;
    boolean teamHead;
    int i = 0;
    String teamUrl;
    View.OnClickListener onclicklistener;

    String missionUrl = "https://missions-yedidim.firebaseio.com/";
    LinearLayout parentLinearLayout;
    int numOfQuestion;
    int id;
    String f;
    private String teamTitle;

    @SuppressLint({"ResourceType", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        numOfQuestion = getIntent().getIntExtra("numOfMission", 0);
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        teamUrl = getIntent().getStringExtra("teamUrl");
        missionUrl = getIntent().getStringExtra("missionUrl");
        teamTitle = getIntent().getStringExtra("teamTitle");
        exit = findViewById(R.id.exit);
        //  ArrayList<String> items=getCountries("countries.json");


        getValueFirebase(missionUrl, "num", new FirebaseCallback() {
            @Override
            public void onCallback(String value) {
                numOfQuestion = Integer.valueOf(value);
                System.out.println("num of question : " + value);

                num_mission(numOfQuestion);

            }
        });


        onclicklistener = new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                for (int j = 1; j <= numOfQuestion; j++) {

                    if (v.getId() == (100 + j)) {

                        id = v.getId();

                        final int finalJ = j;

                        getValueFirebase(missionUrl, "mission", j, "typeOfQuestion", new FirebaseCallback() {
                            @Override
                            public void onCallback(String value) {
                                String fJ = String.valueOf(finalJ);
                                if (value.equals("1")) {
                                    Intent intentQ1 = new Intent(MissionActivity.this, Question1Activity.class);
                                    intentQ1.putExtra("question", fJ);
                                    intentQ1.putExtra("missionUrl", missionUrl);
                                    intentQ1.putExtra("teamHead", teamHead);
                                    intentQ1.putExtra("teamTitle", teamTitle);
                                    startActivityForResult(intentQ1, REQUEST_CODE_Question);

                                } else if (value.equals("2")) {
                                    Intent intentQ2 = new Intent(MissionActivity.this, Question2Activity.class);
                                    intentQ2.putExtra("question", fJ);
                                    intentQ2.putExtra("missionUrl", missionUrl);
                                    intentQ2.putExtra("teamHead", teamHead);
                                    intentQ2.putExtra("teamTitle", teamTitle);
                                    startActivityForResult(intentQ2, REQUEST_CODE_Question);

                                } else if (value.equals("3")) {
                                    Intent intentQ3 = new Intent(MissionActivity.this, Question3Activity.class);
                                    intentQ3.putExtra("question", fJ);
                                    intentQ3.putExtra("missionUrl", missionUrl);
                                    intentQ3.putExtra("teamHead", teamHead);
                                    intentQ3.putExtra("teamTitle", teamTitle);
                                    startActivityForResult(intentQ3, REQUEST_CODE_Question);

                                } else if (value.equals("4")) {
                                    Intent intentQ4 = new Intent(MissionActivity.this, Question4Activity.class);
                                    intentQ4.putExtra("question", fJ);
                                    intentQ4.putExtra("missionUrl", missionUrl);
                                    intentQ4.putExtra("teamHead", teamHead);
                                    intentQ4.putExtra("teamTitle", teamTitle);
                                    startActivityForResult(intentQ4, REQUEST_CODE_Question);

                                }
                            }
                        });


                    }
                }
            }

        };


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void num_mission(int num) {

        for (int j = 0; j < num; j++) {
            onAddField();
            TextView textView = findViewById(201 + j);
            textView.setText("" + (j + 1));

        }
    }

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child(myChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                str = dataSnapshot.getValue().toString();
                firebaseCallback.onCallback(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void getValueFirebase(String url, String myChild, int mission, String myChild2, final FirebaseCallback firebaseCallback) {
        String l = String.valueOf(mission);

        Firebase link = new Firebase(url);
        link.child(myChild).child(l).child(myChild2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                str = dataSnapshot.getValue().toString();


                firebaseCallback.onCallback(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    @SuppressLint("ResourceType")
    public void onAddField() {

        i++;

        if (i > 9)
            return;
        if (i < 4) {
            parentLinearLayout = findViewById(R.id.mission_layout);

        } else if (i < 7) {
            parentLinearLayout = findViewById(R.id.mission_layout2);

        } else {
            parentLinearLayout = findViewById(R.id.mission_layout3);

        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") final View rowView = inflater.inflate(R.layout.mission, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        TextView textView = findViewById(rowView.findViewById(R.id.mission_num).getId());
        textView.setId(200 + i);
        final ImageButton ImageButton = findViewById(rowView.findViewById(R.id.missionButton).getId());
        ImageButton.setId(100 + i);
        getValueFirebase(missionUrl, "mission", i, "finish", new FirebaseCallback() {
            @Override
            public void onCallback(String value) {
                if (value.equals("yes"))
                    ImageButton.setImageResource(R.drawable.complete);
            }
        });
        ImageButton.setOnClickListener(onclicklistener);

    }


    public void completeMission(int id) {
        ImageButton imageButton = findViewById(id);
        imageButton.setImageResource(R.drawable.complete);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_Question) {

            if (Activity.RESULT_OK == resultCode) {

                if (data.getBooleanExtra("done", false))
                    try {


                        completeMission(id);


                    } catch (Exception e) {
                        System.out.println("the last mission fail");
                    }

            }
        }
    }


    private interface FirebaseCallback {
        void onCallback(String value);
    }


}
