package com.submeter.android.activity.invitation.params;

/**
 * 封装请求参数
 * @author thm
 * @date 2018/12/4
 */
public class InvitationParam {
    /**
     * 搜索标题的关键字
     */
    private String key;
    /**
     * 招投标状态 招标预告-3 正在报名中-4 评审中-5 已结束-6
     */
    private String status;
    /**
     * 招投标分类 装饰建材-14 机械设备-15 电器-16 家居-17 智能系统-18 基础建材-53
     */
    private String invitationCategoryId;
    /**
     * 开始时间 2018-11-01 00:00:00
     */
    private String startDate;
    /**
     * 结束时间 2018-11-01 00:00:00
     */
    private String endDate;
    /**
     * 综合排序 升序-aes 降序-desc
     */
    private String integratedSort;
    /**
     * 时间排序 升序-aes 降序-desc 默认升序
     */
    private String timeSort;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvitationCategoryId() {
        return invitationCategoryId;
    }

    public void setInvitationCategoryId(String invitationCategoryId) {
        this.invitationCategoryId = invitationCategoryId;
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

    public String getIntegratedSort() {
        return integratedSort;
    }

    public void setIntegratedSort(String integratedSort) {
        this.integratedSort = integratedSort;
    }

    public String getTimeSort() {
        return timeSort;
    }

    public void setTimeSort(String timeSort) {
        this.timeSort = timeSort;
    }
}
