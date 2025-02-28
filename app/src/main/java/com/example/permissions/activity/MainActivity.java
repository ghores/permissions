package com.example.permissions.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.permissions.R;
import com.example.permissions.helper.RequestHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestForWriteSDCardPermission();
        requestForReceivedSMSPermissions();
    }

    private void requestForWriteSDCardPermission() {
        RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, new RequestHelper.OnGrantedListener() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "Granted for write external storage", Toast.LENGTH_SHORT).show();
                createAppDir();
            }
        }, new RequestHelper.OnDeniedListener() {
            @Override
            public void onDenied() {
                Toast.makeText(MainActivity.this, "Denied for write external storage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestForReceivedSMSPermissions() {
        RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.request(Manifest.permission.RECEIVE_SMS, new RequestHelper.OnGrantedListener() {
            @Override
            public void onGranted() {
                Toast.makeText(MainActivity.this, "Granted for received sms", Toast.LENGTH_SHORT).show();
            }
        }, new RequestHelper.OnDeniedListener() {
            @Override
            public void onDenied() {
                Toast.makeText(MainActivity.this, "Denied for received sms", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Permission Required")
                        .setMessage("Receiving SMS required for this app.")
                        .setPositiveButton("Ask me again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestForReceivedSMSPermissions();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RequestHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
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