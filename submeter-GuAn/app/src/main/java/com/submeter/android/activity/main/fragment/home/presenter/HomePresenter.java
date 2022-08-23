package com.submeter.android.activity.main.fragment.home.presenter;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.android.volley.VolleyError;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.entity.HomeData;
import com.submeter.android.activity.main.fragment.home.model.HomeModel;
import com.submeter.android.activity.main.fragment.home.model.IHomeModel;
import com.submeter.android.activity.main.fragment.home.view.IHomeView;
import com.submeter.android.entity.Weather;
import com.submeter.android.interfacce.IDataSourceListener;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.HomeAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 赵勃 on 2018/11/28.
 */

public class HomePresenter implements IHomePresenter, IDataSourceListener<HomeData>,WeatherSearch.OnWeatherSearchListener {

    private IHomeView<HomeData> homeView;

    private IHomeModel homeModel;

    private WeatherSearchQuery mquery;
    private String cityname ="廊坊市固安县";
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;

    private NetworkResponseListener networkResponseListener;

    public HomePresenter(IHomeView<HomeData> homeView) {
        this.homeView = homeView;
        homeModel = new HomeModel();
    }

    @Override
    public void loadData() {
        homeView.getBaseActivity().showLoadingView();
//        Location location = SubmeterApp.getInstance().getCurrentLocation();
//        if(location != null) {
//            homeView.showCurrentLocation(location.getCity());
//        }
//        getWeather();
        getJHWeather();
        //load data
        homeModel.loadData(this);
    }

    private void getJHWeather() {
        networkResponseListener = new NetworkResponseListener(new INetworkResponseListener() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    HomeData homeData = new HomeData();
                    if (jsonObject.getInt("error_code")==0){
                        ShareStoreProcess.getInstance().setKeyAndValue("weather",result);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        JSONObject realtime = jsonObject1.getJSONObject("realtime");
                        JSONArray future = jsonObject1.getJSONArray("future");
                        Weather weather = new Weather();
                        weather.setCity("廊坊市固安县");
                        weather.setDate(future.getJSONObject(0).getString("date"));
                        weather.setPm(realtime.getString("aqi"));
                        weather.setTemp(realtime.getString("temperature") + "°");
                        weather.setWeatherType(realtime.getString("info"));
                        weather.setAlert(realtime.getString("humidity"));

                        homeData.setWeather(weather);
                        homeView.updateWeather(homeData);
                    }else {
                        String weatherResult = ShareStoreProcess.getInstance().getDataByKey("weather");
                        if (weatherResult!=null&&!weatherResult.equals("")){
                            JSONObject object = new JSONObject(weatherResult);
                            JSONObject jsonObject1 = object.getJSONObject("result");
                            JSONObject realtime = jsonObject1.getJSONObject("realtime");
                            JSONArray future = jsonObject1.getJSONArray("future");
                            Weather weather = new Weather();
                            weather.setCity("廊坊市固安县");
                            weather.setDate(future.getJSONObject(0).getString("date"));
                            weather.setPm(realtime.getString("aqi"));
                            weather.setTemp(realtime.getString("temperature") + "°");
                            weather.setWeatherType(realtime.getString("info"));
                            weather.setAlert(realtime.getString("humidity"));

                            homeData.setWeather(weather);
                            homeView.updateWeather(homeData);
                        }else {
                            homeView.getBaseActivity().showToast("没有天气数据");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                homeView.getBaseActivity().showToast(networkResponseListener.getErrorMessage());
            }
        });
        HomeAction.getWeather(networkResponseListener);
    }

    public void getWeather() throws AMapException {
        BaseActivity baseActivity = homeView.getBaseActivity();
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(baseActivity);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    @Override
    public void gotoMoreNotice() {

    }

    @Override
    public void onLoadFinish(HomeData data) {
        homeView.getBaseActivity().hideLoadingView();
        if (data!=null){
            HomeData homeData = new HomeData();
            homeData.setNotices(data.getNotices());
            homeData.setCompanyNum(data.getCompanyNum());
            homeView.updateView(data);
        }
    }

    @Override
    public void onLoadFail(int errorCode, String errorMessage) {
        homeView.getBaseActivity().hideLoadingView();
    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                Weather weather = new Weather();
                weather.setCity(weatherlive.getCity());
                weather.setDate(weatherlive.getReportTime().split(" ")[0]);
                weather.setPm(weatherlive.getHumidity());
                weather.setTemp(weatherlive.getTemperature() + "°");
                weather.setWeatherType(weatherlive.getWeather());

//                homeData.setWeather(weather);
//                homeView.updateView(homeData);
            } else {
                homeView.getBaseActivity().showToast("没有数据");
            }
        } else {
            homeView.getBaseActivity().showToast("查询错误");
        }
        homeView.getBaseActivity().hideLoadingView();
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }
}
