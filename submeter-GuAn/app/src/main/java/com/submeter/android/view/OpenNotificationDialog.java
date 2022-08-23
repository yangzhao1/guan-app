package com.submeter.android.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.submeter.android.R;
import com.submeter.android.tools.NotificationTools;
import com.submeter.android.tools.Utils;


public class OpenNotificationDialog {

    private Context context;

    private View openView;

    private Dialog dialog;

    private LayoutInflater layoutInflater;

    public OpenNotificationDialog(final Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    private void init() {// 初始化
        openView = Utils.inflateView(context, layoutInflater, R.layout.open_notification_view, null);

        /*RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadii(Utils.dip2px(context, 10), Utils.dip2px(context, 10), 0, 0);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.build();
        hierarchy.setRoundingParams(roundingParams);

        SimpleDraweeView topImageView = (SimpleDraweeView)openView.findViewById(R.id.top_imageView);
        topImageView.setImageURI(Uri.parse(Utils.getResUriPrefix(context) + R.mipmap.push_layer));
        topImageView.setHierarchy(hierarchy);

        openView.findViewById(R.id.close_btn).setOnClickListener(onClickListener);
        openView.findViewById(R.id.ok_btn).setOnClickListener(onClickListener);*/
    }

    public void show() {
        if (null == openView) {
            init();
        } else {
            ((ViewGroup) openView.getParent()).removeAllViews();
        }

        dialog = new Dialog(context, R.style.DialogScale);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(openView);
        // set a large value put it in bottom
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = Utils.dip2px(context, 264);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        w.setAttributes(lp);

        dialog.show();
    }

    public void close() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_btn: {
                    close();
                    NotificationTools.ignoreOpenNotification();
                    break;
                }

                case R.id.ok_btn: {
                    NotificationTools.needCheckNotificationAgain = true;
                    NotificationTools.getAppDetailSettingIntent(context);
                    break;
                }

                default:
                    break;
            }
        }
    };

    public boolean isShowed() {
        if(null != dialog) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }
}