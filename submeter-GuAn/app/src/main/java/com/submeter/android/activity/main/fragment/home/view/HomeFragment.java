package com.submeter.android.activity.main.fragment.home.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.weather.LocalWeatherLive;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.submeter.android.R;
import com.submeter.android.SubmeterApp;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.cityPie.view.CityPieActivity;
import com.submeter.android.activity.companyList.view.CompanyListActivity;
import com.submeter.android.activity.handlingProblem.view.HandlingProblemActivity;
import com.submeter.android.activity.main.fragment.home.presenter.HomePresenter;
import com.submeter.android.activity.main.fragment.home.presenter.IHomePresenter;
import com.submeter.android.activity.main.view.MainActivity;
import com.submeter.android.activity.messageDetail.view.MessageDetailActivity;
import com.submeter.android.activity.scan.ScanActivity;
import com.submeter.android.adapter.HomeMenuAdapter;
import com.submeter.android.entity.HomeData;
import com.submeter.android.entity.HomeMenu;
import com.submeter.android.entity.Notice;
import com.submeter.android.entity.Weather;
import com.submeter.android.interfacce.IChildFragment;
import com.submeter.android.view.OnRefreshListener;
import com.submeter.android.view.SpaceGridVIewItemDecoration;
import com.submeter.android.view.UPMarqueeView;
import com.zbar.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yangzhao on 2019/3/27.
 */

public class HomeFragment extends Fragment implements IChildFragment, IHomeView<HomeData>, OnRefreshListener {

