package com.submeter.android.activity.historyData.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.historyData.presenter.HistoryDataPresenter;
import com.submeter.android.activity.historyData.presenter.IHistoryDataPresenter;
import com.submeter.android.entity.Company;
import com.submeter.android.entity.Power;
import com.submeter.android.entity.Power.DataBean;
import com.submeter.android.entity.Power.DataBean.CoulometryDtosBean;
import com.submeter.android.entity.Power.DataBean.CoulometryDtosBean.EleDataDtosBean;
import com.submeter.android.tools.DynamicLineChartManager;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangzhao on 2019/3/28.
 */

public class HistoryDataActivity extends BaseActivity implements IHistoryDataView {

    @BindView(R.id.selectTime)
    TextView selectTime;
    @BindView(R.id.companyName)
    TextView companyName;
    //    @BindView(R.id.navigation)
//    TextView navigation;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.companyCode)
    TextView companyCode;
    @BindView(R.id.linkman)
    TextView linkman;
    @BindView(R.id.phone)
    TextView phone;
    //    @BindView(R.id.info_lin)
//    LinearLayout infoLin;
//    @BindView(R.id.responseLevel)
//    TextView responseLevel;
//    @BindView(R.id.companyLevel)
//    TextView companyLevel;
//    @BindView(R.id.businessType)
//    TextView businessType;
    @BindView(R.id.score)
    TextView score;
    //    @BindView(R.id.right_text)
//    TextView right_text;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.chartLin)
    LinearLayout chartLin;
    @BindView(R.id.chart)
    LineChart lineChart;
    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.spinner_type_day)
    Spinner spinnerTypeDay;
    @BindView(R.id.spinner_stateType_day)
    Spinner spinnerStateTypeDay;
    @BindView(R.id.chartDay)
    LineChart chartDay;
    @BindView(R.id.day_du)
    LinearLayout dayDu;

    //    private double lat = 1.1;
//    private double lng = 2.1;
    private String addressS;

    private IHistoryDataPresenter presenter;
    private int height = 0;
    private TimePickerView pvTime;
    private int dayType=-1;
    private int dayStateType=1;
    private int ids;
    private String currDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historydata, R.string.history, R.drawable.ic_back, "", false);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        initTimePicker();
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        height = width * 3 / 5;

        Company company = getIntent().getParcelableExtra("company");

        companyName.setText(company.getName() == null ? "??????" : company.getName());
        address.setText(company.getAddress() == null ? "??????" : company.getAddress());
        addressS = company.getAddress();
//        if (company.getLat()!=null){
//            lat = Double.parseDouble(company.getLat());
//            lng = Double.parseDouble(company.getLon());
//        }
        String numberS = company.getNumber();
        numberS = numberS == null ? "??????" : numberS;
        String personS = company.getPerson();
        personS = personS == null ? "??????" : personS;
        String phoneS = company.getPhone();
        phoneS = phoneS == null ? "??????" : phoneS;
        int scoreI = company.getScore();
        String scoreS = "???";
        if (scoreI >= 90) {
            scoreS = "???";
        } else if ((90 > scoreI) && (scoreI >= 80)) {
            scoreS = "???";
        } else if ((80 > scoreI) && (scoreI > 60)) {
            scoreS = "??????";
        } else if (60 > scoreI) {
            scoreS = "???";
        }
//        String controlLevelS = company.getControlLevel();
//        controlLevelS = controlLevelS==null?"??????":controlLevelS;
//        String enterpriseLevelS = company.getEnterpriseLevel();
//        enterpriseLevelS = enterpriseLevelS==null?"??????":enterpriseLevelS;
//        String allowEiaS = company.getIndustryNav();
//        allowEiaS = allowEiaS==null?"??????":allowEiaS;
        companyCode.setText(getString(R.string.company_num) + numberS);
        linkman.setText(getString(R.string.linkman) + personS);
        phone.setText(getString(R.string.phone) + phoneS);
        score.setText(getString(R.string.score) + scoreS);

