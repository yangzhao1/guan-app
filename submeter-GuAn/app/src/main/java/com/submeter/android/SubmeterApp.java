package com.submeter.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.submeter.android.activity.main.view.MainActivity;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.constants.NetworkResConstant;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.entity.Location;
import com.submeter.android.entity.Profile;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkRequestTool;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.tools.ImagePipelineConfigFactory;
import com.submeter.hybridcache.lib.HybridCacheManager;
import com.submeter.hybridcache.lib.ImageInterceptor;
import com.submeter.hybridcache_fresco.FrescoImageProvider;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


public class SubmeterApp extends Application implements INetworkResponseListener {
    public static String TAG = SubmeterApp.class.getSimpleName();

    private static SubmeterApp _instance = null;

    public boolean isForeground = false;

    public boolean suicideTimerRunning = false;

    private int frontActivityCount = 0;

    private int wxPayCode = -1;

    /*jpush id*/
    private String pushId;

    /*当前用户的profile*/
    private Profile currentProfile;

    private AMapLocationClient locationClient;

    private Location currentLocation;

    private CountDownTimer suicideTimer;

    private NetworkResponseListener defaultResponseListener;

    private HybridCacheManager hybridCacheManager;

    public static SubmeterApp getInstance() {
        return _instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        getCurrentProfile();
        //init Fresco
        initFresco();
        //configure Push
        initPushServer();
        //configure umeng analytic
        initUmengStatistics();
        loadUserCache();

        String locationCache = ShareStoreProcess.getInstance().getDataByKey(DBConstant.CURRENT_LOCATION);
        if (!TextUtils.isEmpty(locationCache)) {
            currentLocation = new Gson().fromJson(locationCache, Location.class);
        }

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        String deviceId = getPushId();
        if(!TextUtils.isEmpty(deviceId)) {
            //UserAction.updateDeviceStatus(getUserToken(), deviceId, "ONLINE", getDefaultResponseListener());
        }
    }

    private void initFresco() {
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        hybridCacheManager = HybridCacheManager.newInstance();
        hybridCacheManager.addCacheInterceptor(new ImageInterceptor(this, FrescoImageProvider.getInstance()));
    }

    public void initPushServer() {

    }

    private void initUmengStatistics() {
        AnalyticsConfig.sEncrypt = true;
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setSessionContinueMillis(600000);
        MobclickAgent.openActivityDurationTrack(false);
    }

    private boolean isBackground = false;
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            /*if(isBackground) {
                UserAction.updateDeviceStatus(getUserToken(), getPushId(), "ONLINE", getDefaultResponseListener());
            }*/
            ++frontActivityCount;
            Log.d(TAG, "--------------before----------------background = " + isBackground);

            isBackground = false;
            if (null != suicideTimer && suicideTimerRunning) {
                suicideTimer.cancel();
                suicideTimerRunning = false;
            }

            Log.d(TAG, "------------------after------------background = " + isBackground);
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            --frontActivityCount;
            if (0 == frontActivityCount && isBackground()) {
                if (null == suicideTimer) {
                    suicideTimer = new CountDownTimer(60000, 30000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            suicideTimerRunning = false;
                            suicideTimer.cancel();
                            SubmeterApp.getInstance().exitApp();
                        }
                    };
                } else {
                    suicideTimer.cancel();
                }

                suicideTimerRunning = true;
                suicideTimer.start();

