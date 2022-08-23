package com.submeter.android.tools;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Marker;
import com.submeter.android.SubmeterApp;
import com.submeter.android.R;
import com.submeter.android.activity.companyInfo.view.CompanyInfoActivity;
import com.submeter.android.entity.Company;
import com.submeter.android.pop.CommonPopupWindow;


public class ShareUtil {
    private static ShareUtil _instance = null;

    private ShareUtil() {
    }

    public static ShareUtil getInstance() {
        if(null == _instance) {
            _instance = new ShareUtil();

            Context context = SubmeterApp.getInstance();
//            ShareSDK.closeDebug();
//            MobSDK.init(context);
        }

        return _instance;
    }

    private static CommonPopupWindow commonPopupWindow;
    private static CommonPopupWindow commonPopupWindow1;
    /**
     * 弹出
     */
    public static void showInfoPOP(Context context, View rel, Marker marker, Company company) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_pop, null);
        TextView textInfo = (TextView) view.findViewById(R.id.textInfo);
        TextView navigation = (TextView) view.findViewById(R.id.navigation);
        TextView companyName = (TextView) view.findViewById(R.id.companyName);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView serialNumber = (TextView) view.findViewById(R.id.serialNumber);
        TextView linkman = (TextView) view.findViewById(R.id.linkman);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        TextView warnLevel = (TextView) view.findViewById(R.id.warnLevel);
        TextView companyLevel = (TextView) view.findViewById(R.id.companyLevel);
        TextView type = (TextView) view.findViewById(R.id.type);

        String companyNameS = company.getName();
        if (companyNameS==null){
            companyNameS="暂无";
        }
        String addressS = company.getAddress();
        if (addressS==null){
            addressS="暂无";
        }
        String serialNumberS = company.getNumber();
        if (serialNumberS==null){
            serialNumberS="暂无";
        }
        String linkmanS = company.getPerson();
        if (linkmanS==null){
            linkmanS="暂无";
        }
        String phoneS = company.getPhone();
        if (phoneS==null){
            phoneS="暂无";
        }
        String warnLevelS = company.getControlLevel();
        if (warnLevelS==null){
            warnLevelS="暂无";
        }
        String companyLevelS = company.getEnterpriseLevel();
        if (companyLevelS==null){
            companyLevelS="暂无";
        }
        String typeS = company.getIndustryNav();
        if (typeS==null){
            typeS="暂无";
        }

        companyName.setText(companyNameS);
        address.setText(addressS);

        serialNumber.setText(context.getString(R.string.company_num) +serialNumberS);
        linkman.setText(context.getString(R.string.linkman)+linkmanS);
        phone.setText(context.getString(R.string.phone) + phoneS);

        warnLevel.setText(context.getString(R.string.response_level) + warnLevelS);
        companyLevel.setText(context.getString(R.string.company_level) + companyLevelS);
        type.setText(context.getString(R.string.business_type) + typeS);

        textInfo.setOnClickListener((view1) -> {
                Intent intent = new Intent(context, CompanyInfoActivity.class);
                intent.putExtra("company",company);
                context.startActivity(intent);
                commonPopupWindow.dismiss();
        });
        navigation.setOnClickListener((view1) -> {
            showSelectMapPOP(context,rel,marker.getPosition().latitude,
                    marker.getPosition().longitude,marker.getTitle());
            commonPopupWindow.dismiss();
        });

        commonPopupWindow = new CommonPopupWindow.Builder(context)
                .setView(view)
                .setAnimationStyle(R.style.bottomShowAnimation)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)
                .create();

        commonPopupWindow.showAtLocation(rel, Gravity.BOTTOM,0,0);
    }

    /**
     * 弹出
     */
    public static void showSelectMapPOP(Context context, View rel, double lat,double lng,String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_selectmap, null);
        TextView gaode = (TextView) view.findViewById(R.id.gaoDeText);
        TextView baidu = (TextView) view.findViewById(R.id.baiDuText);
        TextView tengxun = (TextView) view.findViewById(R.id.tengxunText);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView text_pop = (TextView) view.findViewById(R.id.text_pop);
        gaode.setOnClickListener((view1) -> {
            if (MapUtil.isGdMapInstalled()) {
                MapUtil.openGaoDeNavi(context, 0, 0, null, lat, lng, title);
            } else {
                Toast.makeText(context, String.format(context.getString(R.string.gaode_uninstall)), Toast.LENGTH_SHORT).show();
            }
        });

        baidu.setOnClickListener((view1) -> {
            if (MapUtil.isBaiduMapInstalled()) {
                MapUtil.openBaiDuNavi(context, 0, 0, null,
                        lat, lng, title);
            } else {
                Toast.makeText(context, String.format(context.getString(R.string.baidu_uninstall)), Toast.LENGTH_SHORT).show();
            }
        });

        tengxun.setOnClickListener((view1) -> {
            if (MapUtil.isTencentMapInstalled()) {
                MapUtil.openTencentMap(context, 0, 0, null,
                        lat, lng, title);
            } else {
                Toast.makeText(context,String.format(context.getString(R.string.tengxun_uninstall)), Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener((view1) -> {
            commonPopupWindow1.dismiss();
        });
        text_pop.setOnClickListener((view1) -> {
            commonPopupWindow1.dismiss();
        });

        commonPopupWindow1 = new CommonPopupWindow.Builder(context)
                .setView(view)
                .setAnimationStyle(R.style.alphaShowAnimation)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setOutsideTouchable(true)
                .create();

        commonPopupWindow1.showAtLocation(rel, Gravity.BOTTOM,0,0);
    }
}