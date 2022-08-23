package com.submeter.android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.submeter.android.R;
import com.submeter.android.constants.DBConstant;
import com.submeter.android.db.ShareStoreProcess;
import com.submeter.android.tools.Utils;

public class UpgradeDialog {
    private boolean isForceUpgrade;

    private String describe;

    private String versionCode;

    private String downloadUrl;

    private String filePath;

    private Button downloadBtn;

    private Context context;

    private View upgradeView;

    private Dialog dialog;

    private Handler handler;

    private LayoutInflater layoutInflater;

    public UpgradeDialog(final Context context, String describe, String versionCode, String downloadUrl, boolean isForceUpgrade) {
        this.context = context;
        this.describe = describe;
        this.versionCode = versionCode;
        this.downloadUrl = downloadUrl;
        this.isForceUpgrade = isForceUpgrade;
        layoutInflater = LayoutInflater.from(context);
    }

    private void init() {// 初始化
        upgradeView = Utils.inflateView(context, layoutInflater, R.layout.upgrade_view, null);

        /*RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadii(Utils.dip2px(context, 10), Utils.dip2px(context, 10), 0, 0);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.build();
        hierarchy.setRoundingParams(roundingParams);

        SimpleDraweeView topImageView = (SimpleDraweeView)upgradeView.findViewById(R.id.top_imageView);
        topImageView.setImageURI(Uri.parse(Utils.getResUriPrefix(context) + R.mipmap.upgrade_top));
        topImageView.setHierarchy(hierarchy);

        TextView textView = (TextView) upgradeView.findViewById(R.id.describe_view);
        if (!TextUtils.isEmpty(describe)) {
            textView.setText(describe);
        }

        View closeView = upgradeView.findViewById(R.id.close_btn);
        if (isForceUpgrade) {
            closeView.setVisibility(View.GONE);
        } else {
            closeView.setVisibility(View.VISIBLE);
            closeView.setOnClickListener(onClickListener);
        }*/
        downloadBtn = (Button) upgradeView.findViewById(R.id.ok_btn);
        downloadBtn.setOnClickListener(onClickListener);
    }

    public void show() {
        if (null == upgradeView) {
            init();
        } else {
            ((ViewGroup) upgradeView.getParent()).removeAllViews();
        }

        dialog = new Dialog(context, R.style.DialogScale);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(upgradeView);
        // set a large value put it in bottom
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = Utils.dip2px(context, 277);
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
                    ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.UPGRADE_VERSION, versionCode);
                    ShareStoreProcess.getInstance().setKeyAndValue(DBConstant.UPGRADE_TIME, String.valueOf(System.currentTimeMillis()));
                    break;
                }

                case R.id.ok_btn: {
                    close();
                    break;
                }

                default:
                    break;
            }
        }
    };
}