//        responseLevel.setText(getString(R.string.response_level) + controlLevelS);
//        companyLevel.setText(getString(R.string.company_level) + enterpriseLevelS);
//        businessType.setText(getString(R.string.business_type) + allowEiaS);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //??????????????????
        Date date = new Date(System.currentTimeMillis());
        currDate = simpleDateFormat.format(date);
        presenter = new HistoryDataPresenter(this);
        ids = company.getId();
        presenter.loadData(ids,currDate,dayType,dayStateType);
        ArrayAdapter<CharSequence> typeArray = ArrayAdapter.createFromResource(
                this, R.array.type_array, R.layout.custom_spiner_text_item);
        typeArray.setDropDownViewResource(R.layout.custom_spiner_text_item);
        ArrayAdapter<CharSequence> stateTypeArray = ArrayAdapter.createFromResource(
                this, R.array.state_type_array,R.layout.custom_spiner_text_item);
        stateTypeArray.setDropDownViewResource(R.layout.custom_spiner_text_item);
        spinnerTypeDay.setAdapter(typeArray);
        spinnerStateTypeDay.setAdapter(stateTypeArray);
        spinnerTypeDay.setSelection(0,true);
        spinnerStateTypeDay.setSelection(0,true);
        spinnerTypeDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayType = position-1;
                presenter.loadDayData(ids,currDate,dayType,dayStateType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStateTypeDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayStateType = position+1;
                presenter.loadDayData(ids,currDate,dayType,dayStateType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.selectTime)
    public void onViewClicked() {
        pvTime.show(selectTime);
    }

    private void initTimePicker() {//Dialog ???????????????????????????
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(getApplicationContext(), getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");
                currDate = getTime(date);
                selectTime.setText("???????????????: " + currDate);
                presenter.loadData(ids,currDate,dayType,dayStateType);
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //????????????false ??????????????????DecorView ????????????????????????
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);//??????Bottom,????????????
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private String getTime(Date date) {//???????????????????????????????????????
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private DynamicLineChartManager dynamicLineChartManager;
    private List<Float> list = new ArrayList<>(); //????????????
    private List<String> names = new ArrayList<>(); //??????????????????
    private List<Integer> colour = new ArrayList<>();//??????????????????
    private List<String> dateList = new ArrayList<>();//?????????
    private int[] colors = {Color.BLACK,Color.RED,Color.GREEN,
            Color.YELLOW,Color.BLUE,Color.CYAN,Color.MAGENTA,Color.DKGRAY,Color.GRAY,Color.LTGRAY,Color.BLACK,Color.RED,Color.GREEN,
            Color.YELLOW,Color.BLUE,Color.CYAN,Color.MAGENTA,Color.DKGRAY,Color.GRAY,Color.LTGRAY};
    private List<Float> dlist = new ArrayList<>(); //????????????
    private List<String> dnames = new ArrayList<>(); //??????????????????
    private List<Integer> dcolour = new ArrayList<>();//??????????????????
    private List<String> dateDayList = new ArrayList<>();//?????????

    @Override
    public void updateView(Power data) {
//        initSet();
//        for (Power power : data) {
//            // turn your data into Entry objects
//            entries.add(new Entry(,));
//        }
//        chartLin.removeAllViews();
        if (data != null) {
            List<DataBean> chartDatas = data.getData(); //???
            List<CoulometryDtosBean> lineDatas; //???
            List<EleDataDtosBean> dotDatas; //???
            List<List<EleDataDtosBean>> dotsDatas; //
//            LineChart lineChart;
            if (chartDatas.size() != 0) {
                for (DataBean dataBean : chartDatas) { //???
//                    lineChart = new LineChart(this);
                    Description description = lineChart.getDescription();
                    description.setText("min/??????");
                    description.setTextColor(getResources().getColor(R.color.title_color));
                    lineDatas = dataBean.getCoulometryDtos(); //???
                    if (lineDatas.size() != 0) {
//                        chartLin.setVisibility(View.VISIBLE);
                        if (lineDatas.get(0).getEleDataDtos().size() != 0) {
                            dateList.clear();
                            for (int m = 0; m < lineDatas.size(); m++) {
                                names.add(lineDatas.get(m).getEquipmentName());
                                colour.add(colors[m]);
                            }
//                            chartLin.addView(lineChart);
                            for (int n = 0; n < lineDatas.get(0).getEleDataDtos().size(); n++) {
                                if (lineDatas.get(0).getEleDataDtos().get(n).getDataTime() != null) {
                                    String date = lineDatas.get(0).getEleDataDtos().get(n).getDataTime().split(" ")[1].substring(0, 5);
                                    dateList.add(date);
                                }
                            }
                            giveLinchart(lineChart, names, colour, dateList);
                            float max = 0f;
                            for (int j = 0; j < lineDatas.get(0).getEleDataDtos().size(); j++) {
                                for (int i = 0; i < lineDatas.size(); i++) {
                                    float p = (float) lineDatas.get(i).getEleDataDtos().get(j).getPower();
                                    if (p > max) {
                                        max = p;
                                    }
                                    list.add(p);
                                }
                                dynamicLineChartManager.addEntry(list);
                                list.clear();
                            }
                            float maxf = (max / 10);
                            int maxi = Math.round(maxf);
                            float m = 0f;
                            if (maxi == 0) {
                                if (max>1.5&&max<3){
                                    m=3.5f;
                                }else if (max>3.5&&max<5){
                                    m=5.5f;
                                }else {
                                    m=1.5f;
                                }
                            } else {
                                m = (maxi * 14);
                            }
                            dynamicLineChartManager.setYAxis(m, 0.0f, 5);
                        }
                    }
//            dateList.clear();
                    names.clear();
                    colour.clear();
                }
            }
        }


        //????????????
//        names.add("???????????????");
//        names.add("???????????????");
//        names.add("???????????????");
        //????????????
//        colour.add(Color.CYAN);
//        colour.add(Color.GREEN);
//        colour.add(Color.BLUE);

//        LineChart lineChart1 = new LineChart(this);
//        LineChart lineChart2 = new LineChart(this);
//        LineChart lineChart3 = new LineChart(this);

//        chartLin.addView(lineChart1);
//        chartLin.addView(lineChart2);
//        chartLin.addView(lineChart3);

//        giveLinchart(lineChart1);
//        giveLinchart(lineChart2);
//        giveLinchart(lineChart3);
    }

    @Override
    public void updatCharDay(JSONArray lineDatas) {
        try{
            if (lineDatas!=null){
                Description description = chartDay.getDescription();
                description.setText("???/??????");
                description.setTextColor(getResources().getColor(R.color.title_color));
                if (lineDatas.length()!=0){
                    if (lineDatas.getJSONArray(0).length()!=0) {
                        dateDayList.clear();
                        for (int m = 0; m < lineDatas.length() ; m++) {
                            dnames.add(lineDatas.getJSONArray(m).getJSONObject(0).getString("valueDesc"));
                            dcolour.add(colors[m]);
                        }
                        //??????????????????????????????
                        //???????????????array?????????????????????
                        for (int n = 0; n < lineDatas.getJSONArray(0).length()-1; n++) {
                            if (lineDatas.getJSONArray(0).getJSONObject(n).getString("name")!= null) {
                                //??????????????????
                                String date = lineDatas.getJSONArray(0).getJSONObject(n).getString("name").substring(5,10);
                                dateDayList.add(date);
                            }
                        }
                        giveLinchart(chartDay,dnames, dcolour, dateDayList);
                        float max = 0f;
                        //???????????????array?????????size,?????????array?????????size,??????????????????,????????????????????????????????????.????????????
                        for (int j = 0; j < lineDatas.getJSONArray(0).length()-1; j++) {
                            for (int i = 0; i < lineDatas.length(); i++) {
                                float p = (float) lineDatas.getJSONArray(i).getJSONObject(j).getDouble("value");
                                if (p>max){
                                    max=p;
                                }
                                dlist.add(p);
                            }
                            dynamicLineChartManager.addEntry(dlist);
                            dlist.clear();
                        }
                        float maxf = (max/10);
                        int maxi = Math.round(maxf);
                        float m=0f;
                        if (maxi==0){
                            if (max>1.5&&max<3){
                                m=3.5f;
                            }else if (max>3.5&&max<5){
                                m=5.5f;
                            }else {
                                m=1.5f;
                            }
                        }else {
                            m = (maxi*14);
                        }
                        dynamicLineChartManager.setYAxis(m,0.0f,5);
                    }else {
                        dayDu.setVisibility(View.GONE);
                    }
                }
                dnames.clear();
                dcolour.clear();
            }
//            chartDay.invalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void giveLinchart(LineChart lineChart, List<String> names, List<Integer> colour, List<String> dateList) {
        lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour, dateList);
//        dynamicLineChartManager.setYAxis(max,min,labelCount);

//        for (int i = 0; i < 7; i++){
//            list.add((int) (Math.random() * 50) + 10);
//            list.add((int) (Math.random() * 70) + 10);
//            list.add((int) (Math.random() * 90));
//            dynamicLineChartManager.addEntry(list);
//            list.clear();
//        }
    }

    private void initSet(LineChart lineChart) {
        lineChart.getDescription().setEnabled(false);//????????????
//        lineChart.setPinchZoom(true);//??????????????????????????????

        //x???????????????
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//??????X?????????????????????
        xAxis.setDrawGridLines(false);//??????????????????
        xAxis.setGranularity(1f);//???????????????????????????????????????????????????????????????
        xAxis.setLabelCount(7);//??????x????????????????????????
        xAxis.setAxisLineWidth(2f);//??????x?????????, ...????????????

        //y?????????
        YAxis leftAxis = lineChart.getAxisLeft();//????????????y???
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y????????????????????????
        leftAxis.setDrawGridLines(false);//?????????y????????????
        leftAxis.setDrawLabels(false);//???????????????????????????, ...????????????

        lineChart.getAxisRight().setEnabled(false);

        //????????????
//        Legend legend = lineChart.getLegend();
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        legend.setDrawInside(false);
//        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        legend.setForm(Legend.LegendForm.LINE);
//        legend.setTextSize(12f);//????????????????????????, ...????????????

        lineChart.setExtraOffsets(10, 30, 20, 10);//????????????????????????
        lineChart.animateX(1500);//?????????????????????????????????????????????
    }

    /**
     * ????????????????????????????????????????????????????????????????????????y????????????
     *
     * @param lineChart
     * @param xAxisValue x?????????
     * @param yXAxisValues y?????????
     * @param titles ??????????????????????????????
     * @param showSetValues ??????????????????????????????????????????true??????????????????y??????????????????????????????????????????
     * @param lineColors ????????????????????????null??????????????????????????????????????????????????????
     */
//    public static void setLinesChart(LineChart lineChart, List<String> xAxisValue, List<List<Float>> yXAxisValues, List<String> titles, boolean showSetValues, int[] lineColors) {
//
//        lineChart.getDescription().setEnabled(false);//????????????
//        lineChart.setPinchZoom(true);//??????????????????????????????
//
//        MPChartMarkerView markerView = new MPChartMarkerView(lineChart.getContext(), R.layout.custom_marker_view);
//        lineChart.setMarker(markerView);
//
//        //x???????????????
//        IAxisValueFormatter xAxisFormatter = new StringAxisValueFormatter(xAxisValue);
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(xAxisValue.size());
//             /*xAxis.setAxisLineWidth(2f);*/
//        xAxis.setValueFormatter(xAxisFormatter);
//
//        //y?????????
//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setDrawGridLines(false);
//        if (showSetValues) {
//            leftAxis.setDrawLabels(false);//???????????????????????????????????????????????????
//        }
//        //leftAxis.setDrawZeroLine(true);
//     /*leftAxis.setAxisMinimum(0f);*/
//     /*leftAxis.setAxisLineWidth(2f);*/
//
//        lineChart.getAxisRight().setEnabled(false);
//
//        //????????????
//        Legend legend = lineChart.getLegend();
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        legend.setDrawInside(false);
//        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        legend.setForm(Legend.LegendForm.LINE);
//        legend.setTextSize(12f);
//
//        //?????????????????????
//        setLinesChartData(lineChart, yXAxisValues, titles, showSetValues,lineColors);
//
//        lineChart.setExtraOffsets(10, 30, 20, 10);
//        lineChart.animateX(1500);//?????????????????????????????????????????????
//    }

//    private static void setLinesChartData(LineChart lineChart, List<List<Float>> yXAxisValues, List<String> titles, boolean showSetValues, int[] lineColors) {
//
//        List<List<Entry>> entriesList = new ArrayList<>();
//        for (int i = 0; i < yXAxisValues.size(); ++i) {
//            ArrayList<Entry> entries = new ArrayList<>();
//            for (int j = 0, n = yXAxisValues.get(i).size(); j < n; j++) {
//                entries.add(new Entry(j, yXAxisValues.get(i).get(j)));
//            }
//            entriesList.add(entries);
//        }
//
//        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
//
//            for (int i = 0; i < lineChart.getData().getDataSetCount(); ++i) {
//                LineDataSet set = (LineDataSet) lineChart.getData().getDataSetByIndex(i);
//                set.setValues(entriesList.get(i));
//                set.setLabel(titles.get(i));
//            }
//
//            lineChart.getData().notifyDataChanged();
//            lineChart.notifyDataSetChanged();
//        } else {
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//
//            for (int i = 0; i < entriesList.size(); ++i) {
//                LineDataSet set = new LineDataSet(entriesList.get(i), titles.get(i));
//                if(lineColors!=null){
//                    set.setColor(lineColors[i % entriesList.size()]);
//                    set.setCircleColor(lineColors[i % entriesList.size()]);
//                    set.setCircleColorHole(Color.WHITE);
//                } else {
//                    set.setColor(LINE_COLORS[i % 3]);
//                    set.setCircleColor(LINE_COLORS[i % 3]);
//                    set.setCircleColorHole(Color.WHITE);
//                }
//
//                if (entriesList.size() == 1) {
//                    set.setDrawFilled(true);
//                    set.setFillColor(LINE_FILL_COLORS[i % 3]);
//                }
//                dataSets.add(set);
//            }
//
//            LineData data = new LineData(dataSets);
//            if (showSetValues) {
//                data.setValueTextSize(10f);
//                data.setValueFormatter(new IValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
//                        return StringUtils.double2String(value, 1);
//                    }
//                });
//            } else {
//                data.setDrawValues(false);
//            }
//
//            lineChart.setData(data);
//        }
//    }
}
