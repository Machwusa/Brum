package com.machwusa.brum.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {

    private final File mFile;
    private final Context mContext;

    public FileUtil(Context context) {
        this.mContext = context;
        this.mFile = new File(context.getExternalFilesDir(null), "testfile.txt");
    }

    public void writeToFile(String data) {


        try {
            if (!mFile.exists()){
                mFile.createNewFile();
            }


            FileOutputStream fileOutput = new FileOutputStream(mFile, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutput);

            outputStreamWriter.write(data +"\n" );

            outputStreamWriter.flush();
            fileOutput.getFD().sync();
            outputStreamWriter.close();

            Toast.makeText(mContext, data + " added", Toast.LENGTH_LONG).show();

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
