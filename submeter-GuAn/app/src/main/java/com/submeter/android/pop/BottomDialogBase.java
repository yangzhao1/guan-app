package com.submeter.android.pop;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.submeter.android.R;


/**
 * Created by user on 2018/9/11.
 * 底部弹出框
 */

public class BottomDialogBase extends Dialog {

    public BottomDialogBase(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        Window win = this.getWindow();
        win.requestFeature(Window.FEATURE_NO_TITLE);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.CustomDialog;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        win.setBackgroundDrawableResource(android.R.color.transparent);
    }

}
