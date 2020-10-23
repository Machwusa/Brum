package com.machwusa.brum.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtil {

    private Activity mActivity;
    private String mPermission;
    private int mRequestCode;

    public PermissionsUtil(Activity activity, String permission, int requestCode) {
        this.mActivity = activity;
        this.mPermission = permission;
        this.mRequestCode = requestCode;
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(mActivity, mPermission)
                == PackageManager.PERMISSION_DENIED;
    }

    // Function to check and request permission.
    public void requestPermission() {
            ActivityCompat.requestPermissions(mActivity,
                    new String[] { mPermission },
                    mRequestCode);
    }
}
