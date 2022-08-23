package com.submeter.android.tools;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.submeter.android.activity.BaseActivity;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.view.OpenNotificationDialog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Herry on 2016/11/ic_common_sort_default.
 */
public class NotificationTools {

    private static OpenNotificationDialog notificationDialog;

    public static boolean needCheckNotificationAgain = false;

    public static boolean notificationEnabled(Context context) {
        if(Build.VERSION.SDK_INT >= 19) {
            return notificationEnabled_up43(context);
        } else {
            return notificationEnabled_down43(context);
        }
    }

    //up OS 4.ic_common_sort_default
    @TargetApi(19)
    public static boolean notificationEnabled_up43(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;
        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);

            boolean boo = ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            return boo;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean notificationEnabled_down43(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    public static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    public static void ignoreOpenNotification() {
        int count = 0;
        String lastShowTime = ShareStoreProcess.getInstance().getDataByKey(DBConstant.LAST_NOTIFICATION_PROMPT_TIME);
        if(!TextUtils.isEmpty(lastShowTime)) {
            String[] showTime = lastShowTime.split("&");
            count = Integer.parseInt(showTime[0]);
        }
        ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.LAST_NOTIFICATION_PROMPT_TIME, String.valueOf(++count) + "&" + String.valueOf(System.currentTimeMillis()));
    }

    public static void showOpenSettingView(BaseActivity activity) {
        notificationDialog = new OpenNotificationDialog(activity);
        notificationDialog.show();
    }

    public static boolean needShowNotification() {
        String lastShowTime = ShareStoreProcess.getInstance().getDataByKey(DBConstant.LAST_NOTIFICATION_PROMPT_TIME);
        if(TextUtils.isEmpty(lastShowTime)) {
            return true;
        } else {
            long dayTime = 24 * 60 * 60 * 1000;
            String[] showTime = lastShowTime.split("&");
            if(Integer.parseInt(showTime[0]) < 2) {
                if(System.currentTimeMillis() - Long.parseLong(showTime[1]) > 6 * dayTime) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if(System.currentTimeMillis() - Long.parseLong(showTime[1]) > 14 * dayTime) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public static boolean notificationShowed() {
        if(null != notificationDialog) {
            return notificationDialog.isShowed();
        } else {
            return false;
        }
    }

    public static void closeNotificationView() {
        if(null != notificationDialog) {
            notificationDialog.close();
        }
    }
}
