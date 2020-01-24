package com.nicoladaniello.lobstar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // Permission request context
    private static int REQUEST_CODE_PERMISSIONS = 10;

    // This is an array of all the permission specified in the manifest.
    private String[] REQUIRED_PERMISSIONS = { Manifest.permission.CAMERA };

    TextureView viewFinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFinder = findViewById(R.id.textureView);

        if (allPermissionsGranted()) {
            startCamera();
        }
        else  {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }


    private void startCamera() {
        CameraX.unbindAll();

        // Set target resolution
        Size screenSize = new Size(viewFinder.getWidth(), viewFinder.getHeight());
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(screenSize)
                .build();

        // Instantiate preview
        Preview preview = new Preview(previewConfig);

        // Set surface texture every update
        preview.setOnPreviewOutputUpdateListener(
                (@NonNull Preview.PreviewOutput output) -> {
                    ViewGroup parent = (ViewGroup) viewFinder.getParent();
                    parent.removeView(viewFinder);
                    parent.addView(viewFinder, 0);
                    viewFinder.setSurfaceTexture(output.getSurfaceTexture());
                    updateTransform();
                }
        );

        CameraX.bindToLifecycle(this, preview);
    }


    // compensate for changes in device orientation
    // to display our viewfinder in upright rotation
    private void updateTransform() {
        Matrix matrix = new Matrix();

        // Compute the center of the view finder
        float centerX = viewFinder.getWidth() / 2f;
        float centerY = viewFinder.getHeight() / 2f;

        // Correct preview output to account for display rotation
        int rotationDegree;
        int rotation = (int) viewFinder.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDegree = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegree = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegree = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegree = 270;
                break;
            default:
                return;
        }
        matrix.postRotate(rotationDegree, centerX, centerY);

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            }
            else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission: REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
