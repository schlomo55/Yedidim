package com.example.schlomo.test7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Pop extends Activity {
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    Button b6;
    boolean teamHead;
    String teamUrl;
    double location[];
    private int numOfTeam;
    private int numOfMission;
    private String missionUrl;
    private String teamTitle;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_win);
        final String s = getIntent().getStringExtra("test");
        location = getIntent().getDoubleArrayExtra("location");
        System.out.println("s : " + s);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        teamTitle = getIntent().getStringExtra("teamTitle");
        numOfTeam = getIntent().getIntExtra("numOfTeam", 0);
        numOfMission = getIntent().getIntExtra("numOfMission", 0);
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        teamUrl = getIntent().getStringExtra("teamUrl");
        missionUrl = getIntent().getStringExtra("missionUrl");
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        teamHead = getIntent().getBooleanExtra("teamHead", false);
        teamUrl = getIntent().getStringExtra("teamUrl");
        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        final Intent intentM = new Intent(this, MissionActivity.class);
        final Intent intentH = new Intent(this, HelpActivity.class);
        final Intent intentI = new Intent(this, InstructionActivity.class);
        final Intent intentS = new Intent(this, ScoresActivity.class);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentM.putExtra("numOfMission", numOfMission);
                intentM.putExtra("missionUrl", missionUrl);
                intentM.putExtra("teamHead", teamHead);
                intentM.putExtra("teamUrl", teamUrl);
                intentM.putExtra("teamTitle", teamTitle);
                startActivity(intentM);
                finish();
                Toast.makeText(Pop.this, "Mission", Toast.LENGTH_SHORT).show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentS.putExtra("teamUrl", teamUrl);
                intentS.putExtra("numOfTeam", numOfTeam);
                startActivity(intentS);
                finish();
                Toast.makeText(Pop.this, "Scores", Toast.LENGTH_SHORT).show();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Pop.this, "Instuction", Toast.LENGTH_SHORT).show();
                startActivity(intentI);
                finish();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    Intent intent = new Intent();
                    intent.putExtra("mylocation", true);
                    setResult(Activity.RESULT_OK,intent);
//                    System.out.println("lat is : " + location[0] + "lng is : " + location[1]);
//                    Toast.makeText(Pop.this, "lat : " + location[0] + " lng : " + location[1], Toast.LENGTH_SHORT).show();
////
                } catch (Exception ignored) {
                    Toast.makeText(Pop.this, "connect your GPS please and try again ", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("lastlocation", true);
                setResult(Activity.RESULT_OK,intent);
                Toast.makeText(Pop.this, "Last Mission", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Pop.this, "help", Toast.LENGTH_SHORT).show();
                startActivity(intentH);
                finish();


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {



        }catch (Exception e){

        }
    }
}


