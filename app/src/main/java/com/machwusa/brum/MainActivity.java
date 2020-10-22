package com.machwusa.brum;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private static String PREF_DEVICE_NAME = "device_name";

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
            updatePreference(PREF_DEVICE_NAME,binding.etDeviceName.getText().toString().trim());
            Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
            startActivity(intent);
        });


        binding.btnSave.setOnClickListener(view1 -> {

            if (validate()){
                String data = binding.etDeviceName.getText().toString().trim() + " " +  binding.etBarcode.getText().toString().trim();

                fu.writeToFile(data);

                binding.etDeviceName.setText("");
                binding.etBarcode.setText("");
                updatePreference(PREF_DEVICE_NAME, "");
            }


        });
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
}

