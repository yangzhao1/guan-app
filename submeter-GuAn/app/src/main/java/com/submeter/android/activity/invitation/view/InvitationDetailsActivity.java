package com.submeter.android.activity.invitation.view;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.submeter.android.R;
import com.submeter.android.activity.BaseActivity;
import com.submeter.android.activity.invitation.presenter.IInvitationDetailsPresenter;
import com.submeter.android.activity.invitation.presenter.InvitationDetailsPresenter;
import com.submeter.android.constants.SystemConstant;
import com.submeter.android.entity.Invitation;
import com.submeter.android.tools.DownloadManagerUtils;
import com.submeter.android.tools.JSCallback;
import com.submeter.android.tools.SDCardHelper;
import com.submeter.android.tools.Utils;
import java.io.File;
import butterknife.BindView;

/**
 * 招投标详情页面
 *
 * @author thm
 * date 2018/12/08
 */
public class InvitationDetailsActivity extends BaseActivity implements IInvitationDetailsView {

    @BindView(R.id.ll_invitation_details)
    LinearLayout mRoot;

    @BindView(R.id.tv_invitation_details_address)
    TextView mTvAddress;

    @BindView(R.id.tv_invitation_details_company_name)
    TextView mTvCompanyName;

    @BindView(R.id.tv_invitation_details_contarct_name)
    TextView mTvContarctName;

    @BindView(R.id.tv_invitation_details_date)
    TextView mTvDate;

    @BindView(R.id.webview)
    WebView mWVContent;

    @BindView(R.id.tv_invitation_details_title)
    TextView mTvTitle;

//    @BindView(R.id.tv_invitation_details_time)
//    TextView mTvTime;

    @BindView(R.id.tv_invitation_details_phone)
    TextView mTvPhone;

    @BindView(R.id.btn_invitation_details_download)
    Button mBtnFileDownload;

    @BindView(R.id.btn_invitation_details_apply)
    Button mBtnApply;

    private DownloadManager mDownloadManager;

    private DownloadManagerUtils mDownloadManagerUtils;

    private long mDownloadId;

    private DownLoadCompleteReceiver mDownLoadCompleteReceiver;

    private IInvitationDetailsPresenter mInvitationDetailsPresenter;

    private Invitation mInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details, -1, R.drawable.ic_back, R.drawable.ic_more,false);
        initData();
    }

    private void initView() {
        if (mInvitation != null) {
            if (!TextUtils.isEmpty(mInvitation.getMemo())) {
                mBtnFileDownload.setVisibility(View.VISIBLE);
            } else {
                mBtnFileDownload.setVisibility(View.GONE);
            }
            if (mInvitation.getStatus() == SystemConstant.INVITATION_LOADING) {
                mBtnApply.setVisibility(View.VISIBLE);
            } else {
                mBtnApply.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void addEvent() {
        mBtnFileDownload.setOnClickListener(view -> download());
    }

    private void download() {
        if (mInvitation != null && !TextUtils.isEmpty(mInvitation.getMemo())) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mInvitation.getMemo()));
            //设置在什么网络情况下进行下载
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            //设置通知栏标题可见
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //设置通知栏标题
            request.setTitle(mInvitation.getMubanFileName());
            request.setDescription(getString(R.string.label_invitation_details_downloading));
            request.setAllowedOverRoaming(false);
            //设置文件存放目录
            request.setDestinationInExternalFilesDir(this, SystemConstant.DOWNLOAD_FOLDER_NAME, mInvitation.getMubanFileName());
            mDownloadId = mDownloadManager.enqueue(request);
        } else {
            Utils.showToast(this, getString(R.string.label_invitation_details_download_error));
        }
    }

    private void initData() {
        registerReceiver();
        initDownloadManager();
        initWebView();
        requestData();
    }

    private void initWebView(){
        initWebView(mWVContent, new JSCallback(this));
        mWVContent.getSettings().setBlockNetworkImage(false);
    }

    private void requestData() {
        mRoot.setVisibility(View.GONE);
        Intent intent = getIntent();
        String id = intent.getStringExtra(SystemConstant.INVITATION_KEY);
        mInvitationDetailsPresenter = new InvitationDetailsPresenter(this);
        mInvitationDetailsPresenter.loadData(id);
    }

    private void initDownloadManager() {
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mDownloadManagerUtils = new DownloadManagerUtils(mDownloadManager);
    }

    private void registerReceiver() {
        mDownLoadCompleteReceiver = new DownLoadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mDownLoadCompleteReceiver, intentFilter);
    }

    private void unRegisterReceiver() {
        if (mDownLoadCompleteReceiver != null) {
            unregisterReceiver(mDownLoadCompleteReceiver);
        }
    }

    @Override
    public void updateView(Invitation invitationDetails) {
        mInvitation = invitationDetails;
        setData();
        initView();
        addEvent();
    }

    private void setData() {
        if (mInvitation != null) {
            mRoot.setVisibility(View.VISIBLE);
            mTvTitle.setText(mInvitation.getTitle());
            mTvCompanyName.setText(mInvitation.getInvitationCompanyName());
            String startDate = Utils.formatDate(mInvitation.getStartDate(), "yyyy/MM/dd");
            String endDate = Utils.formatDate(mInvitation.getEndDate(), "yyyy/MM/dd");
            mTvDate.setText(String.format(getString(R.string.label_invitation_details_date), startDate, endDate));
            mTvAddress.setText(mInvitation.getAdress());
            mTvContarctName.setText(mInvitation.getContarctName());
            mTvPhone.setText(mInvitation.getPhone());
            mWVContent.loadDataWithBaseURL(null, getHtmlData(mInvitation.getContent()),
                    "text/html", "utf-8", null);
        }
    }

    /**
     * 对富文本文件的样式进行适配
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head><style type=\"text/css\">img{max-width: 100%; width:auto; height: auto;} p{color:#888888} video{width: 100%; height:auto; object-fit: contain}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private class DownLoadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeDownloadId == mDownloadId) {
                if (mDownloadManagerUtils.getStatusById(mDownloadId) == DownloadManager.STATUS_SUCCESSFUL) {
                    String downloadFileFolderPath = SDCardHelper.getSDCardPrivateFilesDir(InvitationDetailsActivity.this, SystemConstant.DOWNLOAD_FOLDER_NAME);
                    String filePath = new File(downloadFileFolderPath, mInvitation.getMubanFileName()).getAbsolutePath();
                    Utils.openFile(context, filePath);
                }
            }
        }
    }
}
