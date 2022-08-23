package com.submeter.android.activity.cityPie.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.cityPie.presenter.CityPiePresenter;
import com.submeter.android.activity.cityPie.presenter.ICityPiePresenter;
import com.submeter.android.adapter.CityPieAdapter;
import com.submeter.android.entity.CityPieData;
import com.teaanddogdog.mpandroidchartutil.PieChartFixCover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangzhao on 2019/6/15.
 */

public class CityPieActivity extends BaseActivity implements OnChartValueSelectedListener,ICityPieView {

    @BindView(R.id.pieChartEnt)
    PieChartFixCover pieChartEnt;
    @BindView(R.id.pieChartMeter)
    PieChartFixCover pieChartMeter;
    @BindView(R.id.pieChartViolation)
    PieChartFixCover pieChartViolation;
    @BindView(R.id.pieChartViolationMonth)
    PieChartFixCover pieChartViolationMonth;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private int colorsSize=5;
    private List<Map> listMap;
//    @BindView(R.id.pieLin)
//    LinearLayout pieLin;

    private ICityPiePresenter presenter;
    private int compamyNums=0;
    private int compamyMeter=0;
    private int compamyViolation=0;
    private int compamyViolationMonth=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citypie, R.string.city_statistics, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);

        presenter = new CityPiePresenter(this);
        presenter.loadData("");
    }

    private void initView(PieChart mPieChart,String text) {
//饼状图
        mPieChart.setUsePercentValues(true);//设置value是否用显示百分数,默认为false
        mPieChart.getDescription().setEnabled(false);//设置描述
        mPieChart.setExtraOffsets(5, 10, 5, 5);//设置饼状图距离上下左右的偏移量
        mPieChart.setDragDecelerationFrictionCoef(0.95f);//设置阻尼系数,范围在[0,1]之间,越小饼状图转动越困难
//设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText(text));
// mPieChart.setCenterText("总学生数:100人");
        mPieChart.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
        mPieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        mPieChart.setTransparentCircleColor(Color.WHITE);//设置圆环的颜色
        mPieChart.setTransparentCircleAlpha(110);//设置圆环的透明度[0,255]
        mPieChart.setHoleRadius(40f);//饼状图中间的圆的半径大小
        mPieChart.setTransparentCircleRadius(40f);//设置圆环的半径值
        mPieChart.setDrawCenterText(true);//是否绘制中间的文字
        mPieChart.setRotationAngle(0);//设置饼状图旋转的角度
// 触摸旋转
        mPieChart.setRotationEnabled(false);//设置饼状图是否可以旋转(默认为true)
//        mPieChart.setHighlightPerTapEnabled(true);//设置旋转的时候点中的tab是否高亮(默认为true)
//变化监听
        mPieChart.setOnChartValueSelectedListener(this);
    }
    private void initData(PieChart mPieChart,ArrayList<PieEntry> entries) {
//模拟数据
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//        entries.add(new PieEntry(40, "1级乡镇"));
//        entries.add(new PieEntry(20, "2级乡镇"));
//        entries.add(new PieEntry(30, "3级乡镇"));
//        entries.add(new PieEntry(10, "4级乡镇"));
//设置数据
        setData(mPieChart,entries);
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//设置每个tab的显示位置
        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);//设置tab之间Y轴方向上的空白间距值
//        l.setYOffset(0f);
// 输入标签样式
        mPieChart.setDrawEntryLabels(false);//设置是否绘制Label
//        mPieChart.setEntryLabelColor(Color.WHITE);//设置绘制Label的颜色
//        mPieChart.setEntryLabelTextSize(12f);//设置绘制Label的字体大小
    }
    //设置中间文字
    private SpannableString generateCenterSpannableText(String text) {
//原文：MPAndroidChart\ndeveloped by Philipp Jahoda
        SpannableString s = new SpannableString(text);
        s.setSpan(new RelativeSizeSpan(1.0f), 0, text.length(), 0);
// s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
// s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
// s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
// s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
// s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
    //设置数据
    private void setData(PieChart mPieChart,ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//        colors.add(ColorTemplate.getHoloBlue());
        for (int i = 0; i < listMap.size() ; i++) {
            colors.add((int) listMap.get(i).get("color"));
        }
//        colors.add(getResources().getColor(R.color.blue_color));
//        colors.add(getResources().getColor(R.color.reds_color));
//        colors.add(getResources().getColor(R.color.greensss_color));
//        colors.add(getResources().getColor(R.color.greenss_color));
//        colors.add(getResources().getColor(R.color.green_color));
//        colors.add(getResources().getColor(R.color.greens_color));
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
//刷新
        mPieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }
    @Override
    public void onNothingSelected() {
    }

    private List<String> pieNames = new ArrayList<>();
    private ArrayList<PieEntry> entries1 ;
    private ArrayList<PieEntry> entries2 ;
    private ArrayList<PieEntry> entries3 ;
    private ArrayList<PieEntry> entries4 ;

    @Override
    public void updateView(List<CityPieData.DataBean> data) {

        if (data!=null&&data.size()!=0){
            entries1 = new ArrayList();
            entries2 = new ArrayList();
            entries3 = new ArrayList();
            entries4 = new ArrayList();

//            colorsSize = data.size();
            listMap = new ArrayList<>();
            for (CityPieData.DataBean bean:data) {
                pieNames.add(bean.getTownName());
                compamyNums+=bean.getEntTotal();
                compamyMeter+=bean.getMeterTotal();
                compamyViolationMonth+=bean.getViolationMonthTotal();
                compamyViolation+=bean.getViolationTotal();
                entries1.add(new PieEntry(bean.getEntTotal(), bean.getTownName()));
                entries2.add(new PieEntry(bean.getMeterTotal(), bean.getTownName()));
                entries3.add(new PieEntry(bean.getViolationMonthTotal(), bean.getTownName()));
                entries4.add(new PieEntry(bean.getViolationTotal(), bean.getTownName()));
                Map map = new HashMap();
                map.put("name",bean.getTownName());
                int r = new Random().nextInt(255);
                int g = new Random().nextInt(255);
                int b = new Random().nextInt(255);
                map.put("color",Color.rgb(r, g, b));
                listMap.add(map);
            }

            if (compamyNums<1){
                pieChartEnt.setVisibility(View.GONE);
            }
            if (compamyMeter<1){
                pieChartMeter.setVisibility(View.GONE);
            }
            if (compamyViolationMonth<1){
                pieChartViolationMonth.setVisibility(View.GONE);
            }
            if (compamyViolation<1){
                pieChartViolation.setVisibility(View.GONE);
            }

            initView(pieChartEnt,"企业总数\n" + compamyNums);
            initData(pieChartEnt,entries1);
            initView(pieChartMeter,"电表总数\n"+compamyMeter);
            initData(pieChartMeter,entries2);
            initView(pieChartViolationMonth,"本月违规数量\n"+compamyViolationMonth);
            initData(pieChartViolationMonth,entries3);
            initView(pieChartViolation,"总违规数量\n"+compamyViolation);
            initData(pieChartViolation,entries4);

            initDataAdapter(listMap);
        }
    }

    private void initDataAdapter(List<Map> list) {
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recycler.setLayoutManager(manager);
        CityPieAdapter adapter = new CityPieAdapter(this,list);
        recycler.setAdapter(adapter);
    }
}