                isBackground = true;
                //UserAction.updateDeviceStatus(getUserToken(), getPushId(), "BACKYARD", getDefaultResponseListener());
                Log.d(TAG, "------------------------------background = " + isBackground);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "------------------------------activity = " + activity.getLocalClassName());
        }
    };

    private boolean isBackground() {
        ActivityManager manager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        String packageName = "";
        if (Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.RunningAppProcessInfo> pis = manager.getRunningAppProcesses();
            ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);
            if (topAppProcess != null && topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                packageName = topAppProcess.processName;
            }
        } else {
            //getRunningTasks() is deprecated since API Level 21 (Android 5.0)
            List localList = manager.getRunningTasks(1);
            if(localList != null && !localList.isEmpty()) {
                ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo) localList.get(0);
                packageName = localRunningTaskInfo.topActivity.getPackageName();
            }
        }

        return !getPackageName().equals(packageName);
    }

    //此方法为每隔60S发起一次定位请求
    public void startGpsService() throws Exception {
        if (null != locationClient) {
            return;
        }
        //声明AMapLocationClient类对象
        locationClient = new AMapLocationClient(this);
        //设置定位回调监听
        locationClient.setLocationListener(locationListener);

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);   //高精度定位，包括GPS，网络定位
        mLocationOption.setOnceLocationLatest(true);    //单次定位，返回最近3s内精度最高的一次定位结果
        mLocationOption.setNeedAddress(true);    //返回结果是否包含地址信息
        mLocationOption.setWifiActiveScan(true);    //是否强制刷新WIFI，默认为强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        mLocationOption.setHttpTimeOut(NetworkResConstant.NETWORK_REQUEST_TIMEOUT);    //如果单次定位发生超时情况，定位随即终止
        mLocationOption.setLocationCacheEnable(false);    //是否缓存网络定位结果，gps结果不缓存
        locationClient.setLocationOption(mLocationOption);

        locationClient.startLocation();    //启动定位
    }

    private void stopGpsService() {
        if (null != locationClient) {
            locationClient.unRegisterLocationListener(locationListener);
            locationClient.stopLocation();    //停止定位后，本地定位服务并不会被销毁
            locationClient.onDestroy();    //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
            locationClient = null;
        }
    }

    public void exitApp() {
        if (null != activityLifecycleCallbacks) {
            unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }

        saveUserCache();
        stopGpsService();
        NetworkRequestTool.getInstance().stopAllRequest();

        Intent it = new Intent(SystemConstant.SEND_EXIT_BROADCAST);
        sendBroadcast(it);

        MobclickAgent.onKillProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                ActivityManager activityMan = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                activityMan.restartPackage(getPackageName());
                System.exit(0);
            }
        }, 500);
    }

    public void handleSignOut() {
//        ShareStoreProcess shareStore = ShareStoreProcess.getInstance();
//        saveUserCache();
        SubmeterApp.getInstance().updateLoginUserInfo("");
    }

    private void loadUserCache() {
        ShareStoreProcess shareStore = ShareStoreProcess.getInstance();
        /*String userId;
        if (null != loginUser) {
            userId = loginUser.getId();
        } else {
            userId = "";
        }

        String cache = shareStore.getDataByKey(DBConstant.EVENT_SEARCH_HISTORY + userId);
        if (!TextUtils.isEmpty(cache)) {
            String[] splits = cache.split(SystemConstant.CACHE_SEP);
            for (String item : splits) {
                if (!TextUtils.isEmpty(item)) {
                    eventSearchHistory.add(item);
                }
            }
        }*/
    }

    public void saveUserCache() {
        /*ShareStoreProcess shareStore = ShareStoreProcess.getInstance();
        String userId;
        if (null != loginUser) {
            userId = loginUser.getId();
        } else {
            userId = "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = eventSearchHistory.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(SystemConstant.CACHE_SEP).append(iterator.next());
        }
        if (stringBuilder.length() > SystemConstant.CACHE_SEP.length()) {
            stringBuilder.append(SystemConstant.CACHE_SEP);
            shareStore.setKeyAndValue(DBConstant.EVENT_SEARCH_HISTORY + userId, stringBuilder.toString());
        }*/
    }

    public String getPushId() {
        if (TextUtils.isEmpty(pushId)) {
            pushId = ShareStoreProcess.getInstance().getDataByKey(DBConstant.PUSH_ID);
        }

        /*if (TextUtils.isEmpty(pushId)) {
            pushId = XGPushConfig.getToken(this);
        }*/

        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
        ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.PUSH_ID, pushId);
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    double geoLat = amapLocation.getLatitude();
                    double geoLng = amapLocation.getLongitude();
                    if (geoLat > 0 && geoLng > 0) {
                        currentLocation = new Location();
                        currentLocation.setLatitude(amapLocation.getLatitude());
                        currentLocation.setLongitude(amapLocation.getLongitude());
                        currentLocation.setCity(amapLocation.getCity());
                        currentLocation.setDistrict(amapLocation.getDistrict());
                        currentLocation.setProvince(amapLocation.getProvince());
                        currentLocation.setStreet(amapLocation.getStreet());

                        String locationStr = new Gson().toJson(currentLocation);
                        ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.CURRENT_LOCATION, locationStr);
                        ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.CURRENT_LOCATION_TIME, String.valueOf(System.currentTimeMillis()));
                        stopGpsService();
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    //Utils.showToast(MeetownApp.this, amapLocation.getErrorInfo());
                }
            }
        }
    };

    public Profile updateLoginUserInfo(String profileInfo) {
        ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.CURRENT_PROFILE_INFO, profileInfo);
        currentProfile = null;
        currentProfile = getCurrentProfile();
        return currentProfile;
    }

    public Profile getCurrentProfile() {
        if (currentProfile == null) {
            try {
                String profileInfo = ShareStoreProcess.getInstance().getDataByKey(DBConstant.CURRENT_PROFILE_INFO);
                if (!TextUtils.isEmpty(profileInfo)) {
                    currentProfile = new Gson().fromJson(profileInfo, Profile.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return currentProfile;
    }

    public String getUserToken() {
        if (null == currentProfile) {
            return "";
        } else {
            return currentProfile.getToken();
        }
    }

    public String getUserType() {
        if (null == currentProfile) {
            return "";
        } else {
            return currentProfile.getUser().getUserType();
        }
    }

    public boolean isVisitor() {
        return null == currentProfile ? true : false;
    }

    @Override
    public void onResponse(String result) {
        // TODO Auto-generated method stub
        //do nothing when receive the response result
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //do nothing when receive the error
    }

    public String getCustomSizeImageStyle(String url, int width) {
        return getCustomSizeImageStyle(url, width, width);
    }

    public String getCustomSizeImageStyle(String url, int width, int height) {
        if(TextUtils.isEmpty(url)) {
            return "";
        }

        return url + String.format(SystemConstant.IMAGE_STYLE, width, height);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }


    public int getWxPayCode() {
        return wxPayCode;
    }

    public void setWxPayCode(int wxPayCode) {
        this.wxPayCode = wxPayCode;
    }
/*
    *//**
     * 解析省市区的XML数据
     *//*
    public void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            /*//*//* 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            /*//*//*
            if (null != provinceList) {
                mProvinceDatas = new String[provinceList.size()];
                for (int i = 0; i < provinceList.size(); i++) {
                    // 遍历所有省的数据
                    mProvinceDatas[i] = provinceList.get(i).getName();
                    List<CityModel> cityList = provinceList.get(i).getCityList();
                    String[] cityNames = new String[cityList.size()];
                    for (int j = 0; j < cityList.size(); j++) {
                        // 遍历省下面的所有市的数据
                        cityNames[j] = cityList.get(j).getName();
                        List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                        String[] distrinctNameArray = new String[districtList.size()];
                        DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                        for (int k = 0; k < districtList.size(); k++) {
                            // 遍历市下面所有区/县的数据
                            DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                            // 区/县对于的邮编，保存到mZipcodeDatasMap
                            mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                            distrinctArray[k] = districtModel;
                            distrinctNameArray[k] = districtModel.getName();
                        }
                        // 市-区/县的数据，保存到mDistrictDatasMap
                        mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public String[] getProvinceDatas() {
        return mProvinceDatas;
    }

    public HashMap<String, String[]> getCityDatas() {
        return mCitisDatasMap;
    }

    public HashMap<String, String[]> getDistrictDatasMap() {
        return mDistrictDatasMap;
    }

    public ArrayList<PostAddress> getPostAddressArrayList() {
        if (null == postAddressArrayList) {
            postAddressArrayList = new ArrayList<>();
            String cachePostAddress = ShareStoreProcess.getInstance().getDataByKey(DBConstant.POST_ADDRESS);
            if (!TextUtils.isEmpty(cachePostAddress)) {
                try {
                    PostAddress postAddress;
                    JSONObject itemObj;
                    JSONArray postJSONArray = new JSONArray(cachePostAddress);
                    int count = postJSONArray.length();
                    for (int i = 0; i < count; ++i) {
                        itemObj = postJSONArray.getJSONObject(i);
                        if (null != itemObj) {
                            postAddress = new Gson().fromJson(itemObj.toString(), PostAddress.class);
                            postAddressArrayList.add(postAddress);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return postAddressArrayList;
    }

    public void addPostAddress(PostAddress address) {
        if (null == postAddressArrayList) {
            postAddressArrayList = new ArrayList<>();
        }

        postAddressArrayList.add(address);
        updateCache();
    }

    public void deletePostAddress(PostAddress address) {
        if (null == postAddressArrayList || postAddressArrayList.isEmpty()) {
            return;
        }

        boolean hasChanged = false;
        PostAddress postAddress;
        Iterator<PostAddress> it = postAddressArrayList.iterator();
        while (it.hasNext()) {
            postAddress = it.next();
            if (postAddress.getId().equals(address.getId())) {
                it.remove();
                hasChanged = true;
                break;
            }
        }

        if (hasChanged) {
            updateCache();
        }
    }

    public void updatePostAddress(PostAddress address) {
        if (null == postAddressArrayList || postAddressArrayList.isEmpty()) {
            return;
        }

        updateCache();
    }*/

    private void updateCache() {
        /*JSONObject itemObj;
        JSONArray addressJSonArray = new JSONArray();
        PostAddress postAddress;
        Iterator<PostAddress> it = postAddressArrayList.iterator();
        while (it.hasNext()) {
            postAddress = it.next();
            try {
                itemObj = new JSONObject();
                itemObj.put("id", postAddress.getId());
                itemObj.put("name", postAddress.getName());
                itemObj.put("phone", postAddress.getPhone());
                itemObj.put("province", postAddress.getProvince());
                itemObj.put("city", postAddress.getCity());
                itemObj.put("district", postAddress.getDistrict());
                itemObj.put("detailAddress", postAddress.getDetailAddress());

                addressJSonArray.put(itemObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.POST_ADDRESS, addressJSonArray.toString());*/
    }

    public NetworkResponseListener getDefaultResponseListener() {
        if (null == defaultResponseListener) {
            defaultResponseListener = new NetworkResponseListener(this);
        }
        return defaultResponseListener;
    }

    public HybridCacheManager getHybridCacheManager() {
        return hybridCacheManager;
    }

    public boolean isAppRunning() {
        if (!MainActivity.isRunning) {
            return false;
        } else {
            return true;
        }
    }
}