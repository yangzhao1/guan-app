package com.submeter.android.activity.main.fragment.monitor.fragment.all.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.android.volley.VolleyError;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.main.fragment.monitor.fragment.all.presenter.AllPresenter;
import com.submeter.android.activity.main.fragment.monitor.fragment.all.presenter.IAllPresenter;
import com.submeter.android.adapter.PopCompanyListAdapter;
import com.submeter.android.adapter.SpaceItemDecoration;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.Company;
import com.submeter.android.entity.Monitor;
import com.submeter.android.entity.MonitorData;
import com.submeter.android.interfacce.IChildMFragment;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.network.action.MonitorAction;
import com.submeter.android.pop.CommonPopupWindow;
import com.submeter.android.tools.ShareUtil;
import com.submeter.android.tools.SharedPreferencesUtils;
import com.submeter.android.tools.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AllFragment extends Fragment implements IChildMFragment, IAllView,
        AMap.OnMarkerClickListener,INetworkResponseListener,AMap.InfoWindowAdapter,AMap.OnMapClickListener {

    public static final String TAG = AllFragment.class.getSimpleName();
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.normal)
    TextView normal;
    @BindView(R.id.abnormal)
    TextView abnormal;
    @BindView(R.id.stoppage)
    TextView stoppage;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.allC)
    TextView allC;
    private boolean isFirst = true;

    private boolean isHidden = true;

    private View contentView;

    private BaseActivity parentActivity;

    private Unbinder mainUnbinder;

    private AMap aMap;

    private IAllPresenter presenter;
    private ArrayList<MarkerOptions> markerOptionsList = null;
    private List<Company> companyList = null;
    private String isHandle="";

    private NetworkResponseListener networkResponseListener;
    private String allNum;
    private String zcNum;
    private String gwNum;
    private String yjNum;
    private Marker marker = null;
    private PopCompanyListAdapter adapter = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = (BaseActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = Utils.inflateView(parentActivity, inflater, R.layout.activity_monitor_all, container, false);
            mainUnbinder = ButterKnife.bind(this, contentView);
            initView();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
            mainUnbinder = ButterKnife.bind(this, contentView);
            //????????????,????????????
            if (markerOptionsList==null){
                initView();
            }
        }

        mapView.onCreate(savedInstanceState);// ?????????????????????
        return contentView;
    }

    private void initView() {
        presenter = new AllPresenter(this);
        init();
        networkResponseListener = new NetworkResponseListener(this);
        MonitorAction.getNum(SubmeterApp.getInstance().getUserToken(),networkResponseListener);
    }

    /**
     * ?????????AMap??????
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            presenter.loadData("",aMap,isHandle);
            aMap.setOnMarkerClickListener(this);// ????????????marker???????????????
            aMap.setInfoWindowAdapter(this);
            aMap.setOnMapClickListener(this);
        }else {
            presenter.loadData("",aMap,isHandle);
        }
    }

    @Override
    public void setNum(String all, String zc, String wg){

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    public void pageShow() {
        if (!isHidden) {
            return;
        }

        isHidden = false;
        if (isFirst) {
//            initView();
        }
        isFirst = false;
    }

    public void pageHide() {
        if (isHidden) {
            return;
        }

        isHidden = true;
    }

    @Override
    public boolean getStatusbarDardMode() {
        return false;
    }

    @Override
    public void updateView(Monitor monitor) {
        MonitorData monitor1 = monitor.getData();
        companyList = monitor.getCompanies();
        markerOptionsList = monitor.getMarkerOptionsList();

        if (markerOptionsList.size() != 0) {
            aMap.addMarkers(markerOptionsList, true);
        }else {
//            LatLng latLng = new LatLng(Double.parseDouble(getString(R.string.lat)),
//                    Double.parseDouble(getString(R.string.lon)));
            moveToCamera(0,SystemConstant.LOCAL_YONGQINGXIAN,SystemConstant.ZOOM_LEVEL_4);
        }
    }

    @Override
    public void updatePopData(Monitor monitor) {
//        MonitorData monitor1 = monitor.getData();
//        companyList = monitor.getCompanies();
        ArrayList<MarkerOptions> markerOptionsList = monitor.getMarkerOptionsList();
        showCompanyListPOP(true,markerOptionsList);
//        if (markerOptionsList.size() != 0) {
//            aMap.addMarkers(markerOptionsList, true);
//        }else {
//            LatLng latLng = new LatLng(Double.parseDouble(getString(R.string.lat)),
//                    Double.parseDouble(getString(R.string.lon)));
//            moveToCamera(0,SystemConstant.LOCAL_YONGQINGXIAN,SystemConstant.ZOOM_LEVEL_4);
//        }
    }

    /**
     * onDestroyView?????????????????????
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mainUnbinder) {
            mainUnbinder.unbind();
        }
        /*if(null != headUnbinder) {
            headUnbinder.unbind();
        }*/
    }

    @Override
    public boolean onMarkerClick(Marker markers) {
        if (aMap != null) {
            marker = markers;
            markers.showInfoWindow();
            int pos = markers.getPeriod()-1;
            if (pos<=0){
                pos=0;
            }
            Company company = companyList.get(pos);
            ShareUtil.showInfoPOP(parentActivity, rel, markers,company);
        }
        return true;
    }

    @OnClick(R.id.companyName)
    public void onViewClicked() {
        if (markerOptionsList!=null){
            showCompanyListPOP(false,markerOptionsList);
        }
    }

    private CommonPopupWindow commonPopupWindow;
    private View viewpop;
    private EditText title_search;
    private ImageView left_image;
    private RecyclerView recyclerView;
    /**
     * ??????
     */
    private void showCompanyListPOP(boolean isRefresh,ArrayList<MarkerOptions> markerOptionsList) {
        if (!isRefresh){
            viewpop = LayoutInflater.from(getContext()).inflate(R.layout.pop_companylist, null);
            recyclerView = (RecyclerView) viewpop.findViewById(R.id.recycler);
            title_search = (EditText) viewpop.findViewById(R.id.title_search);
            left_image = (ImageView) viewpop.findViewById(R.id.left_image);
        }
        left_image.setOnClickListener(v -> {
            commonPopupWindow.dismiss();
        });
        title_search.setOnTouchListener((v, event) -> {
            // et.getCompoundDrawables()?????????????????????4????????????????????????????????????????????????
            Drawable drawable = title_search.getCompoundDrawables()[2];
            //???????????????????????????????????????
            if (drawable == null)
                return false;
            //???????????????????????????????????????
            if (event.getAction() != MotionEvent.ACTION_UP)
                return false;
            if (event.getX() > title_search.getWidth()
                    - title_search.getPaddingRight()
                    - drawable.getIntrinsicWidth()){
                title_search.setText("");
            }
//            titleSearch.setText("");
            return false;
        });
        title_search.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = title_search.getEditableText().toString().trim();
//                homeSearchPresenter.goSearch(keyword,ALL_VIEW);
                presenter.loadData(keyword,aMap,isHandle);
            }
            return false;
        });
