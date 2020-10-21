package com.machwusa.brum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.machwusa.brum.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnSave.setOnClickListener(view1 -> {

            String data = binding.etDeviceName.getText().toString().trim() + " " +  binding.etBarcode.getText().toString().trim();

            writeToFile(data, getApplicationContext());
        });
    }

    private void writeToFile(String data, Context context) {

        File file = new File(context.getExternalFilesDir(null), "testfile.txt");



        try {
            if (!file.exists()){
                file.createNewFile();
            }


            FileOutputStream fileOutput = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);

            outputStreamWriter.write(data +"\n" );

            outputStreamWriter.flush();
            fileOutput.getFD().sync();
            outputStreamWriter.close();

            Toast.makeText(context, data + " added", Toast.LENGTH_LONG).show();
            binding.etDeviceName.setText("");
            binding.etBarcode.setText("");

            /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("writefileout.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            Log.d(TAG, "writeToFile: " + data);
            outputStreamWriter.close();*/
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

