package com.submeter.android.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.webActivity.ShowWebActivity;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.Banner;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    //public static Typeface fontFace = Typeface.createFromAsset(SubmeterApp.getInstance().getAssets(), "fonts/fang_zheng_lanting_GBK.TTF");
    private static Toast toast;
    private static long lastToastTime = -1;
    private static int REQUEST_CODE_CUT_PHOTO = 5;

    public static void showBanner(BaseActivity activity, Banner banner) {
        if (null == activity || null == banner) {
            return;
        }

        /*NetworkResponseListener defaultResponseListener = MeetownApp.getInstance().getDefaultResponseListener();
        if (!TextUtils.isEmpty(banner.getActivityId())) {
            String activityType = banner.getActivityType();
            Utils.clickEventItem(activity, activityType, banner.getActivityId(), null, null);

            SystemAction.recordLog(SystemConstant.RECORD_BANNER, banner.getId(), SystemConstant.ACTIVITY_LOG, banner.getActivityId(), MeetownApp.getInstance().getUserToken(), defaultResponseListener);
        } else if(!TextUtils.isEmpty(banner.getTopicId())) {
            Intent intent = new Intent(activity, CategoryEventsActivity.class);
            intent.putExtra("id", banner.getTopicId());
            intent.putExtra("name", banner.getName());
            intent.putExtra("type", EventDataSource.CHANNEL_EVENTS);
            activity.startActivity(intent);

            SystemAction.recordLog(SystemConstant.RECORD_BANNER, banner.getId(), SystemConstant.TOPIC_LOG, banner.getTopicId(), MeetownApp.getInstance().getUserToken(), defaultResponseListener);
        } else if (!TextUtils.isEmpty(banner.getHref())) {
            Intent intent = new Intent(activity, ShowWebActivity.class);
            intent.putExtra("title", banner.getName());
            intent.putExtra("url", banner.getHref());
            intent.putExtra("bannerId", banner.getId());
            intent.putExtra("coverImage", banner.getFileUrl());
            intent.putExtra("h5pageUrl", banner.getH5pageUrl());
            intent.putExtra("wxh5pageUrl", banner.getWxh5pageUrl());
            activity.startActivity(intent);

            SystemAction.recordLog(SystemConstant.RECORD_BANNER, banner.getId(), SystemConstant.HREF_LOG, banner.getHref(), MeetownApp.getInstance().getUserToken(), defaultResponseListener);
        }*/
    }

    public static String getCommodityType(Context context, String storeType) {
        if("0".equals(storeType)) {
            return context.getString(R.string.match_support_label);
        } else if("1".equals(storeType)) {
            return context.getString(R.string.self_support_label);
        } else if("2".equals(storeType)) {
            return context.getString(R.string.joint_support_label);
        } else {
            return "";
        }
    }

    public static void showToast(Context context, String content) {
        showToast(context, content, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String message, int duration) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (null != toast) {
            TextView messageText = (TextView) toast.getView().findViewById(R.id.text_message);
            if (messageText.getText().equals(message) && System.currentTimeMillis() - lastToastTime < 500) {
                return;
            }
            toast.cancel();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View layout = Utils.inflateView(context, layoutInflater, R.layout.pop_toast, null);

        TextView messageText = (TextView) layout.findViewById(R.id.text_message);
        messageText.setText(message);

        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
        lastToastTime = System.currentTimeMillis();
    }

    public static boolean isValidPhone(String strPhone) {
        if (!TextUtils.isEmpty(strPhone) && isNumber(strPhone)) {
            if (strPhone.startsWith("1") && strPhone.length() == 11) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isNumber(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int dip2px(float dipValue) {
        return dip2px(SubmeterApp.getInstance(), dipValue);
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        return sp2px(SubmeterApp.getInstance(), spValue);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String timeToShow(Context context, long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(context.getResources().getString(R.string.time_year_month_day_HH_mm));
        Date date = new Date(time);
        return formatter.format(date);
    }

    public static void takePicture(Activity activity, String filePath) {
        File fExist = new File(filePath);
        if (fExist.exists()) {
            fExist.delete();
        }

        Uri outputUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            outputUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", fExist);
        } else {
            outputUri = Uri.fromFile(fExist);
        }

        if (null != outputUri) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            activity.startActivityForResult(intent, SystemConstant.REQUEST_TAKE_PICTURE);
        }
    }

    public static void selectImageFromAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, SystemConstant.REQUEST_FROM_ALBUM);
    }

    public static String generateImagePath(Context context) {
        return ImagePipelineConfigFactory.getExternalCacheDir(context) + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    public static boolean isSameDay(long time) {
        return isSameDay(System.currentTimeMillis(), time);
    }

    public static boolean isSameDay(long firstTime, long secondTime) {
        if (secondTime > 0) {
            Date date0 = new Date(secondTime);
            Date date1 = new Date(firstTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            String ds1 = sdf.format(date0);
            String ds2 = sdf.format(date1);
            if (ds1.equals(ds2)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return versionName;
    }

    public static String getResUriPrefix(Context context) {
        return "res://" + context.getPackageName() + "/";
    }

    /**
     * 裁剪图片
     * @param uri
     */
    public static Uri reSizeImage(Uri uri, Uri userImageUri, Fragment context) {
        //重新剪裁图片的大小
        //保证输出的图片文件是一个唯一的空的图片文件。
        File outputImage = new File(Environment.getExternalStorageDirectory(), "crop.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        userImageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", 500);//输出X方向的像素
        intent.putExtra("outputY", 500);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);//设置为不返回数据
        /**
         * 此方法返回的图片只能是小图片（测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
//        intent.putExtra("return-data", true);
//        intent.putExtra("output", Uri.fromFile(new File("/mnt/sdcard/temp")));//保存路径
//        userImageUri = Uri.parse("file:///"+ Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, userImageUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Log.e("TAG","reSizeImage() called with: " + "uri = [" + userImageUri + "]");
        (context).startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
        return userImageUri;
    }

    public static void showWebView(Context context, String title, String webUrl) {
        if (webUrl == null || webUrl.trim().length() == 0) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("url", webUrl);
        bundle.putString("title", null == title ? "" : title);
        Intent intent = new Intent(context, ShowWebActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static String urlSafeBase64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    public static boolean isAppInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static int compareVersion(String firstVersion, String secondVersion) {
        if (TextUtils.isEmpty(firstVersion)) {
            if (TextUtils.isEmpty(secondVersion)) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (TextUtils.isEmpty(secondVersion)) {
                return -1;
            } else {
                String[] firstVersionArray = firstVersion.split("\\.");
                String[] secondVersionArray = secondVersion.split("\\.");
                if (firstVersionArray.length == 0 || secondVersionArray.length == 0) {
                    return 0;
                } else {
                    int length = Math.max(firstVersionArray.length, secondVersionArray.length);
                    for (int i = 0; i < length; ++i) {
                        if (i < firstVersionArray.length) {
                            if (i < secondVersionArray.length) {
                                int sub = Integer.parseInt(firstVersionArray[i]) - Integer.parseInt(secondVersionArray[i]);
                                if (sub == 0 && i < length - 1) {
                                    continue;
                                } else {
                                    return sub;
                                }
                            } else {
                                if (0 == Integer.parseInt(firstVersionArray[i])) {
                                    return 0;
                                } else {
                                    return 1;
                                }
                            }
                        } else {
                            if (0 == Integer.parseInt(secondVersionArray[i])) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }

    public static String getFormatPrice(double price) {
        DecimalFormat df = new DecimalFormat("###########.##");
        return df.format(price);
    }

    public static String formatOrderNumber(String orderNumber) {
        if (TextUtils.isEmpty(orderNumber)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        int length = orderNumber.length();
        for (int i = 0; i < length; ) {
            if (i + 4 < length) {
                stringBuilder.append(orderNumber.substring(i, i + 4)).append(" ");
                i += 4;
            } else {
                stringBuilder.append(orderNumber.substring(i));
                break;
            }
        }

        return stringBuilder.toString().trim();
    }

    public static boolean isValidAddress(String address) {
        String strPattern = "^([ a-zA-Z0-9\\u4e00-\\u9fa5-#＃]{1,150})$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(address);
        return m.matches();
    }

    public static boolean isValidName(String name) {
        /*String strPattern = "^[a-zA-Z\\u4e00-\\u9fa5 ]{1,40}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(name);
        return m.matches();*/
        if(TextUtils.isEmpty(name)) {
            return false;
        }

        if(name.length() < 4 || name.length() > 20) {
            return false;
        }

        return true;
    }

    public static boolean isValidActiveCode(String code) {
        /*String strPattern = "/^\\d{4}$/";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(code);
        return m.matches();*/
        return true;
    }

    public static boolean isValidPassword(String password) {
        /*String strPattern = "^[a-zA-Z\\u4e00-\\u9fa5 ]{1,40}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(name);
        return m.matches();*/

        return true;
    }

    /**
     * 处理日期格式
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(String date, String pattern) {
        try {
            if (!TextUtils.isEmpty(date)) {
                if(pattern == null) {
                    pattern = "yyyy-MM-dd";
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = format.parse(date);
                format = new SimpleDateFormat(pattern);
                return format.format(parse);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 打开指定位置的文件
     * @param context
     * @param filePath
     */
    public static void openFile(Context context, String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            String type = getMIMEType(file);
            Intent intent = null;
            //判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //在AndroidManifest中的android:authorities值
                Uri uri = FileProvider.getUriForFile(context, "com.ouralt.android.fileprovider", file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, type);
            } else {
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                //设置intent的data和Type属性。
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 根据文件后缀名匹配MIMEType
     *
     * @param file
     * @return
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String name = file.getName();
        int index = name.lastIndexOf('.');
        if (index < 0) {
            return type;
        }
        String end = name.substring(index, name.length()).toLowerCase();
        if (TextUtils.isEmpty(end)) return type;

        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static void gotoSearch(BaseActivity activity) {
//        Intent intent = new Intent(activity, SearchActivity.class);
//        activity.startActivity(intent);
    }

    public static View inflateView(Context context, LayoutInflater layoutInflater, int layoutId, ViewGroup viewGroup) {
        return inflateView(context, layoutInflater, layoutId, viewGroup, false);
    }

    public static View inflateView(Context context, LayoutInflater layoutInflater, int layoutId, ViewGroup viewGroup, boolean attachToRoot) {
        if(null == context || null == layoutInflater || layoutId < 0) {
            return null;
        }

        return layoutInflater.inflate(layoutId, viewGroup, attachToRoot);
    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean endTimeIsMoreBig(String startTime, String endTime){
        int i=0;
        // 注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime()<date1.getTime()){
                //结束时间小于开始时间
                i= 1;
                return false;
            }else if (date2.getTime()==date1.getTime()){
                //开始时间与结束时间相同
                i= 2;
                return false;
            }else if (date2.getTime()>date1.getTime()){
                //结束时间大于开始时间
                i= 3;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return  false;
    }

}