package com.example.schlomo.test7;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Question3Activity extends AppCompatActivity {
    TextView question3;
    EditText answer3;
    Button send3;
    ImageButton exit3;
    private String question;
    private String missionUrl;
    private ProgressDialog progressDialog;
    private boolean teamHead;
    private String teamTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question3);
        question3 = findViewById(R.id.question3);
        send3 = findViewById(R.id.send3);
        answer3 = findViewById(R.id.answer3);
        exit3 = findViewById(R.id.exit3);
        exit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        question = getIntent().getStringExtra("question");
        missionUrl = getIntent().getStringExtra("missionUrl");
        teamTitle = getIntent().getStringExtra("teamTitle");
        send3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamHead){
                uploadAnswer(teamTitle, question, answer3.getText().toString());}
                else {
                    Toast.makeText(Question3Activity.this,"רק ראש קבוצה יכול לענות",Toast.LENGTH_SHORT).show();
                }
            }
        });
        getValueFirebase(missionUrl, question, new FirebaseCallback() {
            @Override
            public void onCallback(String code2) {

                question3.setText(code2);
            }
        });


    }

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child("mission").child(myChild).child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue().toString();
                firebaseCallback.onCallback(str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void uploadAnswer(String team, String numOfMIssion, String str) {

        if (str == null || str.equals("")) {
            Toast.makeText(Question3Activity.this, "התשובה ריקה ", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseStorage mStor = FirebaseStorage.getInstance();
        StorageReference storageRef = mStor.getReferenceFromUrl("gs://teams-yedidim.appspot.com" + "/" + team + "/");

        StorageReference mountainsRef = storageRef.child("mission" + numOfMIssion + ".txt");


        progressDialog = new ProgressDialog(Question3Activity.this);
        progressDialog.setTitle("שליחת תשובה ");
        progressDialog.show();
        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        UploadTask uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Question3Activity.this, "the Upload as fail try again", Toast.LENGTH_SHORT).show();
                System.out.println("try again");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                System.out.println("we done it");
                Intent intent = new Intent();
                progressDialog.dismiss();
                intent.putExtra("done", true);
                setResult(Activity.RESULT_OK, intent);
                setValueFirebase(missionUrl,question,"finish","yes");
                finish();
            }
        });
    }

    private interface FirebaseCallback {
        void onCallback(String code2);
    }

    public void setValueFirebase(String url, String numOfQuestion,String myChild,String value) {



        Firebase link = new Firebase(url);
        link.child("mission").child(numOfQuestion).child(myChild).setValue(value);


    }
}
