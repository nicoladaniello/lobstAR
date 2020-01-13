package com.nicoladaniello.lobstar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Log.e(TAG, "savedInstanceState is not null. Return.");
            return;
        }
        Log.e(TAG, "Initiating cameraFragment.");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cameraFragment, CameraFragment.newInstance())
                .commit();
    }
}
