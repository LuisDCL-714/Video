package com.example.video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private Button btnVideo;
    private VideoView videoView;
    private int permissionGranted = PackageManager.PERMISSION_GRANTED;
    private String permissionWriteExternalSorage = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            permissionCamera = Manifest.permission.CAMERA;
    private static final int REQUEST_RECORD_VIDEO_PERMISSION = 1, REQUEST_RECORD_VIDEO = 2;
    private Intent intent;
    private Uri uriVideo;
    private String[] listPermissions = new String[]{permissionWriteExternalSorage, permissionCamera};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVideo = findViewById(R.id.btnVideo);
        videoView = findViewById(R.id.videoView);

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != permissionGranted ||  ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != permissionGranted){
                    checkPermissionVideo();
                }else{
                    captureVideo();
                }
            }
        });
    }

    private void captureVideo() {
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_RECORD_VIDEO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_RECORD_VIDEO && resultCode == RESULT_OK){
            uriVideo = data.getData();
            videoView.setVideoURI(uriVideo);
            videoView.start();
        }
    }

    private void checkPermissionVideo() {
        ActivityCompat.requestPermissions(this, listPermissions, REQUEST_RECORD_VIDEO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_RECORD_VIDEO_PERMISSION){
            if(grantResults.length > 0 && (grantResults[0] == permissionGranted && grantResults[1] == permissionGranted)){
                captureVideo();
            }else{
                checkPermissionVideo();
            }
        }
    }
}