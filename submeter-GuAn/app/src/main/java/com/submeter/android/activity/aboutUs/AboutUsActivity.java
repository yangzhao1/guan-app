package com.submeter.android.activity.aboutUs;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by yangzhao on 2019/4/24.
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aboutus, R.string.label_aboutus,
                R.drawable.ic_back, "", false);

        version.setText("版本号: "+ getOldVersionCode());
    }

    public String getOldVersionCode() {
        String versionName = null;
        try {
            PackageInfo info = getBaseActivity().getPackageManager().getPackageInfo(
                    getBaseActivity().getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
