package com.example.permissions.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.permissions.R;

import java.io.File;

public class TempActivity extends AppCompatActivity {
    private static final String TAG = "TempActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.i(TAG, "permission : " + permission);
        if (permission == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            askUserForPermission(permissions, 100);
        }
    }

    private void askUserForPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Write Access to SDCARD granted");
                createAppDir();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("Write Access to SDCARD required for app")
                        .setPositiveButton("Ask me again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askUserForPermission(permissions, requestCode);
                            }
                        })
                        .create()
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createAppDir() {
        String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/uncox";
        File file = new File(dirName);
        boolean wasCreated = file.mkdirs();
        if (wasCreated) {
            Log.i(TAG, "Yes");
        } else {
            Log.i(TAG, "No");
        }
    }
}