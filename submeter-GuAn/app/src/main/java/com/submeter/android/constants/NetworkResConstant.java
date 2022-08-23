package com.submeter.android.constants;

public class NetworkResConstant {

    //Release 云
    /**
     * 固安县
     */
    public static final String SERVER_IP = "http://124.238.180.235:8083/ele";
//    public static final String SERVER_IP = "http://192.168.0.52:8080/tr-model-admin";

    /**
     * 固安县
     */
//    public static final String SERVER_IP = "http://124.238.180.235:8083/ele";

//    public static final String SERVER_IP = "http://192.168.0.94:8080/tr-model-admin";

    //Debug  建华
//    public static final String SERVER_IP = "http://192.168.0.49:8080/tr-model-admin";
    // 建伟
//    public static final String SERVER_IP = "http://192.168.1.115:8080/tr-model-admin";

    public static final String SDK_SERVER_URL = SERVER_IP;

    //sub
    public static final String post_login = "/app/login";
    public static final String post_noticeList = "/app/notice/noticList";
    public static final String post_outCompanyList = "/app/vio/list";
    public static final String post_submit = "/app/vio/wgSave";
    public static final String get_outInfo = "/app/vio/wginfo";
    public static final String post_uploadImage = "/file/upload";
    public static final String post_warnList = "/app/notice/warnList";
    public static final String get_monitorList = "/app/enterprise/allinfo";
    public static final String get_violatorInfo = "/app/vio/wginfo";
    public static final String get_powerList = "/app/enterprise/coulometry";
//    public static final String get_dayPowerList = "/app/enterprise/sevenDaysOfElectricity";
    public static final String get_dayPowerList = "/app/enterprise/getData";
//    public static final String get_hoursPowerList = "/app/enterprise/sevenHoursOfElectricity";
    public static final String get_hoursPowerList = "/app/enterprise/eleCountByHour";
    public static final String get_homeData = "/app/index/allinfo";
    public static final String get_versions = "/app/getAppVersion";
    public static final String post_modifyPass = "/app/modifyPass";
    public static final String get_myinfo = "/app/info/";
    public static final String get_num = "/app/enterprise/getNum";
    public static final String getPost_uploadImage = "/app/updateInfo/";
    public static final String post_signIn = "/app/onsiteInspection/save";
    public static final String get_area = "/app/sta/area";
    public static final String get_handleInfo = "/app/vio/handleInfo";
    public static final String get_companyList = "/app/enterprise/list";
    public static final String get_areaList = "/app/index/sysOrganizationEntities";
    public static final String get_facilityList = "/app/vio/eleStatisticalHisty";
    //使用聚合数据获取
    public static final String getWeather = "http://apis.juhe.cn/simpleWeather/query?city=廊坊&key=5b38876d78b309c187748f39d8495667";




    public static final String GET_HOME = "/app/home/getAppHomePage";
    public static final String GET_CATEGORY = "/app/ad/getAppClassify";
    public static final String REGISTER = "/app/user/register";
    public static final String SEND_REGISTER_SMS = "/app/user/sendRegisterSms";
    public static final String LOGIN = "/app/login";
    public static final String LOGOUT = "/app/logout";
    public static final String SEND_LOGIN_SMS = "/app/sendLoginSms";
    public static final String GET_USER_INFO = "/app/user/info";
    public static final String GET_BRAND_CATEGORY = "/app/productFrontCategory/searchBrandClassify";
    public static final String SEARCH_COMMODITY = "/app/product/search";
    public static final String GET_COMMODITY = "/app/product/searchProductDetail";
    public static final String SEND_RESET_PSD_SMS = "/app/user/sendRestPassSms";
    public static final String RESET_PSD = "/app/user/resetPass";

    //invitation
    public static final String GET_INVITATION_SEARCH = "/app/invitation/search";
    public static final String GET_INVITATION_STATUS = "/app/invitation/status";
    public static final String GET_INVITATION_CATEGORY = "/app/invitation/categorys";
    public static final String GET_INVITATION_DETAILS = "/app/invitation/invitationDetail";

    //commodity
    public static final String GET_SEARCHPRODUCT = "/app/product/searchProduct";
    public static final String GET_PRODUCTFRONTCATEGORY_SEARCH = "/app/productFrontCategory/search";
    public static final String GET_AD_SEARCH = "/app/ad/search";

    //page size
    public static final int LIST_PAGE_SIZE = 10;

    //timeout
    public static final int NETWORK_REQUEST_TIMEOUT = 45000;

    //special error code
    public static final int REQUEST_SUCCESS = 0; //成功
    public static final int TOKEN_EXPIRED = 401;
}
