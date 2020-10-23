package com.machwusa.brum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.machwusa.brum.databinding.ActivityMainBinding;
import com.machwusa.brum.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static final String PREF_DEVICE_NAME = "device_name";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private ActivityMainBinding binding;
    private FileUtil fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fu = new FileUtil(this);




        binding.btnScan.setOnClickListener(view12 -> {
            if (isPermissionGranted(Manifest.permission.CAMERA)){
                openScanner();
            }else {
                requestPermission(Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE);
            }

        });


        binding.btnSave.setOnClickListener(view1 -> {

            if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    saveData();
            }else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE);
            }




        });
    }

    private void openScanner(){
        updatePreference(PREF_DEVICE_NAME,binding.etDeviceName.getText().toString().trim());
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        startActivity(intent);
    }

    private void saveData(){
        if (validate()){
        String data = binding.etDeviceName.getText().toString().trim() + " " +  binding.etBarcode.getText().toString().trim();

        fu.writeToFile(data);

        binding.etDeviceName.setText("");
        binding.etBarcode.setText("");
        updatePreference(PREF_DEVICE_NAME, "");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        binding.etDeviceName.setText(getPref(PREF_DEVICE_NAME));

        Intent intent = getIntent();

        if (intent != null) {
            String barcode = (String) intent.getSerializableExtra("barcode");
            binding.etBarcode.setText(barcode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.etDeviceName.setText(getPref(PREF_DEVICE_NAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        updatePreference(PREF_DEVICE_NAME,binding.etDeviceName.getText().toString().trim());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updatePreference(PREF_DEVICE_NAME, "");
    }

    private boolean validate() {
        boolean valid = true;

        final String deviceName = binding.etDeviceName.getText().toString().trim();
        final String barcode = binding.etBarcode.getText().toString().trim();

        if (deviceName.isEmpty()) {
            binding.etDeviceName.setError("Enter device name");
            valid = false;
        } else {
            binding.etDeviceName.setError(null);
        }

        if (barcode.isEmpty()) {
            binding.etBarcode.setError("Enter barcode");
            valid = false;
        } else {
            binding.etBarcode.setError(null);
        }

        return valid;
    }

    @SuppressLint("ApplySharedPref")
    private void updatePreference(String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.commit();
    }

    private String getPref(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(key, "");
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Function to check and request permission.
    public void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] { permission },
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                openScanner();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                saveData();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}

