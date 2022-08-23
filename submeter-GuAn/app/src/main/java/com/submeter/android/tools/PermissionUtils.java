package com.submeter.android.tools;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Herry on 2016/11/3.
 */
public class PermissionUtils {
    public static final int REQUEST_DIAL_PERMISSION = 1;
    public static final int REQUEST_CAMERA_PERMISSION = 2;
    public static final int REQUEST_LOCATION_PERMISSION = 3;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 4;

    public static boolean enablePermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            int permissionResult = ContextCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, android.os.Process.myUid(), context.getPackageName());
                if (checkOp == AppOpsManager.MODE_IGNORED) {
                    return false;
                }
                return true;
            }
        }
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public interface IPermissionRequestCallback {
        public void permissionGranted(String permission);

        public void permissionDenied(String permission);
    }
}
