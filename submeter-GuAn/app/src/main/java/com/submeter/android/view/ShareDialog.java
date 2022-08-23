package com.submeter.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.submeter.android.R;
import com.submeter.android.entity.ShareInfo;
import com.submeter.android.network.INetworkResponseListener;
import com.submeter.android.network.NetworkResponseListener;
import com.submeter.android.tools.DeviceUtils;
import com.submeter.android.tools.Utils;

public class ShareDialog implements INetworkResponseListener {
    private Activity activity;

    private LayoutInflater layoutInflater;

    private View shareView;

    private Dialog dialog;

    private ShareInfo shareInfo;

    private NetworkResponseListener responseListener;

    private ShareDialog(Activity activity, ShareInfo shareInfo) {
        this.activity = activity;
        this.shareInfo = shareInfo;
        layoutInflater = LayoutInflater.from(activity);
        responseListener = new NetworkResponseListener(this);
    }

    private void init() {// 初始化
        shareView = Utils.inflateView(activity, layoutInflater, R.layout.share_layout, null);

        View wxFriendView = shareView.findViewById(R.id.wx_button);
        View wxCircleView = shareView.findViewById(R.id.wx_circle_button);
        if (TextUtils.isEmpty(shareInfo.getWxH5PageUrl())) {
            wxFriendView.setVisibility(View.GONE);
            wxCircleView.setVisibility(View.GONE);
        } else {
            wxFriendView.setOnClickListener(shareClickListener);
            wxCircleView.setOnClickListener(shareClickListener);
        }

        View qqView = shareView.findViewById(R.id.qq_friend_button);
        View sinaView = shareView.findViewById(R.id.sina_button);
        if (TextUtils.isEmpty(shareInfo.getH5PageUrl())) {
            qqView.setVisibility(View.GONE);
            sinaView.setVisibility(View.GONE);
        } else {
            qqView.setOnClickListener(shareClickListener);
            sinaView.setOnClickListener(shareClickListener);
        }

        shareView.findViewById(R.id.cancel_btn).setOnClickListener(shareClickListener);
    }

    private void show(int width, int height) {
        if (null == shareView) {
            init();
        } else {
            ((ViewGroup) shareView.getParent()).removeAllViews();
        }

        dialog = new Dialog(activity, R.style.dialog_share_style);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(shareView);
        // set a large value put it in bottom
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = width;
        if (height == -1) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = height;
        }
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        w.setAttributes(lp);

        dialog.show();
    }

    public void close() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    private View.OnClickListener shareClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.wx_button: {
                    /*if (ShareUtil.getInstance().isWxInstalled(activity)) {
                        String shareType = shareInfo.getShareType();
                        if(Event.SHOW.equalsIgnoreCase(shareType) || Event.ACTIVITY.equalsIgnoreCase(shareType)) {
                            ShareUtil.getInstance().shareToMinProgram(activity, shareInfo.getWxH5PageUrl(), "pages/activityDetail/activityDetail?id=" + shareInfo.getId() + "&type=" + shareType + "&name=" + shareInfo.getTitle(), shareInfo.getTitle(), shareInfo.getSubTitle(), shareInfo.getMiniProgramImage());
                        } else if(SystemConstant.SHARE_TOPIC.equalsIgnoreCase(shareType)) {
                            ShareUtil.getInstance().shareToMinProgram(activity, shareInfo.getWxH5PageUrl(), "pages/home/channelList/channelList?id=" + shareInfo.getId() + "&curType=TOPIC&name=" + shareInfo.getTitle(), shareInfo.getTitle(), shareInfo.getSubTitle(), shareInfo.getMiniProgramImage());
                        } else if(SystemConstant.SHARE_MERCHANT_SIGNUP.equalsIgnoreCase(shareType)) {
                            String url = "";
                            String serverUrl = MeetownApp.getInstance().getServerUrl();
                            if (Uri.parse(serverUrl).getHost().startsWith("api")) {
                                url = SystemConstant.H5_DOMAIN + SystemConstant.MERCHANT_SIGN_URL;
                            } else {
                                url = serverUrl + SystemConstant.MERCHANT_SIGN_URL;
                            }
                            ShareUtil.getInstance().shareToMinProgram(activity, shareInfo.getWxH5PageUrl(), "pages/me/businessEnter/businessEnter?title=商家入驻&url=" + url, shareInfo.getTitle(), shareInfo.getSubTitle(), shareInfo.getMiniProgramImage());
                        } else {
                            ShareUtil.getInstance().showShare(activity, Wechat.NAME, shareInfo.getTitle(), null, MeetownApp.getInstance().getCustomSizeImageStyle(shareInfo.getIcon(), Utils.dip2px(activity, 80)), shareInfo.getSubTitle(), shareInfo.getWxH5PageUrl(), shareListener);
                        }
                        MobclickAgent.onEvent(activity, UmengAnalyticsConstant.SHARE_EVENT_WX);
                    } else {
                        Utils.showToast(activity, activity.getString(R.string.wx_no_installed));
                    }*/

                    break;
                }

                case R.id.wx_circle_button: {
                    /*if (ShareUtil.getInstance().isWxInstalled(activity)) {
                        ShareUtil.getInstance().showShare(activity, WechatMoments.NAME, shareInfo.getTitle(), null, MeetownApp.getInstance().getCustomSizeImageStyle(shareInfo.getIcon(), Utils.dip2px(activity, 80)), shareInfo.getSubTitle(), shareInfo.getWxH5PageUrl(), shareListener);
                        MobclickAgent.onEvent(activity, UmengAnalyticsConstant.SHARE_EVENT_WX_CIRCLE);
                    } else {
                        Utils.showToast(activity, activity.getString(R.string.wx_no_installed));
                    }*/
                    break;
                }

                case R.id.sina_button: {
                    /*ShareUtil.getInstance().showShare(activity, SinaWeibo.NAME, null, null, MeetownApp.getInstance().getCustomSizeImageStyle(shareInfo.getIcon(), Utils.dip2px(activity, 80)), shareInfo.getTitle() + " " + shareInfo.getH5PageUrl(), null, shareListener);
                    MobclickAgent.onEvent(activity, UmengAnalyticsConstant.SHARE_EVENT_SINA);*/
                    break;
                }

                case R.id.qq_friend_button: {
                    /*ShareUtil.getInstance().showShare(activity, QQ.NAME, shareInfo.getTitle(), null, MeetownApp.getInstance().getCustomSizeImageStyle(shareInfo.getIcon(), Utils.dip2px(activity, 80)), shareInfo.getSubTitle(), shareInfo.getH5PageUrl(), shareListener);
                    MobclickAgent.onEvent(activity, UmengAnalyticsConstant.SHARE_EVENT_QQ);*/
                    break;
                }

                default:
                    break;
            }

            close();
        }
    };

    @Override
    public void onResponse(String result) {
        // TODO Auto-generated method stub
        /*try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("credit")) {
                int credit = jsonObject.optInt("credit", 0);
                if (credit > 0) {
                    RegisterModel user = MeetownApp.getInstance().getLoginUser();
                    user.setCreditScore(user.getCreditScore() + credit);

                    Utils.showToast(activity, String.format(activity.getString(R.string.receive_credit_toast), credit));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    public static void showShareView(Activity activity, ShareInfo shareInfo) {
        ShareDialog shareDialog = new ShareDialog(activity, shareInfo);
        shareDialog.show(DeviceUtils.getScreenWidth(activity), -1);
    }
}