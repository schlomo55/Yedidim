package com.example.schlomo.test7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Question1Activity extends AppCompatActivity {
    ImageButton exit;
    TextView theQuestion;
    Button choise1;
    Button choise2;
    Button choise3;
    Button choise4;
    String str[] = new String[6];
    View.OnClickListener onclicklistener;
    private String missionUrl;
    private String question;
    private boolean makeChoise = false;
    private boolean teamHead;
    private String teamTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);
        missionUrl = getIntent().getStringExtra("missionUrl");
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        question = getIntent().getStringExtra("question");
        teamTitle = getIntent().getStringExtra("teamTitle");
        theQuestion = findViewById(R.id.question1);
        choise1 = findViewById(R.id.choice1);
        choise2 = findViewById(R.id.choice2);
        choise3 = findViewById(R.id.choice3);
        choise4 = findViewById(R.id.choice4);

        getValueFirebase(missionUrl, question, new FirebaseCallback() {
            @Override
            public void onCallback(String[] str) {
                theQuestion.setText(str[0]);
                choise1.setText(str[1]);
                choise2.setText(str[2]);
                choise3.setText(str[3]);
                choise4.setText(str[4]);



            }
        });
        exit = findViewById(R.id.exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        choise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamHead){
                if (!makeChoise) {
                    if (str[5].equals("1"))
                        Toast.makeText(Question1Activity.this, "נכון", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Question1Activity.this, "לא נכון", Toast.LENGTH_SHORT).show();
                    makeChoise= true;

                    setValueFirebase(missionUrl,question,"finish","yes");
                }}
                else {
                    Toast.makeText(Question1Activity.this,"רק ראש קבוצה יכול לענות",Toast.LENGTH_SHORT).show();
                }
            }
        });
        choise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamHead){
                if (!makeChoise) {
                    if (str[5].equals("2"))
                        Toast.makeText(Question1Activity.this, "נכון", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Question1Activity.this, "לא נכון", Toast.LENGTH_SHORT).show();
                    makeChoise= true;
                    setValueFirebase(missionUrl,question,"finish","yes");
                }}
                else {
                    Toast.makeText(Question1Activity.this,"רק ראש קבוצה יכול לענות",Toast.LENGTH_SHORT).show();
                }
            }
        });
        choise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamHead){
                if (!makeChoise) {
                    if (str[5].equals("3"))
                        Toast.makeText(Question1Activity.this, "נכון", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Question1Activity.this, "לא נכון", Toast.LENGTH_SHORT).show();
                 makeChoise= true;
                    setValueFirebase(missionUrl,question,"finish","yes");
                }}
                else {
                    Toast.makeText(Question1Activity.this,"רק ראש קבוצה יכול לענות",Toast.LENGTH_SHORT).show();
                }
            }
        });
  choise4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamHead){
                if (!makeChoise) {
                    if (str[5].equals("4"))
                        Toast.makeText(Question1Activity.this, "נכון", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Question1Activity.this, "לא נכון", Toast.LENGTH_SHORT).show();
                 makeChoise= true;
                    setValueFirebase(missionUrl,question,"finish","yes");
                }}
                else {
                    Toast.makeText(Question1Activity.this,"רק ראש קבוצה יכול לענות",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public void setValueFirebase(String url, String numOfQuestion,String myChild,String value) {



        Firebase link = new Firebase(url);
        link.child("mission").child(numOfQuestion).child(myChild).setValue(value);


    }

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child("mission").child(myChild).child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                str[0] = dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        link.child("mission").child(myChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                str[5] = dataSnapshot.child("rightAnwers").getValue().toString();
                str[1] = dataSnapshot.child("option").child("1").getValue().toString();
                str[2] = dataSnapshot.child("option").child("2").getValue().toString();
                str[3] = dataSnapshot.child("option").child("3").getValue().toString();
                str[4] = dataSnapshot.child("option").child("4").getValue().toString();
                //dataSnapshot.child("4").getValue();
                firebaseCallback.onCallback(str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("done", true);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private interface FirebaseCallback {
        void onCallback(String[] str);
    }
}
