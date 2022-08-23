package com.submeter.android.activity.main.fragment.message.view;

import android.os.Bundle;

import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;

/**
 * Created by yangzhao on 2019/3/29.
 */

public class MessageInfoActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messageinfo,R.string.message_info,R.drawable.ic_back,"",false);

    }
}