    @BindView(R.id.home_title)
    TextView homeTitle;
    @BindView(R.id.news)
    UPMarqueeView news;
    @BindView(R.id.more_invitation_view)
    TextView moreInvitationView;
    @BindView(R.id.weatherType)
    TextView weatherType;
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.pm)
    TextView pm;
    @BindView(R.id.companyNum)
    TextView companyNum;
    @BindView(R.id.humid)
    TextView humid;
    @BindView(R.id.weatherBg)
    LinearLayout weatherBg;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.companyList)
    LinearLayout companyList;
    private MainActivity parentActivity;

    private View contentView;
    private Unbinder unbinder;
    private boolean isFirst = true;
    private boolean statusDarkMode = false;
    private LocalWeatherLive weatherlive;

    private IHomePresenter presenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("HomeFragment----", "onCreate");
        parentActivity = (MainActivity) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.activity_homenew, container, false);
            unbinder = ButterKnife.bind(this, contentView);

            initView();
        } else {
            ViewGroup p = (ViewGroup) contentView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
            unbinder = ButterKnife.bind(this, contentView);

        }
        Log.i("HomeFragment----", "onCreateView");

        return contentView;
    }

    private void updateScroll(List<Notice> list) {
        ArrayList<View> itemViews = new ArrayList<>();
        View itemView;
        for (Notice notice : list) {
            itemView = LayoutInflater.from(parentActivity).inflate(R.layout.item_home_news, null);
            itemView.setTag(notice);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(parentActivity, MessageDetailActivity.class);
                intent.putExtra("title", notice.getTitle());
                intent.putExtra("time", notice.getCreateTime());
                intent.putExtra("content", notice.getContent());
                startActivity(intent);
            });
            itemView.findViewById(R.id.new_flag).setVisibility("1".equalsIgnoreCase(notice.getState()) ? View.VISIBLE : View.GONE);
            if (notice.getContent()!=null){
                ((TextView) itemView.findViewById(R.id.news)).setText(Html.fromHtml(notice.getContent()));
            }else {
                ((TextView) itemView.findViewById(R.id.news)).setText(notice.getContent());
            }
            itemViews.add(itemView);
        }
        news.setViews(itemViews);
    }

    @Override
    public BaseActivity getBaseActivity() {
        return parentActivity;
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showToast(String toast) {
    }

    @Override
    public void updateView(HomeData data) {
        Log.i("updateView--","updateView");
        srlRefresh.finishRefresh();
        int c = data.getCompanyNum();
        companyNum.setText(c+"");

        List<Notice> notices =  data.getNotices();
        if (notices!=null){
            updateScroll(notices);
        }
    }

    @Override
    public void updateWeather(HomeData data) {
        Log.i("updateView--","updateView");
        srlRefresh.finishRefresh();
        Weather weather = data.getWeather();
        if (weather != null) {
            String type = weather.getWeatherType();
            if (type.contains("雨")) {
                weatherBg.setBackgroundResource(R.mipmap.rainy);
            } else if (type.contains("晴")) {
                weatherBg.setBackgroundResource(R.mipmap.fine);
            } else {
                weatherBg.setBackgroundResource(R.mipmap.cloudy);
            }
            date.setText(weather.getDate());
            temp.setText(weather.getTemp());
            weatherType.setText(weather.getWeatherType());
            homeTitle.setText(weather.getCity());
            pm.setText(weather.getPm());
            humid.setText(weather.getAlert()+"%");
        }
    }

    @Override
    public void loginStatusChanged(boolean loginStatus) {

    }

    @Override
    public void showCurrentLocation(String location) {

    }

    @Override
    public void onRefresh() {
        presenter.loadData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public boolean isRefresh() {
        return false;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void pageShow() {
        if (isFirst) {
            initView();
        }
        isFirst = false;
    }

    private void initView() {
        homeTitle.setFocusable(true);
        homeTitle.setFocusableInTouchMode(true);
        homeTitle.requestFocus();

        presenter = new HomePresenter(this);
        presenter.loadData();

        srlRefresh.setOnRefreshListener(refreshLayout -> onRefresh());
        initMenuData();
    }

    private void initMenuData() {
        List<HomeMenu> list = new ArrayList<>();
        HomeMenu menu;
        menu = new HomeMenu();
        menu.setMenuName("企业运行图");
        menu.setImageUrl(R.mipmap.business_icon);
        list.add(menu);
        menu = new HomeMenu();
        menu.setMenuName("历史违规企业");
        menu.setImageUrl(R.mipmap.outcompany);
        list.add(menu);
        menu = new HomeMenu();
        menu.setMenuName("今日违规企业");
        menu.setImageUrl(R.mipmap.untreated);
        list.add(menu);
        menu = new HomeMenu();
        menu.setMenuName("扫一扫");
        menu.setImageUrl(R.mipmap.scan_icon);
        list.add(menu);
        menu = new HomeMenu();
        menu.setMenuName("企业列表");
        menu.setImageUrl(R.mipmap.companylist);
        list.add(menu);

        if (SubmeterApp.getInstance().getUserType().equals("2")){
            menu = new HomeMenu();
            menu.setMenuName("乡镇维度统计");
            menu.setImageUrl(R.mipmap.tongji);
            list.add(menu);
        }
        GridLayoutManager manager = new GridLayoutManager(parentActivity,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new SpaceGridVIewItemDecoration(2));
        HomeMenuAdapter adapter = new HomeMenuAdapter(parentActivity,list);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener((view, pos) -> {
            HomeMenu homeMenu = list.get(pos);
            String menuName = homeMenu.getMenuName();
            if (menuName.equals("企业运行图")){
                parentActivity.goMonitorFragment();
            }else if (menuName.equals("历史违规企业")){
                Intent intent = new Intent(getBaseActivity(), HandlingProblemActivity.class);
                intent.putExtra("currIndex", 0);
                startActivity(intent);
            }else if (menuName.equals("今日违规企业")){
                Intent intent1 = new Intent(getBaseActivity(), HandlingProblemActivity.class);
                intent1.putExtra("currIndex", 1);
                startActivity(intent1);
            }else if (menuName.equals("扫一扫")){
                //sdk大于23的。动态权限自动获取
                Intent intent;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            ||ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            ) {
                        requestSetPermissions();
                    } else {
                        intent = new Intent(parentActivity, CaptureActivity.class);
                        startActivity(intent);
                    }
                }else {
                    intent = new Intent(parentActivity, CaptureActivity.class);
                    startActivity(intent);
                }
            }else if (menuName.equals("企业列表")){
                Intent intent2 = new Intent(getBaseActivity(), CompanyListActivity.class);
                startActivity(intent2);
            }else if (menuName.equals("乡镇维度统计")){
                Intent intent3 = new Intent(getBaseActivity(), CityPieActivity.class);
                startActivity(intent3);
            }
        });
    }

    @Override
    public void pageHide() {

    }

    @Override
    public boolean getStatusbarDardMode() {
        return statusDarkMode;
    }

    @OnClick({R.id.more_invitation_view,R.id.companyList})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_invitation_view:
                parentActivity.goMessageFragment();

//                startActivity(new Intent(getBaseActivity(), NoticeActivity.class));
                break;
            case R.id.companyList:
                Intent intent2 = new Intent(getBaseActivity(), CompanyListActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CONTACTS = 1000;

    private void requestSetPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(parentActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(parentActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(parentActivity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(parentActivity, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        } else {
            ActivityCompat.requestPermissions(parentActivity, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CONTACTS) {
            Intent intent = new Intent(parentActivity, ScanActivity.class);
            startActivity(intent);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
