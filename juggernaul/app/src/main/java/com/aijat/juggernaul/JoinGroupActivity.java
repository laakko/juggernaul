package com.aijat.juggernaul;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class JoinGroupActivity extends AppCompatActivity {

    private SurfaceView cameraView;
    public String QRString;


    // NOTE: Based on this tutorial: https://codelabs.developers.google.com/codelabs/bar-codes/#0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        getSupportActionBar().setTitle("Looking for QR-codes...");

        cameraView = (SurfaceView) findViewById(R.id.cameraView);

        // set up barcode detector (Google Mobile Vision API)
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if(!detector.isOperational()){
            // Add toast "could not set up barcode detector)
            return;
        }

        // Set up camera into the surface view (Google Mobile Vision API)
        final CameraSource cameraSource = new CameraSource
                .Builder(this, detector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1280, 720)
                .build();


        // Set up camera view, check for permission
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                ActivityCompat.requestPermissions(JoinGroupActivity.this, new String[]{ android.Manifest.permission.CAMERA}, 1);
                try {

                    if(ContextCompat.checkSelfPermission(JoinGroupActivity.this,
                            android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    }

                } catch (IOException ie) {
                    // Add toast "Camera error"

                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        // Detect barcodes in real-time (Google Mobile Vision API)
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> found_barcodes = detections.getDetectedItems();

                if (found_barcodes.size() != 0) {

                    Barcode QRCode = found_barcodes.valueAt(0);
                    QRString = QRCode.rawValue;

                    Toast toast = Toast.makeText(getApplication().getApplicationContext(), "QR-Code found:" + QRString, Toast.LENGTH_LONG);
                    toast.show();

                    // TODO Call backend API here with the access token (?) gotten from QR-code

                }
            }

        });

    }
}
