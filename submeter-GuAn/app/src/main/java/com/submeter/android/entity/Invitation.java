package com.submeter.android.entity;

import com.submeter.android.R;
import com.submeter.android.constants.SystemConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thm
 * @date 2018/12/ic_common_sort_default
 */
public class Invitation implements Serializable {
    /**
     * 编号
     */
    private Integer id;
    /**
     * 详细内容
     */
    private String content;
    /**
     * 标题
     */
    private String title;
    /**
     * 开始时间
     */
    private String startDate;
    /**
     * 结束时间
     */
    private String endDate;
    /**
     * 招投标状态 编辑中:0,待审核: 1,未通过 ：2,ic_invitation_status_start_icon :ic_common_sort_default,报名中: 4,ic_invitation_status_review_icon：5,ic_invitation_status_end_icon ：6,已取消:7
     */
    private Integer status;
    /**
     * 招标公司
     */
    private String invitationCompanyName;
    /**
     * 招投标分类
     */
    private Integer invitationCategoryId;
    /**
     * 项目地址
     */
    private String adress;
    /**
     * 联系人
     */
    private String contarctName;
    /**
     * 联系电话
     */
    private String phone;

    private String companyId;
    /**
     * 采购条件
     */
    private String invitCondition;
    /**
     * 是否已经添加招标公告
     */
    private boolean isNotice;
    /**
     * 附件
     */
    private String memo;
    /**
     * 附件名称
     */
    private String mubanFileName;
    /**
     * 标段数
     */
    private String sectionNumber;
    /**
     * 中标通知
     */
    private String winTheBidding;
    /**
     * 中标通知标题
     */
    private String winTheBiddingTitle;

    private Map<Integer, Item> statusMap = new HashMap<>();

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    /*首页根据此字段判断滚动招标信息是否为新的；1表示新的*/
    private String isNew;

    public Invitation() {
        statusMap.put(SystemConstant.INVITATION_START, new Item(R.drawable.ic_invitation_status_start_bg, R.drawable.ic_invitation_status_start_icon));
        statusMap.put(SystemConstant.INVITATION_LOADING, new Item(R.drawable.ic_invitation_status_loading_bg, R.drawable.ic_invitation_status_loading_icon));
        statusMap.put(SystemConstant.INVITATION_REVIEW, new Item(R.drawable.ic_invitation_status_review_bg, R.drawable.ic_invitation_status_review_icon));
        statusMap.put(SystemConstant.INVITATION_END, new Item(R.drawable.ic_invitation_status_end_bg, R.drawable.ic_invitation_status_end_icon));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInvitationCompanyName() {
        return invitationCompanyName;
    }

    public void setInvitationCompanyName(String invitationCompanyName) {
        this.invitationCompanyName = invitationCompanyName;
    }

    public Integer getInvitationCategoryId() {
        return invitationCategoryId;
    }

    public void setInvitationCategoryId(Integer invitationCategoryId) {
        this.invitationCategoryId = invitationCategoryId;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getContarctName() {
        return contarctName;
    }

    public void setContarctName(String contarctName) {
        this.contarctName = contarctName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInvitCondition() {
        return invitCondition;
    }

    public void setInvitCondition(String invitCondition) {
        this.invitCondition = invitCondition;
    }

    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean notice) {
        isNotice = notice;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMubanFileName() {
        return mubanFileName;
    }

    public void setMubanFileName(String mubanFileName) {
        this.mubanFileName = mubanFileName;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getWinTheBidding() {
        return winTheBidding;
    }

    public void setWinTheBidding(String winTheBidding) {
        this.winTheBidding = winTheBidding;
    }

    public String getWinTheBiddingTitle() {
        return winTheBiddingTitle;
    }

    public void setWinTheBiddingTitle(String winTheBiddingTitle) {
        this.winTheBiddingTitle = winTheBiddingTitle;
    }

    public Item getItem() {
        return statusMap.get(status);
    }

    public class Item implements Serializable {

        private int viewBgResId;
        private int imageViewResId;

        public Item(int viewBgResId, int imageViewResId) {
            this.viewBgResId = viewBgResId;
            this.imageViewResId = imageViewResId;
        }

        public int getViewBgResId() {
            return viewBgResId;
        }

        public int getImageViewResId() {
            return imageViewResId;
        }
    }

    public String getIsNew() {
        return isNew;
    }


}