//        WindowManager manager1 = getBaseActivity().getWindowManager();
//        DisplayMetrics metrics = new DisplayMetrics();
//        manager1.getDefaultDisplay().getMetrics(metrics);
//        int widthPixels = metrics.widthPixels;
        if (markerOptionsList.size()!=0){
            LinearLayoutManager manager = new LinearLayoutManager(parentActivity);
            recyclerView.setLayoutManager(manager);
            recyclerView.addItemDecoration(new SpaceItemDecoration(1));
            adapter = new PopCompanyListAdapter(parentActivity,markerOptionsList);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(((view1, pos) -> {
                MarkerOptions markerOptions = markerOptionsList.get(pos);
                companyName.setText(markerOptions.getTitle());
                if (markerOptions.getPosition()==null){
                    getBaseActivity().showToast("????????????????????????,??????????????????");
                }else {
//                    Marker markers  = aMap.getMapScreenMarkers().get(pos);
//                    markers.showInfoWindow();
////                    int pos = markers.getPeriod()-1;
//                    if (pos<=0){
//                        pos=0;
//                    }
//                    Company company = companyList.get(pos);
//                    ShareUtil.showInfoPOP(parentActivity, rel, markers,company);
                    moveToCamera(pos,markerOptions.getPosition(),SystemConstant.ZOOM_LEVEL_2);
                }
                commonPopupWindow.dismiss();
            }));

            if (!isRefresh){
                commonPopupWindow = new CommonPopupWindow.Builder(getContext())
                        .setView(viewpop)
                        .setAnimationStyle(R.style.bottomShowAnimation)
                        .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        .setOutsideTouchable(true)
                        .create();

                commonPopupWindow.showAtLocation(rel, Gravity.BOTTOM, 0, 0);
            }
        }
    }

    private void moveToCamera(int pos,LatLng latLng,float level) {

        changeCamera(
                aMap, CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(latLng,level, 0, 0)
                        ),null);
    }

    /**
     * ???????????????????????????????????????animateCamera???moveCamera?????????????????????
     */
    private void changeCamera(AMap aMap, CameraUpdate update, AMap.CancelableCallback callback) {
//        boolean animated = ((CompoundButton) findViewById(R.id.animate))
//                .isChecked();
//        if (animated) {
//            aMap.animateCamera(update, 1000, callback);
//        } else {
        aMap.moveCamera(update);
//        }
    }

    @Override
    public void onResponse(String result) {
        if (!networkResponseListener.handleInnerError(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                allNum = jsonObject.getJSONObject("data").getString("allNum");
                zcNum = jsonObject.getJSONObject("data").getString("zcNum");
                gwNum = jsonObject.getJSONObject("data").getString("wgNum");
                yjNum = jsonObject.getJSONObject("data").getString("yjNum");

                allC.setText(getString(R.string.label_all) + " "+allNum);
                normal.setText(getString(R.string.label_normal) +" "+zcNum);
                abnormal.setText(getString(R.string.label_abnormal) +" "+yjNum);
                stoppage.setText(getString(R.string.label_stop) +" "+gwNum);

                SharedPreferencesUtils.setParam(parentActivity,"allNum",allNum);
                SharedPreferencesUtils.setParam(parentActivity,"zcNum",zcNum);
                SharedPreferencesUtils.setParam(parentActivity,"yjNum",yjNum);
                SharedPreferencesUtils.setParam(parentActivity,"gwNum",gwNum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String msg = networkResponseListener.getErrorMessage();
        parentActivity.showToast(msg);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getBaseActivity().getLayoutInflater().inflate(
                R.layout.amap_infowindow, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * ?????????infowinfow????????????????????????infoWindow???Marker????????????
     */
    public void render(Marker marker, View view) {
        String title = marker.getTitle();

        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (TextUtils.isEmpty(title)){
            titleUi.setText("??????");
        }else {
            titleUi.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        snippetUi.setText(snippet);
        if (TextUtils.isEmpty(snippet)){
            snippetUi.setText("??????");
        }else {
            snippetUi.setText(snippet);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (marker!=null&&marker.isInfoWindowShown()){
            marker.hideInfoWindow();
        }
    }
}