package com.example.schlomo.test7;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Question4Activity extends AppCompatActivity {
    public static TextView textView;
    Button camera;
    ImageView imageView;
    Bitmap bitmap;
    int mission;
    FirebaseApp firebaseApp;
    String question;
    ImageButton exit;
    private ProgressDialog progressDialog;
    private String missionUrl;
    String team="team";
    private boolean teamHead;
    private String teamTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question4);
        camera = findViewById(R.id.cameraVideo);
        textView = findViewById(R.id.videoQuestion);
        firebaseApp = FirebaseApp.initializeApp(this);
        question = getIntent().getStringExtra("question");
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        missionUrl = getIntent().getStringExtra("missionUrl");
        teamTitle = getIntent().getStringExtra("teamTitle");
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamHead) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(intent, 0);
                }
                else {
                    Toast.makeText(Question4Activity.this,"רק ראש קבוצה יכול לענות",Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit = findViewById(R.id.exit4);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getValueFirebase(missionUrl, question, new FirebaseCallback() {
            @Override
            public void onCallback(String code2) {

                textView.setText(code2);
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri videoUri = data.getData();
            uploadVideo( question, videoUri);
        }
        catch (Exception e){

        }
    }

    ///String team, String numOfMIssion,
    public void uploadVideo( String numOfMIssion, Uri videoUri) {


        mission = Integer.parseInt(numOfMIssion);
        FirebaseStorage mStor = FirebaseStorage.getInstance();
        StorageReference storageRef = mStor.getReferenceFromUrl("gs://teams-yedidim.appspot.com" + "/" + teamTitle + "/");

        StorageReference mountainsRef = storageRef.child("mission" + numOfMIssion + ".avi");



        progressDialog = new ProgressDialog(Question4Activity.this);
        progressDialog.setTitle("שליחת סירטון ");
        progressDialog.show();
        UploadTask uploadTask = mountainsRef.putFile(videoUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Question4Activity.this, "the Upload as fail try again", Toast.LENGTH_SHORT).show();
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

    public void getValueFirebase(String url, String myChild, final FirebaseCallback firebaseCallback) {

        Firebase link = new Firebase(url);
        link.child("mission").child(myChild).child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue().toString();
                firebaseCallback.onCallback(str);
            }

            @Override
            public void onCancelled(com.firebase.client.FirebaseError firebaseError) {

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
            public void onCancelled(com.firebase.client.FirebaseError firebaseError) {

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