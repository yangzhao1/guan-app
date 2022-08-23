package com.submeter.android.activity.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.UserAction;


import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate,INetworkResponseListener,
        AMapLocationListener {

    public static final String TAG = ScanActivity.class.getSimpleName();
    @BindView(R.id.zxingview)
    ZXingView mQRCodeView;
    @BindView(R.id.signLin)
    LinearLayout signLin;
    @BindView(R.id.refreshLocal)
    TextView refreshLocal;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.resetSignIn)
    TextView resetSignIn;
    @BindView(R.id.signImage)
    ImageView signImage;
    @BindView(R.id.signText)
    TextView signText;

    private NetworkResponseListener listener;
    private double lng=0;
    private double lat=0;
    private String add=null;
    private int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan, R.string.scan, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);

        mQRCodeView.setDelegate(this);
        listener = new NetworkResponseListener(this);
        try {
            getLocalAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshLocal.setOnClickListener(v -> {
            try {
                getLocalAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetSignIn.setVisibility(View.VISIBLE);
        });
        resetSignIn.setOnClickListener(v -> {
            if (lat!=0){
                showLoadingView();
                UserAction.signIn(SubmeterApp.getInstance().getUserToken(),companyId,lat,lng,listener);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startCamera();
        mQRCodeView.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopSpotAndHiddenRect();
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate(int reslut) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        mQRCodeView.setVisibility(View.GONE);
        signLin.setVisibility(View.VISIBLE);
        if (reslut!=0){
            if (lat!=0){
                showLoadingView();
                UserAction.signIn(SubmeterApp.getInstance().getUserToken(),reslut,lat,lng,listener);
            }else {
                showToast("请重新定位");
                refreshLocal.setVisibility(View.VISIBLE);
            }
        }else {
            showToast("无效二維碼");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String scanResult = bundle.getString("result");
                Log.i("扫描结果", scanResult);
            }
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        try{
            if (!result.equals("null")){
                String code = result.split(",")[0];
                int r = Integer.valueOf(code);
                companyId = r;
                vibrate(r);
            }else {
                showToast("请扫描正确二维码");
                finish();
            }
        }catch (Exception e){
            showToast("请扫描正确二维码");
            finish();
        }
//        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
//        Utils.showToast(this, getString(R.string.start_camera_fail));
        showToast("开启相机失败");
        finish();
    }

    @Override
    public void onResponse(String result) {
        hideLoadingView();
        if (!listener.handleInnerError(result)){
            signImage.setImageResource(R.mipmap.signin);
            signText.setText("打卡成功");
            refreshLocal.setVisibility(View.GONE);
            resetSignIn.setVisibility(View.GONE);
        }else {
            signImage.setImageResource(R.mipmap.sign_fail);
            signText.setText("打卡失败");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideLoadingView();
        String msg = listener.getErrorMessage();
        showToast(msg);
        signImage.setImageResource(R.mipmap.sign_fail);
        signText.setText("打卡失败");
    }

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private void getLocalAddress() throws Exception {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mlocationClient){
//设置定位监听
            mlocationClient.setLocationListener(this);
            //单次定位
            mLocationOption.setOnceLocation(true);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
//设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mlocationClient.stopLocation();
            mlocationClient.startLocation();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation){
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                lat = amapLocation.getLatitude();//获取纬度
                lng = amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                add = amapLocation.getAddress();
                address.setText("当前位置: " + add);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                String info = amapLocation.getErrorInfo();
                showToast(info);
            }
        }
    }
}
