package com.olabode.wilson.recoder;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    MediaPlayer mediaPlayer ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private Button micButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        random = new Random();





        final File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Recorder");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }


        micButton = (Button) findViewById(R.id.mic);


        micButton.setOnTouchListener(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View v, MotionEvent event) {
                if(checkPermission()) {
                    int count = 0;
                    AudioSavePathInDevice = mediaStorageDir.getAbsolutePath()+ "/" + "AudioRecording" +count + ".3gp";
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(MainActivity.this, "recording", Toast.LENGTH_SHORT).show();
                        MediaRecorderReady();
                        try {
                            Toast.makeText(MainActivity.this,AudioSavePathInDevice,Toast.LENGTH_SHORT).show();
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IllegalStateException e) {

                            e.printStackTrace();
                        } catch (IOException e) {

                            e.printStackTrace();
                        }

                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        mediaRecorder.stop();
                        Toast.makeText(getApplicationContext(),"recording Stopped",Toast.LENGTH_SHORT).show();
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(AudioSavePathInDevice);

                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                        Toast.makeText(MainActivity.this, "Recording Playing",
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(MainActivity.this,AudioSavePathInDevice ,Toast.LENGTH_SHORT).show();


                    }
                }
                else {
                    requestPermission();
                }

                return true;
            }
        });
    }


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }




}
