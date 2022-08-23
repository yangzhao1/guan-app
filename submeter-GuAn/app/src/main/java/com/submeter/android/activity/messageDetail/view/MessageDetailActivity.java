package com.submeter.android.activity.messageDetail.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;

import java.net.URL;

import butterknife.BindView;

/**
 * Created by yangzhao on 2019/4/22.
 */

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.statusbar_view)
    View statusbarView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg, R.string.message_info, R.drawable.ic_back, "", false);
        String t = getIntent().getStringExtra("title");
        String ti = getIntent().getStringExtra("time");
        if (ti!=null){
            ti = getIntent().getStringExtra("time").split(" ")[0];
        }
        String c = getIntent().getStringExtra("content");
        title.setText(t);
        time.setText(ti);

        if (c!=null){
            new Thread(()->{
                final Spanned html;
                html = Html.fromHtml(c,imageGetter,null);
                runOnUiThread(()->{
                    content.setText(html);
                });
            }).start();
        }
    }

    Html.ImageGetter imageGetter = source -> {
        Log.i("RG", "source---?>>>" + source);
        Drawable drawable = null;
        URL url;
        try {
            url = new URL(source);
            Log.i("RG", "url---?>>>" + url);
            drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片

            WindowManager manager = getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            int width = outMetrics.widthPixels;
            int height = width*2/3;
            Log.i("RG", "url---?>>" + url);
            int w = width/drawable.getIntrinsicWidth();
            if (drawable!=null){
                drawable.setBounds(0, 0,width, drawable.getIntrinsicHeight()*w);
                return drawable;
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
}
