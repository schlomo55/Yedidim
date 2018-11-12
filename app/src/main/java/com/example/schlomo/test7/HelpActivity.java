package com.example.schlomo.test7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class HelpActivity extends AppCompatActivity {
    ImageButton exit;
    TextView nameAdmin;
    TextView copytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
         copytext = findViewById(R.id.textView5);
        nameAdmin = findViewById(R.id.nameAdmin);
        copytext.setTextIsSelectable(true);

        getValueFirebase("https://teams-yedidim.firebaseio.com/start", "admin", new FirebaseCallback() {
            @Override
            public void onCallback(String code2) {

            }
        });
        exit =  findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child(myChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameAdmin.setText(dataSnapshot.child("name").getValue().toString());
                copytext.setText(dataSnapshot.child("num").getValue().toString());
                firebaseCallback.onCallback(dataSnapshot.getValue().toString());
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