package com.nicoladaniello.lobstar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Camera2ExampleActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2_example);
        if (savedInstanceState != null) {
            Log.e(TAG, "savedInstanceState is not null. Return.");
            return;
        }
        Log.e(TAG, "Initiating cameraFragment.");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cameraLayout, CameraFragment.newInstance())
                .commit();
    }
}
