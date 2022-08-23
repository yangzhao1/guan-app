package com.zbar;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

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

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureActivity extends BaseActivity implements INetworkResponseListener, AMapLocationListener {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CameraManager mCameraManager;
	private LinearLayout signLin;

	private ImageView signImage;
	private TextView signText;
	private TextView refreshLocal;
	private TextView resetSignIn;
	private TextView address;

	private TextView scanResult;
	private FrameLayout scanPreview;
	private Button scanRestart;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private ImageScanner mImageScanner = null;

	private double lng=0;
	private double lat=0;
	private String add=null;
	private NetworkResponseListener listener;

	static {
		System.loadLibrary("iconv");
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
//		hideLoadingView();
//		String msg = listener.getErrorMessage();
//		showToast(msg);
//		signImage.setImageResource(R.mipmap.sign_fail);
//		signText.setText("打卡失败");
	}

	public void onCreate(Bundle savedInstanceState) {
		listener = new NetworkResponseListener(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_capture, R.string.scan, R.drawable.ic_back, "", false);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		findViewById();
        try {
            getLocalAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		addEvents();
		initViews();
	}

	private void findViewById() {
		scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
		scanResult = (TextView) findViewById(R.id.signText);
        signLin =(LinearLayout) findViewById(R.id.signLin);
		  signImage = (ImageView) findViewById(R.id.signImage) ;
		  signText =(TextView) findViewById(R.id.signText);
		  refreshLocal =(TextView) findViewById(R.id.refreshLocal);
		  resetSignIn  =(TextView) findViewById(R.id.resetSignIn);;
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		address  =(TextView) findViewById(R.id.address);

	}

//	private void addEvents() {
//		scanRestart.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				if (barcodeScanned) {
//					barcodeScanned = false;
//					scanResult.setText("Scanning...");
//					mCamera.setPreviewCallback(previewCb);
//					mCamera.startPreview();
//					previewing = true;
//					mCamera.autoFocus(autoFocusCB);
//				}
//			}
//		});
//	}

	private void initViews() {
		mImageScanner = new ImageScanner();
		mImageScanner.setConfig(0, Config.X_DENSITY, 3);
		mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

		autoFocusHandler = new Handler();
		mCameraManager = new CameraManager(this);
		try {
			mCameraManager.openDriver();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mCamera = mCameraManager.getCamera();
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);

		scanPreview.addView(mPreview);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.85f);
		animation.setDuration(3000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.REVERSE);
		scanLine.startAnimation(animation);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = camera.getParameters().getPreviewSize();

			// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < size.height; y++) {
				for (int x = 0; x < size.width; x++)
					rotatedData[x * size.height + size.height - y - 1] = data[x
							+ y * size.width];
			}

			// 宽高也要调整
			int tmp = size.width;
			size.width = size.height;
			size.height = tmp;

			initCrop();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(rotatedData);
			barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
					mCropRect.height());

			int result = mImageScanner.scanImage(barcode);
			String resultStr = null;

			if (result != 0) {
				SymbolSet syms = mImageScanner.getResults();
				for (Symbol sym : syms) {
					resultStr = sym.getData();
				}
			}

			if (!TextUtils.isEmpty(resultStr)) {
				Log.d("capture",resultStr);
				previewing = false;
				mCamera.setPreviewCallback(null);
				try {
					String code = resultStr.split(",")[0];
					int r = Integer.valueOf(code);
					Log.d("capture",code);
					scanContainer.setVisibility(View.GONE);
					mCamera.stopPreview();
					scanPreview.setVisibility(View.GONE);
					mPreview.setVisibility(View.GONE);
					signLin.setVisibility(View.VISIBLE);
					UserAction.signIn(SubmeterApp.getInstance().getUserToken(),r,lat,lng,listener);
				} catch (Exception e){
					Log.d("capture",e.getMessage());
					if (barcodeScanned) {
						barcodeScanned = false;
						scanResult.setText("Scanning...");
						mCamera.setPreviewCallback(previewCb);
						mCamera.startPreview();
						previewing = true;
						mCamera.autoFocus(autoFocusCB);
					}

				}


				barcodeScanned = true;
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = mCameraManager.getCameraResolution().y;
		int cameraHeight = mCameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeightdd();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeightdd() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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
